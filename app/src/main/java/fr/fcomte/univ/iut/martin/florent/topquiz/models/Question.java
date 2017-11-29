package fr.fcomte.univ.iut.martin.florent.topquiz.models;

public final class Question {

    private String question;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private byte   goodAnswer;
    private byte score = 4;

    public Question(final String question, final String answer1, final String answer2, final String answer3, final String answer4, final byte goodAnswer) {
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.goodAnswer = goodAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer1() {
        return answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public byte getScore() {
        return score;
    }

    public byte getGoodAnswer() {
        score--;
        return goodAnswer;
    }
}
