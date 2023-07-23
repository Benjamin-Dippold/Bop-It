/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.prompts;

import java.io.IOException;

import at.iver.bop_it.R;

public class TapPrompt extends AbstractPrompt {

    public TapPrompt() {
        super(R.layout.tap_promt);
    }

    @Override
    protected void onSingleTapUp() {
        try {
            callBackVictorious();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
