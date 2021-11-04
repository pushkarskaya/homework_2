package demo.steps;

import demo.WebDriverFactory;
import demo.WebDriverName;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.List;

public class SearchExpensiveCourse {
    private final static By catalogOfCourse = By.xpath("//p[contains(text(),'Курсы')]");
    private final static By trainingCourses = By.xpath("//a[@class='header2-menu__dropdown-link header2-menu__dropdown-link_no-wrap' and @title='Подготовительные курсы']");
    private final static By viewCourses = By.xpath("//h1[contains(text(),'Онлайн-курсы для подготовки к поступлению на основные курсы')]");
    private final static By priceCourse = By.xpath("//div[@class='lessons__new-item-price']");
    private static Logger logger = LogManager.getLogger(SearchExpensiveCourse.class);
    private WebDriver wd;
    private String maxPrice;
    private String minPrice;

    @Given("View list of courses")
    public void viewListOfCourses() {
        List<String> browserOptions = new ArrayList();
        browserOptions.add("--incognito");
        browserOptions.add("--disable-notifications");
        wd = WebDriverFactory.createNewDriver(WebDriverName.CHROME, browserOptions);
        String url = "https://otus.ru";
        wd.get(url);
        wd.findElement(catalogOfCourse).click();
        Actions action = new Actions(wd);
        action.moveToElement(wd.findElement(catalogOfCourse)).click();
        action.moveToElement(wd.findElement(trainingCourses)).build().perform();
        wd.findElement(trainingCourses).click();
        Assert.assertEquals("Онлайн-курсы для подготовки к поступлению на основные курсы", wd.findElement(viewCourses).getText());

    }


    @When("Search the most expensive course")
    public void searchTheMostExpensiveCourse() {
        List<WebElement> priceList = wd.findElements(priceCourse).stream().toList();
        List<String> price = priceList.stream().map(element -> element.getText()).sorted((a, b) -> {
            int valueA = Integer.valueOf(a.replaceAll("\\s", "").replaceAll("₽", ""));
            int valueB = Integer.valueOf(b.replaceAll("\\s", "").replaceAll("₽", ""));

            if (valueA == valueB) return 0;
            if (valueA > valueB) {
                return 1;
            } else {
                return -1;
            }
        }).toList();
        logger.info(price);
        maxPrice = price.get(price.size() - 1);
        minPrice = price.get(0);
        logger.info("Цена самого дорогого курса " + maxPrice);
        logger.info("Цена самого дешевого курса " + minPrice);
    }


    @Then("Show course")
    public void showCourse() {
        By courseMaxPrice = By.xpath("//div[@class='lessons__new-item-price'  and contains(text(),'" + maxPrice + "')]//parent::div//preceding-sibling::div[@class='lessons__new-item-title lessons__new-item-title_with-bg js-ellipse']");
        By courseMinPrice = By.xpath("//div[@class='lessons__new-item-price'  and contains(text(),'" + minPrice + "')]//parent::div//preceding-sibling::div[@class='lessons__new-item-title lessons__new-item-title_with-bg js-ellipse']");
        logger.info("Самый дорогой курс " + wd.findElement(courseMaxPrice).getText() + " " + maxPrice);
        logger.info("Самый дешевый курс " + wd.findElement(courseMinPrice).getText() + " " + minPrice);
        wd.quit();
    }
}
