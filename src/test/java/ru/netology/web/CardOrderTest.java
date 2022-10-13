package ru.netology.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import static org.junit.jupiter.api.Assertions.*;
import io.github.bonigarcia.wdm.WebDriverManager;

public class CardOrderTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearsDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldFillForm() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий Пупкин");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+78883331313");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__text")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldFillFormIfЁInInputName() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ёлка Иванов");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+78883331313");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__text")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldShowErrorTextIfInvalidInputName(){
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Boris Britva");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+78883331313");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__text")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldShowErrorTextIfIncorrectNumber() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Елка Иванов");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+7888333131313");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__text")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldShowErrorTextIfEmptyInputName() {
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+7888333131313");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__text")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldShowErrorTextIfEmptyInputPhone() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Елка Иванов");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__text")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldShowErrorTextIfNotClickCheckbox() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий Пупкин");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+78883331313");
        driver.findElement(By.className("button__text")).click();
        String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        String actual = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid")).getText();
        assertEquals(expected, actual);
    }
}
