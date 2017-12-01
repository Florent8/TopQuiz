package fr.fcomte.univ.iut.martin.florent.topquiz.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import static lombok.AccessLevel.PRIVATE;

/**
 * Représentation d'une question en base de données <br/>
 * Implémente {@link Parcelable}
 */
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Getter
public final class Question implements Parcelable {

    public static final Creator<Question> CREATOR = new Creator<Question>() {

        @Override
        public Question createFromParcel(final Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(final int size) {
            return new Question[size];
        }
    };
    String question;
    String answer1;
    String answer2;
    String answer3;
    String answer4;
    byte   goodAnswer;
    @NonFinal byte score = 4;

    /**
     * Constructeur pour {@link Parcelable}
     *
     * @param in {@link Parcel}
     */
    private Question(final Parcel in) {
        this(in.readString(), in.readString(), in.readString(), in.readString(), in.readString(),
             in.readByte(), in.readByte()
        );
    }

    /**
     * Réduit de 1 {@link Question#score} avant de retourner la bonne réponse
     *
     * @return {@link Question#goodAnswer}
     */
    public byte getGoodAnswer() {
        score--;
        return goodAnswer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Écris les données à sauvegarder lors d'une « parcelisation »
     *
     * @param parcel {@link Parcel}
     * @param i      flags
     */
    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeString(question);
        parcel.writeString(answer1);
        parcel.writeString(answer2);
        parcel.writeString(answer3);
        parcel.writeString(answer4);
        parcel.writeByte(goodAnswer);
        parcel.writeByte(score);
    }
}
