package OrangeHRM.Utility;

/**
 * @author Ganesh.Mahure
 */
public class ConfigeDataProvider extends PageClassObject {

    
    public static final String USER_DIRECTORY_PATH = System.getProperty("user.dir");        
    
    
    public static String getOrangeHrmUrl() {
        return Library.getStringConfigData("orngHRMURL");
    }
    
    public static String getAutomationExerciseUrl() {
    	return Library.getStringConfigData("automationExercise");
    }
    
	public static String naukri() {
		return Library.getStringConfigData("naukri");
	}
    
    public static String getBrowserName() {
        String browser = Library.getStringConfigData("browser");
        return (browser != null) ? browser.trim().toLowerCase() : "chrome"; 
    }

    
}
