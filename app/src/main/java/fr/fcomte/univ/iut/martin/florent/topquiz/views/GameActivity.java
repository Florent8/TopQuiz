package fr.fcomte.univ.iut.martin.florent.topquiz.views;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.fcomte.univ.iut.martin.florent.topquiz.managers.QuestionBank;
import fr.fcomte.univ.iut.martin.florent.topquiz.models.Question;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.RED;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.color.buttonGoodAnswer;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.answer1_btn;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.answer2_btn;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.answer3_btn;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.answer4_btn;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.question_text;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.layout.activity_game;
import static lombok.AccessLevel.PRIVATE;

/**
 * Activité où se déroule le jeu <br/>
 * Générée depuis {@link MainActivity}, elle lui retournera le score du joueur lorsque'elle se terminera
 */
@FieldDefaults(level = PRIVATE)
@Accessors(fluent = true)
public final class GameActivity extends AppCompatActivity implements Button.OnClickListener {

    public static final String BUNDLE_EXTRA_SCORE    = "BUNDLE_EXTRA_SCORE";
    static final        String BUNDLE_STATE_SCORE    = "currentScore";
    static final        String BUNDLE_STATE_QUESTION = "currentQuestion";
    static final        String BUNDLE_STATE_PRECEDENT_QUESTIONS_IDS_LIST
                                                     = "precedentQuestionsIdsList";
    static final        String BUNDLE_STATE_PRECEDENT_QUESTIONS_IDS_STRING
                                                     = "precedentQuestionsIdsString";
    static final        String BUNDLE_STATE_COLOR    = "answersButtonColor";
    QuestionBank bank;
    byte numberOfQuestions = 1;
    byte score             = 0;
    @Getter Question q;
    TextView question;
    Button   answer1;
    Button   answer2;
    Button   answer3;
    Button   answer4;
    boolean enabledClick = true;

    /**
     * Initialisation des attributs l'instance de {@link GameActivity} <br/>
     * Affichage de la première question
     *
     * @param savedInstanceState bundle
     * @see QuestionBank#getQuestion()
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_game);

        bank = new QuestionBank(this);

        question = findViewById(question_text);
        answer1 = findViewById(answer1_btn);
        answer1.setOnClickListener(this);
        answer2 = findViewById(answer2_btn);
        answer2.setOnClickListener(this);
        answer3 = findViewById(answer3_btn);
        answer3.setOnClickListener(this);
        answer4 = findViewById(answer4_btn);
        answer4.setOnClickListener(this);

        q = bank.getQuestion();
        displayQuestion();
    }

    /**
     * Affichage sur l'interface d'une question choisie aléatoirement
     */
    private void displayQuestion() {
        question.setText(q.question());

        setButtonDefault(answer1, q.answer1());
        setButtonDefault(answer2, q.answer2());
        setButtonDefault(answer3, q.answer3());
        setButtonDefault(answer4, q.answer4());
    }

    /**
     * Création de la vue d'un bouton de réponse à la création d'une question
     *
     * @param button bouton
     * @param text   réponse
     * @see GameActivity#setButton(Button, int, String)
     */
    private void setButtonDefault(final Button button, final String text) {
        setButton(button, BLACK, text);
    }

    /**
     * Création de la vue d'un bouton de réponse
     *
     * @param button bouton
     * @param color  couleur du bouton
     * @param text   réponse
     */
    private void setButton(final Button button, final int color, final String text) {
        button.setBackgroundColor(color);
        button.setClickable(color != RED);
        button.setText(text);
    }

    /**
     * Gestion de l'appui sur une réponse <br/>
     * Vérifie si la réponse sélectionnée est correcte ou non <br/>
     * Si la réponse est correcte, deux cas possibles :
     * <ul>
     * <li>affichage de la question suivante (sélectionnée aléatoirement) ;</li>
     * <li>fin de l'activité, retour du score à {@link MainActivity}.</li>
     * </ul>
     *
     * @param view bouton-réponse cliqué
     */
    @Override
    public void onClick(final View view) {
        if (Byte.parseByte(view.getTag().toString()) == q.goodAnswer()) {
            enabledClick = false;
            view.setBackgroundColor(getResources().getColor(buttonGoodAnswer));
            makeText(this, "Bonne réponse !", LENGTH_SHORT).show();
            score += q.score();
            if (--numberOfQuestions == 0)
                new AlertDialog.Builder(this)
                        .setTitle("Jeu terminé")
                        .setMessage("Votre score est de " + score + ".")
                        .setPositiveButton("Ok", (dialogInterface, which) -> {
                            setResult(RESULT_OK, new Intent().putExtra(BUNDLE_EXTRA_SCORE, score));
                            finish();
                        })
                        .create()
                        .show();
            else {
                new Handler().postDelayed(() -> {
                    q = bank.getQuestion();
                    displayQuestion();
                    enabledClick = true;
                }, 1000);
            }
        }
        else {
            view.setClickable(false);
            view.setBackgroundColor(RED);
        }
    }

    /**
     * Gestion de l'activation ou non des clics <br/>
     * Quand le message indiquant au joueur qu'il a trouvé la bonne réponse est affiché, on
     * doit désactiver les clics.
     *
     * @param ev événement de clic
     * @return si le clic est autorisé ou non
     */
    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        return enabledClick && super.dispatchTouchEvent(ev);
    }

    /**
     * Sauvegarde le score,la question courante et les questions déjà posées
     * lors d'une destruction de l'activité
     *
     * @param outState bundle
     */
    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putByte(BUNDLE_STATE_SCORE, score);
        outState.putParcelable(BUNDLE_STATE_QUESTION, q);
        outState.putStringArrayList(BUNDLE_STATE_PRECEDENT_QUESTIONS_IDS_LIST, bank.idsList());
        outState.putSerializable(BUNDLE_STATE_PRECEDENT_QUESTIONS_IDS_STRING, bank.idsString());
        outState.putIntArray(BUNDLE_STATE_COLOR, new int[]{
                ((ColorDrawable) answer1.getBackground()).getColor(),
                ((ColorDrawable) answer2.getBackground()).getColor(),
                ((ColorDrawable) answer3.getBackground()).getColor(),
                ((ColorDrawable) answer4.getBackground()).getColor()
        });
    }

    /**
     * Restaure les valeurs sauvegardées à la destruction de l'activité
     *
     * @param savedInstanceState bundle
     * @see GameActivity#onSaveInstanceState(Bundle)
     */
    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        score = savedInstanceState.getByte(BUNDLE_STATE_SCORE);
        q = savedInstanceState.getParcelable(BUNDLE_STATE_QUESTION);
        bank.idsList(
                savedInstanceState.getStringArrayList(BUNDLE_STATE_PRECEDENT_QUESTIONS_IDS_LIST));
        bank.idsString(
                (StringBuilder) savedInstanceState
                        .getSerializable(BUNDLE_STATE_PRECEDENT_QUESTIONS_IDS_STRING));

        question.setText(q.question());
        answer1.setText(q.answer1());
        answer2.setText(q.answer2());
        answer3.setText(q.answer3());
        answer4.setText(q.answer4());

        final int[] colors = savedInstanceState.getIntArray(BUNDLE_STATE_COLOR);
        assert colors != null;
        answer1.setBackgroundColor(colors[0]);
        answer2.setBackgroundColor(colors[1]);
        answer3.setBackgroundColor(colors[2]);
        answer4.setBackgroundColor(colors[3]);
    }
}
