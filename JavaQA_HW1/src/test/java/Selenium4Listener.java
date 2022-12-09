import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.events.WebDriverListener;
import org.openqa.selenium.support.ui.Sleeper;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.cropper.indent.IndentCropper;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Selenium4Listener implements WebDriverListener {

    private final static Logger logger = LogManager.getLogger(Selenium4Listener.class);

    @Override
    public void afterClick(WebElement element){
        logger.info("клик по событию \"after Click\" по элементу: "+element.toString());
    }

    @Override
    public void afterFindElement(WebDriver driver, By locator, WebElement result){
        JavascriptExecutor js = (JavascriptExecutor)driver;
        Long pageYOffset = 0L;
        Object pageYOffsetObj = null;
        logger.info("event afterFindElement(WebDriver ...) with: "+(result.getAccessibleName().equals("")?"unnamed element":result.getAccessibleName()));
        String borderStyle = "0px none rgb(51, 51, 51)";

        try {
            Sleeper.SYSTEM_SLEEPER.sleep(Duration.ofSeconds(2));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try{
            pageYOffsetObj = js.executeScript("return window.pageYOffset;");

        } catch (JavascriptException e) {
            logger.info("getOffset operation: JavascriptException: " + e.getRawMessage());
        }

        try {
            Double tmpOffset = Double.parseDouble(pageYOffsetObj.toString());
            pageYOffset = tmpOffset.longValue();
        }catch (NullPointerException e)
        {
            logger.info(e.getMessage());
        }

        try{
            borderStyle = js.executeScript("return window.getComputedStyle(arguments[0]).border;",result).toString();
            js.executeScript("arguments[0].style.border='2px solid red' ;",result);
        } catch (JavascriptException e) {
            logger.info("border operation: JavascriptException: " + e.getRawMessage());
        }

        try {
            Sleeper.SYSTEM_SLEEPER.sleep(Duration.ofSeconds(3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        getScreenShootFullPage(driver);

        try {
            Sleeper.SYSTEM_SLEEPER.sleep(Duration.ofSeconds(3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try{
            js.executeScript("arguments[0].style.border = arguments[1];",result,borderStyle);
        } catch(JavascriptException e){
            logger.info("border clearing: JavascriptException: " + e.getRawMessage());
        }
        try {
            Sleeper.SYSTEM_SLEEPER.sleep(Duration.ofSeconds(2));
        } catch (InterruptedException e) {
            e.printStackTrace();
            }

        try{
            js.executeScript("window.scrollTo(0, arguments[0]);", pageYOffset);
        }
        catch(JavascriptException e){
            logger.info("roll back operation: JavascriptException: " + e.getRawMessage());
            }
        try {
            Sleeper.SYSTEM_SLEEPER.sleep(Duration.ofSeconds(3));
        } catch (InterruptedException e) {
            e.printStackTrace();
            }

    }
    @Override
    public void afterFindElement(WebElement driver, By locator, WebElement result){
        logger.info("event afterFindElement(WebElement ...)");

        afterFindElement(DNSCasesTests.driver, locator, result);
    }
    @Override
    public void afterFindElements(WebDriver driver, By locator, List result){
        JavascriptExecutor js = (JavascriptExecutor)driver;
        Long pageYOffset = 0L;
        Object pageYOffsetObj = null;
        logger.info("event afterFindElement(WebDrivers ...) with: List");
        List<String> borderStyles =null;

        try {
            Sleeper.SYSTEM_SLEEPER.sleep(Duration.ofSeconds(2));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try{
            pageYOffsetObj = js.executeScript("return window.pageYOffset;");

        } catch (JavascriptException e) {
            logger.info("getOffset operation: JavascriptException: " + e.getRawMessage());
        }

        try {
            Double tmpOffset = Double.parseDouble(pageYOffsetObj.toString());
            pageYOffset = tmpOffset.longValue();
        }catch (NullPointerException e)
        {
            logger.info(e.getMessage());
        }

        try{
            borderStyles = (List<String>)js.executeScript("var arr =[]; arguments[0].forEach(function(item, i, arguments[0]){arr.push(window.getComputedStyle(item).border)})); return arr;",result.toArray());
            js.executeScript("arguments[0].style.border='2px solid red' ;",result);
        } catch (JavascriptException e) {
            logger.info("border operation: JavascriptException: " + e.getRawMessage());
        }

        try {
            Sleeper.SYSTEM_SLEEPER.sleep(Duration.ofSeconds(3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        getScreenShootFullPage(driver);

        try {
            Sleeper.SYSTEM_SLEEPER.sleep(Duration.ofSeconds(3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try{
            js.executeScript("arguments[0].forEach(function(item, i, arguments[0]){item.style.border=arguments[1][i])})); return arr",result,borderStyles);
        } catch(JavascriptException e){
            logger.info("border clearing: JavascriptException: " + e.getRawMessage());
        }
        try {
            Sleeper.SYSTEM_SLEEPER.sleep(Duration.ofSeconds(2));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try{
            js.executeScript("window.scrollTo(0, arguments[0]);", pageYOffset);
        }
        catch(JavascriptException e){
            logger.info("roll back operation: JavascriptException: " + e.getRawMessage());
        }
        try {
            Sleeper.SYSTEM_SLEEPER.sleep(Duration.ofSeconds(3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void afterFindElements(WebElement element, By locator, List result){
        logger.info("event afterFindElements(WebElement ...)");
        afterFindElements(DNSCasesTests.driver,locator,result);

    }

    @Override
    public void afterGetText(WebElement element, String result){
        logger.info("Событие afterGetText по элементу "+element.toString());
    }

    @Override
    public void afterPerform(WebDriver driver, Collection<Sequence> actions){
        boolean isMouse = false;
        boolean isMove = false;
        boolean isPointerDown = false;
        boolean isPointerUp = false;
        RemoteWebElement targetWebElement = null;
        for(Sequence sequence:actions){
            for(Map.Entry<String, Object> entry:sequence.encode().entrySet())
            {
                //строка чтобы просмотреть в логах структуру действий
//                logger.info("Key: "+entry.getKey()+" | Value: "+entry.getValue()+" | Type: " +entry.getValue().getClass());
                if(entry.getKey().equals("parameters"))
                {
                    HashMap<String, String> val = (HashMap<String, String>) entry.getValue();//;
                    if(val.get("pointerType").equals("mouse"))
                    {
                        isMouse = true;
                    }
                }
                if(entry.getKey().equals("actions"))
                {
                    LinkedList<HashMap<String, ?>> actionsEntries = (LinkedList<HashMap<String, ?>>)entry.getValue();
                    for(HashMap<String, ?> actionEntry:actionsEntries)
                    {
                        if(actionEntry.get("type") != null){
                            if (actionEntry.get("type").equals("pointerMove") && isMouse){
                                //значит мы перемещаем и перемещаем указатель мыши
                                isMove = true;
                            }
                            if(actionEntry.get("type").equals("pointerDown")){
                                isPointerDown = true;
                            }
                            if(actionEntry.get("type").equals("pointerUp")){
                                isPointerUp = true;
                            }
                        }
                        if(actionEntry.get("origin") != null)
                        {
                            targetWebElement = (RemoteWebElement)actionEntry.get("origin");
                        }
                    }
                }
            }
        }
        if(isMouse && isMove && targetWebElement != null){
            getFullPageScreen(driver);
        }
        if(isPointerDown && isPointerUp && isMouse){
            logger.info("кликк по событию \"after Perform \"");
            getFullPageScreen(driver);
        }
    }

    private void getScreenShootFullPage(WebDriver driver){
        try {
                Sleeper.SYSTEM_SLEEPER.sleep(Duration.ofSeconds(1));
            } catch (InterruptedException e) {
                e.printStackTrace();
        }
        try{
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(100))
                    .takeScreenshot(driver);

            ImageIO.write(screenshot.getImage(), "png", new File("temp\\screenASFullPage_"+DNSCasesTests.caseName+"_"+DNSCasesTests.stepName+"_"+ ++ DNSCasesTests.screenEnumerator + ".png"));
            logger.info("Скриншот сохранен в файле [temp\\screenASFullPage_"+DNSCasesTests.caseName+"_"+DNSCasesTests.stepName+"_"+ DNSCasesTests.screenEnumerator + ".png]");

        }
            catch (IOException e) {
                e.printStackTrace();
        }
        try {
            Sleeper.SYSTEM_SLEEPER.sleep(Duration.ofSeconds(3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void getFullPageScreen(WebDriver driver){
        JavascriptExecutor js = (JavascriptExecutor)driver;

        try {
            Sleeper.SYSTEM_SLEEPER.sleep(Duration.ofSeconds(5));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(100))
                    .imageCropper(new IndentCropper())
                    .takeScreenshot(driver);

            ImageIO.write(screenshot.getImage(), "png", new File("temp\\screenASFullPage_" + DNSCasesTests.caseName + "_" + DNSCasesTests.stepName + "_" + ++DNSCasesTests.screenEnumerator + ".png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Скриншот сохранен в файле [temp\\screenASFullPage_"+DNSCasesTests.caseName+"_"+DNSCasesTests.stepName+"_"+ DNSCasesTests.screenEnumerator + ".png]");

        try{
           js.executeScript("window.scrollTo(0, 0);");
        } catch (JavascriptException e) {
            logger.info("JavascriptException: " + e.getRawMessage());
        }
    }
}
