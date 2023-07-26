/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.prompts;

import at.iver.bop_it.R;

public class FlingPrompt extends AbstractPrompt {

    public FlingPrompt() {
        super(R.layout.fling_prompt, R.raw.do_fling, R.raw.fling_normal);
    }

    @Override
    protected void onFling() {
        callBackVictorious();
    }
}
