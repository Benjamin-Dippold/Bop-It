/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import at.iver.bop_it.prompts.*;
import at.iver.bop_it.sound.SoundProvider;
import at.iver.bop_it.sound.VoiceLine;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private FragmentContainerView fragmentContainerView;
    private SoundProvider sp = SoundProvider.getInstance();
    VoiceLine[] voices = VoiceLine.values();
    private int currentVoice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentContainerView = findViewById(R.id.fragmentContainerView2);

        // TODO: Like all the server shit
    }

    public void giveRandomPrompt(View v) {
        // TODO: Get which prompt to do from server, instead of generating itself.
        Class<? extends AbstractPrompt>[] possiblePrompts;
        possiblePrompts =
                new Class[] {
                    FlingPrompt.class,
                    TapPrompt.class,
                    DoubleTapPrompt.class,
                    HoldPrompt.class,
                    ShakePrompt.class,
                    TurnPrompt.class,
                    PinchPrompt.class,
                    ZoomPrompt.class,
                        VolumeUpPrompt.class,
                        VolumeDownPrompt.class
                };
        int randomIndex = new Random().nextInt(possiblePrompts.length);
        swapFragmentTo(possiblePrompts[randomIndex]);
    }

    public void changeVoice(View view) {
        currentVoice = (currentVoice + 1) % voices.length;
        sp.setVoiceLine(voices[currentVoice]);
    }


    public void promptComplete(long takenTime) {
        //  Toast.makeText(this, "You did it in " + takenTime + "ms!", Toast.LENGTH_SHORT).show();
        swapFragmentToWaiting(takenTime);
        // TODO: Tell server how long it took and stuff.
    }

    public void swapFragmentTo(Class<? extends AbstractPrompt> targetFragment) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(
                    fragmentContainerView.getId(), targetFragment.newInstance());
            fragmentTransaction.commit();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void swapFragmentToWaiting(long takenTime) {
        try {
            WaitingFragment waitingFragment = WaitingFragment.class.newInstance();
            waitingFragment.setTakenTime(takenTime);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(fragmentContainerView.getId(), waitingFragment);
            fragmentTransaction.commit();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
