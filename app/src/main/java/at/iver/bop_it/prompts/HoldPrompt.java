/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.prompts;

import java.io.IOException;

import at.iver.bop_it.R;

public class HoldPrompt extends AbstractPrompt {

    public HoldPrompt() {
        super(R.layout.hold_prompt);
    }

    @Override
    protected void onLongPress() {
        try {
            callBackVictorious();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
