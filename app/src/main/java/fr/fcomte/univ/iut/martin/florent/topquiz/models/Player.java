package fr.fcomte.univ.iut.martin.florent.topquiz.models;

import android.support.annotation.NonNull;

public final class Player implements Comparable<Player> {

    private final byte   id;
    private final String name;
    private       byte   score;

    public Player(final byte id, final String name, final byte score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    public byte getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public byte getScore() {
        return score;
    }

    public void setScore(final byte score) {
        this.score = score;
    }

    @Override
    public int compareTo(@NonNull final Player player) {
        return Byte.compare(score, player.getScore());
    }
}
