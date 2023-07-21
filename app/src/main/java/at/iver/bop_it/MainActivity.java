/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import at.iver.bop_it.prompts.*;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private FragmentContainerView fragmentContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        while(!(Settings.System.canWrite(this))) {

            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + this.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
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
                        VolumeDownPrompt.class,
                        BrightnessUpPrompt.class,
                        BrightnessDownPrompt.class,
                        NorthPrompt.class,
                        ThrowPrompt.class
                };
        int randomIndex = new Random().nextInt(possiblePrompts.length);
        swapFragmentTo(possiblePrompts[randomIndex]);
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
