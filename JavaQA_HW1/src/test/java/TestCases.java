import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestCases {

    protected static WebDriver driver;
    private final Logger logger = LogManager.getLogger(TestCases.class);
    final String env = System.getProperty("browser","chrome");
    final String pageLoadStrategy = System.getProperty("loadstrategy", "normal").toLowerCase();

    @BeforeEach
    public void setUp(){
        logger.info("env = " + env);
        driver = WebDriverFactory.getDriver(env.toLowerCase(), pageLoadStrategy);
        if("firefox".equals(env) )
            driver.manage().window().maximize();
        logger.info("Драйвер стартовал");
    }

    @AfterEach
    public void setDown(){
        if(driver != null){
            driver.quit();
            logger.info("Драйвер остановлен!");
        }
    }

    @Test
    public void HW1Test(){
          Case1test();
          Case2Test();
    }

    public void Case1test()
    {
        logger.info("Тест-кейс №01\n");
        WebDriverWait  wait = new WebDriverWait(driver, Duration.ofSeconds(55));
        driver.get("https://www.dns-shop.ru/");

        logger.info("Открыта странциа DNS - " + driver.getCurrentUrl());
        logger.info("Title: " + driver.getTitle());
        logger.info("URL: " + driver.getCurrentUrl());
        logger.info(String.format("Ширина окна: %d", driver.manage().window().getSize().getWidth()));
        logger.info(String.format("Высота окна: %d", driver.manage().window().getSize().getHeight()));

        //Более элегантная задержка, чем sleep явно завязанная на нужную ссылку
        wait.withTimeout(Duration.ofSeconds(600)).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Бытовая техника")));
        try{
            WebElement cityButton = driver.findElement(By.xpath("(//div[@class=\"v-confirm-city\"]//span[@class=\"base-ui-button-v2__text\"])"));
            cityButton.click();
        } catch (NoSuchElementException e)
        {
            logger.info("Дурацкой кнопки с подтверждением города нет, и слава богу! Сколько она крови попила...");
        }

        // Почему не работает без этой задержки???!!!
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //и только теперь переходим по ссылке
        wait.withTimeout(Duration.ofSeconds(600)).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Бытовая техника")));
        wait.withTimeout(Duration.ofSeconds(600)).until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Бытовая техника")));
        WebElement link = wait.withTimeout(Duration.ofSeconds(600)).until(ExpectedConditions.elementToBeClickable(By.linkText("Бытовая техника")));
        link.click();

        //пока так
        wait.withTimeout(Duration.ofSeconds(600)).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Техника для кухни")));
        try{
            WebElement H1 = driver.findElement(By.xpath("(//div[@class=\"subcategory\"]/*[@class=\"subcategory__page-title\" ][contains(text(),\"Бытовая техника\")])"));
            logger.info("Выведен текст заголовка: " + H1.getText());
        } catch (NoSuchElementException e)
        {
            logger.error("не найден элемент Бытовая техника");
            driver.quit();
        }
        //переходим по ссылке "Техника для кухни"
        wait.withTimeout(Duration.ofSeconds(600)).until(ExpectedConditions.elementToBeClickable(By.linkText("Техника для кухни"))).click();
        //даём время прогрузку страницы
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //выбираем все категории в подкатегории
        List<WebElement> subCategories = driver.findElements(By.xpath("(//div[@class=\"subcategory__item-container \"]//span[@class=\"subcategory__title\"])"));
        logger.info("Выбранные категории:");
        for (WebElement suCat : subCategories) {
            logger.info(suCat.getText());
        }

        logger.info("Количество найденых категорий: "+subCategories.size());
// Добавление задержки Thread.sleep, чтобы увидеть результат
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("_____________________________\n");
    }

    public void Case2Test()
    {
        logger.info("Тест-кейс №01\n");
        WebDriverWait  wait = new WebDriverWait(driver, Duration.ofSeconds(55));
        driver.get("https://www.dns-shop.ru/");

        //Более элегантная задержка, чем sleep явно завязанная на нужную ссылку
        wait.withTimeout(Duration.ofSeconds(600)).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Бытовая техника")));
        try{
            WebElement cityButton = driver.findElement(By.xpath("(//div[@class=\"v-confirm-city\"]//span[@class=\"base-ui-button-v2__text\"])"));
            cityButton.click();
        } catch (NoSuchElementException e)
        {
            logger.info("Дурацкой кнопки с подтверждением города нет, и слава богу! Сколько она крови попила...");
        }
        // Почему не работает без этой задержки???!!!
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Actions actions = new Actions(driver);
        WebElement link = wait.withTimeout(Duration.ofSeconds(600)).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Бытовая техника")));
        actions.moveToElement(link).perform();

        //задержка чтобы отобразился элемент
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            driver.findElement(By.xpath("(//div[@class=\"menu-desktop__submenu menu-desktop__submenu_top\"]//*[@class=\"ui-link menu-desktop__first-level\"][contains(text(),\"Техника для кухни\")])"));
            logger.info("ссылка Техника для кухни - отображается");
        } catch (NoSuchElementException e) {
            logger.error("ссылка Техника для кухни - НЕ отображается!");
        }

        try {
            driver.findElement(By.xpath("(//div[@class=\"menu-desktop__submenu menu-desktop__submenu_top\"]//*[@class=\"ui-link menu-desktop__first-level\"][contains(text(),\"Техника для дома\")])"));
            logger.info("ссылка Техника для дома - отображается");
        } catch (NoSuchElementException e) {
            logger.error("ссылка Техника для дома - НЕ отображается!");
        }

        try {
            driver.findElement(By.xpath("(//div[@class=\"menu-desktop__submenu menu-desktop__submenu_top\"]//*[@class=\"ui-link menu-desktop__first-level\"][contains(text(),\"Красота и здоровье\")])"));
            logger.info("ссылка Красота и здоровье - отображается");
        } catch (NoSuchElementException e) {
            logger.error("ссылка Красота и здоровье - НЕ отображается!");
        }

        link = wait.withTimeout(Duration.ofSeconds(600)).until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText("Приготовление пищи")));
        actions.moveToElement(link).perform();

        //задержка на то, чтобы отобразился элемент
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<WebElement> subMenuLinks = driver.findElements(By.xpath("(//*[@class=\"menu-desktop__popup\"]/*[@class=\"ui-link menu-desktop__popup-link\"])"));
        if( 5 < subMenuLinks.size())
            logger.info("количество ссылок в подменю Приготовление пищи больше 5");
        else
            logger.warn("количество ссылок в подменю Приготовление пищи НЕ больше 5");

        link = wait.withTimeout(Duration.ofSeconds(600)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[@class=\"menu-desktop__popup\"]/*[@class=\"ui-link menu-desktop__popup-link\"][contains(text(),\"Плиты\")])")));
        actions.moveToElement(link).click().perform();

        // Добавление задержки Thread.sleep, чтобы увидеть результат
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        wait.withTimeout(Duration.ofSeconds(600)).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Плиты электрические"))).click();

        WebElement textElement = wait.withTimeout(Duration.ofSeconds(600)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//span[@class=\"products-count\"][@data-role=\"items-count\"])")));

        String regex = "(\\d*) товаров";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(textElement.getText());
        if (matcher.find()) {
            int count = Integer.parseInt(matcher.group(1));
            logger.info("В тексте Плиты электрические количество товаров = " + count + " Это " + (100 < count ? "больше 100" : "меньше 100"));
        }
        else
            logger.error("Регулярное выражение не сработало");
        logger.info("_____________________________\n");
    }
}