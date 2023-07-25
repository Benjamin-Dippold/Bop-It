/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.network;

public enum MessageType {
    // Client -> Server
    GIVE_ID,
    PROMPT,
    RESULTS,
    // Server -> Client
    FINISH

}
