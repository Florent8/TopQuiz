package fr.fcomte.univ.iut.martin.florent.topquiz.views;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.fcomte.univ.iut.martin.florent.topquiz.managers.QuestionBank;
import fr.fcomte.univ.iut.martin.florent.topquiz.models.Question;

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

public final class GameActivity extends AppCompatActivity implements Button.OnClickListener {

    private QuestionBank bank;
    private Question     q;
    private TextView     question;
    private Button       answer1;
    private Button       answer2;
    private Button       answer3;
    private Button       answer4;

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

        displayQuestion(bank.getQuestion());
    }

    void displayQuestion(Question q) {
        this.q = q;
        question.setText(q.getQuestion());
        answer1.setBackgroundColor(WHITE);
        answer1.setText(q.getAnswer1());
        answer2.setBackgroundColor(WHITE);
        answer2.setText(q.getAnswer2());
        answer3.setBackgroundColor(WHITE);
        answer3.setText(q.getAnswer3());
        answer4.setBackgroundColor(WHITE);
        answer4.setText(q.getAnswer4());
    }

    @Override
    public void onClick(View view) {
        if (Byte.parseByte(view.getTag().toString()) == q.getGoodAnswer()) {
            view.setBackgroundColor(GREEN);
            makeText(this, "Bonne réponse !", LENGTH_SHORT).show();
            new Handler().postDelayed(() -> displayQuestion(bank.getQuestion()), 1000);
        } else
            view.setBackgroundColor(RED);
    }
}
