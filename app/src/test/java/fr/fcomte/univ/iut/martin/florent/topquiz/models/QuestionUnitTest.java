package fr.fcomte.univ.iut.martin.florent.topquiz.models;

import com.google.gson.Gson;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertNotNull;

public final class QuestionUnitTest {

    @Test
    public void testQuestionsFromJSON() throws IOException {
        final Question[] questions = new Gson().fromJson(
                new InputStreamReader(
                        new FileInputStream(new File("app/src/main/assets/questions.json"))),
                Question[].class
        );

        for (final Question question : questions) {
            assertNotNull(question.question());
            assertNotNull(question.answer1());
            assertNotNull(question.answer2());
            assertNotNull(question.answer3());
            assertNotNull(question.answer4());
            assertNotNull(question.goodAnswer());
        }
    }
}
