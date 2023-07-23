/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.prompts;

import java.io.IOException;

import at.iver.bop_it.R;

public class DoubleTapPrompt extends AbstractPrompt {
    public DoubleTapPrompt() {
        super(R.layout.double_tap_prompt);
    }

    @Override
    protected void onDoubleTap() throws IOException {
        callBackVictorious();
    }
}
