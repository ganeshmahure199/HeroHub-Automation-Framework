package OrangeHRM.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import OrangeHRM.Utility.Library;
import OrangeHRM.Utility.PageClassObject;

public class loginPage extends PageClassObject {
  
    public loginPage(WebDriver driver) {       
        PageFactory.initElements(driver, this);
    }     
    
    @FindBy(name = "username") private WebElement username;    
    @FindBy(name = "password") private WebElement password;    
    @FindBy(xpath = "//button[@type='submit']") private WebElement loginBtn;
    
   
    public void loginDetail(String user, String pass) {          
        Library.Custom_SendKeys(driver, username, user, "Username Text Field");
        Library.Custom_SendKeys(driver, password, pass, "Password Text Field");
        Library.Custom_Click(driver, loginBtn, "Login Submit Button");
    }
}
