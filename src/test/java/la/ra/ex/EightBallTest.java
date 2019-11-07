package la.ra.ex;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class EightBallTest {
    private EightBall eightBall = new EightBall() {
        private int nextIdx = 0;

        @Override
        public String nextResponse() {
            return PREDICTIONS[nextIdx++];
        }
    };
    private static final String[] answers = new String[]{
            "It is certain.",
            "It is decidedly so.",
            "Without a doubt.",
            "Yes - definitely.",
            "You may rely on it.",
            "As I see it, yes.",
            "Most likely.",
            "Outlook good.",
            "Yes.",
            "Signs point to yes.",
            "Reply hazy, try again.",
            "Ask again later.",
            "Better not tell you now.",
            "Cannot predict now.",
            "Concentrate and ask again.",
            "Don't count on it.",
            "My reply is no.",
            "My sources say no.",
            "Outlook not so good.",
            "Very doubtful.",
    };


    @Test
    void containsAllOfTheEightBallAnswers() {
        for (String answer : answers) {
            assertThat("the answer '" + answer + "' is missing from the EightBall", eightBall.nextResponse(), is(equalTo(answer)));
        }
    }

    @Test
    void doesNotHaveExtraAnswers() {
        assertThat("there are too many answers", eightBall.PREDICTIONS.length, is(not(greaterThan(answers.length))));
    }

    @Test
    void isNotMissingAnswers() {
        assertThat("there are too few answers", eightBall.PREDICTIONS.length, is(not(lessThan(answers.length))));
    }
}
