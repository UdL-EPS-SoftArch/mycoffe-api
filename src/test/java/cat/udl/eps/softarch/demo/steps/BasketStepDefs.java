package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Basket;
import cat.udl.eps.softarch.demo.domain.Customer;
import cat.udl.eps.softarch.demo.repository.BasketRepository;
import cat.udl.eps.softarch.demo.repository.CustomerRepository;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

public class BasketStepDefs {

    @Autowired
    private StepDefs stepDefs;

    public static Basket currentBasket;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Before
    public void setUp() {
        currentBasket = null;
    }

    @When("^I create a new basket for customer \"([^\"]*)\"$")
    public void iCreateANewBasketForCustomer(String customerUsername) throws Exception {
        // Buscar el customer por username
        Customer customer = customerRepository.findById(customerUsername)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + customerUsername));

        currentBasket = new Basket();
        currentBasket.setCustomer(customer);

        // Crear el request builder base
        var requestBuilder = post("/baskets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(stepDefs.mapper.writeValueAsString(currentBasket))
                .accept(MediaType.APPLICATION_JSON);

        // Solo añadir autenticación si hay un usuario logueado
        if (AuthenticationStepDefs.currentUsername != null) {
            requestBuilder = requestBuilder.with(AuthenticationStepDefs.authenticate());
        }

        stepDefs.result = stepDefs.mockMvc.perform(requestBuilder)
                .andDo(print());
    }

    @And("^Customer \"([^\"]*)\" has a basket$")
    public void customerHasABasket(String customerUsername) throws Exception {
        Customer customer = customerRepository.findById(customerUsername)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + customerUsername));

        // Verificar si el customer ya tiene un basket
        Optional<Basket> existingBasket = basketRepository.findByCustomer(customer);

        if (existingBasket.isEmpty()) {
            Basket basket = new Basket();
            basket.setCustomer(customer);
            basketRepository.save(basket);
        }
    }

    @And("^Customer \"([^\"]*)\" does not have a basket$")
    public void customerDoesNotHaveABasket(String customerUsername) throws Exception {
        Customer customer = customerRepository.findById(customerUsername)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + customerUsername));

        // Si existe un basket, eliminarlo para asegurar el estado
        Optional<Basket> existingBasket = basketRepository.findByCustomer(customer);
        existingBasket.ifPresent(basketRepository::delete);
    }

    @When("^I retrieve the basket for customer \"([^\"]*)\"$")
    public void iRetrieveTheBasketForCustomer(String customerUsername) throws Exception {
        Customer customer = customerRepository.findById(customerUsername)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + customerUsername));

        Optional<Basket> basket = basketRepository.findByCustomer(customer);

        if (basket.isPresent()) {
            stepDefs.result = stepDefs.mockMvc.perform(
                            get("/baskets/" + basket.get().getId())
                                    .accept(MediaType.APPLICATION_JSON)
                                    .with(AuthenticationStepDefs.authenticate()))
                    .andDo(print());
        } else {
            // Si no existe, intentar buscar y debería devolver 404
            stepDefs.result = stepDefs.mockMvc.perform(
                            get("/baskets/search/findByCustomer")
                                    .param("customer", customer.getId())
                                    .accept(MediaType.APPLICATION_JSON)
                                    .with(AuthenticationStepDefs.authenticate()))
                    .andDo(print());
        }
    }

    @And("^The basket belongs to customer \"([^\"]*)\"$")
    public void theBasketBelongsToCustomer(String customerUsername) throws Exception {
        // Verificar que el customer del basket coincide con el esperado
        stepDefs.result.andExpect(jsonPath("$.customer.id", is(customerUsername)));
    }

    @And("^It has been created a basket for customer \"([^\"]*)\"$")
    public void itHasBeenCreatedABasketForCustomer(String customerUsername) throws Exception {
        Customer customer = customerRepository.findById(customerUsername)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + customerUsername));

        Optional<Basket> basket = basketRepository.findByCustomer(customer);

        if (basket.isEmpty()) {
            throw new AssertionError("Basket was not created for customer: " + customerUsername);
        }

        currentBasket = basket.get();
    }
}