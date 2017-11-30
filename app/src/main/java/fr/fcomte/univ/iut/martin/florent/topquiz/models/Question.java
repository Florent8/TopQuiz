package fr.fcomte.univ.iut.martin.florent.topquiz.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Représentation d'une question en base de données
 */
@RequiredArgsConstructor
@Getter
public final class Question {

    private final String question;
    private final String answer1;
    private final String answer2;
    private final String answer3;
    private final String answer4;
    private final byte   goodAnswer;
    private byte score = 4;

    /**
     * Réduit de 1 {@link Question#score} avant de retourner la bonne réponse
     *
     * @return {@link Question#goodAnswer}
     */
    public byte getGoodAnswer() {
        score--;
        return goodAnswer;
    }
}
