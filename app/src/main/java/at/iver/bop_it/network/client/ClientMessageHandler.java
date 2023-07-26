/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.network.client;

import at.iver.bop_it.UIUpdateListener;
import at.iver.bop_it.network.IHandleMessage;
import at.iver.bop_it.network.Message;

public class ClientMessageHandler implements IHandleMessage {
    private static final String TAG = "ClientMessageHandler";
    private final UIUpdateListener uiUpdater;

    public ClientMessageHandler(UIUpdateListener uiUpdater) {
        this.uiUpdater = uiUpdater;
    }

    public void handleMessage(Message message) {
        uiUpdater.updateUI(message);
    }
}
