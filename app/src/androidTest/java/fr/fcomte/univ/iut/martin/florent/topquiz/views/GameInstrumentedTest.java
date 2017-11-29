package fr.fcomte.univ.iut.martin.florent.topquiz.views;

import android.graphics.drawable.ColorDrawable;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.fcomte.univ.iut.martin.florent.topquiz.models.Question;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.answer1_btn;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.answer2_btn;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.answer3_btn;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.answer4_btn;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public final class GameInstrumentedTest {

    @Rule
    public final ActivityTestRule<GameActivity> rule = new ActivityTestRule<>(GameActivity.class);

    @Test
    public void answer() {
        final GameActivity activity = rule.getActivity();
        activity.runOnUiThread(() -> activity.displayQuestion(new Question(
                "test", "test", "test", "test", "test", (byte) 4
        )));

        onView(withId(answer1_btn)).perform(click());
        assertEquals(RED, ((ColorDrawable) activity.findViewById(answer1_btn).getBackground()).getColor());

        onView(withId(answer2_btn)).perform(click());
        assertEquals(RED, ((ColorDrawable) activity.findViewById(answer2_btn).getBackground()).getColor());

        onView(withId(answer3_btn)).perform(click());
        assertEquals(RED, ((ColorDrawable) activity.findViewById(answer3_btn).getBackground()).getColor());

        onView(withId(answer4_btn)).perform(click());
        assertEquals(GREEN, ((ColorDrawable) activity.findViewById(answer4_btn).getBackground()).getColor());
    }
}
