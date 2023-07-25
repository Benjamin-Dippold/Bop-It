/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.prompts.solve_it;

import java.util.List;
import java.util.Random;

public class Question {
    private final String question, optionA, optionB, optionC, optionD;
    private final int correctAnswer;

    private static final List<Question> allQuestions =
            List.of(
                    new Question("5 + 3 =", "7", "8", "9", "10", 2),
                    new Question("12 - 6 =", "4", "6", "8", "10", 2),
                    new Question("2 * 5 =", "8", "10", "12", "15", 2),
                    new Question("18 / 3 =", "4", "6", "8", "9", 2),
                    new Question("9 + 7 =", "14", "15", "16", "17", 3),
                    new Question("20 - 9 =", "8", "9", "10", "11", 4),
                    new Question("4 * 6 =", "20", "22", "24", "26", 1),
                    new Question("25 / 5 =", "3", "4", "5", "6", 3),
                    new Question("12 + 15 =", "25", "26", "27", "28", 3),
                    new Question("30 - 14 =", "12", "14", "16", "18", 3),
                    new Question("3 * 4 =", "8", "10", "12", "14", 3),
                    new Question("27 / 9 =", "2", "3", "4", "5", 2),
                    new Question("7 + 9 =", "14", "15", "16", "17", 3),
                    new Question("16 - 8 =", "6", "7", "8", "9", 3),
                    new Question("5 * 7 =", "30", "32", "34", "35", 4),
                    new Question("45 / 9 =", "4", "5", "6", "7", 2),
                    new Question("10 + 12 =", "20", "21", "22", "23", 3),
                    new Question("24 - 7 =", "15", "16", "17", "18", 3),
                    new Question("8 * 3 =", "22", "24", "26", "28", 2),
                    new Question("36 / 6 =", "4", "5", "6", "7", 3),
                    new Question("15 + 8 =", "22", "23", "24", "25", 2),
                    new Question("14 - 5 =", "8", "9", "10", "11", 2),
                    new Question("6 * 9 =", "45", "48", "51", "54", 4),
                    new Question("72 / 8 =", "7", "8", "9", "10", 3),
                    new Question("11 + 19 =", "28", "29", "30", "31", 3),
                    new Question("2 + 3 =", "4", "5", "6", "7", 2),
                    new Question("8 - 6 =", "1", "2", "3", "4", 2),
                    new Question("4 * 5 =", "16", "18", "20", "22", 3),
                    new Question("10 / 2 =", "3", "4", "5", "6", 3),
                    new Question("9 + 6 =", "13", "14", "15", "16", 3),
                    new Question("7 - 3 =", "3", "4", "5", "6", 2),
                    new Question("5 * 4 =", "16", "18", "20", "22", 3),
                    new Question("12 / 3 =", "2", "3", "4", "5", 3),
                    new Question("6 + 7 =", "11", "12", "13", "14", 3),
                    new Question("10 - 5 =", "3", "4", "5", "6", 3),
                    new Question("8 * 3 =", "16", "24", "32", "40", 2),
                    new Question("15 / 5 =", "2", "3", "4", "5", 2),
                    new Question("4 + 9 =", "11", "12", "13", "14", 3),
                    new Question("14 - 8 =", "3", "4", "5", "6", 4),
                    new Question("6 * 6 =", "24", "30", "36", "42", 3),
                    new Question("18 / 3 =", "4", "5", "6", "7", 3),
                    new Question("5 + 8 =", "12", "13", "14", "15", 2),
                    new Question("9 - 2 =", "5", "6", "7", "8", 3),
                    new Question("7 * 3 =", "18", "20", "21", "24", 3),
                    new Question("21 / 7 =", "2", "3", "4", "5", 2),
                    new Question("3 + 5 =", "7", "8", "9", "10", 2),
                    new Question("12 - 4 =", "6", "7", "8", "9", 3),
                    new Question("8 * 4 =", "26", "28", "30", "32", 4),
                    new Question("24 / 4 =", "4", "5", "6", "7", 3),
                    new Question("6 + 4 =", "9", "10", "11", "12", 2),
                    new Question("11 - 7 =", "3", "4", "5", "6", 2),
                    new Question("7 * 5 =", "30", "35", "40", "45", 2),
                    new Question("15 / 5 =", "2", "3", "4", "5", 2),
                    new Question("8 + 3 =", "10", "11", "12", "13", 2),
                    new Question("9 - 4 =", "4", "5", "6", "7", 2),
                    new Question("3 * 8 =", "20", "21", "22", "24", 4),
                    new Question("24 / 8 =", "2", "3", "4", "5", 2),
                    new Question("5 + 2 =", "6", "7", "8", "9", 2),
                    new Question("10 - 6 =", "3", "4", "5", "6", 2),
                    new Question("4 * 7 =", "24", "25", "28", "30", 3),
                    new Question("28 / 4 =", "6", "7", "8", "9", 2),
                    new Question("9 + 8 =", "15", "16", "17", "18", 3),
                    new Question("14 - 5 =", "7", "8", "9", "10", 3),
                    new Question("6 * 8 =", "36", "40", "42", "48", 4),
                    new Question("48 / 8 =", "4", "5", "6", "7", 3),
                    new Question("3 + 6 =", "8", "9", "10", "11", 2),
                    new Question("11 - 8 =", "2", "3", "4", "5", 2),
                    new Question("7 * 6 =", "36", "42", "48", "54", 2),
                    new Question("42 / 6 =", "6", "7", "8", "9", 2),
                    new Question("4 + 3 =", "6", "7", "8", "9", 2),
                    new Question("9 - 6 =", "2", "3", "4", "5", 2),
                    new Question("5 * 9 =", "35", "40", "45", "50", 3),
                    new Question("45 / 9 =", "4", "5", "6", "7", 2),
                    new Question("8 + 7 =", "14", "15", "16", "17", 2),
                    new Question("13 - 9 =", "3", "4", "5", "6", 2),
                    new Question("6 * 7 =", "36", "40", "42", "48", 3),
                    new Question("42 / 7 =", "5", "6", "7", "8", 2),
                    new Question("4 + 2 =", "5", "6", "7", "8", 2),
                    new Question("10 - 4 =", "4", "5", "6", "7", 3),
                    new Question("3 * 9 =", "24", "27", "30", "33", 2),
                    new Question("27 / 9 =", "2", "3", "4", "5", 2),
                    new Question("7 + 6 =", "12", "13", "14", "15", 2),
                    new Question("9 - 5 =", "3", "4", "5", "6", 2),
                    new Question("5 * 8 =", "32", "36", "40", "45", 3),
                    new Question("40 / 8 =", "4", "5", "6", "7", 2),
                    new Question("6 + 8 =", "14", "15", "16", "17", 1),
                    new Question("15 - 7 =", "6", "7", "8", "9", 3),
                    new Question("8 * 5 =", "30", "35", "40", "45", 3),
                    new Question("40 / 5 =", "8", "9", "10", "11", 1),
                    new Question("3 + 4 =", "6", "7", "8", "9", 2),
                    new Question("10 - 3 =", "4", "5", "6", "7", 4),
                    new Question("6 * 5 =", "25", "30", "35", "40", 2),
                    new Question("35 / 5 =", "5", "6", "7", "8", 3),
                    new Question("7 + 5 =", "11", "12", "13", "14", 2),
                    new Question("15 - 6 =", "7", "8", "9", "10", 3),
                    new Question("8 * 6 =", "42", "45", "48", "50", 3),
                    new Question("48 / 6 =", "6", "7", "8", "9", 3),
                    new Question("5 + 6 =", "10", "11", "12", "13", 2),
                    new Question("12 - 7 =", "3", "4", "5", "6", 3),
                    new Question("7 * 4 =", "24", "25", "28", "30", 3));

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
