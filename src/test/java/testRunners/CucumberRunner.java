package testRunners;

import io.cucumber.junit.Cucumber;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.TestNGCucumberRunner;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.runner.RunWith;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@RunWith(Cucumber.class)
@Test
@CucumberOptions (
            features = {"src/test/resources/features"},
            glue= "stepDefinitions",
            publish = true,
            tags = "@homePage and not @ignore",
            monochrome = true,
            plugin = {
                    "pretty",
                    "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
                },
            dryRun = true
    )
    public class CucumberRunner extends AbstractTestNGCucumberTests {
        private TestNGCucumberRunner testNGCucumberRunner;

        @BeforeClass(alwaysRun = true)
        public void setupCucumber(){
            testNGCucumberRunner =  new TestNGCucumberRunner(this.getClass());
            Dotenv dotenv = Dotenv.configure().directory("./src/test/resources").load();
            dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
        }


        @DataProvider
        public Object[][] features() {
            return testNGCucumberRunner.provideScenarios();
        }

//        allow parallel testing
//        @Override
//        @DataProvider(parallel = true)
//        public Object [][] scenarios() {
//            return super.scenarios();
//        }
//@AfterStep
//public void addScreenshot(Scenario scenario){
//    final byte[] screenshot = ((TakesScreenshot) BaseDefinitions.getDriver()).getScreenshotAs(OutputType.BYTES);
//    scenario.attach(screenshot, "image/png", "image");
//
//}

        @AfterClass(alwaysRun = true)
        public void tearDownThisClass() {
            testNGCucumberRunner.finish();
        }
}
