package demo;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Test {
    private Logger logger = LogManager.getLogger(Test.class);
    private WebDriver wd;


    List<String> browserOptions = new ArrayList();
    String url = "https://otus.ru";
    private By catalogOfCourse = By.cssSelector("a[title='Каталог курсов']");

    public void clickButtonCatalogOfCourse() {
        wd.findElement(catalogOfCourse).click();
    }

    public void filterCourse(String keyword) {
        //Отбор всех курсов на вкладке Программирование,в названии которых есть keyword
        List<String> arrayOfCourse = new ArrayList<String>();
        int countOfCourses = wd.findElements(By.xpath("//div[contains(concat(' ',@class,' '),' lessons__new-item-title ')]")).size();
        logger.info("Количество курсов на странице всего: " + countOfCourses);
        List<WebElement> allCourses = wd.findElements(By.xpath("//div[contains(concat(' ',@class,' '),' lessons__new-item-title ')]"));
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

    public void chooseCourse() {
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

        Date firstDate = dates.stream().reduce(dates.get(1), (date1, date2) -> date1.compareTo(date2) < 0 ? date1 : date2);
        Date lastDate = dates.stream().reduce(dates.get(1), (date1, date2) -> date1.compareTo(date2) < 0 ? date2 : date1);
        logger.info("Самая ранняя дата начала курсов");
        logger.info(firstDate.toString());
        logger.info("Самая поздняя дата начала курсов");
        logger.info(lastDate.toString());
        //clickButtonCatalogOfCourse();
        String nameOfChoosedCourseFirst = chooseCourseByDate(firstDate);
        logger.info("Выбран курс с самой ранней датой начала " + nameOfChoosedCourseFirst);
        clickButtonCatalogOfCourse();
        String nameOfChoosedCourseLast = chooseCourseByDate(lastDate);
        logger.info("Выбран курс с самой поздней датой начала " + nameOfChoosedCourseLast);

    }


    String chooseCourseByDate(Date dateOfCourse) {

        final String DATE_FORMAT = "EEE MMM d HH:mm:ss z yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ROOT);
        Date newDate = new Date();
        try {
            newDate = simpleDateFormat.parse(dateOfCourse.toString());
            logger.info(newDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat dateFormatOut = new SimpleDateFormat("dd MMMM", new Locale("ru"));
        logger.info(dateFormatOut.format(newDate));
        String ruDate = dateFormatOut.format(newDate);
        Actions builder = setActionsBuilder();
        //clickButtonCatalogOfCourse();
        // WebElement courseFirst = wd.findElement(By.xpath("//div[contains(@class,'lessons__new-item-start') and contains(normalize-space(), '" + ruDate+ "')]//ancestor::div[contains(@class,'lessons__new-item-container')]"));
        WebElement courseFirst = (new WebDriverWait(wd, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'lessons__new-item-start') and contains(normalize-space(), '" + ruDate + "')]//ancestor::div[contains(@class,'lessons__new-item-container')]")));
        String nameOfCourse = wd.findElement(By.xpath("//div[contains(@class,'lessons__new-item-start') and contains(normalize-space(), '" + ruDate + "')]//ancestor::div[contains(@class,'lessons__new-item-container')]//descendant::div[contains(@class,'lessons__new-item-title')]")).getText();

        if (wd instanceof JavascriptExecutor) {
            ((JavascriptExecutor) wd).executeScript("arguments[0].style.border='5px solid blue'", courseFirst);
        }
        builder.click(courseFirst).pause(3000).build().perform();

//        builder.contextClick(courseFirst).click().pause(2000).build().perform();
        // WebElement h1=(new WebDriverWait(wd,10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[contains(text(),'')]")));
        return nameOfCourse;
    }

    public Actions setActionsBuilder() {
        return new Actions(wd);
    }

    @org.junit.Test
    public void filterAndChooseCourse() {
        logger.info("Старт теста");
        // browserOptions.add("--start-fullscreen");
        browserOptions.add("--incognito");
        browserOptions.add("--disable-notifications");
        wd = WebDriverFactory.createNewDriver(WebDriverName.CHROME, browserOptions);
        wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        wd.get(url);
        Assert.assertEquals("Онлайн‑курсы для профессионалов, дистанционное обучение современным профессиям",
                wd.getTitle());
        clickButtonCatalogOfCourse();
        filterCourse("C#");
        chooseCourse();
        wd.quit();
    }
}