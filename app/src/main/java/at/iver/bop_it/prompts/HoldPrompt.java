package at.iver.bop_it.prompts;

import at.iver.bop_it.R;

public class HoldPrompt extends AbstractPrompt {

    public HoldPrompt() {
        super(R.layout.hold_prompt);
    }

    @Override
    protected void onLongPress() {
        callBackVictorious();
    }
}
