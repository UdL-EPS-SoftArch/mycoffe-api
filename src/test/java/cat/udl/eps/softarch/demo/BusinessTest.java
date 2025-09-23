package cat.udl.eps.softarch.demo;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"},
        features = "src/test/resources/features",
        glue = "cat.udl.eps.softarch.demo.steps",
        tags = "@business"
)
public class BusinessTest {
}


