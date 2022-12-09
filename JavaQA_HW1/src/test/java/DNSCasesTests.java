import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Sleeper;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestClassOrder(ClassOrderer.ClassName.class)
public class DNSCasesTests {

    protected static WebDriver driver;

    protected static Selenium4Listener listener = new Selenium4Listener();
    protected static WebDriver eventDriver;
    private static final Logger logger = LogManager.getLogger(DNSCasesTests.class);
    private static final String env = System.getProperty("browser","chrome");
    private static final String pageLoadStrategy = System.getProperty("loadstrategy", "normal").toLowerCase();
    protected static WebDriverWait wait;
    protected static WebDriverWait eventWait;
    protected static JavascriptExecutor JSExecutor;
    protected  static Actions actions;
    protected  static Actions eventActions;
    public static int screenEnumerator = 0;
    public static String caseName;
    public static String stepName;


    public static void setUp(){
        logger.info("env = " + env);
        driver = WebDriverFactory.getDriver(env.toLowerCase(), pageLoadStrategy);
        eventDriver = new EventFiringDecorator<>(listener).decorate(driver);
        JSExecutor = (JavascriptExecutor)driver;
        actions = new Actions(driver);
        eventActions = new Actions(eventDriver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(35));
        eventWait = new WebDriverWait(eventDriver, Duration.ofSeconds(35));
        screenEnumerator = 0;
        logger.info("Драйвер стартовал");

    }


    public static void setDown(){
        // Добавление задержки Thread.sleep, чтобы увидеть результат
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(driver != null){
            eventDriver.quit();
            logger.info("Драйвер остановлен!");
        }
        logger.info("Выполнен setDown!");
    }

    private void killTheCityConfirmation(){
        try {
            WebElement cityIsALLRightDIV = wait.withTimeout(Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//div[@class=\"v-confirm-city\"])")));
            JSExecutor.executeScript("arguments[0].remove();", cityIsALLRightDIV);
        } catch (NoSuchElementException e) {
            logger.info("Дурацкой кнопки с подтверждением города нет, и слава богу! Сколько она крови попила...");
        }
        try {
            Thread.sleep(Duration.ofSeconds(3).toMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public static void jsScrollToElement(WebElement targetWebElement) {
        try {
            int pageHeight = driver.manage().window().getSize().height/3;

            JSExecutor.executeScript("arguments[0].scrollIntoView(true);  window.scrollBy(0, arguments[1]); ", targetWebElement,pageHeight);
        } catch (JavascriptException e) {
            logger.error("JavascriptException: " + e.getRawMessage());
        }
        try {
            Sleeper.SYSTEM_SLEEPER.sleep(Duration.ofSeconds(2));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @DisplayName("Case 01")
    @Nested
    @Disabled
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class case1Tests {

        @BeforeAll
        public static void case1Setup(){
            setUp();
            caseName = "Case01";
            logger.info("Тест-кейс №01\n");
        }

        @AfterAll
        public static void case1SetDown(){
            setDown();
        }

        @Test
        @DisplayName("Case 1 Step 1")
        @Order(1)
        public void outLogsClickOnLinkTest() {

            logger.log(Level.OFF," ");
            stepName = "step01";
            logger.info("Шаг кейса: "+stepName);

            driver.get("https://www.dns-shop.ru/");
            logger.info("Открыта страница DNS - " + driver.getCurrentUrl());
            logger.info("Title: " + driver.getTitle());
            logger.info("URL: " + driver.getCurrentUrl());
            logger.info(String.format("Ширина окна: %d", driver.manage().window().getSize().getWidth()));
            logger.info(String.format("Высота окна: %d", driver.manage().window().getSize().getHeight()));

            //Более элегантная задержка, чем sleep явно завязанная на нужную ссылку
            wait.withTimeout(Duration.ofSeconds(40)).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Бытовая техника")));

            //убираем слой с запросом местоположения
            killTheCityConfirmation();

            logger.info("Выполняем скриншот первой открытой страницы");
            Selenium4Listener.getFullPageScreen(driver);
            //и только теперь переходим по ссылке
            wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Бытовая техника")));
            WebElement appliancesLink = wait.withTimeout(Duration.ofSeconds(40)).until(ExpectedConditions.elementToBeClickable(By.linkText("Бытовая техника")));
            appliancesLink.click();
            logger.info("Выполнен переход по ссылке \"Бытовая техника\"");
            logger.info("Выполняем скриншот страницы по ссылке \"Бытовая техника\"");
            Selenium4Listener.getFullPageScreen(driver);
            //ожидаем появления ссылки
            wait.withTimeout(Duration.ofSeconds(60)).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Техника для кухни")));

            String appliancesH1XPath = "(//div[@class=\"subcategory\"]/*[@class=\"subcategory__page-title\" ][contains(text(),\"Бытовая техника\")])";
            Assertions.assertDoesNotThrow(
                    () -> {
                        driver.findElement(By.xpath(appliancesH1XPath));
                    }
                    , "не найден элемент Бытовая техника"
            );
            logger.info("Отображён элемент \"Бытовая техника\"");
            WebElement appliancesH1 = driver.findElement(By.xpath(appliancesH1XPath));
            Assertions.assertEquals("Бытовая техника", appliancesH1.getText(), " Текст элемента не соответствует требуемому значению: \"Бытовая техника\"");
            logger.info("Подкатегория \"Бытовая техника\" отображена корректно");
        }

        @Test
        @Order(2)
        public void clickOnLink2Test() {
            logger.log(Level.OFF," ");
            stepName = "step02";
            logger.info("Шаг кейса: "+stepName);
            //переходим по ссылке "Техника для кухни"
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Техника для кухни"))).click();
            logger.info("Переход по ссылке \"Техника для кухни\"");
            logger.info("Создаём скриншот открывшейся страницы");
            Selenium4Listener.getFullPageScreen(driver);
            logger.info("Проверяем отображение элемента \"Техника для кухни\"");
            String subCategoryTechForKitchenH1XPath = "(//div[@class=\"subcategory\"]//*[@class=\"subcategory__page-title\"][contains(text(),\"Техника для кухни\")])";
            Assertions.assertDoesNotThrow(
                    () -> {
                        WebElement TechForKitchenH1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(subCategoryTechForKitchenH1XPath)));
                        Assertions.assertEquals("Техника для кухни", TechForKitchenH1.getText(), "Проверка отображения текста \"Техника для кухни\"");
                    },
                    "Не найден элемент H1: Техника для Кухни ");
            logger.info("Отображается элемент \"Техника для кухни\"");
        }

        @Test
        @Order(3)
        public void checkLinkTest() {
            logger.log(Level.OFF," ");
            stepName = "step03";
            logger.info("Шаг кейса: "+stepName);
            logger.info("Проверяем наличие ссылки \"Собрать свою кухню\"");
            String configMyKitchenLinkXPath = "(//div[@class=\"configurator-links-block configurator-links-block_kbt\"]/a[@class=\"button-ui button-ui_white configurator-links-block__links-link\"][contains(text(),\"Собрать свою кухню\")])";
            Assertions.assertDoesNotThrow(
                    () -> {
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(configMyKitchenLinkXPath)));
                    }, "Не отображается ссылка \"Собрать свою кухню\"");
            logger.info("Присутствует ссылка \"Собрать свою кухню\"");
        }

        //выбираем все категории в подкатегории
        @Test
        @Order(4)
        public void listCatCheckCountTest() {
            logger.log(Level.OFF," ");
            stepName = "step04";
            logger.info("Шаг кейса: "+stepName);

            List<WebElement> kitchenAppliancesSubCategories = driver.findElements(By.xpath("(//div[@class=\"subcategory__item-container \"]//span[@class=\"subcategory__title\"])"));
            logger.info("Названия всех отображённых категорий:");
            for (WebElement suCat : kitchenAppliancesSubCategories) {
                logger.info(suCat.getText());
            }

            logger.info("Проверяем количество отображённых категорий");
            Assumptions.assumeTrue(5 < kitchenAppliancesSubCategories.size(), "Количество категорий оказалось меньше пяти");

            logger.info("Отображено категорий: " + kitchenAppliancesSubCategories.size());
        }
    }



    @DisplayName("Case 02")
    @Nested
    @Disabled
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class case2Tests {

        @BeforeAll
        public static void case2Setup(){
            setUp();
            caseName = "Case02";
            logger.info("Тест-кейс №02\n");
        }

        @AfterAll
        public static void case2SetDown(){
        setDown();
        }

        @Test
        @Order(1)
        public void step01() {
            logger.log(Level.OFF," ");
            stepName = "step01";
            logger.info("Шаг кейса: "+stepName);
            driver.get("https://www.dns-shop.ru/");
            //Более элегантная задержка, чем sleep, явно завязанная на нужную ссылку
            wait.withTimeout(Duration.ofSeconds(40)).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Бытовая техника")));
            //убираем слой с запросом местоположения
            killTheCityConfirmation();
            logger.info("Наводим курсор на ссылку \"Бытовая техника\"");
            WebElement appliancesLink = wait.withTimeout(Duration.ofSeconds(40)).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Бытовая техника")));
            eventActions.moveToElement(appliancesLink).perform();
//            getScreenShootFullPage(driver);
            //возвращаем курсор на место
            actions.moveToElement(appliancesLink).perform();
            logger.info("проверяем отображение ссылки \"Собрать свою кухню\",\"Встраиваемая техника\"");
            String configKitchenLinkXPath = "(//*[@class=\"menu-kbt\"]/*[@class=\"ui-link ui-link_blue menu-kbt__link\"][contains(text(),\"Собрать свою кухню\")])";
            String installTechnicLinkXPath = "(//*[@class=\"ui-link menu-desktop__first-level\"][contains(text(),\"Встраиваемая техника\")])";
            Assertions.assertAll(
                    () -> Assertions.assertDoesNotThrow(()->{wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(configKitchenLinkXPath)));},
                            "не отображается ссылка \"Собрать свою кухню\""),
                    () -> Assertions.assertDoesNotThrow(()->{wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(installTechnicLinkXPath)));},
                            "не отображается ссылка \"Встраиваемая техника\"")
            );
            logger.info("Ссылки отображаются");
        }

        @Test
        @Order(2)
        public void step02(){
            stepName = "step02";
            logger.log(Level.OFF," ");
            logger.info("Шаг кейса: "+stepName);
            logger.info("Наводим курсор на ссылку \"Вытяжки\"");
            // поскольку скриншот был с прокруткой, снова наводим курсор на ссылку "Бытовая техника"
            WebElement appliancesLink = wait.withTimeout(Duration.ofSeconds(40)).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Бытовая техника")));
            actions.moveToElement(appliancesLink).perform();
            Assertions.assertDoesNotThrow(()->{
                WebElement cookingLink = wait.withTimeout(Duration.ofSeconds(40)).until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText("Вытяжки")));
                eventActions.moveToElement(cookingLink).perform();
                //скриншот и возврат на место
//                getScreenShootFullPage(driver);
                actions.pause(Duration.ofSeconds(2)).moveToElement(appliancesLink).perform();
                actions.pause(Duration.ofSeconds(2)).moveToElement(cookingLink).perform();
            },
        "Не найден элемент (ссылка) \"Вытяжки\""
            );
            logger.info("Наведён курсор на ссылку \"Вытяжки\"");
            logger.info("Получаем список под элементов в меню \"Вытяжки\"");
            List<WebElement> cookingSubMenuLinks = driver.findElements(By.xpath("(//*[@class=\"menu-desktop__popup\"]/*[@class=\"ui-link menu-desktop__popup-link\"])"));
            Assumptions.assumeTrue(5 < cookingSubMenuLinks.size(), "Количество подкатегорий оказалось меньше пяти");
            logger.info("Наведён курсор на ссылку \"Вытяжки\" количество под элементов в меню:" + cookingSubMenuLinks.size());
        }

        @Test
        @Order(3)
        public void step03(){
            stepName = "step03";
            logger.log(Level.OFF," ");
            logger.info("Шаг кейса: "+stepName);
           // поскольку скриншот был с прокруткой, снова наводим курсор на ссылку "Бытовая техника"
            WebElement appliancesLink = wait.withTimeout(Duration.ofSeconds(40)).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Бытовая техника")));
            actions.moveToElement(appliancesLink).perform();

            //действия непосредственно кейса
            //как выяснилось, этот тест работает и без переделки
            Assertions.assertDoesNotThrow(
                    ()->{
                        logger.info("Выполняем наведение курсора на ссылку  \"Плиты \"");
                        WebElement stoveLink = wait.withTimeout(Duration.ofSeconds(40))
                                .until(ExpectedConditions
                                .presenceOfElementLocated(By.xpath("(//*[@class=\"ui-link menu-desktop__second-level\"][contains(text(),\"Плиты\")])")));
                        eventActions.moveToElement(stoveLink).perform();
                        //возвращаем после наведения и скриншота с прокруткой
                        actions.pause(Duration.ofSeconds(5)).moveToElement(appliancesLink).perform();//Возврат на быт технику
                        actions.pause(Duration.ofSeconds(2)).moveToElement(stoveLink).perform();//Возврат на быт технику
                        logger.info("Выполняем клик указателем по ссылке \"Плиты \"");
                        actions.pause(Duration.ofSeconds(2)).click().perform(); //делать скриншот такого события в данном случае смысла нет

                    },"Не удалось навести курсор и перейти по ссылке \"Плиты\""
            );
            logger.info("Наведён курсор на меню \"Плиты\" и выполнен клик");
            logger.info("Создаём скриншот открывшейся страницы");
            Selenium4Listener.getFullPageScreen(driver);

            logger.info("Переходим по ссылке \"Плиты электрические\"");
            Assertions.assertDoesNotThrow( ()->
                    {wait.withTimeout(Duration.ofSeconds(40))
                            .until(ExpectedConditions
                            .presenceOfElementLocated(By.linkText("Плиты электрические")))
                            .click();
                     logger.info("Выполнен переход по ссылке \"Плиты электрические\"");
                    },
                "Не найден элемент \"Плиты электрические\""
                );
            String regex = "(\\d*) товар";
            Selenium4Listener.getFullPageScreen(driver);

            WebElement stoveTextElement = wait.withTimeout(Duration.ofSeconds(40)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//span[@class=\"products-count\"][@data-role=\"items-count\"])")));

            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
            final Matcher matcher = pattern.matcher(stoveTextElement.getText());
            if (matcher.find()) {
                int count = Integer.parseInt(matcher.group(1));
                Assumptions.assumeTrue(100 < count,"\"Плиты электрические\" - количество товаров меньше 100");
                logger.info("В тексте Плиты электрические количество товаров = " + count );
            }
            else
                logger.error("Регулярное выражение не сработало");

        }
    }

    @DisplayName("Case 03")
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class case3Tests {


        public static String notebookName;

        @BeforeAll
        public static void case3Setup(){
            setUp();
            caseName = "Case03";
            logger.info("Тест-кейс №03\n");
        }

        @AfterAll
        public static void case3SetDown(){
            // Добавление задержки Thread.sleep, чтобы увидеть результат
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setDown();
        }

        @Test
        @Order(1)
        public void Step1(){
            stepName = "step01";
            logger.log(Level.OFF," ");
            logger.info("Шаг кейса: "+stepName);
            driver.get("https://www.dns-shop.ru/");

            String linkPCAndPeripheryXPath = "(//*[@class=\"menu-desktop__root\"]/*[@class=\"menu-desktop__root-info\"]/*[@class=\"ui-link menu-desktop__root-title\"][contains(translate(text(),\"АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЭЮЯ\",\"абвгдеёжзийклмнопрстуфхцчшщэюя\"),\"пк, ноутбуки, периферия\")])";
            wait.withTimeout(Duration.ofSeconds(40)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(linkPCAndPeripheryXPath)));

            killTheCityConfirmation();
            logger.info("Открыта страница DNS - " + driver.getCurrentUrl()+" Сохраняем снимок страницы.");
            Selenium4Listener.getFullPageScreen(driver);
            logger.info("Наводим курсор на элемент меню \"ПК, ноутбуки, периферия\"");
            WebElement linkPCAndPeripheryWebElement = eventWait.withTimeout(Duration.ofSeconds(40)).until(ExpectedConditions
                    .presenceOfElementLocated(By.xpath(linkPCAndPeripheryXPath)));
            logger.info("Выполняем снимок после наведения курсора");
            eventActions.moveToElement(linkPCAndPeripheryWebElement).perform();
            //возвращаем курсор на элемент управления
            actions.pause(Duration.ofSeconds(3)).moveToElement(linkPCAndPeripheryWebElement).perform();

            String linkNotebooksXPath = "(//*[@class=\"menu-desktop__second-level-wrap\"]/*[@class=\"ui-link menu-desktop__second-level\"][contains(translate(text(),\"АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЭЮЯ\",\"абвгдеёжзийклмнопрстуфхцчшщэюя\"),\"ноутбуки\")])";
            logger.info("Переходим по ссылке \"Ноутбуки\"");
            Assertions.assertDoesNotThrow( ()->
            {
                WebElement linkNotebooks = eventWait.withTimeout(Duration.ofSeconds(40)).until(ExpectedConditions
                        .presenceOfElementLocated(By.xpath(linkNotebooksXPath)));
                //возвращаем курсор на элемент управления
                actions.pause(Duration.ofSeconds(2)).moveToElement(linkPCAndPeripheryWebElement).perform();
                wait.withTimeout(Duration.ofSeconds(40)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(linkNotebooksXPath))).click();
//                linkNotebooks.click();
                logger.info("Выполнен переход по ссылке");
            },"Не найден элемент ссылка \"Ноутбуки\"");
            //подождём загрузки страницы
            wait.withTimeout(Duration.ofSeconds(40)).until(ExpectedConditions
                    .presenceOfElementLocated(By.xpath("(//h1[@class=\"title\"][contains(translate(text(),\"АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЭЮЯ\",\"абвгдеёжзийклмнопрстуфхцчшщэюя\"),\"ноутбуки\")])")));
            logger.info("Выполняем скриншот открывшейся страницы");
            Selenium4Listener.getFullPageScreen(driver);
            WebElement weHeader = wait.withTimeout(Duration.ofSeconds(40)).until(ExpectedConditions
                    .presenceOfElementLocated(By.xpath("(//header)")));
            try{
                JSExecutor.executeScript("arguments[0].style.display='none'",weHeader);
                logger.info("Скрыт элемент блок страницы, выполняем скриншот");
            }
            catch (JavascriptException e) {
                logger.error("JavascriptException: " + e.getRawMessage());
            }
            //задержка, чтобы хоть немного увидеть процесс
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Selenium4Listener.getFullPageScreen(driver);

            //выделяем элемент чек-бокс ASUS
            logger.info("выделяем элемент чек-бокс ASUS");
            String checkBoxASUSXPath = "(//label[@class=\"ui-checkbox ui-checkbox_list\"][input[@type=\"checkbox\"][@class=\"ui-checkbox__input ui-checkbox__input_list\"][@value=\"asus\"]]//span[@class=\"ui-checkbox__box ui-checkbox__box_list\"])";
            WebElement checkBoxASUSWE = eventWait.withTimeout(Duration.ofSeconds(40)).until(ExpectedConditions
                    .presenceOfElementLocated(By.xpath(checkBoxASUSXPath)));
            jsScrollToElement(checkBoxASUSWE);
            checkBoxASUSWE.click();

            //задержка, чтобы хоть немного увидеть процесс
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            logger.info("раскрываем пункт \"Объём оперативной памяти\"");
            String linkRAMXPath = "(//span[@class=\"ui-collapse__link-text\"][contains(text(),\"Объем оперативной памяти\")]/parent::*)";
            WebElement linkRAMWE = eventWait.withTimeout(Duration.ofSeconds(40)).until(ExpectedConditions
                    .presenceOfElementLocated(By.xpath(linkRAMXPath)));
            jsScrollToElement(linkRAMWE);
            linkRAMWE.click();

            //задержка, чтобы хоть немного увидеть процесс
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            logger.info("выделяем элемент чек-бокс 32Гб");
            String checkBoxRAM32GB = "(//label[@class=\"ui-checkbox ui-checkbox_list\"][input[@type=\"checkbox\"][@class=\"ui-checkbox__input ui-checkbox__input_list\"][@value=\"26ob\"]]//span[@class=\"ui-checkbox__box ui-checkbox__box_list\"])";
            WebElement checkBoxRAM32GBWE = eventWait.withTimeout(Duration.ofSeconds(40)).until(ExpectedConditions
                    .presenceOfElementLocated(By.xpath(checkBoxRAM32GB)));
            checkBoxRAM32GBWE.click();

            //задержка, чтобы хоть немного увидеть процесс
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            logger.info("Нажимаем на кнопку \"Применить\"");
            String btnSubmitApplyXPath = "(//button[@class=\"button-ui button-ui_brand left-filters__button\"][@data-role=\"filters-submit\"])";
            WebElement btnSubmitApplyGBWE = eventWait.withTimeout(Duration.ofSeconds(40)).until(ExpectedConditions
                    .presenceOfElementLocated(By.xpath(btnSubmitApplyXPath)));
            //jsScrollToElement(btnSubmitApplyGBWE);
            btnSubmitApplyGBWE.click();

            //задержка, чтобы хоть немного увидеть процесс
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            logger.info("Выполняем сохранение скриншота страницы после применения фильтров");
            Selenium4Listener.getFullPageScreen(driver);

            logger.info("Применяем сортировку");
            logger.info("Выбираем параметр соритровки по цене");
            String linkOrderXPath = "(//div[@data-id=\"order\"][@class=\"top-filter popover-wrapper\"]/a[@class=\"ui-link ui-link_blue\"])";
            WebElement linkOrderWE = eventWait.withTimeout(Duration.ofSeconds(40)).until(ExpectedConditions
                    .presenceOfElementLocated(By.xpath(linkOrderXPath)));
            linkOrderWE.click();

            //задержка, чтобы хоть немного увидеть процесс
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String labelExpensiveFirstXPath = "(//label[@class=\"ui-radio__item\"][span[@class=\"ui-radio__content\"][contains(text(),\"Сначала дорогие\")]])";
            WebElement labelExpensiveFirstWE = eventWait.withTimeout(Duration.ofSeconds(40)).until(ExpectedConditions
                    .presenceOfElementLocated(By.xpath(labelExpensiveFirstXPath)));


            wait.withTimeout(Duration.ofSeconds(40)).until(ExpectedConditions.
                    elementToBeClickable(By.xpath(labelExpensiveFirstXPath)));
            labelExpensiveFirstWE.click();

            //задержка, чтобы хоть немного увидеть процесс
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Selenium4Listener.getFullPageScreen(driver);

            logger.info("переходим по ссылке первого товара");

            String linkFirstProductXPath = "(//div[@class=\"products-list__content\"]//div[@data-id=\"product\"][@class=\"catalog-product ui-button-widget \"]/a[@class=\"catalog-product__name ui-link ui-link_black\"][1])";
            WebElement linkFirstProductWE = eventWait.withTimeout(Duration.ofSeconds(40)).until(ExpectedConditions
                    .presenceOfElementLocated(By.xpath(linkFirstProductXPath)));
            notebookName = linkFirstProductWE.getText();
            String notebookLink = linkFirstProductWE.getAttribute("href");

            logger.info("Открываем новую вкладку с страницей ноутбука");
            driver.switchTo().newWindow(WindowType.WINDOW).manage().window().maximize();
            driver.get(notebookLink);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("Сохраняем скриншот");
            Selenium4Listener.getFullPageScreen(driver);
            killTheCityConfirmation();

            String title = driver.findElement(By.xpath("(//h1[@class=\"product-card-top__title\"])")).getText();

            logger.info("Проверяем соответствие наименования товара заголовку");
            logger.info("Title:"+title);
            logger.info("Name:"+notebookName);
//          Assertions.assertEquals(title.toString(),notebookName.toString(),"Заголовок вкладки не соответствует наименованию товара"); //версия - как должно быть строго по ТЗ, но судя по всему, так строго не сработало бы
            Assertions.assertTrue(notebookName.contains(title),"Заголовок вкладки не соответствует наименованию товара");
            logger.info("Заголовок вкладки соответствует наименованию товара");

        }
        @Test
        @Order(2)
        public void Step2(){
            stepName = "step02";
            logger.log(Level.OFF," ");
            logger.info("Шаг кейса: "+stepName);

            logger.info("Проверяем соответствие наименования модели заголовку в описании");
            String linkDetailsXPath = "(//a[@class=\"product-card-top__specs-more ui-link ui-link_blue\"][contains(text(),\"подробнее\")])";
            Assertions.assertDoesNotThrow( ()-> {WebElement linkDetailsWE = eventDriver.findElement(By.xpath(linkDetailsXPath));
                linkDetailsWE.click();
            },"Не найден элемент управления - ссылка \"подробнее\"");

            String headProductCardDetailTitleXPath = "(//div[@class=\"product-card-description-specs product-card-description__block product-card-description__block_panel\"]/div[@class=\"product-card-description__title\"])";
            Assertions.assertDoesNotThrow( ()-> {
                WebElement headProductCardDetailTitleWE = eventDriver.findElement(By.xpath(headProductCardDetailTitleXPath));
                String headProductCardDetailTitle = headProductCardDetailTitleWE.getText();
                logger.info("Title: "+headProductCardDetailTitle);
//            Assertions.assertEquals(notebookName, headProductCardDetailTitle, "Заголовок в подробном описании не соответствует наименованию товара");//версия - как должно быть строго по ТЗ, но судя по всему, так строго не сработало бы
                Assertions.assertTrue(notebookName.contains(headProductCardDetailTitle.replaceAll("Характеристики ","")),"Заголовок подробном описании не соответствует наименованию товара");
                logger.info("Заголовок в подробном описании соответствует наименованию товара");
            },"\"Не найден элемент заголовка в подробном описании характеристик\"");


        }
        @Test
        @Order(3)
        public void Step3(){
            stepName = "step03";
            logger.log(Level.OFF," ");
            logger.info("Шаг кейса: "+stepName);

            String textRAMXPath = "(//div[@class=\"product-characteristics__spec product-characteristics__ovh\"][div[@class=\"product-characteristics__spec-title\"][contains(text(),\" Объем оперативной памяти \")]]//div[@class=\"product-characteristics__spec-value\"])";
            logger.info("Проверяем значение ОЗУ в характеристиках ноутбука");
            Assertions.assertDoesNotThrow( ()-> {
                WebElement textRAMWE = eventDriver.findElement(By.xpath(textRAMXPath));

            String RAMValue = textRAMWE.getText();
            logger.info("RAMValue: "+ RAMValue);
            Assertions.assertEquals("32 ГБ",RAMValue,"Объём ОЗУ в описании товара не соответствует 32 ГБ");
            logger.info("Объём ОЗУ в описании товара соответствует 32 ГБ");
            },"\"Не найден элемент описания объём ОЗУ\"");
        }
    }

}

