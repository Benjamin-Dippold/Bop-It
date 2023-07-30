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
        super(R.layout.solve_prompt,
                R.raw.do_solve,
                R.raw.solve_normal);
    }

    @Override
    public void onStart() {
        super.onStart();
        View v = getView();

        int questionId = getArguments().getInt("extra");
        question = Question.getQuestionFromId(questionId);

        ((TextView) v.findViewById(R.id.question)).setText(question.getQuestion());

        Button optionA = v.findViewById(R.id.optionA);
        optionA.setText(question.getOptionA());
        optionA.setOnClickListener((View view) -> evaluateResult(question.getOptionA()));

        Button optionB = v.findViewById(R.id.optionB);
        optionB.setText(question.getOptionB());
        optionB.setOnClickListener((View view) -> evaluateResult(question.getOptionB()));

        Button optionC = v.findViewById(R.id.optionC);
        optionC.setText(question.getOptionC());
        optionC.setOnClickListener((View view) -> evaluateResult(question.getOptionC()));

        Button optionD = v.findViewById(R.id.optionD);
        optionD.setText(question.getOptionD());
        optionD.setOnClickListener((View view) -> evaluateResult(question.getOptionD()));
    }

    private void evaluateResult(String pressedAnswer) {
        if (pressedAnswer.equals(question.getCorrectAnswer())) {
            callBackVictorious();
        } else {
            callBackFailure();
        }
    }
}
