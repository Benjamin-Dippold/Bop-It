/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.network;

import android.util.Log;
import at.iver.bop_it.RoundRecord;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Communication {

    private static final String TAG = "Communication";
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private volatile Boolean isRunning = true;
    ExecutorService executor = Executors.newFixedThreadPool(2);
    IHandleMessage messageHandler;

    public Communication(
            ObjectInputStream input, ObjectOutputStream output, IHandleMessage messageHandler) {
        this.input = input;
        this.output = output;
        this.messageHandler = messageHandler;
        executor.submit(this::listenForMessages);
    }

    public void sendMessage(Message msg) {
        executor.submit(
                () -> {
                    try {
                        output.writeObject(msg);
                        Log.i(TAG, "Executor submitted message.");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        Log.i(TAG, "Sent: " + msg.getType());
    }

    private void listenForMessages() {
        while (isRunning) {
            try {
                Message msg = (Message) input.readObject();
                Log.i(TAG, "Received: " + msg.getType());
                messageHandler.handleMessage(msg);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void closeCommunication() throws IOException {
        isRunning = false;
        executor.shutdown();
        input.close();
        output.close();
    }

    public static Message generateGiveIdMessage(int id) {
        Message message = new Message(MessageType.GIVE_ID);
        message.setData(DataKey.ID, id);
        return message;
    }

    public static Message generatePromptMessage(int prompt, boolean isSimon) {
        Message message = new Message(MessageType.PROMPT);
        message.setData(DataKey.TYPE, prompt);
        message.setData(DataKey.ISSIMON, isSimon);
        return message;
    }

    public static Message generatePromptWithExtraMessage(int prompt, int extra, boolean isSimon) {
        Message message = new Message(MessageType.PROMPT_WITH_EXTRA);
        message.setData(DataKey.TYPE, prompt);
        message.setData(DataKey.EXTRA, extra);
        message.setData(DataKey.ISSIMON, isSimon);
        return message;
    }

    public static Message generateFinishMessage(int id, long time) {
        Message message = new Message(MessageType.FINISH);
        message.setData(DataKey.ID, id);
        message.setData(DataKey.TIME, time);
        return message;
    }

    public static Message generateResultsMessage(long[] times, int[] scores) {
        Message message = new Message(MessageType.RESULTS);
        message.setData(DataKey.RESULTS, times);
        message.setData(DataKey.SCORES, scores);
        return message;
    }

    public static Message generateVictoryMessage(int winner, List<RoundRecord> rounds) {
        Message message = new Message(MessageType.VICTORY);
        message.setData(DataKey.ID, winner);
        message.setData(DataKey.ROUND_RECORDS, rounds);
        return message;
    }

    public static Message generateNameChangeMessage(String name, int playerId) {
        Message message = new Message(MessageType.UPDATE_NAME);
        message.setData(DataKey.ID, playerId);
        message.setData(DataKey.NAME, name);
        return message;
    }

    public static Message generateStartGameMessage() {
        return new Message(MessageType.START_GAME);
    }

    public static Message generateStartGameMessage(int rounds, boolean simonMode) {
        Message message = new Message(MessageType.START_GAME);
        message.setData(DataKey.ROUNDS, rounds);
        message.setData(DataKey.SIMON_MODE, simonMode);
        return message;
    }

    public static Message generatePlayAgainMessage() {
        return new Message(MessageType.PLAY_AGAIN);
    }
}
