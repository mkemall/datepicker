import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main {

    public static void main(String[] args) {
    
        String driverPath = "/path/to/chromedriver";
        System.setProperty("webdriver.chrome.driver", driverPath);
        
        WebDriver driver = new ChromeDriver();

        String url = "https://ais.usvisa-info.com/en-tr/niv/users/sign_in/"; 
        driver.get(url);

        String username = "";  
        String password = ""; 

        login(driver, username, password);

        while (true) {
            if (checkAndBookAppointment(driver)) {
                break;  
            }
            try {
                Thread.sleep(3600000);  
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        driver.quit();
    }

    public static void login(WebDriver driver, String username, String password) {
        try {
        
            WebElement userField = new WebDriverWait(driver, 10).until(
                    ExpectedConditions.presenceOfElementLocated(By.id("user_email")));  
            WebElement passField = driver.findElement(By.id("user_password"));  

            userField.sendKeys(username);
            passField.sendKeys(password);

            
            WebElement loginButton = driver.findElement(By.id("submit"));  
            loginButton.click();
        } catch (Exception e) {
            System.out.println("Giriş sayfası yüklenemedi.");
            driver.quit();
        }
    }

    public static boolean checkAndBookAppointment(WebDriver driver) {
        try {
            
            driver.get("https://ais.usvisa-info.com/en-tr/niv/groups/32525176");  // Gerçek URL ile değiştirin

            
            WebDriverWait wait = new WebDriverWait(driver, 10);
            WebElement date = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Randevu Al')]"))); // Gerçek XPath'i kullanarak değiştirin
            if (date != null && date.getText().contains("Randevu Al")) {
                
                date.click();
                WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("appointments_consulate_appointment_date"))); // Gerçek ID ile değiştirin
                confirmButton.click();

                System.out.println("Randevu başarıyla alındı: " + date.getText());
                return true;  
            }
        } catch (Exception e) {
            System.out.println("Uygun bir randevu bulunamadı veya sayfa yüklenemedi.");
        }
        return false;  
    }
}
