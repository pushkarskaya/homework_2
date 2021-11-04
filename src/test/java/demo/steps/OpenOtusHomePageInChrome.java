package demo.steps;

import demo.WebDriverFactory;
import demo.WebDriverName;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import java.util.ArrayList;
import java.util.List;


public class OpenOtusHomePageInChrome {
    private WebDriver wd;

    @Given("We have browser {string}")
    public void weHaveBrowserChrome(String browser) {
        List<String> browserOptions = new ArrayList();
        browserOptions.add("--incognito");
        browserOptions.add("--disable-notifications");
        try {
            WebDriverName.valueOf(browser);
        } catch (RuntimeException e) {
            System.out.println("Недопустимый тип браузера "+ browser);
        }
        wd = WebDriverFactory.createNewDriver(WebDriverName.valueOf(browser), browserOptions);
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
