/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.network;

public enum DataKey {
    TARGET_PLAYER, // used to specify the target player of an action
    CARD_ID, // used to specify the card id of an action
    DECK, // used to specify the deck of an action, uses DeckType
    CHOICE,
    PLAYER_STATS, // used to specify the player stats of a player
    OPTIONS,
    CHEAT_ACTIVATE,
    YOUR_TURN, // used to specify if it is the turn of the player
    COLLECTION, // carries the collection of cards
    NAME
}
