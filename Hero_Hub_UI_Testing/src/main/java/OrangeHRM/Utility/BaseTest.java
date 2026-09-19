package OrangeHRM.Utility;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentTest;
import com.google.common.io.Files;

import OrangeHRM.ExcelDataProvider.excelTestData;
import net.bytebuddy.utility.RandomString;

public class BaseTest extends ConfigeDataProvider {
    
    @BeforeSuite(alwaysRun = true)
    public void setupSuite() {        
        Log.initialiseExtentReport();
    }
    
    @BeforeClass(alwaysRun = true)
    public void beforeClass() throws Exception {     
        launchBrowser();
        exceldata = new excelTestData(0, 1);
        login = new OrangeHRM.pages.loginPage(driver);
    }
    
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method, ITestResult result) throws Exception {      
        String testName = result.getTestClass().getName() + " = " + method.getName();              
        ExtentTest test = OrangeHRM.Utility.Log.extent.createTest(testName);
        OrangeHRM.Utility.Log.setTest(test);
        LOGGER.debug("Start -> Test -> " + method.getName());
}

    @AfterMethod(alwaysRun = true)
    public void afterMethod(Method method, ITestResult result) throws Exception  {
        Log.afterMethodLogResult(method, result, driver);
        Library.threadSleep(1000);
        LOGGER.debug("End -> Test -> " + method.getName());                                
        OrangeHRM.Utility.Log.removeTest();
    }

    @AfterClass(alwaysRun = true)
    public void afterClass()  {             
      quitBrowser(); 
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {           
        OrangeHRM.Utility.Log.flushExtent();
    }

//=================================================================================================================    
   
    public static void launchBrowser() throws Exception {               
        String browserName = ConfigeDataProvider.getBrowserName();        
        if (browserName.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();            
        } else if (browserName.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();            
        } else {            
            ChromeOptions options = new ChromeOptions();        
            options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));                     
            options.addArguments("--force-device-scale-factor=0.9");         
            
            if (System.getenv("JENKINS_URL") != null) {
                options.addArguments("--headless=new", "--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");
            }                
            
            System.setProperty("webdriver.chrome.silentOutput", "true");        
            driver = new ChromeDriver(options);      
        }
        driver.manage().window().maximize();                
        driver.get(ConfigeDataProvider.getOrangeHrmUrl());                    
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));      
    }
 

    public static void quitBrowser() {
        if (driver != null) {
            driver.quit(); 
            driver = null; 
        }
    }   

    public static String getscreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);   
    }

    public static String takeScreenshot() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy-hhmmss");
        String strDate = formatter.format(date);
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);                
        String path = System.getProperty("user.dir") + File.separator + "Reports" + File.separator 
                    + "ChromeTestScreenShots" + File.separator + strDate + "_" + RandomString.make(5) + "_.jpg";
        try {
            Files.copy(srcFile, new File(path));
        } catch (IOException e) {          
            e.printStackTrace();
        }
        return path;
    }
}
