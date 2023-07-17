/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.network;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import at.vunfer.openrealms.model.Player;

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

    public static Message createAddCardMessage(
            int targetPlayerTurnNumber, DeckType deckType, int cardId) {
        Message addCardMsg = new Message(MessageType.ADD_CARD);
        addCardMsg.setData(DataKey.TARGET_PLAYER, targetPlayerTurnNumber);
        addCardMsg.setData(DataKey.DECK, deckType);
        addCardMsg.setData(DataKey.CARD_ID, cardId);
        return addCardMsg;
    }

    public static Message createRemoveCardMessage(
            int targetPlayerTurnNumber, DeckType deckType, int cardId) {
        Message removeCardMsg = new Message(MessageType.REMOVE_CARD);
        removeCardMsg.setData(DataKey.TARGET_PLAYER, targetPlayerTurnNumber);
        removeCardMsg.setData(DataKey.DECK, deckType);
        removeCardMsg.setData(DataKey.CARD_ID, cardId);
        return removeCardMsg;
    }

    public static Message createExpendChampionMessage(int targetPlayerTurnNumber, int cardId) {
        Message expendChampionMsg = new Message(MessageType.EXPEND_CHAMPION);
        expendChampionMsg.setData(DataKey.TARGET_PLAYER, targetPlayerTurnNumber);
        expendChampionMsg.setData(DataKey.CARD_ID, cardId);
        return expendChampionMsg;
    }

    public static Message createResetChampionMessage(int targetPlayerTurnNumber, int cardId) {
        Message resetChampionsMsg = new Message(MessageType.RESET_CHAMPION);
        resetChampionsMsg.setData(DataKey.TARGET_PLAYER, targetPlayerTurnNumber);
        resetChampionsMsg.setData(DataKey.CARD_ID, cardId);
        return resetChampionsMsg;
    }

    public static Message createTurnNotificationMessage(int targetPlayerTurnNumber) {
        Message turnNotificationMsg = new Message(MessageType.TURN_NOTIFICATION);
        turnNotificationMsg.setData(DataKey.TARGET_PLAYER, targetPlayerTurnNumber);
        return turnNotificationMsg;
    }

    public static Message createAddMarketCardMessage(DeckType deckType, int cardId) {
        Message addCardMsg = new Message(MessageType.ADD_CARD);
        addCardMsg.setData(DataKey.DECK, deckType);
        addCardMsg.setData(DataKey.CARD_ID, cardId);
        return addCardMsg;
    }

    public static Message createRemoveMarketCardMessage(DeckType deckType, int cardId) {
        Message removeCardMsg = new Message(MessageType.REMOVE_CARD);
        removeCardMsg.setData(DataKey.DECK, deckType);
        removeCardMsg.setData(DataKey.CARD_ID, cardId);
        return removeCardMsg;
    }

    public static Message createPlayerStatsMessage(int targetPlayerTurnNumber, Player player) {
        Message playerStatsMsg = new Message(MessageType.UPDATE_PLAYER_STATS);
        playerStatsMsg.setData(DataKey.TARGET_PLAYER, targetPlayerTurnNumber);
        playerStatsMsg.setData(
                DataKey.PLAYER_STATS,
                new PlayerStats(
                        player.getPlayerName(),
                        player.getPlayArea().getHealth(),
                        player.getPlayArea().getTurnDamage(),
                        player.getPlayArea().getTurnHealing(),
                        player.getPlayArea().getTurnCoins()));
        return playerStatsMsg;
    }

    public static Message createCheatStatusMessage(boolean isCheating) {
        Message cheatStatusMsg = new Message(MessageType.CHEAT);
        cheatStatusMsg.setData(DataKey.CHEAT_ACTIVATE, isCheating);
        return cheatStatusMsg;
    }
}
