package demo.steps;

import demo.WebDriverFactory;
import demo.webDriverName;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;


public class MyStepDefinitions {
    private WebDriver wd;

    @Given("We have browser {string}")
    public void weHaveBrowserChrome(String browser) {
        List<String> browserOptions = new ArrayList();
        browserOptions.add("--incognito");
        browserOptions.add("--disable-notifications");
        wd = WebDriverFactory.createNewDriver(webDriverName.valueOf(browser), browserOptions);
    }

    @When("User open page url {string}")
    public void userOpenPageUrl(String url) {
        wd.get(url);
    }

    @Then("Otus homepage is opened")
    public void otusHomepageIsOpened() {
        Assert.assertEquals("Онлайн‑курсы для профессионалов, дистанционное обучение современным профессиям",
                wd.getTitle());
        wd.quit();

    }


}
