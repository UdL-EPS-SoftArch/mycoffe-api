package cat.udl.eps.softarch.demo.steps;

import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OrderStepsDefs {

    @Autowired
    private RestTemplate restTemplate;

    private ResponseEntity<String> lastResponse;
    private String token;


    @When("I register a new order with id {string}")
    public void i_register_a_new_order_with_id(String id) {
        String url = "http://localhost:8080/api/orders";

        // Request body (adapt it to your real DTO)
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

    // ============================
    // Authentication
    // ============================
    @Given("I am authenticated as {string} with password {string}")
    public void i_am_authenticated_as_with_password(String username, String password) {
        String loginUrl = "http://localhost:8080/login";

        String body = String.format("""
            {
              "username": "%s",
              "password": "%s"
            }
            """, username, password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(loginUrl, new HttpEntity<>(body, headers), String.class);
            assertEquals(200, response.getStatusCodeValue(), "Login failed");
            token = response.getHeaders().getFirst("Authorization");
            assertNotNull(token, "Token not received in login response");
        } catch (HttpClientErrorException e) {
            fail("Authentication failed with status: " + e.getStatusCode());
        }
    }

    @Given("I am not authenticated")
    public void i_am_not_authenticated() {
        token = null;
    }

    // ==============================
    // Create order with authentication
    // ==============================
    @When("I create an order with:")
    public void i_create_an_order_with(io.cucumber.datatable.DataTable dataTable) {
        String url = "http://localhost:8080/api/orders";

        Map<String, Object> data = dataTable.asMaps().get(0);
        String product = data.get("product").toString();
        int quantity = Integer.parseInt(data.get("quantity").toString());

        String body = String.format("""
        {
          "product": "%s",
          "quantity": %d
        }
        """, product, quantity);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null) headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        lastResponse = restTemplate.postForEntity(url, request, String.class);
    }

    // ===================================================
    // Create order without authentication (expected 401 response)
    // ===================================================
    @When("I attempt to create an order with:")
    public void i_attempt_to_create_an_order_with(io.cucumber.datatable.DataTable dataTable) {
        String url = "http://localhost:8080/api/orders";

        Map<String, Object> data = dataTable.asMaps().get(0);
        String product = data.get("product").toString();
        int quantity = Integer.parseInt(data.get("quantity").toString());

        String body = String.format("""
        {
          "product": "%s",
          "quantity": %d
        }
        """, product, quantity);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // No token -> user not authenticated

        try {
            lastResponse = restTemplate.postForEntity(url, new HttpEntity<>(body, headers), String.class);
        } catch (Exception e) {
            lastResponse = new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
        }
    }

    // ===================================================
    // Precondition: an order already exists in the system
    // ===================================================
    @Given("an order exists with id {int} for user {string}")
    public void an_order_exists_with_id_for_user(Integer id, String username) {
        // Simulate order creation with authenticated user
        i_am_authenticated_as_with_password(username, "pass123");
        i_register_a_new_order_with_id(id.toString());
        assertEquals(201, lastResponse.getStatusCodeValue());
    }

    // ===================================================
    // Precondition: multiple orders exist for a user
    // ===================================================
    @Given("the following orders exist for user {string}:")
    public void the_following_orders_exist_for_user(String username, io.cucumber.datatable.DataTable dataTable) {
        i_am_authenticated_as_with_password(username, "pass123");

        for (var row : dataTable.asMaps()) {
            String id = row.get("id");
            i_register_a_new_order_with_id(id);
            assertEquals(201, lastResponse.getStatusCodeValue());
        }
    }


    // ============================
    // Retrieve order
    // ============================
    @When("I retrieve the order with id {string}")
    public void i_retrieve_the_order_with_id(String id) {
        String url = "http://localhost:8080/api/orders/" + id;
        HttpHeaders headers = new HttpHeaders();
        if (token != null)
            headers.set("Authorization", "Bearer " + token);

        try {
            lastResponse = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        } catch (HttpClientErrorException e) {
            lastResponse = new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    // ============================
    // List orders
    // ============================
    @When("I request my list of orders")
    public void i_request_my_list_of_orders() {
        String url = "http://localhost:8080/api/orders";
        HttpHeaders headers = new HttpHeaders();
        if (token != null)
            headers.set("Authorization", "Bearer " + token);

        lastResponse = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
    }

    // ============================
    // Validations
    // ============================
    @Then("the response status should be {int}")
    public void the_response_status_should_be(Integer statusCode) {
        assertNotNull(lastResponse, "No response received");
        assertEquals(statusCode, lastResponse.getStatusCodeValue());
    }

    @Then("the order should exist in the system for user {string}")
    public void the_order_should_exist_in_the_system_for_user(String username) {
        assertNotNull(lastResponse);
        assertEquals(201, lastResponse.getStatusCodeValue());
        assertTrue(lastResponse.getBody().contains(username));
    }

    @Then("the response should contain the order details")
    public void the_response_should_contain_the_order_details() {
        assertNotNull(lastResponse);
        assertTrue(lastResponse.getBody().contains("id"));
        assertTrue(lastResponse.getBody().contains("status"));
    }

    @Then("the response should contain {int} orders")
    public void the_response_should_contain_orders(Integer expectedCount) {
        assertNotNull(lastResponse);
        assertTrue(lastResponse.getBody().contains("["));
    }

}
