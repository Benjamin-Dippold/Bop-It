/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.prompts;

import at.iver.bop_it.R;

public class TapPrompt extends AbstractPrompt {

    public TapPrompt() {
        super(R.layout.tap_promt, R.raw.do_tap, R.raw.single_tap_normal);
    }

    @Override
    protected void onSingleTapUp() {
        callBackVictorious();
    }
}
