/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it;

import static at.iver.bop_it.network.Communication.generateFinishMessage;
import static at.iver.bop_it.network.Communication.generateNameChangeMessage;
import static at.iver.bop_it.network.Communication.generatePlayAgainMessage;
import static at.iver.bop_it.network.Communication.generateStartGameMessage;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import at.iver.bop_it.network.DataKey;
import at.iver.bop_it.network.Message;
import at.iver.bop_it.network.client.ClientConnector;
import at.iver.bop_it.network.server.ServerThread;
import at.iver.bop_it.prompts.*;
import at.iver.bop_it.prompts.solve_it.SolvePrompt;
import at.iver.bop_it.sound.SoundProvider;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements UIUpdateListener {

    private FragmentContainerView fragmentContainerView;
    private ServerThread server;
    private static final int connectionPort = 1337;
    private boolean isHost = false;
    private int playerId = -1;
    private static final String TAG = MainActivity.class.getSimpleName();
    protected String playerName = "";
    private String connectionIP;
    private static ClientConnector connection;
    private final Context context = this;

    public static Class<? extends AbstractPrompt>[] possiblePrompts =
            new Class[]{
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
                    ThrowPrompt.class,
                    SolvePrompt.class
            };
    public static String[] promptNames =
            new String[]{
                    "Fling it!",
                    "Tap it!",
                    "Double Tap it!",
                    "Hold it!",
                    "Shake it!",
                    "Turn it!",
                    "Pinch it!",
                    "Zoom it!",
                    "Volume Up!",
                    "Volume Down!",
                    "Brightness Up!",
                    "Brightness Down!",
                    "North!",
                    "Throw it!",
                    "Solve it!"
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
        String enteredName = nameBox.getText().toString();
        if (!enteredName.isEmpty()) {
            playerName = enteredName;
        }
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

    public void promptComplete(long takenTime) throws IOException {
        Log.i(TAG, "Prompt complete, time taken: " + takenTime);
        connection.sendMessage(generateFinishMessage(playerId, takenTime));
        long[] takenTimeArray = new long[2];
        takenTimeArray[0] = takenTime;
        swapFragmentToWaiting(takenTimeArray, new int[] {-1, -1});
    }

    public void showResult(long[] takenTime, int[] scores) {
        long[] adjustedResults = new long[2];
        adjustedResults[0] = takenTime[playerId];
        adjustedResults[1] = takenTime[1 - playerId];

        int[] adjustedScores = new int[2];
        adjustedScores[0] = scores[playerId];
        adjustedScores[1] = scores[1 - playerId];

        swapFragmentToWaiting(adjustedResults, adjustedScores);
    }

    public void swapFragmentTo(int prompt, int extra, boolean isSimon) {
        try {
            Fragment newPrompt = possiblePrompts[prompt].newInstance();
            Bundle args = new Bundle();
            args.putBoolean("isSimon", isSimon);
            if (extra != -1) {
                args.putInt("extra", extra);
            }
            newPrompt.setArguments(args);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(fragmentContainerView.getId(), newPrompt, "currentPrompt");

            fragmentTransaction.commit();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void swapFragmentToWaiting(long[] results, int[] scores) {
        try {
            WaitingFragment waitingFragment = WaitingFragment.class.newInstance();
            waitingFragment.setResults(results);
            waitingFragment.setScores(scores);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(
                    fragmentContainerView.getId(), waitingFragment, "waitingFragment");
            fragmentTransaction.commit();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void connectServer(View view) {
        EditText getIp = (EditText) findViewById(R.id.get_text);
        Button join = (Button) findViewById(R.id.joinButton);

        join.setVisibility(View.INVISIBLE);
        connectionIP = getIp.getText().toString();
        Log.i(TAG, "Connecting to IP: " + connectionIP);
        connection = new ClientConnector(this);

        connection.setConnectionTarget(connectionIP, connectionPort);
        connection.start();
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
                    switch (message.getType()) {
                        case PROMPT:
                            swapFragmentTo(
                                    (int) message.getData(DataKey.TYPE),
                                    -1,
                                    (Boolean) message.getData(DataKey.ISSIMON));
                            break;
                        case PROMPT_WITH_EXTRA:
                            swapFragmentTo(
                                    (int) message.getData(DataKey.TYPE),
                                    (int) message.getData(DataKey.EXTRA),
                                    (Boolean) message.getData(DataKey.ISSIMON));
                            break;
                        case GIVE_ID:
                            playerId = (int) message.getData(DataKey.ID);
                            Log.d(TAG, "Player ID: " + playerId);
                            sendName(); // positioned here, to ensure a playerID has been given.
                            break;
                        case RESULTS:
                            long[] takenTime = (long[]) message.getData(DataKey.RESULTS);
                            int[] scores = (int[]) message.getData(DataKey.SCORES);
                            Log.d(TAG, "Score received: " + java.util.Arrays.toString(scores));
                            showResult(takenTime, scores);
                            break;
                        case VICTORY:
                            int winner = (int) message.getData(DataKey.ID);
                            List<RoundRecord> roundHistory = (List<RoundRecord>) message.getData(DataKey.ROUND_RECORDS);
                            endGame(playerId == winner, roundHistory);
                            break;
                        case UPDATE_NAME:
                            String name = (String) message.getData(DataKey.NAME);
                            int id = (int) message.getData(DataKey.ID);
                            Log.i(TAG, "GOTTEN NAME: " + name + " id: " + id);
                            if (id == playerId) {
                                WaitingFragment.playerName = name;
                            } else {
                                WaitingFragment.enemyName = name;
                            }
                            Fragment f = getSupportFragmentManager().findFragmentByTag("waitingFragment");
                            if (f != null)
                                ((WaitingFragment) f).updateTextViews();
                            break;
                        case START_GAME:
                            startGame();
                            break;
                        case PLAY_AGAIN:
                            goToSettings();
                            break;
                    }
                });
    }

    private void endGame(boolean isVictorious, List<RoundRecord> history) {
        Fragment currentPrompt = getSupportFragmentManager().findFragmentByTag("currentPrompt");
        if (currentPrompt != null)
            currentPrompt.onDestroyView(); // make sure the DNF-Timer is no longer running
        setContentView(R.layout.post_game);

        if (isVictorious) {
            ((TextView) findViewById(R.id.victory_message)).setText("You Won!");
        } else {
            ((TextView) findViewById(R.id.victory_message)).setText("You Lost!");
        }

        if (!isHost) {
            for (RoundRecord record : history) {
                record.swapScores();
            }
        }
        ((RecyclerView) findViewById(R.id.postGameHistory)).setLayoutManager(new LinearLayoutManager(this));
        ((RecyclerView) findViewById(R.id.postGameHistory)).setAdapter(new RoundRecord.RoundRecordAdapter(history));

        setupTableHeader();

        if (!isHost) findViewById(R.id.btn_play_again).setVisibility(View.INVISIBLE);
    }

    public void setupTableHeader() {
        View tableHeader = findViewById(R.id.tableHeader);

        TextView prompt = tableHeader.findViewById(R.id.promptName);
        prompt.setText("Prompt");
        prompt.setTypeface(null, Typeface.BOLD);

        TextView player = tableHeader.findViewById(R.id.timePlayerOne);
        player.setText(WaitingFragment.playerName);
        player.setTypeface(null, Typeface.BOLD);

        TextView opponent = tableHeader.findViewById(R.id.timePlayerTwo);
        opponent.setText(WaitingFragment.enemyName);
        opponent.setTypeface(null, Typeface.BOLD);

        TextView isSimonText = tableHeader.findViewById(R.id.simonPrompt);
        isSimonText.setText("Fake");
        isSimonText.setTypeface(null, Typeface.BOLD);
    }

    public void clientCallback(boolean success) {
        runOnUiThread(
                () -> {
                    if (!isHost) {
                        Button join = (Button) findViewById(R.id.joinButton);
                        join.setVisibility(View.VISIBLE);

                        if (!success) {
                            Toast.makeText(
                                            this,
                                            "Unable to make Connection. Please recheck IP-Address.",
                                            Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        goToSettings();
                    }
                });
    }

    public void sendName() {
        if (!playerName.isEmpty()) {
            Log.i(TAG, "SENDING NAME: " + playerName + " id: " + playerId);
            connection.sendMessage(generateNameChangeMessage(playerName, playerId));
        }
    }

    public void goToSettings() {
        if (!isHost) {
            setContentView(R.layout.client_waiting_screen);
        } else {
            setContentView(R.layout.settings_screen);
        }
    }

    public void sendStartGameRequest(View v) {
        String requiredScoreText = ((EditText) findViewById(R.id.requiredPointsText)).getText().toString();
        int requiredScore = Integer.parseInt(requiredScoreText);
        boolean simonMode = ((RadioButton) findViewById(R.id.radioHard)).isChecked();

        connection.sendMessage(generateStartGameMessage(requiredScore, simonMode));
    }

    public void startGame() {
        setContentView(R.layout.activity_main);
        fragmentContainerView = findViewById(R.id.fragmentContainerView2);

        swapFragmentToWaiting(new long[2], new int[2]);
    }

    public void playNormalPreview(View v) {
        SoundProvider.getInstance().playVoiceLine(R.raw.do_hold, this);
    }

    public void playImposterPreview(View v) {
        SoundProvider.getInstance().playVoiceLine(R.raw.hold_normal, this);
    }

    public void playAgain(View v) {
        connection.sendMessage(generatePlayAgainMessage());
    }
}
