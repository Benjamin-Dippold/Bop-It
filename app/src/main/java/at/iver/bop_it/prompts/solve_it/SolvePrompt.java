/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.prompts.solve_it;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import at.iver.bop_it.R;
import at.iver.bop_it.prompts.AbstractPrompt;

public class SolvePrompt extends AbstractPrompt {
    private Question question;

    public SolvePrompt() {
        super(R.layout.solve_prompt);
        // TODO: Figure out how to get this from Server
        question = new Question("12 + 5 = ", "1", "2", "3", "17", 4);
    }

    @Override
    public void onStart() {
        super.onStart();
        View v = getView();
        ((TextView) v.findViewById(R.id.question)).setText(question.getQuestion());

        Button optionA = v.findViewById(R.id.optionA);
        optionA.setText(question.getOptionA());
        optionA.setOnClickListener((View view) -> callBackFailure());

        Button optionB = v.findViewById(R.id.optionB);
        optionB.setText(question.getOptionB());
        optionB.setOnClickListener((View view) -> callBackFailure());

        Button optionC = v.findViewById(R.id.optionC);
        optionC.setText(question.getOptionC());
        optionC.setOnClickListener((View view) -> callBackFailure());

        Button optionD = v.findViewById(R.id.optionD);
        optionD.setText(question.getOptionD());
        optionD.setOnClickListener((View view) -> callBackFailure());

        Button correctBtn = null;
        switch (question.getCorrectAnswer()) {
            case 1:
                correctBtn = optionA;
                break;
            case 2:
                correctBtn = optionB;
                break;
            case 3:
                correctBtn = optionC;
                break;
            case 4:
                correctBtn = optionD;
                break;
        }
        correctBtn.setOnClickListener((View view) -> callBackVictorious());
    }
}
