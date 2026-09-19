package OrangeHRM.ExcelDataProvider;

import OrangeHRM.Utility.Library;

public class excelTestData {
    
    
    public String username1;
    public String password1;
    
    
    public excelTestData(int sheetNumber, int rowNumber) {
    
        this.username1 = Library.getExcelData(sheetNumber, rowNumber, 1); 
        this.password1 = Library.getExcelData(sheetNumber, rowNumber, 2); 
    }
}
