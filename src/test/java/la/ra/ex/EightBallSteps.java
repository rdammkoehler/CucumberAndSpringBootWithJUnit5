package la.ra.ex;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasKey;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EightBallSteps {

    @LocalServerPort
    int serverPort;

    private String query;

    private ResponseEntity<HashMap> response;

    @Given("no query")
    public void no_query() {
        query = null;
    }

    @When("a request is made")
    public void a_request_is_made() {
        response = new RestTemplate().getForEntity(constructServiceURLString(), HashMap.class);
    }

    @Then("a response is received")
    public void a_response_is_received() {
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.OK)));
    }

    @Given("the query {string}")
    public void the_query(String string) {
        query = string;
    }

    @Then("has no query in the response")
    public void has_no_query_in_the_response() {
        assertThat((HashMap<String, String>) response.getBody(), not(hasKey(equalTo("query"))));
    }

    @Then("the query is in the response")
    public void the_query_is_in_the_response() {
        assertThat(((HashMap<String, String>) response.getBody()).get("query"), is(equalTo(query)));
    }

    private String constructServiceURLString() {
        String url;
        if (null == query || "".equals(query)) {
            url = "http://localhost:" + serverPort + "/prediction";
        } else {
            url = "http://localhost:" + serverPort + "/prediction?query=" + query;
        }
        return url;
    }

}
