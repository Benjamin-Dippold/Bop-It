/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.prompts.solve_it;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Question {
    private final String question;
    private final List<String> answers;
    private final String correctAnswer;

    private static final List<Question> allQuestions =
            List.of(
                    new Question("5 + 3 =", new ArrayList<>(List.of("7", "8", "9", "10")), "8"),
                    new Question("12 - 6 =", new ArrayList<>(List.of("4", "6", "8", "10")), "6"),
                    new Question("2 * 5 =", new ArrayList<>(List.of("8", "10", "12", "15")), "10"),
                    new Question("18 / 3 =", new ArrayList<>(List.of("4", "6", "8", "9")), "6"),
                    new Question("9 + 7 =", new ArrayList<>(List.of("14", "15", "16", "17")), "16"),
                    new Question("20 - 9 =", new ArrayList<>(List.of("8", "9", "10", "11")), "11"),
                    new Question("4 * 6 =", new ArrayList<>(List.of("20", "22", "24", "26")), "20"),
                    new Question("25 / 5 =", new ArrayList<>(List.of("3", "4", "5", "6")), "5"),
                    new Question("12 + 15 =", new ArrayList<>(List.of("25", "26", "27", "28")), "27"),
                    new Question("30 - 14 =", new ArrayList<>(List.of("12", "14", "16", "18")), "16"),
                    new Question("3 * 4 =", new ArrayList<>(List.of("8", "10", "12", "14")), "12"),
                    new Question("27 / 9 =", new ArrayList<>(List.of("2", "3", "4", "5")), "3"),
                    new Question("7 + 9 =", new ArrayList<>(List.of("14", "15", "16", "17")), "16"),
                    new Question("16 - 8 =", new ArrayList<>(List.of("6", "7", "8", "9")), "8"),
                    new Question("5 * 7 =", new ArrayList<>(List.of("30", "32", "34", "35")), "35"),
                    new Question("45 / 9 =", new ArrayList<>(List.of("4", "5", "6", "7")), "5"),
                    new Question("10 + 12 =", new ArrayList<>(List.of("20", "21", "22", "23")), "22"),
                    new Question("24 - 7 =", new ArrayList<>(List.of("15", "16", "17", "18")), "17"),
                    new Question("8 * 3 =", new ArrayList<>(List.of("22", "24", "26", "28")), "24"),
                    new Question("36 / 6 =", new ArrayList<>(List.of("4", "5", "6", "7")), "6"),
                    new Question("15 + 8 =", new ArrayList<>(List.of("22", "23", "24", "25")), "23"),
                    new Question("14 - 5 =", new ArrayList<>(List.of("8", "9", "10", "11")), "9"),
                    new Question("6 * 9 =", new ArrayList<>(List.of("45", "48", "51", "54")), "54"),
                    new Question("72 / 8 =", new ArrayList<>(List.of("7", "8", "9", "10")), "9"),
                    new Question("11 + 19 =", new ArrayList<>(List.of("28", "29", "30", "31")), "30"),
                    new Question("2 + 3 =", new ArrayList<>(List.of("4", "5", "6", "7")), "5"),
                    new Question("8 - 6 =", new ArrayList<>(List.of("1", "2", "3", "4")), "2"),
                    new Question("4 * 5 =", new ArrayList<>(List.of("16", "18", "20", "22")), "20"),
                    new Question("10 / 2 =", new ArrayList<>(List.of("3", "4", "5", "6")), "5"),
                    new Question("9 + 6 =", new ArrayList<>(List.of("13", "14", "15", "16")), "15"),
                    new Question("7 - 3 =", new ArrayList<>(List.of("3", "4", "5", "6")), "4"),
                    new Question("5 * 4 =", new ArrayList<>(List.of("16", "18", "20", "22")), "20"),
                    new Question("12 / 3 =", new ArrayList<>(List.of("2", "3", "4", "5")), "4"),
                    new Question("6 + 7 =", new ArrayList<>(List.of("11", "12", "13", "14")), "13"),
                    new Question("10 - 5 =", new ArrayList<>(List.of("3", "4", "5", "6")), "5"),
                    new Question("8 * 3 =", new ArrayList<>(List.of("16", "24", "32", "40")), "24"),
                    new Question("15 / 5 =", new ArrayList<>(List.of("2", "3", "4", "5")), "3"),
                    new Question("4 + 9 =", new ArrayList<>(List.of("11", "12", "13", "14")), "13"),
                    new Question("14 - 8 =", new ArrayList<>(List.of("3", "4", "5", "6")), "6"),
                    new Question("6 * 6 =", new ArrayList<>(List.of("24", "30", "36", "42")), "36"),
                    new Question("18 / 3 =", new ArrayList<>(List.of("4", "5", "6", "7")), "6"),
                    new Question("5 + 8 =", new ArrayList<>(List.of("12", "13", "14", "15")), "13"),
                    new Question("9 - 2 =", new ArrayList<>(List.of("5", "6", "7", "8")), "7"),
                    new Question("7 * 3 =", new ArrayList<>(List.of("18", "20", "21", "24")), "23"),
                    new Question("21 / 7 =", new ArrayList<>(List.of("2", "3", "4", "5")), "3"),
                    new Question("3 + 5 =", new ArrayList<>(List.of("7", "8", "9", "10")), "8"),
                    new Question("12 - 4 =", new ArrayList<>(List.of("6", "7", "8", "9")), "8"),
                    new Question("8 * 4 =", new ArrayList<>(List.of("26", "28", "30", "32")), "32"),
                    new Question("24 / 4 =", new ArrayList<>(List.of("4", "5", "6", "7")), "6"),
                    new Question("6 + 4 =", new ArrayList<>(List.of("9", "10", "11", "12")), "10"),
                    new Question("11 - 7 =", new ArrayList<>(List.of("3", "4", "5", "6")), "4"),
                    new Question("7 * 5 =", new ArrayList<>(List.of("30", "35", "40", "45")), "35"),
                    new Question("15 / 5 =", new ArrayList<>(List.of("2", "3", "4", "5")), "3"),
                    new Question("8 + 3 =", new ArrayList<>(List.of("10", "11", "12", "13")), "11"),
                    new Question("9 - 4 =", new ArrayList<>(List.of("4", "5", "6", "7")), "5"),
                    new Question("3 * 8 =", new ArrayList<>(List.of("20", "21", "22", "24")), "24"),
                    new Question("24 / 8 =", new ArrayList<>(List.of("2", "3", "4", "5")), "3"),
                    new Question("5 + 2 =", new ArrayList<>(List.of("6", "7", "8", "9")), "7"),
                    new Question("10 - 6 =", new ArrayList<>(List.of("3", "4", "5", "6")), "4"),
                    new Question("4 * 7 =", new ArrayList<>(List.of("24", "25", "28", "30")), "28"),
                    new Question("28 / 4 =", new ArrayList<>(List.of("6", "7", "8", "9")), "7"),
                    new Question("9 + 8 =", new ArrayList<>(List.of("15", "16", "17", "18")), "17"),
                    new Question("14 - 5 =", new ArrayList<>(List.of("7", "8", "9", "10")), "9"),
                    new Question("6 * 8 =", new ArrayList<>(List.of("36", "40", "42", "48")), "48"),
                    new Question("48 / 8 =", new ArrayList<>(List.of("4", "5", "6", "7")), "6"),
                    new Question("3 + 6 =", new ArrayList<>(List.of("8", "9", "10", "11")), "9"),
                    new Question("11 - 8 =", new ArrayList<>(List.of("2", "3", "4", "5")), "3"),
                    new Question("7 * 6 =", new ArrayList<>(List.of("36", "42", "48", "54")), "42"),
                    new Question("42 / 6 =", new ArrayList<>(List.of("6", "7", "8", "9")), "7"),
                    new Question("4 + 3 =", new ArrayList<>(List.of("6", "7", "8", "9")), "7"),
                    new Question("9 - 6 =", new ArrayList<>(List.of("2", "3", "4", "5")), "3"),
                    new Question("5 * 9 =", new ArrayList<>(List.of("35", "40", "45", "50")), "45"),
                    new Question("45 / 9 =", new ArrayList<>(List.of("4", "5", "6", "7")), "5"),
                    new Question("8 + 7 =", new ArrayList<>(List.of("14", "15", "16", "17")), "15"),
                    new Question("13 - 9 =", new ArrayList<>(List.of("3", "4", "5", "6")), "4"),
                    new Question("6 * 7 =", new ArrayList<>(List.of("36", "40", "42", "48")), "42"),
                    new Question("42 / 7 =", new ArrayList<>(List.of("5", "6", "7", "8")), "6"),
                    new Question("4 + 2 =", new ArrayList<>(List.of("5", "6", "7", "8")), "6"),
                    new Question("10 - 4 =", new ArrayList<>(List.of("4", "5", "6", "7")), "6"),
                    new Question("3 * 9 =", new ArrayList<>(List.of("24", "27", "30", "33")), "27"),
                    new Question("27 / 9 =", new ArrayList<>(List.of("2", "3", "4", "5")), "3"),
                    new Question("7 + 6 =", new ArrayList<>(List.of("12", "13", "14", "15")), "13"),
                    new Question("9 - 5 =", new ArrayList<>(List.of("3", "4", "5", "6")), "4"),
                    new Question("5 * 8 =", new ArrayList<>(List.of("32", "36", "40", "45")), "40"),
                    new Question("40 / 8 =", new ArrayList<>(List.of("4", "5", "6", "7")), "5"),
                    new Question("6 + 8 =", new ArrayList<>(List.of("14", "15", "16", "17")), "14"),
                    new Question("15 - 7 =", new ArrayList<>(List.of("6", "7", "8", "9")), "8"),
                    new Question("8 * 5 =", new ArrayList<>(List.of("30", "35", "40", "45")), "40"),
                    new Question("40 / 5 =", new ArrayList<>(List.of("8", "9", "10", "11")), "8"),
                    new Question("3 + 4 =", new ArrayList<>(List.of("6", "7", "8", "9")), "7"),
                    new Question("10 - 3 =", new ArrayList<>(List.of("4", "5", "6", "7")), "7"),
                    new Question("6 * 5 =", new ArrayList<>(List.of("25", "30", "35", "40")), "30"),
                    new Question("35 / 5 =", new ArrayList<>(List.of("5", "6", "7", "8")), "7"),
                    new Question("7 + 5 =", new ArrayList<>(List.of("11", "12", "13", "14")), "12"),
                    new Question("15 - 6 =", new ArrayList<>(List.of("7", "8", "9", "10")), "9"),
                    new Question("8 * 6 =", new ArrayList<>(List.of("42", "45", "48", "50")), "48"),
                    new Question("48 / 6 =", new ArrayList<>(List.of("6", "7", "8", "9")), "8"),
                    new Question("5 + 6 =", new ArrayList<>(List.of("10", "11", "12", "13")), "11"),
                    new Question("12 - 7 =", new ArrayList<>(List.of("3", "4", "5", "6")), "5"),
                    new Question("7 * 4 =", new ArrayList<>(List.of("24", "25", "28", "30")), "28"));

    public static int getRandomQuestionId() {
        return new Random().nextInt(allQuestions.size());
    }

    public static Question getQuestionFromId(int id) {
        Question q = allQuestions.get(id);
        Collections.shuffle(q.answers);
        return q;
    }

    public Question(
            String question, List<String> answers, String correctAnswer) {
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getOptionA() {
        return answers.get(0);
    }

    public String getOptionB() {
        return answers.get(1);
    }

    public String getOptionC() {
        return answers.get(2);
    }

    public String getOptionD() {
        return answers.get(3);
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
