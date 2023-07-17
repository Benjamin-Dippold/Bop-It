/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.prompts.solve_it;

import java.util.List;
import java.util.Random;

public class Question {
    private final String question, optionA, optionB, optionC, optionD;
    private final int correctAnswer;

    // TODO: fill with more Questions
    private static final List<Question> allQuestions =
            List.of(
                    new Question("Q1", "A1", "A2", "A3", "A4", 1),
                    new Question("Q2", "A1", "A2", "A3", "A4", 1),
                    new Question("Q3", "A1", "A2", "A3", "A4", 1),
                    new Question("Q4", "A1", "A2", "A3", "A4", 1),
                    new Question("Q5", "A1", "A2", "A3", "A4", 1),
                    new Question("Q6", "A1", "A2", "A3", "A4", 1));

    public static Question getRandomQuestion() {
        int randomId = new Random().nextInt(allQuestions.size());
        return allQuestions.get(randomId);
    }

    public static Question getQuestionFromId(int id) {
        return allQuestions.get(id);
    }

    public Question(
            String question,
            String optionA,
            String optionB,
            String optionC,
            String optionD,
            int correctAnswer) {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        // TODO: randomize positions of answers
    }

    public String getQuestion() {
        return question;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }
}
