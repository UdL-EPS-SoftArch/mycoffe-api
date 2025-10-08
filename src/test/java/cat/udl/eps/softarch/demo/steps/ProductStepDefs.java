package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Product;
import cat.udl.eps.softarch.demo.repository.ProductRepository;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;




public class ProductStepDefs {

    @Autowired
    private StepDefs stepDefs;

    public static Product currentProduct;

    @Autowired
    private ProductRepository productRepository;

    @Before
    public void setUp(){
        //Mirar si es bona practica ficar un step de el RegisterStepDefs
    }

    @When("^I register a new product with name \"([^\"]*)\"$")
    public void iRegisterANewProductWithName(String name) throws Exception {
        currentProduct = new Product();
        currentProduct.setName(name);

        // Crear el request builder base
        var requestBuilder = post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(stepDefs.mapper.writeValueAsString(currentProduct))
                .accept(MediaType.APPLICATION_JSON);

        // Solo añadir autenticación si hay un usuario logueado
        if (AuthenticationStepDefs.currentUsername != null) {
            requestBuilder = requestBuilder.with(AuthenticationStepDefs.authenticate());
        }

        stepDefs.result = stepDefs.mockMvc.perform(requestBuilder)
                .andDo(print());
    }

    @And("^The product with name \"([^\"]*)\" is registered$")
    public void theProductWithNameIsRegistered(String productName) throws Exception {

        if (productRepository.findByName(productName).isEmpty()) {
            Product product = new Product();
            product.setName(productName);
            productRepository.save(product);
        }

    }

    @And("^The product with name \"([^\"]*)\" is not registered$")
    public void theProductWithNameIsNotRegistered(String productName) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/products/search/findByName")
                                .param("name", productName)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.products", hasSize(0))); // Solo 1
    }
}
