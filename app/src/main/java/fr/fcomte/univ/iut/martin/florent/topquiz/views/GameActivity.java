package fr.fcomte.univ.iut.martin.florent.topquiz.views;

import android.content.Intent;
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

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.answer1_btn;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.answer2_btn;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.answer3_btn;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.answer4_btn;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.question_text;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.layout.activity_game;

/**
 * Activité où se déroule le jeu <br/>
 * Générée depuis {@link MainActivity}, elle lui retournera le score du joueur lorsque'elle se terminera <br/>
 * Hérite de {@link AppCompatActivity} <br/>
 * Implémente {@link Button.OnClickListener}
 */
public final class GameActivity extends AppCompatActivity implements Button.OnClickListener {

    public static final  String BUNDLE_EXTRA_SCORE    = "BUNDLE_EXTRA_SCORE";
    private static final String BUNDLE_STATE_SCORE    = "currentScore";
    private static final String BUNDLE_STATE_QUESTION = "currentQuestion";
    private static final String BUNDLE_STATE_PRECEDENT_QUESTIONS_IDS_LIST
                                                      = "precedentQuestionsIdsList";
    private static final String BUNDLE_STATE_PRECEDENT_QUESTIONS_IDS_STRING
                                                      = "precedentQuestionsIdsString";
    private QuestionBank bank;
    private byte numberOfQuestions = 1;
    private byte score             = 0;
    @Getter private Question q;
    private         TextView question;
    private         Button   answer1;
    private         Button   answer2;
    private         Button   answer3;
    private         Button   answer4;
    private boolean enabledClick = true;

    /**
     * Initialisation des attributs l'instance de {@link GameActivity} <br/>
     * Affichage de la première question
     *
     * @param savedInstanceState {@link Bundle}
     * @see GameActivity#displayQuestion()
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

        displayQuestion();
    }

    /**
     * Affichage sur l'interface d'une question choisie aléatoirement
     *
     * @see QuestionBank#getQuestion()
     */
    private void displayQuestion() {
        this.q = bank.getQuestion();
        question.setText(q.getQuestion());

        answer1.setBackgroundColor(WHITE);
        answer1.setClickable(true);
        answer1.setText(q.getAnswer1());

        answer2.setBackgroundColor(WHITE);
        answer2.setClickable(true);
        answer2.setText(q.getAnswer2());

        answer3.setBackgroundColor(WHITE);
        answer3.setClickable(true);
        answer3.setText(q.getAnswer3());

        answer4.setBackgroundColor(WHITE);
        answer4.setText(q.getAnswer4());
        answer4.setClickable(true);
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
     * @param view {@link View}
     * @see QuestionBank#getQuestion()
     */
    @Override
    public void onClick(final View view) {
        if (Byte.parseByte(view.getTag().toString()) == q.getGoodAnswer()) {
            enabledClick = false;
            view.setBackgroundColor(GREEN);
            makeText(this, "Bonne réponse !", LENGTH_SHORT).show();
            score += q.getScore();
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
     * @param ev {@link MotionEvent}
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
     * @param outState {@link Bundle}
     */
    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putByte(BUNDLE_STATE_SCORE, score);
        outState.putParcelable(BUNDLE_STATE_QUESTION, q);
        outState.putStringArrayList(BUNDLE_STATE_PRECEDENT_QUESTIONS_IDS_LIST, bank.getIdsList());
        outState.putSerializable(BUNDLE_STATE_PRECEDENT_QUESTIONS_IDS_STRING, bank.getIdsString());
    }

    /**
     * Restaure les valeurs sauvegardées à la destruction de l'activité
     *
     * @param savedInstanceState {@link Bundle}
     * @see GameActivity#onSaveInstanceState(Bundle)
     */
    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        score = savedInstanceState.getByte(BUNDLE_STATE_SCORE);
        q = savedInstanceState.getParcelable(BUNDLE_STATE_QUESTION);
        bank.setIdsList(
                savedInstanceState.getStringArrayList(BUNDLE_STATE_PRECEDENT_QUESTIONS_IDS_LIST));
        bank.setIdsString(
                (StringBuilder) savedInstanceState
                        .getSerializable(BUNDLE_STATE_PRECEDENT_QUESTIONS_IDS_STRING));
    }
}
