/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WaitingFragment extends Fragment {
    long takenTime = 0;

    @Nullable @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.waiting_screen, null, false);
        ((TextView) v.findViewById(R.id.txtScore)).setText(takenTime + "ms");
        return v;
    }

    public void setTakenTime(long takenTime) {
        this.takenTime = takenTime;
    }
    // TODO: Maybe show current score while waiting.
}
