package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Business;
import cat.udl.eps.softarch.demo.domain.BusinessStatus;
import cat.udl.eps.softarch.demo.repository.BusinessRepository;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

public class BusinessStep {

    @Autowired
    private BusinessRepository businessRepository;

    private Business business;

    @Given("a Business object with status {string}")
    public void a_business_object_with_status(String status) {
        business = new Business();
        business.setStatus(BusinessStatus.valueOf(status));
    }


    @Then("the Business object should have status {string}")
    public void the_business_object_should_have_status(String expectedStatus) {
        Assertions.assertNotNull(business, "The business object was not created");
        Assertions.assertEquals(expectedStatus, business.getStatus().toString(), "The business status does not match");
    }



    @When("I save the business")
    public void i_save_the_business() {
        business = businessRepository.save(business);
    }

    @Then("I should be able to retrieve it from the repository")
    public void i_should_be_able_to_retrieve_it_from_the_repository() {
        Business found = businessRepository.findById(business.getId()).orElse(null);
        Assertions.assertNotNull(found, "Business was not found in repository");
        Assertions.assertEquals(business.getStatus(), found.getStatus(), "Business status does not match");
    }
}