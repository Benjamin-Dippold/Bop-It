/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.prompts;

import at.iver.bop_it.R;

public class DoubleTapPrompt extends AbstractPrompt {

    public DoubleTapPrompt() {
        super(R.layout.double_tap_prompt, R.raw.do_double_tab, R.raw.double_tap_normal);
    }

    @Override
    protected void onDoubleTap() {
        callBackVictorious();
    }
}
