/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.sound;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class SoundProvider {

    private static SoundProvider instance;

    private SoundProvider() {
    }

    public static SoundProvider getInstance() {
        if (instance == null) {
            instance = new SoundProvider();
        }
        return instance;
    }

    public void playVoiceLine(int voiceLine, Context context) {
        if (voiceLine == -1) {
            Log.e("SoundProvider", "No voice line was set!");
            return;
        }

        MediaPlayer player = MediaPlayer.create(context, voiceLine);
        player.start();
    }
}
