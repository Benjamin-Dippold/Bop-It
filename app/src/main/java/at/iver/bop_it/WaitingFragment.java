/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WaitingFragment extends Fragment {
    @Nullable @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.waiting_screen, null, false);
    }
    // TODO: Maybe show current score while waiting.
}
