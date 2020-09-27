import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.example.JSUtils;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Class12_HW12 {

    private static WebDriver driver;
    // create ExtentReports and attach reporter(s)
    public static ExtentReports extent;
    // creates a toggle for the given test, adds all log events under it
    public static ExtentTest test;
    boolean pageOpened = false;
    boolean clickElement = false;

    @BeforeClass // Chrome Webdriver definition
    public static void beforeClass() {
        System.setProperty("webdriver.chrome.driver", "E:\\QA_Automation_Java\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        ExtentSparkReporter htmlReporter = new ExtentSparkReporter("C:\\Users\\Misha\\Downloads\\Extent_GoogeTranslateTest.html");
        // attach reporter
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        // name your test and add description
        test = extent.createTest("Extent Report for GoogeTranslateTest", "Sample description");
        // add custom system info
        extent.setSystemInfo("Environment", "IntellyJ Idea");
        extent.setSystemInfo("Development & QA", "Michael");
        test.log(Status.INFO, "Report start");
    }

    // 1.
    //• Enter https://dgotlieb.github.io/Navigation/Navigation.html
    //• Print iFrame text.
    @Test
    public void test01_printIframeText() {
        driver.get("https://dgotlieb.github.io/Navigation/Navigation.html");
        driver.switchTo().frame("my-frame");
        System.out.println(driver.findElement(By.id("iframe_container")).getText());

        try {
            File logFile=new File("C:\\Users\\Misha\\Downloads\\HW12.log");
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
            writer.newLine();
            writer.write (Instant.now().toString() + ": " + driver.findElement(By.id("iframe_container")).getText() + "\n");
            writer.newLine();
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    // 2.
    //Create a TestNG test with the following:
    // Enter https://dgotlieb.github.io/Actions
    // Take a screenshot of the box element
    // Drag wheel into box.
    // Double click the text “Double-click…” check what happened
    //and create assertion on result.
    // Make a mouse hover on X image.
    // Select two items from food list.
    // Upload a file.
    // Scroll down to “click me button” try both scroll to element
    //and scroll to location.
    @Test
    public void test02_actions() throws InterruptedException {
        driver.get("https://dgotlieb.github.io/Actions/");
        takeElementScreenshot(driver.findElement(By.id("div1"))); // Take a screenshot of the box element

        WebElement firstElement = driver.findElement(By.id("drag1")); // Drag wheel into box.
        WebElement secondElement = driver.findElement(By.id("div1"));
        JSUtils.JavascriptDragAndDrop(driver,firstElement,secondElement);

        Actions myAction = new Actions(driver); // Double click the text “Double-click…” check what happened + assert
        myAction.doubleClick(driver.findElement(By.xpath("//p[contains(text(), 'Double-click text to trigger a function.')]")));
        myAction.perform();
        Assert.assertEquals("You double clicked", driver.findElement(By.id("demo")).getText());

        myAction.moveToElement(driver.findElement(By.id("close"))).perform(); // Make a mouse hover on X image.
        Thread.sleep(2000);
        myAction.moveToElement(driver.findElement(By.id("title"))).perform();

        // Select two items from food list.
        myAction.click(driver.findElement(By.xpath("//option[@value='pizza']")))
                .keyDown( Keys.CONTROL)
                .click(driver.findElement(By.xpath("//option[@value='burger']")));
        myAction.build().perform();

        // Upload a file.
        driver.findElement(By.xpath("//input[@type='file']")).sendKeys("C:\\Users\\Misha\\Downloads\\ShanaTova.PNG");

        // Scroll down to “click me button” try both scroll to element and scroll to location.
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.id("clickMe")));
        Thread.sleep(2000);
        myAction.moveToElement(driver.findElement(By.id("title"))).perform(); // Scroll to top
        Thread.sleep(2000);
        ((JavascriptExecutor) driver).executeScript("javascript:window.scrollBy(8,860)"); // scroll to location
    }


    // 3.
    // Create XML file with a key name URL and a value with any
    //website URL (e.g. https://www......com).
    // Build a TestNG test with the following:
    //- Read the URL value from the XML file.
    //- Open the browser with the given URL.
    //- Change the URL in the XML file (manually) to test the effect
    @Test
    public void test03_readFromXml() throws Exception {
        driver.get(GetXmlData.getData("Url"));
    }

    // 4. Create a TestNG test with the following:
    // Setup Extent Reports.
    // Log every step in your program into the report (@BeforeClass,
    //@Test, etc.).
    // Go to Google Translate.
    // Take a screen shot and it to report.
    // Press on translation field (the one you enter the text to be
    //translated).
    // Add your company name to system info.
    @Test
    public void test04_extentReport() {
        pageOpened = false;
        try {
            driver.get(GetXmlData.getData("UrlGoogleTranslate")); // read from data.xml  https://translate.google.com
            pageOpened = true;
            String timeNow = String.valueOf(System.currentTimeMillis());
            test.info("ScreenShot", MediaEntityBuilder.createScreenCaptureFromPath(takeScreenShot("C:\\Users\\Misha\\Downloads\\" + timeNow)).build());

        } catch (Exception e) {
            e.printStackTrace();
            test.log(Status.FAIL, "translate.google.com page was not found  " + e.getMessage());
            pageOpened = false;

        } finally {
            if (pageOpened) {
                test.log(Status.PASS, "Open webpage:  " + "Webpage opened successfully");
            }
        }


        //driver.findElement(By.xpath("//textarea[@class='orig tlid-source-text-input goog-textarea']")); // Click on translation field
        clickElement = false;
        try {
            driver.findElement(By.xpath("//textarea[@class='orig tlid-source-text-input goog-textarea']")); // Click on translation field
            clickElement = true;
            String timeNow = String.valueOf(System.currentTimeMillis());
            test.info("ScreenShot", MediaEntityBuilder.createScreenCaptureFromPath(takeScreenShot("C:\\Users\\Misha\\Downloads\\" + timeNow)).build());

        } catch (Exception e) {
            e.printStackTrace();
            test.log(Status.FAIL, "Element 'translation field' is not clickable " + e.getMessage());
            clickElement = false;

        } finally {
            if (clickElement) {
                test.log(Status.PASS, "Click Element:  " + "Element 'translation field' clicked successfully");
            }
        }
    }

    // 6.
    //• Open Google in first tab
    //• Open YouTube on the second tab
    //• Open Google translate in the third tab.
    //• From translate go to Google and from Google go to YouTube.
    @Test
    public void test06_tabsFocus() throws Exception {
        openNewTab(driver, "https://www.google.com", 1);
        openNewTab(driver, "https://www.youtube.com", 2);
        openNewTab(driver, "https://translate.google.com", 3);

        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        Thread.sleep(2000);
        driver.switchTo().window(tabs.get(2));
    }

    // Extent Report flush
    @AfterClass
    public void afterClass() throws Exception {
        extent.flush();
    }




    private static void takeElementScreenshot(WebElement element){
        File screenShotFile = element.getScreenshotAs(OutputType.FILE); // take the screenshot
        try {
            FileUtils.copyFile(screenShotFile, new File("element-screenshot.png")); // save screenshot to disk
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String takeScreenShot(String ImagesPath) {
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File screenShotFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
        File destinationFile = new File(ImagesPath+".png");
        try {
            FileUtils.copyFile(screenShotFile, destinationFile);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return ImagesPath+".png";
    }

    public void openNewTab(WebDriver driver, String url, int position) {
        ((JavascriptExecutor) driver).executeScript("window.open()");

        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        System.out.println("tabs : " + tabs.size() + " >position: " + position + " >\t" + url);
        driver.switchTo().window(tabs.get(position));

        driver.get(url);
    }

}
