package cat.udl.eps.softarch.demo.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


public class ReadProductStepDefs {

    @Autowired
    private StepDefs stepDefs;

    @When("I read the product with name {string}")
    public void iReadTheProductWithName(String productName) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/products/search/findByName")
                                .param("name", productName)
                                .accept("application/json")
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @And("The product details are:")
    public void theProductDetailsAre(io.cucumber.datatable.DataTable dataTable) throws Exception {
        Map<String, String> expectedDetails = dataTable.asMap(String.class, String.class);

        for (Map.Entry<String, String> entry : expectedDetails.entrySet()) {
            String field = entry.getKey();
            String expectedValue = entry.getValue();

            if (field.equals("price")) {
                // Para campos numéricos, verificar como número
                stepDefs.result.andExpect(jsonPath("$." + field).value(Double.parseDouble(expectedValue)));
            } else {
                // Para campos de texto
                stepDefs.result.andExpect(jsonPath("$." + field).value(expectedValue));
            }
        }
    }
}