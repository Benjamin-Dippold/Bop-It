/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.network.server;

import at.iver.bop_it.network.DataKey;
import at.iver.bop_it.network.IHandleMessage;
import at.iver.bop_it.network.Message;
import java.io.IOException;

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
            case UPDATE_NAME:
                handleName(message);
                break;
            case START_GAME:
                handleStartGame(message);
                break;
            case PLAY_AGAIN:
                handlePlayAgain();
                break;
        }
    }

    public void handleFinishMessage(Message message) {
        int playerId = (int) message.getData(DataKey.ID);
        long finishTime = (long) message.getData(DataKey.TIME);

        if (serverThread.isPlayerFinished(playerId)) {
            return;
        }

        serverThread.addFinishedPlayer(playerId, finishTime);

        if (!serverThread.isWaitingForFinishers()) {
            serverThread.setWaitingForFinishers(true);
            new java.util.Timer()
                    .schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    serverThread.setWaitingForFinishers(false);
                                    serverThread.processFinishers();
                                }
                            },
                            WAIT_TIME // wait for 1 second before checking
                            );
        }
    }

    private void handleName(Message message) {
        String name = (String) message.getData(DataKey.NAME);
        int id = (int) message.getData(DataKey.ID);
        serverThread.sendNameChangeToAll(name, id);
    }

    private void handleStartGame(Message message) {
        int rounds = (int) message.getData(DataKey.ROUNDS);
        boolean simonMode = (boolean) message.getData(DataKey.SIMON_MODE);
        serverThread.startGame(rounds, simonMode);
    }

    private void handlePlayAgain() {
        serverThread.sendRematchToAll();
    }
}
