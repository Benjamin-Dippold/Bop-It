/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.network.server;

import android.util.Log;

import java.io.IOException;

import at.iver.bop_it.network.DataKey;
import at.iver.bop_it.network.IHandleMessage;
import at.iver.bop_it.network.Message;

public class ServerMessageHandler implements IHandleMessage {
    public static final String TAG = "ServerMessageHandler";
    private ServerThread serverThread;
    private static final long WAIT_TIME = 1000; // Wait time for second player (in milliseconds)


    public void ensureServerThreadInitialized() {
        if (serverThread == null) {
            serverThread = ServerThread.getInstance();
        }
    }

    public void handleMessage(Message message) throws IOException {
        ensureServerThreadInitialized();

        switch (message.getType()) {
            case FINISH:
                handleFinishMessage(message);
                break;
        }
    }

    public void handleFinishMessage(Message message) throws IOException {
        int playerId = (int) message.getData(DataKey.ID);
        long finishTime = (long) message.getData(DataKey.TIME);

        if(serverThread.isPlayerFinished(playerId)) {
            return;
        }

        serverThread.addFinishedPlayer(playerId, finishTime);

        if (!serverThread.isWaitingForFinishers()) {
            serverThread.setWaitingForFinishers(true);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            serverThread.setWaitingForFinishers(false);
                            try {
                                serverThread.processFinishers();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    },
                    WAIT_TIME // wait for 1 second before checking
            );
        }
    }

    private void handleName(Message message) throws IOException {

    }

    private void handleRematch() throws IOException {
        serverThread.sendRematchToAll();
    }

}
