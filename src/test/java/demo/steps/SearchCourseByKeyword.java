package demo.steps;

import demo.WebDriverFactory;
import demo.WebDriverName;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;
import java.util.List;

public class SearchCourseByKeyword {
    private final static By catalogOfCourse = By.cssSelector("a[title='Каталог курсов']");
    private final static By h1 = By.xpath("//h1[@class='course-header2__title']");
    private final static By countCourses =By.xpath("//div[contains(concat(' ',@class,' '),' lessons__new-item-title ')]");
    private final static By allCourse=By.xpath("//div[contains(concat(' ',@class,' '),' lessons__new-item-title ')]");
    private WebDriver wd;
    private static Logger logger = LogManager.getLogger(SearchCourseByKeyword.class);

    @Given("Find course {string}")
    public void findCourse(String keyword) {
        List<String> browserOptions = new ArrayList();
        browserOptions.add("--incognito");
        browserOptions.add("--disable-notifications");
        wd = WebDriverFactory.createNewDriver(WebDriverName.CHROME, browserOptions);
        String url = "https://otus.ru";
        wd.get(url);
        wd.findElement(catalogOfCourse).click();
        //Отбор всех курсов на вкладке Программирование,в названии которых есть keyword
        List<String> arrayOfCourse = new ArrayList<String>();
        int countOfCourses = wd.findElements(countCourses).size();
        logger.info("Количество курсов на странице всего: " + countOfCourses);
        List<WebElement> allCourses = wd.findElements(allCourse);
        int count = 0;
        for (int i = 0; i < allCourses.size() - 1; i++) { //logger.info(allCourses.get(i).getText()); //Записываем в лог все курсы
            arrayOfCourse.add(i, allCourses.get(i).getText());
            if (arrayOfCourse.get(i).contains(keyword)) {
                logger.info(arrayOfCourse.get(i));
                count++;
            }
        }
        logger.info("Найдено курсов, содержащих в названии текст " + keyword + ": " + count);
    }


    @When("User click finded course {string}")
    public void userClickFindedCourse(String keyword) {
        By choosedcourse = By.xpath("//div[@class='lessons__new-item-container']//descendant::div[contains(text(),'" + keyword + "')]//ancestor::a[@class='js-stats lessons__new-item lessons__new-item_hovered']");
        wd.findElement(choosedcourse).click();
    }

    @Then("Finded course is opened {string}")
    public void findedCourseIsOpened(String keyword) {
        WebDriverWait wait = new WebDriverWait(wd, 10);
        logger.info(wd.findElement(h1).getText());
        if (wd.findElements(h1).size() != 0) {
            System.out.println("Открыта страница с описанием курса");
        } else System.out.println("Не удалось открыть страницу с выбранным курсом");
        wd.quit();
    }


}
