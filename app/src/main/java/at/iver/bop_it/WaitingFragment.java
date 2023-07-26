/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WaitingFragment extends Fragment {
    long[] results = new long[2];
    int[] scores = new int[2];
    public static String playerName = "Player";
    public static String enemyName = "Enemy";
    private View v;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.waiting_screen, null, false);

        updateTextViews();

        return v;
    }

    public void updateTextViews() {
        String playerResult;
        switch ((int) results[0]) {
            case 0:
                playerResult = "Waiting for results!";
                break;
            case -1:
                playerResult = "DNF";
                break;
            case -2:
                playerResult = "Failure";
                break;
            default:
                playerResult = results[0] + "ms";
        }
        ((TextView) v.findViewById(R.id.myTime)).setText(playerResult);

        String otherPlayerResult;
        switch ((int) results[1]) {
            case 0:
                otherPlayerResult = "Waiting for results!";
                break;
            case -1:
                otherPlayerResult = "DNF";
                break;
            case -2:
                otherPlayerResult = "Failure";
                break;
            default:
                otherPlayerResult = results[1] + "ms";
        }

        ((TextView) v.findViewById(R.id.enemyTime)).setText(otherPlayerResult);

        if (scores[0] != -1) ((TextView) v.findViewById(R.id.myScore)).setText(scores[0] + "");
        else ((TextView) v.findViewById(R.id.myScore)).setText("Waiting for results!");

        if (scores[1] != -1) ((TextView) v.findViewById(R.id.enemyScore)).setText(scores[1] + "");
        else ((TextView) v.findViewById(R.id.enemyScore)).setText("Waiting for results!");

        ((TextView) v.findViewById(R.id.myTimeLabel)).setText(playerName);
        ((TextView) v.findViewById(R.id.enemyTimeLabel)).setText(enemyName);

    }

    public void setResults(long[] results) {
        this.results = results;
    }

    public void setScores(int[] scores) {
        this.scores = scores;
    }
}
