package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Category;
import cat.udl.eps.softarch.demo.repository.CategoryRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CategoryStepDefs {

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private CategoryRepository categoryRepository;

    private static Category currentCategory;

    @Given("^There is no registered category with name \"([^\"]*)\"$")
    public void thereIsNoRegisteredCategoryWithName(String name) {
        Assert.assertFalse("Category \"" + name + "\" shouldn't exist",
                categoryRepository.findByName(name).isPresent());
    }

    @Given("^There is a registered category with name \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void thereIsARegisteredCategoryWithNameAndDescription(String name, String description) {
        if (!categoryRepository.findByName(name).isPresent()) {
            Category category = new Category();
            category.setName(name);
            category.setDescription(description);
            categoryRepository.save(category);
        }
    }

    @When("^I register a new category with name \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void iRegisterANewCategoryWithNameAndDescription(String name, String description) throws Exception {
        currentCategory = new Category();
        currentCategory.setName(name);
        currentCategory.setDescription(description);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stepDefs.mapper.writeValueAsString(currentCategory))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate())
        ).andDo(print());
    }

    @And("^It has been created a category with name \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void itHasBeenCreatedACategoryWithNameAndDescription(String name, String description) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/categories/search/findByName")
                                .param("name", name)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.description", is(description)));
    }

    @And("^I can retrieve the category with name \"([^\"]*)\"$")
    public void iCanRetrieveTheCategoryWithName(String name) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/categories/search/findByName")
                                .param("name", name)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)));
    }

    @And("^The category description remains \"([^\"]*)\"$")
    public void theCategoryDescriptionRemains(String description) throws Exception {
        // After a 409 conflict, we need to fetch the existing category to verify its description
        Optional<Category> existingCategory = categoryRepository.findByName(currentCategory.getName());
        Assert.assertTrue("Category should exist", existingCategory.isPresent());
        Assert.assertEquals("Description should remain unchanged", description, existingCategory.get().getDescription());
    }

    @And("^It has not been created a category with name \"([^\"]*)\"$")
    public void itHasNotBeenCreatedACategoryWithName(String name) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/categories/search/findByName")
                                .param("name", name)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andExpect(status().isNotFound());
    }
}
