package at.iver.bop_it.prompts;

import at.iver.bop_it.R;

public class TapPrompt extends AbstractPrompt {

    public TapPrompt() {
        super(R.layout.tap_promt);
    }

    @Override
    protected void onSingleTapUp() {
        callBackVictorious();
    }
}
