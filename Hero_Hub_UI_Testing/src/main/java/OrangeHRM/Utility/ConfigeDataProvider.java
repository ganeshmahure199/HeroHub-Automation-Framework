package OrangeHRM.Utility;

/**
 * @author Ganesh.Mahure
 */
public class ConfigeDataProvider extends PageClassObject {

    public static String getOrangeHrmUrl() {
        return Library.getStringConfigData("orngHRMURL");
    }
    
    public static String getBrowserName() {
        String browser = Library.getStringConfigData("browser");
        return (browser != null) ? browser.trim().toLowerCase() : "chrome"; 
    }

    
}
