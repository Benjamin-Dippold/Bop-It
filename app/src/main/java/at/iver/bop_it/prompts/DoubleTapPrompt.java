package at.iver.bop_it.prompts;

import at.iver.bop_it.R;

public class DoubleTapPrompt extends AbstractPrompt {
    public DoubleTapPrompt() {
        super(R.layout.double_tap_prompt);
    }

    @Override
    protected void onDoubleTap() {
        callBackVictorious();
    }
}
