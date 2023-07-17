/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it;

import at.iver.bop_it.network.Message;

public interface UIUpdateListener {
    void updateUI(Message message);

    void clientCallback(boolean success);
}
