/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.network.client;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import at.iver.bop_it.UIUpdateListener;
import at.iver.bop_it.network.Communication;
import at.iver.bop_it.network.Message;

public class ClientConnector extends Thread {
    private static final String TAG = "ClientConnector";
    private final UIUpdateListener uiUpdater;
    private Socket socket;
    private InetSocketAddress targetServer;
    private Communication comm;
    private String ipAddress;
    private int port;

    public ClientConnector(UIUpdateListener uiUpdater) {
        this.socket = new Socket();
        this.uiUpdater = uiUpdater;
    }

    @Override
    public void run() {
        try {
            targetServer = new InetSocketAddress(ipAddress, port);
            socket.connect(targetServer);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Log.i("Info", "Client was connected to " + targetServer.toString());
            uiUpdater.clientCallback(true);

            comm =
                    new Communication(
                            inputStream, outputStream, new ClientMessageHandler(uiUpdater));
        } catch (IOException e) {
            uiUpdater.clientCallback(false);
        }
    }

    public void setConnectionTarget(String ipAddr, int port) {
        this.ipAddress = ipAddr;
        this.port = port;
    }

    public void sendMessage(Message msg) throws IOException {
        comm.sendMessage(msg);
        Log.i(TAG, "Sent message: " + msg.getType());
    }

    public InetSocketAddress getConnectionTarget() {
        return targetServer;
    }

    public Communication getCommunication() {
        return comm;
    }

    public void setSocket(Socket socket) {
        // This was added to enable easier Mocking of private fields
        this.socket = socket;
    }
}
