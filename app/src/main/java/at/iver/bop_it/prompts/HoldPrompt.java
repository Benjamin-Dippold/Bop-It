/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.prompts;

import at.iver.bop_it.R;

public class HoldPrompt extends AbstractPrompt {

    public HoldPrompt() {
        super(R.layout.hold_prompt, R.raw.do_hold, R.raw.hold_normal);
    }

    @Override
    protected void onLongPress() {
        callBackVictorious();
    }
}
