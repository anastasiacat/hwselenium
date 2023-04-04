import io.github.bonigarcia.wdm.WebDriverManager;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FormTest {
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
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999/");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldTestDoubleSurname() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Имя Фамилия-Фамилия");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991112233");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.xpath("//p[@data-test-id=\"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldTestNameAndSurname() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Имя Фамилия");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991112233");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.xpath("//p[@data-test-id=\"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldTestInvalidNameAndSurname() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Name Surname");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991112233");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals(expected, actual);
    }

    @Test
    void shouldTestEmptyNameAndSurname() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991112233");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals(expected, actual);
    }

    @Test
    void shouldTestInvalidPhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Имя Фамилия");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("79991112233");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals(expected, actual);
    }

    @Test
    void shouldTestEmptyPhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Имя Фамилия");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals(expected, actual);
    }

    @Test
    void shouldTestInvalidPhoneCountOfNumbersLessThanNeeded() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Имя Фамилия");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7999111223");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals(expected, actual);
    }

    @Test
    void shouldTestInvalidPhoneCountOfNumbersMoreThanNeeded() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Имя Фамилия");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+799911122333");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals(expected, actual);
    }

    @Test
    void shouldTestUncheckedCheckbox() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Имя Фамилия");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991112233");
        driver.findElement(By.tagName("button")).click();

        String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        String actual = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid .checkbox__text")).getText();
        assertEquals(expected, actual);
    }
}