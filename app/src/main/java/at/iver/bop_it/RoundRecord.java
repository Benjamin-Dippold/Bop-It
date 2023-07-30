/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.io.Serializable;
import java.util.List;

public class RoundRecord implements Serializable {
    private long playerScore, enemyScore;
    private String prompt;
    private boolean simonMode;

    public RoundRecord(String prompt, boolean simonMode) {
        this.prompt = prompt;
        this.simonMode = simonMode;
    }

    public void setScores(long[] scores) {
        playerScore = scores[0];
        enemyScore = scores[1];
    }

    public long getPlayerScore() {
        return playerScore;
    }

    public long getEnemyScore() {
        return enemyScore;
    }

    public String getPrompt() {
        return prompt;
    }

    public boolean isSimonMode() {
        return simonMode;
    }

    public void swapScores() {
        long temp = playerScore;
        playerScore = enemyScore;
        enemyScore = temp;
    }

    public static class RoundRecordAdapter
            extends RecyclerView.Adapter<RoundRecordAdapter.ViewHolder> {

        private List<RoundRecord> localDataSet;

        /** Provide a reference to the type of views that you are using (custom ViewHolder) */
        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView promptTextView;
            private final TextView timePlayerOneTextView;
            private final TextView timePlayerTwoTextView;
            private final TextView isSimonTextView;

            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View
                promptTextView = (TextView) view.findViewById(R.id.promptName);
                timePlayerOneTextView = (TextView) view.findViewById(R.id.timePlayerOne);
                timePlayerTwoTextView = (TextView) view.findViewById(R.id.timePlayerTwo);
                isSimonTextView = (TextView) view.findViewById(R.id.simonPrompt);
            }

            public void setTexts(RoundRecord record) {
                promptTextView.setText(record.getPrompt() + "");
                timePlayerOneTextView.setText(
                        WaitingFragment.convertResults(record.getPlayerScore()));
                timePlayerTwoTextView.setText(
                        WaitingFragment.convertResults(record.getEnemyScore()));
                if (record.isSimonMode()) isSimonTextView.setText("No");
                else isSimonTextView.setText("Yes");
            }
        }

        /**
         * Initialize the dataset of the Adapter
         *
         * @param dataSet String[] containing the data to populate views to be used by RecyclerView
         */
        public RoundRecordAdapter(List<RoundRecord> dataSet) {
            localDataSet = dataSet;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view =
                    LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.round_record_item, viewGroup, false);

            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            viewHolder.setTexts(localDataSet.get(position));
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return localDataSet.size();
        }
    }
}
