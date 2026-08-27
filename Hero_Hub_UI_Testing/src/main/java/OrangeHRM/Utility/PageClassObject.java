package OrangeHRM.Utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import OrangeHRM.ExcelDataProvider.excelTestData;
import OrangeHRM.pages.loginPage;



public class PageClassObject {

    public static final Logger LOGGER = LogManager.getLogger("Log");      
    
    // Global Shared Framework State Driver and Data Fields
    public static WebDriver driver; 
    public static excelTestData exceldata; 
    
    // OrangeHRM Page Model Registry Hooks
    public static loginPage login;
    
   
}
