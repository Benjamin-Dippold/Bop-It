/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.prompts;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import at.iver.bop_it.R;

public class VolumeDownPrompt extends AbstractPrompt {

    public VolumeDownPrompt() {
        super(R.layout.volumedown_prompt, R.raw.do_volume_down, R.raw.volume_down_normal);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onVolumeDown();
                    return true;
                }
                return false;
            }
        });
    }

    private void onVolumeDown() {
        callBackVictorious();
    }
}
