package demo.steps;

import demo.WebDriverFactory;
import demo.Test;
import demo.WebDriverName;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;
import java.util.List;


public class SearchCourseByDate {
    private WebDriver wd;
    private final static By catalogOfCourse = By.cssSelector("a[title='Каталог курсов']");
    private final static By h1 = By.xpath("//div[@class='course-header2__title']");

    @Given("We have date {string}")
    public void weHaveDate(String date) {
        List<String> browserOptions = new ArrayList();
        browserOptions.add("--incognito");
        browserOptions.add("--disable-notifications");
        wd = WebDriverFactory.createNewDriver(WebDriverName.CHROME, browserOptions);
        Logger logger = LogManager.getLogger(Test.class);
        String url = "https://otus.ru";
        wd.get(url);
        wd.findElement(catalogOfCourse).click();
    }

    @When("Search course by date {string}")
    public void searchCourseByDate(String date) {
        Logger logger = LogManager.getLogger(Test.class);
        WebElement course = (new WebDriverWait(wd, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'lessons__new-item-start') and contains(normalize-space(), '" + date + "')]//ancestor::div[contains(@class,'lessons__new-item-container')]")));
        String nameOfCourse = wd.findElement(By.xpath("//div[contains(@class,'lessons__new-item-start') and contains(normalize-space(), '" + date + "')]//ancestor::div[contains(@class,'lessons__new-item-container')]//descendant::div[contains(@class,'lessons__new-item-title')]")).getText();
        By choosedcourse = By.xpath("//div[@class='lessons__new-item-container']//descendant::div[contains(text(),'" + nameOfCourse + "')]//ancestor::a[@class='js-stats lessons__new-item lessons__new-item_hovered']");
        wd.findElement(choosedcourse).click();
        logger.info("Найден курс с датой начала " + date + " " + nameOfCourse);
    }


    @Then("Logging info about searched course")
    public void loggingInfoAboutSearchedCourse() {
        Logger logger1 = LogManager.getLogger(Test.class);
        logger1.info("Открыта страница с информацией по курсу "+wd.findElement(h1).getText());
        wd.quit();
    }
}
