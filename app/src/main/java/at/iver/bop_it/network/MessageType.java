/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.network;

public enum MessageType {
    // Client -> Server
    TOUCHED,
    CHOICE,
    END_TURN,
    CHEAT,
    REMATCH_REQUEST,
    NAME,

    // Server -> Client
    ADD_CARD,
    REMOVE_CARD,
    CHOOSE_OPTION,
    UPDATE_PLAYER_STATS, // used to update the player stats of a player
    FULL_CARD_DECK, // used to send the full card deck to the client
    TURN_NOTIFICATION, // used to notify the client that it is his turn
    EXPEND_CHAMPION, // used to notify the client that a champion has been expended
    UNCOVER_CHEAT,
    RESET_CHAMPION, // used to notify the client that all champions have been reset
    REMATCH
}
