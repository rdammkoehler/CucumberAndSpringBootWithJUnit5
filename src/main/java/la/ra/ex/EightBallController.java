package la.ra.ex;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

@RestController
public class EightBallController {

    private EightBall eightBall = new EightBall() {
        private Random RANDOM = new Random();

        @Override
        public String nextResponse() {
            return PREDICTIONS[RANDOM.nextInt(PREDICTIONS.length)];
        }
    };

    @GetMapping("/prediction")
    public @ResponseBody
    HashMap<String, String> getPrediction(@RequestParam("query") Optional<String> query) {
        return new HashMap<>() {{
            put("time", isoDateString());
            put("response", eightBall.nextResponse());
            query.ifPresent(s -> put("query", s));
        }};
    }

    private String isoDateString() {
        return DateTimeFormatter.ISO_INSTANT.format(ZonedDateTime.now(ZoneId.of("UTC")));
    }

}
