package la.ra.ex;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.text.MatchesPattern.matchesPattern;

/**
 * The following are Unit Tests.
 * It should be noted, these are Integrated Unit Tests; They test a graph of objects.
 * That said, they more or less run independently of any framework (other than Spring)
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringTestConfiguration.class)
class EightBallControllerTest {

    private class Prediction extends HashMap<String, String> {
        final String response, time, query;

        Prediction() {
            this(null);
        }

        Prediction(String requestQuery) {
            super(eightBallController.getPrediction(Optional.ofNullable(requestQuery)));
            response = get("response");
            time = get("time");
            query = containsKey("query") ? get("query") : "";
        }

        boolean hasQuery() {
            return containsKey("query");
        }

    }

    private static final String ISO9601FMT;

    static {
        /* Riff on https://stackoverflow.com/questions/28020805/regex-validate-correct-iso8601-date-string-with-time# */
        final String year = "[1-9]\\d{3}";
        final String february = "(?:0[1-9]|1[0-2])-(?:0[1-9]|1\\d|2[0-8])";
        final String thirtyDayMonth = "(?:0[13-9]|1[0-2])-(?:29|30)";
        final String thirtyOneDayMonth = "(?:0[13578]|1[02])-31";
        final String date = String.format("(?:%s-(?:%s|%s|%s)", year, february, thirtyDayMonth, thirtyOneDayMonth);
        final String leapYear = "[1-9]\\d(?:0[48]|[2468][048]|[13579][26])|(?:[2468][048]|[13579][26])00)";
        final String feb29 = "-02-29";
        final String leapDate = String.format("(?:%s%s)", leapYear, feb29);
        final String anyDate = String.format("%s|%s", date, leapDate);
        final String hour = "(?:[01]\\d|2[0-3])";
        final String minute = "[0-5]\\d";
        final String second = "[0-5]\\d";
        final String fractionOfSecond = "(?:\\.\\d+|)";
        final String zone = "(?:Z|[+-][01]\\d:[0-5]\\d)";
        final String time = String.format("%s:%s:%s%s%s", hour, minute, second, fractionOfSecond, zone);
        ISO9601FMT = String.format("^%sT%s$", anyDate, time);
    }

    @Autowired
    private EightBallController eightBallController;

    @Test
    void givenNoQueryTheResponseDoesNotContainAQuery() {
        assertThat("query in response when no query provided", new Prediction().hasQuery(), is(false));
    }

    @Test
    void theResponseContainsATimeThatTheResponseOccurredAt() {
        assertThat("time element is not present", new Prediction().time, not(isEmptyString()));
    }

    @Test
    void theTimeInAResponseIsFormattedAsAnISO9601DateTimeWithZoneInUTC() {
        assertThat("the time format is not correct", new Prediction().time, matchesPattern(ISO9601FMT));
    }

    @Test
    void theResponseContainsAResponseElement() {
        assertThat("response element is not present", new Prediction().response, not(isEmptyString()));
    }

    @Test
    void theResponseIsWithinTheValidSetOfResponses() {
        assertThat("response is not in the valid response list", new Prediction().response, isIn(EightBall.PREDICTIONS));
    }

    @Test
    void givenAQueryTheResponseContainsAQuery() {
        String query = "Will I win the lottery?";
        assertThat("query returned is not the query given", new Prediction(query).query, is(equalTo(query)));
    }
}
