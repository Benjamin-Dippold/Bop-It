
package at.iver.bop_it.prompts;

        import android.media.AudioManager;
        import android.media.MediaPlayer;
        import android.os.Bundle;
        import android.view.KeyEvent;
        import android.view.View;

        import at.iver.bop_it.R;
        import at.iver.bop_it.sound.SoundProvider;

public class VolumeDownPrompt extends AbstractPrompt {

    public VolumeDownPrompt() {
        super(R.layout.volumedown_prompt);
    }

    @Override
    protected void playSound() {
        SoundProvider soundProvider = SoundProvider.getInstance();
        soundProvider.playVolumeDown(getContext());
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