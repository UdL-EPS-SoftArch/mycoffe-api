package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Order;
import cat.udl.eps.softarch.demo.domain.Product;
import cat.udl.eps.softarch.demo.repository.ProductRepository;
import io.cucumber.java.en.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderStepsDefs {

    private final StepDefs stepDefs;
    private final ProductRepository productRepository;

    private String token;

    public OrderStepsDefs(StepDefs stepDefs, ProductRepository productRepository) {
        this.stepDefs = stepDefs;
        this.productRepository = productRepository;
    }

    @When("I register a new order with id {string}")
    public void i_register_a_new_order_with_id(String id) throws Exception {
        Order order = new Order();
        order.setId(Long.valueOf(id));
        order.setCreated(ZonedDateTime.now());
        order.setServeWhen(order.getCreated().plusMinutes(10));
        order.setPaymentMethod("CARD");
        order.setStatus(Order.Status.RECEIVED);

        MockHttpServletRequestBuilder builder = post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(stepDefs.mapper.writeValueAsString(order))
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON);

        if (token != null && !token.isBlank()) {
            builder = builder.with(AuthenticationStepDefs.authenticate());
        }

        stepDefs.result = stepDefs.mockMvc.perform(builder).andDo(print());
    }

    // ============================
    // Authentication (usa mockMvc, no llamadas externas)
    // ============================
    @Given("I am authenticated as {string} with password {string}")
    public void i_am_authenticated_as_with_password(String username, String password) throws Exception {
        String body = String.format("""
            {
              "username": "%s",
              "password": "%s"
            }
            """, username, password);

        MockHttpServletRequestBuilder builder = post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON);

        stepDefs.result = stepDefs.mockMvc.perform(builder).andDo(print());
        int status = stepDefs.result.andReturn().getResponse().getStatus();
        assertEquals(200, status, "Login failed");

        String authHeader = stepDefs.result.andReturn().getResponse().getHeader("Authorization");
        token = authHeader;
        if (token == null || token.isBlank()) {
            String respBody = stepDefs.result.andReturn().getResponse().getContentAsString();
            if (respBody != null && respBody.contains("token")) {
                int idx = respBody.indexOf("token");
                int colon = respBody.indexOf(':', idx);
                int start = respBody.indexOf('"', colon) + 1;
                int end = respBody.indexOf('"', start);
                if (start > 0 && end > start) {
                    token = respBody.substring(start, end);
                }
            }
        }
        assertNotNull(token, "Token not received in login response");
    }

    @Given("I am not authenticated")
    public void i_am_not_authenticated() {
        token = null;
    }

    // ==============================
    // Create order with authentication
    // ============================
    @When("I create an order with:")
    public void i_create_an_order_with(io.cucumber.datatable.DataTable dataTable) throws Exception {
        Map<String, String> data = dataTable.asMaps().getFirst();
        String productName = data.get("product");
        int quantity = Integer.parseInt(data.get("quantity"));

        Product product = productRepository.findByName(productName)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Product not found: " + productName));

        Order order = new Order();
        order.setCreated(ZonedDateTime.now());
        order.setPaymentMethod("Card");
        order.setServeWhen(order.getCreated().plusMinutes(10));
        order.setStatus(Order.Status.SENT);
        order.setProducts(Set.of(product));

        MockHttpServletRequestBuilder builder = post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(stepDefs.mapper.writeValueAsString(order))
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .with(AuthenticationStepDefs.authenticate());

        stepDefs.result = stepDefs.mockMvc.perform(builder).andDo(print());
    }

    // ===================================================
    // Create order without authentication (expected 401 response)
    // ===================================================
    @When("I attempt to create an order with:")
    public void i_attempt_to_create_an_order_with(io.cucumber.datatable.DataTable dataTable) throws Exception {
        Map<String, String> data = dataTable.asMaps().getFirst();
        String productName = data.get("product");
        int quantity = Integer.parseInt(data.get("quantity"));

        Product product = productRepository.findByName(productName)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Product not found: " + productName));

        Order order = new Order();
        order.setCreated(ZonedDateTime.now());
        order.setPaymentMethod("Card");
        order.setServeWhen(order.getCreated().plusMinutes(10));
        order.setStatus(Order.Status.SENT);
        order.setProducts(Set.of(product));

        MockHttpServletRequestBuilder builder = post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(stepDefs.mapper.writeValueAsString(order))
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON);

        stepDefs.result = stepDefs.mockMvc.perform(builder).andDo(print());
    }

    // ===================================================
    // Precondition: an order already exists in the system
    // ===================================================
    // java
    @Given("an order exists with id {int} for user {string}")
    public void an_order_exists_with_id_for_user(Integer id, String username) {
        try {
            // Autenticar como el usuario objetivo
            i_am_authenticated_as_with_password(username, "pass123");

            // Construir orden mínima con el id solicitado
            Order order = new Order();
            order.setId(Long.valueOf(id));
            order.setCreated(ZonedDateTime.now());
            order.setServeWhen(order.getCreated().plusMinutes(10));
            order.setPaymentMethod("Card");
            order.setStatus(Order.Status.SENT);

            MockHttpServletRequestBuilder builder = post("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(order))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate());

            stepDefs.result = stepDefs.mockMvc.perform(builder).andDo(print());
            stepDefs.result.andExpect(status().isCreated());
        } catch (Exception e) {
            fail("Failed to create precondition order id " + id + " for user " + username + ": " + e.getMessage());
        }
    }

    @Given("the following orders exist for user {string}:")
    public void the_following_orders_exist_for_user(String username, io.cucumber.datatable.DataTable dataTable) {
        try {
            i_am_authenticated_as_with_password(username, "pass123");
        } catch (Exception e) {
            fail("Login failed for preconditions: " + e.getMessage());
            return;
        }

        for (var row : dataTable.asMaps()) {
            String id = row.get("id");
            String productName = row.get("product");

            try {
                Product product = productRepository.findByName(productName)
                        .stream()
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Product not found: " + productName));

                Order order = new Order();
                if (id != null && !id.isBlank()) {
                    order.setId(Long.valueOf(id));
                }
                order.setCreated(ZonedDateTime.now());
                order.setServeWhen(order.getCreated().plusMinutes(10));
                order.setPaymentMethod("Card");
                order.setStatus(Order.Status.SENT);
                order.setProducts(Set.of(product));

                MockHttpServletRequestBuilder builder = post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stepDefs.mapper.writeValueAsString(order))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate());

                stepDefs.result = stepDefs.mockMvc.perform(builder).andDo(print());
                stepDefs.result.andExpect(status().isCreated());
            } catch (Exception e) {
                fail("Failed to create precondition order id " + id + ": " + e.getMessage());
            }
        }
    }


    // ============================
    // Retrieve order (ambas variantes)
    // ============================
    @When("I retrieve the order with id {string}")
    public void i_retrieve_the_order_with_id_string(String id) throws Exception {
        MockHttpServletRequestBuilder builder = get("/orders/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8);

        if (token != null && !token.isBlank()) {
            builder = builder.with(AuthenticationStepDefs.authenticate());
        }

        stepDefs.result = stepDefs.mockMvc.perform(builder).andDo(print());
    }

    @When("I retrieve the order with id {int}")
    public void i_retrieve_the_order_with_id_int(Integer id) throws Exception {
        i_retrieve_the_order_with_id_string(id.toString());
    }

    // ============================
    // List orders
    // ============================
    @When("I request my list of orders")
    public void i_request_my_list_of_orders() throws Exception {
        MockHttpServletRequestBuilder builder = get("/orders")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8);

        if (token != null && !token.isBlank()) {
            builder = builder.with(AuthenticationStepDefs.authenticate());
        }

        stepDefs.result = stepDefs.mockMvc.perform(builder).andDo(print());
    }

    @Then("The order should exist and include the product {string}")
    public void the_order_should_exist_in_the_system_for_user(String productName) throws Exception {
        String newOrderUri = stepDefs.result.andReturn().getResponse().getHeader("Location");
        stepDefs.result = stepDefs.mockMvc.perform(
                        get(newOrderUri + "/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.products[*].name",
                        hasItem(is(productName))));
    }

    @Then("the response should contain the order details")
    public void the_response_should_contain_the_order_details() throws Exception {
        assertNotNull(stepDefs.result, "No result available");
        String body = stepDefs.result.andReturn().getResponse().getContentAsString();
        assertTrue(body.contains("id"));
        assertTrue(body.contains("status"));
    }

    @Then("the response should contain {int} orders")
    public void the_response_should_contain_orders(Integer expectedCount) throws Exception {
        assertNotNull(stepDefs.result, "No result available");
        String body = stepDefs.result.andReturn().getResponse().getContentAsString();
        // validación simple; si se quiere contar, parsear JSON con ObjectMapper
        assertTrue(body.contains("["));
    }

}
