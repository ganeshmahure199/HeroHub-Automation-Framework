package OrangeHRM.Utility;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import net.bytebuddy.utility.RandomString;

public class Log {
    
    public static ExtentReports extent;
    public static ExtentSparkReporter reporter;
    
    private static final ThreadLocal<ExtentTest> extentLogger = new ThreadLocal<>(); 
    public static final Logger LOGGER = LogManager.getLogger("Log");

    public static ExtentTest getTest() {
        return extentLogger.get();
    }

    public static void setTest(ExtentTest test) {
        extentLogger.set(test);
    }
    
    public static void removeTest() {
        extentLogger.remove();
    }

    public static void info(String message) {
        LOGGER.info(message);
        if (getTest() != null) {
            getTest().log(Status.PASS, message); 
        }
    }

    public static void warn(String message) {
        LOGGER.warn(message);
        if (getTest() != null) {
            getTest().log(Status.WARNING, MarkupHelper.createLabel(message, ExtentColor.YELLOW)); 
        }
    }

    public static void AddScreenshot() {
        if (getTest() != null) {
            getTest().addScreenCaptureFromBase64String(BaseTest.getscreenshot()); 
        }
    }

    public static void error(String message) {
        LOGGER.error(message);
        if (getTest() != null) {
            getTest().log(Status.FAIL, MarkupHelper.createLabel(message, ExtentColor.RED)); 
            getTest().addScreenCaptureFromBase64String(BaseTest.getscreenshot());
        }
    }   

    public static void initialiseExtentReport() {
        String reportPath = Path.of(System.getProperty("user.dir"), "Reports", "ExtentReport.html").toString();
        reporter = new ExtentSparkReporter(reportPath);
        
        reporter.config().setDocumentTitle("Orange_HRM_Automation Web Automation Report");
        reporter.config().setReportName("Orange_HRM_Automation Web Automation Report");
        reporter.config().setTheme(Theme.DARK);
        reporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
        
        extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("Project Name", "Orange_HRM_Automation");
        extent.setSystemInfo("Platform", "Web");
        extent.setSystemInfo("Test Environment", "UAT");
        extent.setSystemInfo("Test Suite", "Sanity Tests");
        extent.setSystemInfo("QA Engineer", "Ganesh Mahure");
    }

    public static void flushExtent() {
        if (extent != null) {
            extent.flush();
        }
    }

    public static void afterMethodLogResult(Method method, ITestResult result, WebDriver driver) throws IOException {
        if (getTest() == null) return;
        if (result.getStatus() == ITestResult.FAILURE) {
            getTest().log(Status.FAIL, MarkupHelper.createLabel("TEST FAILED -- " + result.getName(), ExtentColor.RED));            
            String screenshotPath = takeScreenshot(method.getName(), driver);
            getTest().addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");            
        } else if (ITestResult.SUCCESS == result.getStatus()) {
            getTest().log(Status.PASS, MarkupHelper.createLabel("TEST PASSED -- " + result.getName(), ExtentColor.GREEN));
        } else if (ITestResult.SKIP == result.getStatus()) {
            getTest().log(Status.SKIP, MarkupHelper.createLabel("TEST SKIPPED -- " + result.getName(), ExtentColor.ORANGE));
            getTest().skip(result.getThrowable());
        }
    }

    public static String takeScreenshot(String methodName, WebDriver driver) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy-hhmmss");
        String strDate = formatter.format(date);        
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);        
        String fileName = strDate + "_" + RandomString.make(2) + "_" + methodName + ".jpg";                
        String relativePath = "FailedTestScreenShots" + File.separator + fileName;
        Path targetPath = Path.of(System.getProperty("user.dir"), "Reports", "FailedTestScreenShots", fileName);        
        try {
            Files.createDirectories(targetPath.getParent());
            Files.copy(srcFile.toPath(), targetPath);
        } catch (IOException e) {
            LOGGER.error("Failed to save screenshot: " + e.getMessage());
        }               
        return relativePath;
    }   
}
