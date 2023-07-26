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
        ((TextView) v.findViewById(R.id.myScore)).setText(results[0] + "ms");
        ((TextView) v.findViewById(R.id.enemyScore)).setText(results[1] + "ms");
        return v;
    }

    public void setResults(long[] results) {
        this.results = results;
    }
    // TODO: Maybe show current score while waiting.
}
