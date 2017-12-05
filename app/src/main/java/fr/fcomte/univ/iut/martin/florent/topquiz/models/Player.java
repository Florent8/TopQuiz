package fr.fcomte.univ.iut.martin.florent.topquiz.models;

import android.support.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import static lombok.AccessLevel.PRIVATE;

/**
 * Représentation d'un joueur en base de données
 */
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Accessors(fluent = true)
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public final class Player implements Comparable<Player> {

    String name;
    @Setter @NonFinal byte score;

    /**
     * Compare le score de ce joueur à un autre
     *
     * @param player jouer à comparer
     * @return score > player.score
     */
    @Override
    public int compareTo(@NonNull final Player player) {
        return Byte.compare(score, player.score());
    }
}
