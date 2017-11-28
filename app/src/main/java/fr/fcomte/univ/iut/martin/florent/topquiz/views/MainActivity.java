package fr.fcomte.univ.iut.martin.florent.topquiz.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.name_input;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.start_btn;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.layout.activity_main;

public final class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);

        final Button button = findViewById(start_btn);
        button.setEnabled(false);
        button.setOnClickListener(view -> startActivity(new Intent(this, GameActivity.class)));

        ((EditText) findViewById(name_input)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int i, final int i1, final int i2) {
            }

            @Override
            public void onTextChanged(final CharSequence s, final int i, final int i1, final int i2) {
                button.setEnabled(s.length() != 0);
            }

            @Override
            public void afterTextChanged(final Editable editable) {
            }
        });
    }
}
