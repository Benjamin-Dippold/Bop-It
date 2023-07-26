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

    @SuppressLint("SetTextI18n")
    @Nullable @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View v = inflater.inflate(R.layout.waiting_screen, null, false);

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
        ((TextView) v.findViewById(R.id.myScore)).setText(playerResult);

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
        ((TextView) v.findViewById(R.id.enemyScore)).setText(otherPlayerResult);

        return v;
    }

    public void setResults(long[] results) {
        this.results = results;
    }
    // TODO: Maybe show current score while waiting.
}
