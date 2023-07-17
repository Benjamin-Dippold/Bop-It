/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.network.server;

import android.util.Log;

import java.io.IOException;
import java.util.Arrays;

import at.iver.bop_it.network.DataKey;
import at.iver.bop_it.network.DeckType;
import at.iver.bop_it.network.IHandleMessage;
import at.iver.bop_it.network.Message;

public class ServerMessageHandler implements IHandleMessage {
    public static final String TAG = "ServerMessageHandler";
    private ServerThread serverThread;

    public void ensureServerThreadInitialized() {
        if (serverThread == null) {
            serverThread = ServerThread.getInstance();
        }
    }

    public void handleMessage(Message message) throws IOException {
        ensureServerThreadInitialized();

        switch (message.getType()) {

        }
    }

    private void handleName(Message message) throws IOException {
        serverThread.sendNameChangeToAll(
                (String) message.getData(DataKey.NAME),
                (int) message.getData(DataKey.TARGET_PLAYER));
    }

    private void handleRematch() throws IOException {
        serverThread.sendRematchToAll();
    }
}
