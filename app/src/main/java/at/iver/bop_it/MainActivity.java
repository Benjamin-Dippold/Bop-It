/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import at.iver.bop_it.network.server.ServerThread;
import at.iver.bop_it.prompts.*;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private FragmentContainerView fragmentContainerView;
    private ServerThread server;
    private static final int connectionPort = 1337;
    private boolean isHost = false;
    private int playerId;
    protected String playerName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentContainerView = findViewById(R.id.fragmentContainerView2);

        // TODO: Like all the server shit
        setContentView(R.layout.menu);
        if (!playerName.isEmpty()) {
            TextView nameInput = findViewById(R.id.name_chooser);
            nameInput.setText(playerName);
            playerName = "";
        }
        if (server != null) {
            try {
                server.stopServer();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            server = null;
        }
        TextView outline = findViewById(R.id.startGamePromptOutline);
        outline.getPaint().setStrokeWidth(5);
        outline.getPaint().setStyle(Paint.Style.STROKE);
        isHost = false;
    }

    public void hostGame(View view) {
        setupName();
        setContentView(R.layout.host);
        startServer(view);
    }

    public void joinGame(View view) {
        setupName();
        setContentView(R.layout.join);
        TextView outline = findViewById(R.id.enterHostPromptOutline);
        outline.getPaint().setStrokeWidth(5);
        outline.getPaint().setStyle(Paint.Style.STROKE);
    }

    public void setupName() {
        TextView nameBox = findViewById(R.id.name_chooser);
        playerName = nameBox.getText().toString();
    }

    public void startServer(View view) {
        isHost = true;
        server = new ServerThread(this, connectionPort);

        TextView showIpOutline = (TextView) findViewById(R.id.ip_label_outline);
        TextView promptOutline = findViewById(R.id.server_prompt_outline);
        showIpOutline.getPaint().setStrokeWidth(5);
        showIpOutline.getPaint().setStyle(Paint.Style.STROKE);
        promptOutline.getPaint().setStrokeWidth(5);
        promptOutline.getPaint().setStyle(Paint.Style.STROKE);

        server.start();
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
                    ZoomPrompt.class
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
