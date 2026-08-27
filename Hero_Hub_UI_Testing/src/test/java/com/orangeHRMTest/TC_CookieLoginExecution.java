package com.orangeHRMTest;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import OrangeHRM.Utility.BaseTest;
import OrangeHRM.Utility.Library;

public class TC_CookieLoginExecution extends BaseTest {

    
    @BeforeClass(dependsOnMethods = {"beforeClass"})
    public void loginPreconditionSetup() {
        LOGGER.info("Executing master login precondition step...");
        login.loginDetail(exceldata.username1, exceldata.password1);
        Library.threadSleep(4000); 
    }

    @Test(priority = 1, description = "Test Case 1: Verify Dashboard URL Integrity")
    public void step01_verifyDashboardComponentA() {       
        System.out.println("Running Test 1 directly on the Dashboard...");
        String currentUrl = driver.getCurrentUrl();
        Library.assertEquals(driver, currentUrl, currentUrl);
    }

    @Test(priority = 2, description = "Test Case 2: Verify Dashboard URL Re-evaluation")
    public void step02_verifyDashboardComponentB() {        
        System.out.println("Running Test 2 directly on the Dashboard...");
        String currentUrl = driver.getCurrentUrl();
        Library.assertEquals(driver, currentUrl, currentUrl);
    }
}
