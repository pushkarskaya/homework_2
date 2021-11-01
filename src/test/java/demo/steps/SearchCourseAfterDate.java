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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SearchCourseAfterDate {
    Logger logger = LogManager.getLogger(Test.class);
    List<Date> dd = new ArrayList<>();
    private WebDriver wd;
    private final static By catalogOfCourse = By.cssSelector("a[title='Каталог курсов']");

    @Given("We have afterdate {string}")
    public void weHaveAfterdate(String date) {
        List<String> browserOptions = new ArrayList();
        browserOptions.add("--incognito");
        browserOptions.add("--disable-notifications");
        wd = WebDriverFactory.createNewDriver(WebDriverName.CHROME, browserOptions);
        Logger logger = LogManager.getLogger(Test.class);
        String url = "https://otus.ru";
        wd.get(url);
        wd.findElement(catalogOfCourse).click();
    }

    @When("Search course after date {string}")
    public void searchCourseAfterDate(String data) throws ParseException {
        DateFormat date = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru"));
        List<Date> dates = wd.findElements(By.cssSelector(".lessons__new-item-start"))
                .stream()
                .filter(webElement -> {
                    String text = webElement.getText();
                    return !text.contains("О дате старта") && !text.startsWith("В");
                })
                .map(webElement -> {
                    String currentDate = webElement.getText().replace("С ", "");
                    if (!currentDate.contains("2022")) {
                        currentDate += " 2021";
                    }
                    try {
                        logger.info(currentDate);
                        logger.info(date.parse(currentDate));
                        return date.parse(currentDate);

                    } catch (ParseException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }).toList();
        logger.info("Дата, от которой ищем бОльшие даты " + date.parse(data));
        Date date2 = date.parse(data);
        dd = dates.stream()
                .filter(item -> item.after(date2))
                .distinct()
                .collect(Collectors.toList());

        System.out.println(dd);
        logger.info("Смотрим курсы, которые начинаются позже указанной даты " + data);
    }

    @Then("Logging info about all courses after date")
    public void loggingInfoAboutAllCoursesAfterDate() throws ParseException {
        final String DATE_FORMAT = "EEE MMM d HH:mm:ss z yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ROOT);
        Date newDate = new Date();
        DateFormat dateFormatOut = new SimpleDateFormat("dd MMMM", new Locale("ru"));

        for (int i = 0; i < dd.size(); i++) {
            newDate = simpleDateFormat.parse(String.valueOf(dd.get(i)));
            String ruDate = dateFormatOut.format(newDate);
            List<WebElement> elements = wd.findElements(By.xpath("//div[contains(@class,'lessons__new-item-start') and contains(normalize-space(), '" + ruDate + "')]//ancestor::div[contains(@class,'lessons__new-item-container')]//descendant::div[contains(@class,'lessons__new-item-title')]")).stream().toList();
            elements.stream().forEach(element -> {
                logger.info(element.getText() + "   дата начала курса " + ruDate);

            });
        }
        wd.quit();
    }
}
