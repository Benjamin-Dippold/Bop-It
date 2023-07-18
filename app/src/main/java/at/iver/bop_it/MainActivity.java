/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import at.iver.bop_it.network.DataKey;
import at.iver.bop_it.network.Message;
import at.iver.bop_it.network.MessageType;
import at.iver.bop_it.network.client.ClientConnector;
import at.iver.bop_it.network.server.ServerThread;
import at.iver.bop_it.prompts.*;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements UIUpdateListener {

    private FragmentContainerView fragmentContainerView;
    private ServerThread server;
    private static final int connectionPort = 1337;
    private boolean isHost = false;
    private int playerId;
    private static final String TAG = MainActivity.class.getSimpleName();
    protected String playerName = "";
    private String connectionIP;
    private static ClientConnector connection;
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // server stuff
        toMainMenu(new View(context));
    }

    public void toMainMenu(View view) {
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

    public void startGame() {
        setContentView(R.layout.activity_main);
        fragmentContainerView = findViewById(R.id.fragmentContainerView2);
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
    public void connectServer(View view) {
        EditText getIp = (EditText) findViewById(R.id.get_text);
        Button join = (Button) findViewById(R.id.join2);

        join.setVisibility(View.INVISIBLE);
        connectionIP = getIp.getText().toString();
        Log.i(TAG, "Connecting to IP: " + connectionIP);
        connection = new ClientConnector(this);

        connection.setConnectionTarget(connectionIP, connectionPort);
        connection.start();

        startGame();
    }


    public void showIp(View view) {
        runOnUiThread(
                () -> {
                    TextView showIp = (TextView) findViewById(R.id.ip_label);
                    TextView showIpOutline = (TextView) findViewById(R.id.ip_label_outline);

                    connectionIP = server.getIpAddr();

                    showIp.setText(connectionIP);
                    showIpOutline.setText(connectionIP);

                    connection = new ClientConnector(this);
                    connection.setConnectionTarget(connectionIP, connectionPort);
                    connection.start();
                });
    }

    @Override
    public void updateUI(Message message) {

    }

    public void clientCallback(boolean success) {
        runOnUiThread(
                () -> {
                    if (!isHost) {
                        Button join = (Button) findViewById(R.id.join2);
                        //join.setVisibility(View.VISIBLE);

                        if (!success) {
                            Toast.makeText(
                                            this,
                                            "Unable to make Connection. Please recheck IP-Address.",
                                            Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        startGame(new View(this));
                    }
                });
    }

    public void startGame(View view) {
        if (isHost) {
            server.setupClients();
        }

        if (isHost) {
            this.playerId = 0;
        } else {
            this.playerId = 1;
        }

        if (!playerName.isEmpty()) {
            Message nameChange = new Message(MessageType.NAME);
            nameChange.setData(DataKey.NAME, playerName);
            nameChange.setData(DataKey.TARGET_PLAYER, playerId);
            try {
                connection.sendMessage(nameChange);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        setContentView(R.layout.activity_main);
        fragmentContainerView = findViewById(R.id.fragmentContainerView2);
    }
}
