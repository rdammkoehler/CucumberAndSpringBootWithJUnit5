package la.ra.ex;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MainTest {
    @LocalServerPort
    int serverPort;

    private ResponseEntity<HashMap> response;
    private HashMap<String, String> responseBody;

    @BeforeEach
    void setUp() {
        response = getPrediction();
        responseBody = (HashMap<String, String>) response.getBody();
    }

    private ResponseEntity<HashMap> getPrediction() {
        return getPrediction(null);
    }

    private ResponseEntity<HashMap> getPrediction(String query) {
        String url;
        if (null == query || "".equals(query)) {
            url = "http://localhost:" + serverPort + "/prediction";
        } else {
            url = "http://localhost:" + serverPort + "/prediction?query=" + query;
        }
        return new RestTemplate().getForEntity(url, HashMap.class);
    }

    @Test
    void theServiceRespondsWithOK() {
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.OK)));
    }

    @Test
    void theResponseContainsATime() {
        assertThat(responseBody, hasKey(equalTo("time")));
    }

    @Test
    void theResponseContainsAResponse() {
        assertThat(responseBody, hasKey(equalTo("response")));
    }

    @Test
    void theResponseDoesNotContainAQuery() {
        assertThat(responseBody, not(hasKey(equalTo("query"))));
    }

    @Test
    void theResponseContainsAQueryWhenOneIsGiven() {
        String query = "Will it snow soon?";
        assertThat(((HashMap<String, String>) getPrediction(query).getBody()).get("query"), is(equalTo(query)));
    }
}
