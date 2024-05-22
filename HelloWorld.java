import time
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import NoSuchElementException, TimeoutException

# ChromeDriver yolunu belirtin
driver_path = '/path/to/chromedriver'  # ChromeDriver'ınızı indirip bu yola yerleştirin
driver = webdriver.Chrome(executable_path=driver_path)

# Vize randevu sistemi URL'sini girin
url = 'https://ais.usvisa-info.com/en-tr/niv/users/sign_in/'  # Gerçek URL ile değiştirin
driver.get(url)

def login(username, password):
    try:
        # Kullanıcı adı ve şifre girme
        user_field = WebDriverWait(driver, 10).until(
            EC.presence_of_element_located((By.ID, 'user_email',send_keys("dilarasahinn52@gmail.com")))  # Gerçek ID ile değiştirin
        )
        pass_field = driver.find_element(By.ID, 'user_password',send_keys("aralid5252"))  # Gerçek ID ile değiştirin
        
        user_field.send_keys(username)
        pass_field.send_keys(password)
        checkbox_button = driver.find_element(By.ID,'policy_confirmed'.check)
        # Giriş yap butonuna tıklama
        login_button = driver.find_element(By.ID,'submit')  # Gerçek ID ile değiştirin
        login_button.click()
    except TimeoutException:
        print("Giriş sayfası yüklenemedi.")
        driver.quit()

def check_and_book_appointment():
    try:
        # Randevu kontrol sayfasına gitme
        driver.get('https://ais.usvisa-info.com/en-tr/niv/groups/32525176')  # Gerçek URL ile değiştirin
        
        # Uygun randevu tarihlerini kontrol etme
        dates = WebDriverWait(driver, 10).until(
            EC.presence_of_all_elements_located((By.element_to_be_clickable,'https://ais.usvisa-info.com/en-tr/niv/schedule/57314210/continue_actions').click)  # Gerçek sınıf ismi ile değiştirin
        )
        for date in dates:
            if 'Reschedule Appointment' in date.text:  # 'Erken Tarih' yerine uygun kriteri yazın
                # Randevuyu seç ve onayla
                date.click()
                confirm_button = WebDriverWait(driver, 10).until(
                    EC.element_to_be_clickable((By.element_to_be_clickable, 'Reschedule Appointment'))  # Gerçek ID ile değiştirin
                )
                confirm_button = driver.find_element(By.ID,'appointments_consulate_appointment_date').click
                WebDriverWait(driver,3).until(
                    EC.presence_of_all_elements_located(By.element_to_be_clickable,'ui-datepicker-div',date)
                )
                print(f"Randevu başarıyla alındı: {date.text}")
                return True  # Randevu bulundu ve alındı, döngüden çık
    except (NoSuchElementException, TimeoutException):
        print("Uygun bir randevu bulunamadı veya sayfa yüklenemedi.")
    return False  # Uygun randevu bulunamadı

def main():
    username = 'dilarasahinn52@gmail.com'  # Kendi kullanıcı adınız ile değiştirin
    password = 'aralid5252'  # Kendi şifreniz ile değiştirin
    
    login(username, password)
    
    while True:
        if check_and_book_appointment():
            break  # Randevu alındı, döngüden çık
        time.sleep(3600)  # 1 saat bekle ve tekrar kontrol et

if __name__ == "__main__":
    main()
    driver.quit()
