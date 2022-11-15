import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.sql.Time;
import java.time.Duration;

public class WebDriverFactory {

    private static final Logger logger = LogManager.getLogger(WebDriverFactory.class);

    // Возврат драйвера для конкретного браузера по его названию
    public static WebDriver getDriver(String browserName, String pageLoadStrategy){

        //проверяем, что корректно задано значение параметра стратегии загрузки страницы
        if(!(pageLoadStrategy.equals("normal") || pageLoadStrategy.equals("eager") || pageLoadStrategy.equals("none")))
            throw new RuntimeException("Введено некорректное название стратегии загрузки страницы");

        //создаём и возвращаем соответствующий драйвер браузера
        switch (browserName) {
            case "chrome" -> {
                WebDriverManager.chromedriver().setup();
                logger.info("Драйвер для браузера " + browserName);
                ChromeOptions options = new ChromeOptions();
                options.setCapability("pageLoadStrategy", PageLoadStrategy.fromString(pageLoadStrategy));
                options.addArguments("--start-maximized");
                options.addArguments("--incognito");
                options.setPageLoadTimeout(Duration.ofSeconds(45));
                return new ChromeDriver(options);
            }
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                logger.info("Драйвер для браузера " + browserName);
                FirefoxOptions options = new FirefoxOptions();
                options.setCapability("pageLoadStrategy", PageLoadStrategy.fromString(pageLoadStrategy));
                options.addArguments("--private");
                options.addArguments("--maximized");
                options.setPageLoadTimeout(Duration.ofSeconds(45));
                return new FirefoxDriver(options);
            }
            default -> throw new RuntimeException("Введено некорректное название браузера");
        }
    }

}
