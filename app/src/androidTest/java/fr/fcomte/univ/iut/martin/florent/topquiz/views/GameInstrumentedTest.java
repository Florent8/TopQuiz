package fr.fcomte.univ.iut.martin.florent.topquiz.views;

import android.graphics.drawable.ColorDrawable;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import lombok.experimental.FieldDefaults;

import static android.graphics.Color.RED;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.color.buttonGoodAnswer;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.answer1_btn;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.answer2_btn;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.answer3_btn;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.answer4_btn;
import static org.junit.Assert.assertEquals;

/**
 * Test instrumenté de {@link GameActivity}
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@FieldDefaults(makeFinal = true)
@RunWith(AndroidJUnit4.class)
public final class GameInstrumentedTest {

    @Rule
    public ActivityTestRule<GameActivity> rule = new ActivityTestRule<>(GameActivity.class);

    /**
     * Test la couleur des boutons sur une question
     *
     * @see GameActivity#onClick(View)
     */
    @Test
    public void answer() {
        final GameActivity activity = rule.getActivity();
        final byte goodAnswer = activity.q().goodAnswer();

        onView(withId(answer1_btn)).perform(click());
        assertEquals(goodAnswer == 1 ? activity.getResources().getColor(buttonGoodAnswer) : RED,
                     ((ColorDrawable) activity.findViewById(answer1_btn).getBackground()).getColor()
        );

        if (goodAnswer != 1) {
            onView(withId(answer2_btn)).perform(click());
            assertEquals(goodAnswer == 2 ? activity.getResources().getColor(buttonGoodAnswer) : RED,
                         ((ColorDrawable) activity.findViewById(answer2_btn).getBackground()).getColor()
            );

            if (goodAnswer != 2) {
                onView(withId(answer3_btn)).perform(click());
                assertEquals(goodAnswer == 3 ? activity.getResources().getColor(buttonGoodAnswer) : RED,
                             ((ColorDrawable) activity.findViewById(answer3_btn).getBackground()).getColor()
                );

                if (goodAnswer != 3) {
                    onView(withId(answer4_btn)).perform(click());
                    assertEquals(goodAnswer == 4 ? activity.getResources().getColor(buttonGoodAnswer) : RED,
                                 ((ColorDrawable) activity.findViewById(answer4_btn).getBackground())
                                         .getColor()
                    );
                }
            }
        }
    }
}
