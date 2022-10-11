package ru.netology.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import io.github.bonigarcia.wdm.WebDriverManager;

public class CardOrderTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        if (System.getProperty("os.name").contains("Linux")) {
            System.setProperty("webdriver.chrome.driver", "driver/linux/chromedriver");
        } else {
            System.setProperty("webdriver.chrome.driver", "driver/win/chromedriver.exe");
        }
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
//        driver = new ChromeDriver();
    }

    @AfterEach
    void tearsDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldFillForm() throws InterruptedException {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий Пупкин");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+78883331313");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__text")).click();
//        Thread.sleep(8000);
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldShowErrorTextIfЁInInputName() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ёлка Иванов");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__text")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.className("input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldShowErrorTextIfIncorrectNumber() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Елка Иванов");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+7888333131313");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__text")).click();
        List<WebElement> inputs = driver.findElements(By.className("input__sub"));
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = inputs.get(1).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldShowErrorTextIfEmptyInputName() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__text")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.className("input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldShowErrorTextIfEmptyInputPhone() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Елка Иванов");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__text")).click();
        List<WebElement> inputs = driver.findElements(By.className("input__sub"));
        String expected = "Поле обязательно для заполнения";
        String actual = inputs.get(1).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldShowErrorTextIfNotClickCheckbox() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий Пупкин");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+78883331313");
        driver.findElement(By.className("button__text")).click();
        String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        String actual = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid")).getText();
        assertEquals(expected, actual);
    }
}
