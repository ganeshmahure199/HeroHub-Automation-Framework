package OrangeHRM.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class Library {
    
    private static Properties prop;

    static {
        try {
            String configPath = System.getProperty("user.dir") + File.separator + "src" + File.separator 
                                + "test" + File.separator + "resources" + File.separator + "Config_Data" 
                                + File.separator + "prod_config.Properties";
            try (FileInputStream fis = new FileInputStream(configPath)) {
                prop = new Properties();
                prop.load(fis);
            }
        } catch (Exception e) {
            Log.LOGGER.error("Failed to pre-load configuration file: " + e.getMessage());
        }
    }

    public static void Custom_Click(WebDriver driver, WebElement element, String logMessage)  {
        try {
            waitForVisibilityOf(driver, element);
            element.click();
            Log.info("Element clicked successfully: " + logMessage);
        } catch (Exception e) {     
            Log.error("Unable to click element: " + logMessage + " -- " + element + " | Exception: " + e.getMessage());
            Assert.fail("Test failed due to element click failure: " + logMessage);
        }
    }

    public static void Custom_SendKeys(WebDriver driver, WebElement element, String valueToEnter, String logMessage) {
        try {
            waitForVisibilityOf(driver, element);
            element.click();
            element.clear();
            element.sendKeys(valueToEnter);
            Log.info("Value injected successfully to field [" + logMessage + "]: " + valueToEnter);
        } catch (Exception e) {
            Log.error("Unable to send value to field [" + logMessage + "] -- Element: " + element + " | Exception: " + e.getMessage());
            Assert.fail("Test failed due to sendKeys execution failure: " + logMessage);
        }
    }
        
    public static void custom_HandleDrpDown(WebDriver driver, WebElement element, String text, String logMessage) {
        try {
            waitForVisibilityOf(driver, element);
            Select select = new Select(element);
            select.selectByVisibleText(text);
            Log.info("Dropdown selection completed for [" + logMessage + "] with option: " + text);
        } catch(Exception e) {
            Log.error("Unable to select dropdown options for [" + logMessage + "] -- Element: " + element + " | Exception: " + e.getMessage());
            Assert.fail("Test failed due to dropdown selection failure: " + logMessage);
        }
    }
            
    public static WebElement waitForVisibilityOf(WebDriver driver, WebElement element) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            Log.error("Timeout waiting for visibility of element -- Element: " + element);
            throw e;
        }
        return element;
    }

    public static void threadSleep(int milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Log.LOGGER.error("Thread sleep interrupted: " + e.getMessage());
        }
    }
    
    public static String getStringConfigData(String key) {
        if (prop != null && prop.containsKey(key)) {
            return prop.getProperty(key);
        }
        Log.LOGGER.error("Configuration key not found: " + key);
        return null;
    }

    public static int getNumericConfigData(String key) {
        String value = getStringConfigData(key);
        if (value != null) {
            return Integer.parseInt(value.trim());
        }
        throw new NullPointerException("Numeric configuration value missing for key: " + key);
    }
    
    public static String getExcelData(int SheetNumber, int RowNumber, int ColumnNumber) {
        String path = System.getProperty("user.dir") + File.separator + "src" + File.separator 
                      + "test" + File.separator + "resources" + File.separator + "TestData" + File.separator + "ExcelTestData.xlsx";
        
        try (FileInputStream fis = new FileInputStream(path); 
             XSSFWorkbook wb = new XSSFWorkbook(fis)) {                        
             XSSFSheet sheet = wb.getSheetAt(SheetNumber);
             XSSFRow row = sheet.getRow(RowNumber);
            if (row == null) {
                return ""; 
            }           
            XSSFCell cell = row.getCell(ColumnNumber);
            if (cell == null) {
                return "";
            }            
            try {
                return cell.getStringCellValue();
            } catch (Exception e) {
                double numericValue = cell.getNumericCellValue();
                String testData = String.valueOf(numericValue);
                String[] testDataArray = testData.split("\\.");
                return testDataArray[0];
            }
        } catch (IOException e) {
            Log.LOGGER.error("Failed to read Excel data sheet: " + e.getMessage());
            return "";
        }
    }
        
    public static void isDisplayed(WebDriver driver, WebElement ele, String elementName) {
        try {
            waitForVisibilityOf(driver, ele);
            if (ele.isDisplayed()) {                
                Log.info(elementName + ": Element is verified as Displayed");
            } else {
                Log.error(elementName + ": Element reports hidden validation flag status");
            }
        } catch(Exception e) {                  
            Log.error(elementName + ": Failed display visibility state validation trace: " + e.getMessage());                    
        }
    }      
          
    public static void assertEquals(WebDriver driver, WebElement ele, String expectedValue) {
        try {   
            waitForVisibilityOf(driver, ele);
            String actualValue = ele.getText().trim();
            if (actualValue.equalsIgnoreCase(expectedValue.trim())) {                
                Log.info("Assertion Match Successful! Expected: [" + expectedValue + "] matched Actual text content.");
            } else {
                Log.error("Assertion Mismatch! Expected: [" + expectedValue + "] but found text content: [" + actualValue + "]");
                Assert.assertEquals(actualValue, expectedValue);
            }
        } catch(Exception e) {                  
            Log.error("Assertion Exception Error encountered testing: [" + expectedValue + "] | Trace: " + e.getMessage());                 
            Assert.fail(e.getMessage());
        }
    }          
       
    public static void assertEquals(WebDriver driver, String actualValue, String expectedValue) {
        try {   
            if (actualValue.equalsIgnoreCase(expectedValue)) {              
                Log.info("Value Validation Match Successful! Actual matches Target content: [" + expectedValue + "]");
            } else {
                Log.error("Value Validation Mismatch! Expected Target content: [" + expectedValue + "] but verified value was: [" + actualValue + "]");
                Assert.assertEquals(actualValue, expectedValue);
            }
        } catch(Exception e) {                  
            Log.error("Value comparison processing error while validating string text objects: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }
}
