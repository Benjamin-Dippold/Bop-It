/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import at.iver.bop_it.prompts.AbstractPrompt;
import at.iver.bop_it.prompts.DoubleTapPrompt;
import at.iver.bop_it.prompts.FlingPrompt;
import at.iver.bop_it.prompts.HoldPrompt;
import at.iver.bop_it.prompts.ShakePrompt;
import at.iver.bop_it.prompts.TapPrompt;
import at.iver.bop_it.prompts.TurnPrompt;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private FragmentContainerView fragmentContainerView;

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
                    TurnPrompt.class
                };
        int randomIndex = new Random().nextInt(possiblePrompts.length);
        swapFragmentTo(possiblePrompts[randomIndex]);
    }

    public void promptComplete(long takenTime) {
        Toast.makeText(this, "You did it in " + takenTime + "ms!", Toast.LENGTH_SHORT).show();
        swapFragmentTo(WaitingFragment.class);
        // TODO: Tell server how long it took and stuff.
    }

    public void swapFragmentTo(Class<? extends Fragment> targetFragment) {
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
}
