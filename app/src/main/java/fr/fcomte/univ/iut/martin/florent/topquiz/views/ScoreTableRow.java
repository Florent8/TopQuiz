package fr.fcomte.univ.iut.martin.florent.topquiz.views;

import android.content.Context;
import android.widget.TableRow;
import android.widget.TextView;

import lombok.experimental.FieldDefaults;

import static android.view.Gravity.END;
import static android.view.Gravity.START;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.dimen.textview_table_padding;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE, makeFinal = true)
public final class ScoreTableRow extends TableRow {

    int dim = getResources().getDimensionPixelSize(textview_table_padding);
    Context context;

    private ScoreTableRow(final Context context) {
        super(context);
        this.context = context;
    }

    public ScoreTableRow(final Context context, final String name, final byte score) {
        this(context);
        addTextView(name, START);
        addTextView(Byte.toString(score), END);
    }

    private void addTextView(final String txt, final int gravity) {
        final TextView textView = new TextView(context);
        textView.setPadding(dim, dim, dim, dim);
        textView.setText(txt);
        textView.setGravity(gravity);
        addView(textView);
    }
}
