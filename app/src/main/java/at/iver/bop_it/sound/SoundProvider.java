package at.iver.bop_it.sound;

import android.content.Context;
import android.media.MediaPlayer;

import at.iver.bop_it.R;

public class SoundProvider {

    private static SoundProvider instance;

    private VoiceLine voiceLine = VoiceLine.NORMAL;

    private SoundProvider(){
    }

    public static SoundProvider getInstance(){
        if(instance == null){
            instance = new SoundProvider();
        }
        return instance;
    }

    public void setVoiceLine(VoiceLine voiceLine) {
        this.voiceLine = voiceLine;
    }
    public void playDoubletap(Context context){
        switch (voiceLine) {
            case DIRTY:
                final MediaPlayer dirtyPlayer = MediaPlayer.create(context, R.raw.double_tap);
                dirtyPlayer.start();
                break;
            case NORMAL:
                final MediaPlayer normalPlayer = MediaPlayer.create(context, R.raw.double_tap_normal);
                normalPlayer.start();
                break;
            default:
                throw new IllegalArgumentException("Unknown voice line: " + voiceLine);
        }
    }

    public void playSingletap(Context context){
        switch (voiceLine) {
            case DIRTY:
                final MediaPlayer dirtyPlayer = MediaPlayer.create(context, R.raw.single_tap);
                dirtyPlayer.start();
                break;
            case NORMAL:
                final MediaPlayer normalPlayer = MediaPlayer.create(context, R.raw.single_tap_normal);
                normalPlayer.start();
                break;
            default:
                throw new IllegalArgumentException("Unknown voice line: " + voiceLine);
        }
    }

    public void playFling(Context context){
        switch (voiceLine) {
            case DIRTY:
                final MediaPlayer dirtyPlayer = MediaPlayer.create(context, R.raw.fling);
                dirtyPlayer.start();
                break;
            case NORMAL:
                final MediaPlayer normalPlayer = MediaPlayer.create(context, R.raw.fling_normal);
                normalPlayer.start();
                break;
            default:
                throw new IllegalArgumentException("Unknown voice line: " + voiceLine);
        }
    }

    public void playHold(Context context){
        switch (voiceLine) {
            case DIRTY:
                final MediaPlayer dirtyPlayer = MediaPlayer.create(context, R.raw.hold);
                dirtyPlayer.start();
                break;
            case NORMAL:
                final MediaPlayer normalPlayer = MediaPlayer.create(context, R.raw.hold_normal);
                normalPlayer.start();
                break;
            default:
                throw new IllegalArgumentException("Unknown voice line: " + voiceLine);
        }
    }

    public void playPinch(Context context){
        switch (voiceLine) {
            case DIRTY:
                final MediaPlayer dirtyPlayer = MediaPlayer.create(context, R.raw.pinch);
                dirtyPlayer.start();
                break;
            case NORMAL:
                final MediaPlayer normalPlayer = MediaPlayer.create(context, R.raw.pinch_normal);
                normalPlayer.start();
                break;
            default:
                throw new IllegalArgumentException("Unknown voice line: " + voiceLine);
        }
    }

    public void playShake(Context context){
        switch (voiceLine) {
            case DIRTY:
                final MediaPlayer dirtyPlayer = MediaPlayer.create(context, R.raw.shake);
                dirtyPlayer.start();
                break;
            case NORMAL:
                final MediaPlayer normalPlayer = MediaPlayer.create(context, R.raw.shake_normal);
                normalPlayer.start();
                break;
            default:
                throw new IllegalArgumentException("Unknown voice line: " + voiceLine);
        }
    }

    public void playTurn(Context context){
        switch (voiceLine) {
            case DIRTY:
                final MediaPlayer dirtyPlayer = MediaPlayer.create(context, R.raw.turn);
                dirtyPlayer.start();
                break;
            case NORMAL:
                final MediaPlayer normalPlayer = MediaPlayer.create(context, R.raw.turn_normal);
                normalPlayer.start();
                break;
            default:
                throw new IllegalArgumentException("Unknown voice line: " + voiceLine);
        }
    }

    public void playVolumeUp(Context context){
        switch (voiceLine) {
            case DIRTY:
                final MediaPlayer dirtyPlayer = MediaPlayer.create(context, R.raw.volume_up);
                dirtyPlayer.start();
                break;
            case NORMAL:
                final MediaPlayer normalPlayer = MediaPlayer.create(context, R.raw.volume_up_normal);
                normalPlayer.start();
                break;
            default:
                throw new IllegalArgumentException("Unknown voice line: " + voiceLine);
        }
    }

    public void playVolumeDown(Context context){
        switch (voiceLine) {
            case DIRTY:
                final MediaPlayer dirtyPlayer = MediaPlayer.create(context, R.raw.volume_down);
                dirtyPlayer.start();
                break;
            case NORMAL:
                final MediaPlayer normalPlayer = MediaPlayer.create(context, R.raw.volume_down_normal);
                normalPlayer.start();
                break;
            default:
                throw new IllegalArgumentException("Unknown voice line: " + voiceLine);
        }
    }

    public void playZoom(Context context){
        switch (voiceLine) {
            case DIRTY:
                final MediaPlayer dirtyPlayer = MediaPlayer.create(context, R.raw.zoom);
                dirtyPlayer.start();
                break;
            case NORMAL:
                final MediaPlayer normalPlayer = MediaPlayer.create(context, R.raw.zoom_normal);
                normalPlayer.start();
                break;
            default:
                throw new IllegalArgumentException("Unknown voice line: " + voiceLine);
        }
    }
}
