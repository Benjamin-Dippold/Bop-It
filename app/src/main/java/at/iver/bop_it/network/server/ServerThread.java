/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.network.server;

import static at.iver.bop_it.network.Communication.generateGiveIdMessage;
import static at.iver.bop_it.network.Communication.generatePromptMessage;
import static at.iver.bop_it.network.Communication.generateResultsMessage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import at.iver.bop_it.MainActivity;
import at.iver.bop_it.network.Message;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ServerThread extends Thread {
    private static final int MIN_TIME_BETWEEN_PROMPTS = 2000;
    private static final int MAX_TIME_BETWEEN_PROMPTS = 5000;
    private static final String TAG = "ServerThread";
    private final Context context;
    private final int port;
    private String ipAddr = "empty";

    @SuppressLint("StaticFieldLeak")
    private static ServerThread instance;

    private ServerSocket serverSocket;
    private final ArrayList<ClientHandler> connections = new ArrayList<>();
    private boolean waitingForFinishers = false;
    private final Map<Integer, Long> finishers = new HashMap<>();

    public ServerThread(Context context, int port) {
        this.context = context;
        this.port = port;
        instance = this;
    }

    @Override
    public void run() {
        try {
            setupServer();
            ((MainActivity) context).showIp(new View(context));
            acceptConnections();
            giveClientsTheirId();
        } catch (IOException ex) {
            Log.e(TAG, "IO Exception on Server!", ex);
        }
        if (!serverSocket.isClosed()) {
            createGame();
            ((MainActivity) context)
                    .runOnUiThread(() -> ((MainActivity) context).startGame(new View(context)));
        }
    }

    private void giveClientsTheirId() {
        int i = 0;
        for (ClientHandler client : connections) {
            client.sendMessage(generateGiveIdMessage(i));
            i++;
        }
    }

    private void setupServer() throws IOException {
        serverSocket = new ServerSocket(this.port);
        ipAddr = getIPAddress();
        Log.i(TAG, "Server reachable under: " + ipAddr + ":" + serverSocket.getLocalPort());
    }

    private void acceptConnections() throws IOException {
        connections.add(acceptClient("Local client connected!"));
        connections.add(acceptClient("Remote client connected!"));
    }

    private ClientHandler acceptClient(String logMessage) throws IOException {
        ClientHandler client = new ClientHandler(serverSocket.accept());
        Log.i(TAG, logMessage);
        return client;
    }

    private void createGame() {}

    public synchronized void addFinishedPlayer(int playerId, long finishTime) {
        finishers.put(playerId, finishTime);
    }

    public boolean isWaitingForFinishers() {
        return waitingForFinishers;
    }

    public synchronized void setWaitingForFinishers(boolean waitingForFinishers) {
        this.waitingForFinishers = waitingForFinishers;
    }

    public synchronized void processFinishers() throws IOException {
        // processing finishers
        long[] results = new long[connections.size()];

        for (int i = 0; i < connections.size(); i++) {
            if (finishers.containsKey(i)) {
                results[i] = finishers.get(i);
            } else {
                results[i] = -1;
            }
        }

        sendMessageToAllClients(generateResultsMessage(results));

        finishers.clear(); // clear the list after processing

        waitAndGiveNewPrompt();
    }

    public void waitAndGiveNewPrompt() {
        new java.util.Timer()
                .schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                int randomIndex =
                                        new Random().nextInt(MainActivity.possiblePrompts.length);
                                try {
                                    sendMessageToAllClients(generatePromptMessage(randomIndex));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        },
                        getRandomNumber(MIN_TIME_BETWEEN_PROMPTS, MAX_TIME_BETWEEN_PROMPTS));
    }

    private int getRandomNumber(int minValue, int maxValue) {
        return minValue + new Random().nextInt(maxValue - minValue + 1);
    }

    public int getTurnNumber(ClientHandler client) {
        return connections.indexOf(client);
    }

    public void stopServer() throws IOException {
        int i = 1;
        Log.i(TAG, "Stopping server...");
        for (ClientHandler client : connections) {
            Log.i(TAG, "Disconnecting client " + i);
            client.disconnectClient();
            Log.i(TAG, "Client " + i + " disconnected!");
            i++;
        }
        Log.i(TAG, "All clients disconnected!");
        Log.i(TAG, "Closing server socket...");
        serverSocket.close();
        Log.i(TAG, "Server socket closed!");
    }

    public ArrayList<ClientHandler> getConnections() {
        return connections;
    }

    public InetAddress getLocalAddress() {
        return serverSocket.getInetAddress();
    }

    public void sendMessageToAllClients(Message message) throws IOException {
        for (ClientHandler client : connections) {
            client.sendMessage(message);
        }
    }

    public String getIpAddr() {
        return ipAddr;
    }

    private String getIPAddress() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                WifiManager wifiManager =
                        (WifiManager)
                                context.getApplicationContext()
                                        .getSystemService(Context.WIFI_SERVICE);
                int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
                return Formatter.formatIpAddress(ipAddress);
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                try {
                    for (NetworkInterface networkInterface :
                            Collections.list(NetworkInterface.getNetworkInterfaces())) {
                        if (!networkInterface.isLoopback() && networkInterface.isUp()) {
                            for (InetAddress inetAddress :
                                    Collections.list(networkInterface.getInetAddresses())) {
                                if (!inetAddress.isLoopbackAddress()
                                        && inetAddress.getAddress().length == 4) {
                                    return inetAddress.getHostAddress();
                                }
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static ServerThread getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ServerThread not initialized!");
        }
        return instance;
    }

    private int numOfRematchRequests;

    public void sendRematchToAll() throws IOException {}

    public void sendNameChangeToAll(String data, int data1) {}

    public boolean isPlayerFinished(int playerId) {
        return finishers.containsKey(playerId);
    }
}
