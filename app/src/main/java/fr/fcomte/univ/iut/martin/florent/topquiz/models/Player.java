package fr.fcomte.univ.iut.martin.florent.topquiz.models;

import android.support.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Représentation d'un joueur en base de données <br/>
 * Implémente {@link Comparable}
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public final class Player implements Comparable<Player> {

    private final   String name;
    @Setter private byte   score;

    /**
     * Compare le score de ce joueur à un autre
     *
     * @param player {@link Player}
     * @return score > player.score
     */
    @Override
    public int compareTo(@NonNull final Player player) {
        return Byte.compare(score, player.getScore());
    }
}
