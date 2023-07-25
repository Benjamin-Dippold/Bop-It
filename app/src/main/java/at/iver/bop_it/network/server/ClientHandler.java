/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.network.server;

import android.util.Log;
import at.iver.bop_it.network.Communication;
import at.iver.bop_it.network.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler {

    private final String TAG = "ClientHandler";
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Communication comm;

    public ClientHandler(Socket clientSocket) {
        try {
            socket = clientSocket;
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            comm = new Communication(inputStream, outputStream, new ServerMessageHandler());
            Log.i(TAG, "ClientHandler successfully attached to a new client.");
        } catch (IOException ex) {
            Log.e("Error", "IO Exception!");
        }
    }

    public void disconnectClient() throws IOException {
        socket.close();
    }

    public void sendMessage(Message msg) {
        comm.sendMessage(msg);
        Log.i(TAG, "Sent message: " + msg.getType());
    }
}
