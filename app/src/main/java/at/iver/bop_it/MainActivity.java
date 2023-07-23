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

import static at.iver.bop_it.network.Communication.generatePromptMessage;
import static at.iver.bop_it.network.Communication.generateFinishMessage;

public class MainActivity extends AppCompatActivity implements UIUpdateListener {

    private FragmentContainerView fragmentContainerView;
    private ServerThread server;
    private static final int connectionPort = 1337;
    private boolean isHost = false;
    private int playerId= -1;
    private static final String TAG = MainActivity.class.getSimpleName();
    protected String playerName = "";
    private String connectionIP;
    private static ClientConnector connection;
    private final Context context = this;

    Class<? extends AbstractPrompt>[]
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


    public void giveRandomPrompt(View v) throws IllegalAccessException, InstantiationException, IOException {
        // TODO: Get which prompt to do from server, instead of generating itself.

        int randomIndex = new Random().nextInt(possiblePrompts.length);

        server.sendMessageToAllClients(generatePromptMessage(randomIndex));

    }

    public void promptComplete(long takenTime) throws IOException {
        Log.i(TAG, "Prompt complete, time taken: " + takenTime);
        connection.sendMessage(generateFinishMessage(playerId,takenTime));
        long[] takenTimeArray = new long[2];
        takenTimeArray[0] = takenTime;
        swapFragmentToWaiting(takenTimeArray);
    }

    public void showResult(long[] takenTime) {
        long[] adjustedResults = new long[2];
        adjustedResults[0] = takenTime[playerId];
        adjustedResults[1] = takenTime[1-playerId];
        swapFragmentToWaiting(adjustedResults);
    }

    public void swapFragmentTo(int prompt) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(
                    fragmentContainerView.getId(), possiblePrompts[prompt].newInstance());
            fragmentTransaction.commit();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void swapFragmentToWaiting(long[] results) {
        try {
            WaitingFragment waitingFragment = WaitingFragment.class.newInstance();
            waitingFragment.setResults(results);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(fragmentContainerView.getId(), waitingFragment);
            if (!isHost) {
                Button button = findViewById(R.id.btn_prompt);
                button.setVisibility(View.INVISIBLE);
            }
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

        //startGame(new View(context));
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
        runOnUiThread(
                () -> {
                    if (message.getType() == MessageType.PROMPT) {
                        swapFragmentTo((int)message.getData(DataKey.TYPE));
                    }
                    if (message.getType() == MessageType.GIVE_ID) {
                        playerId = (int) message.getData(DataKey.ID);
                        Log.d(TAG, "Player ID: " + playerId);
                    }
                    if(message.getType() == MessageType.RESULTS){
                        long[] takenTime = (long[]) message.getData(DataKey.RESULTS);
                        showResult(takenTime);
                    }
                });
    }

    public void clientCallback(boolean success) {
        runOnUiThread(
                () -> {
                    if (!isHost) {
                        Button join = (Button) findViewById(R.id.join2);
                        join.setVisibility(View.VISIBLE);

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

        setContentView(R.layout.activity_main);
        fragmentContainerView = findViewById(R.id.fragmentContainerView2);

        if (!isHost) {
            swapFragmentToWaiting(new long[2]);

            Log.i(TAG, "Starting game as client");
        }
        else {
            Log.i(TAG, "Starting game as host");
        }
    }
}
