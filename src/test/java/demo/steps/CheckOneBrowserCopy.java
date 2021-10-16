//Не используется
package demo.steps;
import demo.WebDriverFactory;
import demo.webDriverName;
import org.openqa.selenium.WebDriver;
import java.util.List;

class CheckOneBrowserCopy {  //Проверка, что создан экземпляр браузера
    private WebDriver wd;
    private static CheckOneBrowserCopy copy;

    public static synchronized CheckOneBrowserCopy checkCopy() {
        if (copy == null) {
            copy = new CheckOneBrowserCopy();
        }
        return copy;
    }

    public WebDriver create(webDriverName browser, List<String> browserOptions) {
        wd = WebDriverFactory.createNewDriver(browser, browserOptions);
        return wd;
    }

    public void close() {
        wd.quit();
    }
}