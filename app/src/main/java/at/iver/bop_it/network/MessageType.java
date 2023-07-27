/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.network;

public enum MessageType {
    // Client -> Server
    GIVE_ID,
    PROMPT,
    PROMPT_WITH_EXTRA,
    RESULTS,
    // Server -> Client
    FINISH,
    VICTORY,
    UPDATE_NAME,
    START_GAME
}
