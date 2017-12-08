package fr.fcomte.univ.iut.martin.florent.topquiz.models;

import android.support.annotation.NonNull;

import java.util.Comparator;

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
     * @return score < player.score
     */
    @Override
    public int compareTo(@NonNull final Player player) {
        final int compare = Byte.compare(player.score(), score);
        return compare == 0 ? new PlayerNameComparator().compare(this, player) : compare;
    }

    /**
     * Comparator pour les noms des joueurs
     */
    public static final class PlayerNameComparator implements Comparator<Player> {

        /**
         * Trie {@link Player#name} par ordre alphabétique
         *
         * @param p1 joueur 1
         * @param p2 joueur 2
         * @return si le nom de p1 est avant le nom de p2
         */
        @Override
        public int compare(final Player p1, final Player p2) {
            final int compare = p1.name().toUpperCase().compareTo(p2.name().toUpperCase());
            return compare == 0 ? p1.compareTo(p2) : compare;
        }
    }
}
