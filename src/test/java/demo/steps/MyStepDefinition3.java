package demo.steps;

import demo.WebDriverFactory;
import demo.test;
import demo.webDriverName;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;


public class MyStepDefinition3 {
    private WebDriver wd;

    public Actions setActionsBuilder() {
        return new Actions(wd);
    }

    @Given("We have date {string}")
    public void weHaveDate(String date) {
        List<String> browserOptions = new ArrayList();
        browserOptions.add("--incognito");
        browserOptions.add("--disable-notifications");
        wd = WebDriverFactory.createNewDriver(webDriverName.CHROME, browserOptions);
        Logger logger = LogManager.getLogger(test.class);
        String url = "https://otus.ru";
        wd.get(url);
        By catalogOfCourse = By.cssSelector("a[title='Каталог курсов']");
        wd.findElement(catalogOfCourse).click();
    }

    @When("Search course by date {string}")
    public void searchCourseByDate(String date) {
        Logger logger = LogManager.getLogger(test.class);
        Actions builder = setActionsBuilder();
        WebElement course = (new WebDriverWait(wd, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'lessons__new-item-start') and contains(normalize-space(), '" + date + "')]//ancestor::div[contains(@class,'lessons__new-item-container')]")));
        String nameOfCourse = wd.findElement(By.xpath("//div[contains(@class,'lessons__new-item-start') and contains(normalize-space(), '" + date + "')]//ancestor::div[contains(@class,'lessons__new-item-container')]//descendant::div[contains(@class,'lessons__new-item-title')]")).getText();
        if (wd instanceof JavascriptExecutor) {
            ((JavascriptExecutor) wd).executeScript("arguments[0].style.border='5px solid blue'", course);
        }
        builder.click(course).pause(3000).build().perform();
        logger.info("Найден курс с датой начала " + date + " " + nameOfCourse);
    }


    @Then("Logging info about searched course")
    public void loggingInfoAboutSearchedCourse() {
        By h1 = By.xpath("//h1[@class='course-header2__title']");
        Logger logger1 = LogManager.getLogger(test.class);
        logger1.info(wd.findElement(h1).getText());
        wd.quit();
    }
}
