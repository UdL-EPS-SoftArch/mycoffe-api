package cat.udl.eps.softarch.demo.steps;

import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;

public class OrderStepsDefs {

    @Autowired
    private RestTemplate restTemplate;

    private ResponseEntity<String> lastResponse;
    private String token;


    @When("I register a new order with id {string}")
    public void i_register_a_new_order_with_id(String id) {
        String url = "http://localhost:8080/api/orders";

        // Cuerpo del request (puedes adaptarlo a tu DTO real)
        String body = String.format("""
            {
              "id": "%s",
              "created": "2025-10-07T12:00:00",
              "serveWhen": "2025-10-07T14:00:00",
              "paymentMethod": "CARD",
              "status": "RECEIVED"
            }
            """, id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null)
            headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        lastResponse = restTemplate.postForEntity(url, request, String.class);
    }

}
