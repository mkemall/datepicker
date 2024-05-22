import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main {

    public static void main(String[] args) {
        // ChromeDriver yolunu belirtin
        String driverPath = "/path/to/chromedriver";  // ChromeDriver'ınızı indirip bu yola yerleştirin
        System.setProperty("webdriver.chrome.driver", driverPath);

        // WebDriver'ı başlat
        WebDriver driver = new ChromeDriver();

        // Vize randevu sistemi URL'sini girin
        String url = "https://ais.usvisa-info.com/en-tr/niv/users/sign_in/";  // Gerçek URL ile değiştirin
        driver.get(url);

        // Kullanıcı adı ve şifre
        String username = "dilarasahinn52@gmail.com";  // Kendi kullanıcı adınız ile değiştirin
        String password = "aralid5252";  // Kendi şifreniz ile değiştirin

        login(driver, username, password);

        while (true) {
            if (checkAndBookAppointment(driver)) {
                break;  // Randevu alındı, döngüden çık
            }
            try {
                Thread.sleep(3600000);  // 1 saat bekle ve tekrar kontrol et (1 saat = 3600000 milisaniye)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // WebDriver'ı kapat
        driver.quit();
    }

    public static void login(WebDriver driver, String username, String password) {
        try {
            // Kullanıcı adı ve şifre girme
            WebElement userField = new WebDriverWait(driver, 10).until(
                    ExpectedConditions.presenceOfElementLocated(By.id("user_email")));  // Gerçek ID ile değiştirin
            WebElement passField = driver.findElement(By.id("user_password"));  // Gerçek ID ile değiştirin

            userField.sendKeys(username);
            passField.sendKeys(password);

            // Giriş yap butonuna tıklama
            WebElement loginButton = driver.findElement(By.id("submit"));  // Gerçek ID ile değiştirin
            loginButton.click();
        } catch (Exception e) {
            System.out.println("Giriş sayfası yüklenemedi.");
            driver.quit();
        }
    }

    public static boolean checkAndBookAppointment(WebDriver driver) {
        try {
            // Randevu kontrol sayfasına gitme
            driver.get("https://ais.usvisa-info.com/en-tr/niv/groups/32525176");  // Gerçek URL ile değiştirin

            // Uygun randevu tarihlerini kontrol etme
            WebDriverWait wait = new WebDriverWait(driver, 10);
            WebElement date = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Randevu Al')]"))); // Gerçek XPath'i kullanarak değiştirin
            if (date != null && date.getText().contains("Randevu Al")) {
                // Randevuyu seç ve onayla
                date.click();
                WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("appointments_consulate_appointment_date"))); // Gerçek ID ile değiştirin
                confirmButton.click();

                System.out.println("Randevu başarıyla alındı: " + date.getText());
                return true;  // Randevu bulundu ve alındı, döngüden çık
            }
        } catch (Exception e) {
            System.out.println("Uygun bir randevu bulunamadı veya sayfa yüklenemedi.");
        }
        return false;  // Uygun randevu bulunamadı
    }
}
