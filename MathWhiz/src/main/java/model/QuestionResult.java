package model;

/** How the player did on one question (shown on the result page). */
public class QuestionResult {

    private final String question;
    private final String yourAnswer;
    private final String correctAnswer;
    private final boolean correct;
    private final int marks;      // marks earned
    private final int maxMarks;   // marks available

    public QuestionResult(String question, String yourAnswer, String correctAnswer,
                          boolean correct, int maxMarks) {
        this.question = question;
        this.yourAnswer = yourAnswer;
        this.correctAnswer = correctAnswer;
        this.correct = correct;
        this.maxMarks = maxMarks;
        this.marks = correct ? maxMarks : 0;
    }

    public String getQuestion() {
        return question;
    }

    public String getYourAnswer() {
        return yourAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public int getMarks() {
        return marks;
    }

    public int getMaxMarks() {
        return maxMarks;
    }
}
