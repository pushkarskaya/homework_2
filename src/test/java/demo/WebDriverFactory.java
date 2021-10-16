package demo;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import java.util.List;


public class WebDriverFactory {

    public static WebDriver createNewDriver(webDriverName type, List<String> options) {
        switch (type) {
            case CHROME:
                WebDriverManager.chromedriver().setup();

                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments();

                chromeOptions.addArguments(options);

                return new ChromeDriver(chromeOptions);
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();

                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments(options);

                return new FirefoxDriver(firefoxOptions);
            case OPERA:
                WebDriverManager.operadriver().setup();

                OperaOptions operaOptions= new OperaOptions();
                operaOptions.addArguments(options);

                return new OperaDriver(operaOptions);
            default:
                return null;
        }
    }
}