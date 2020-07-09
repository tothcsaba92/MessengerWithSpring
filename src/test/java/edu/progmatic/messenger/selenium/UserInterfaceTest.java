package edu.progmatic.messenger.selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserInterfaceTest {
    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Csaba\\Downloads\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<>();
        driver.get("http://localhost:8080/login");
        driver.manage().window().setSize(new Dimension(1600, 860));
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void loginAndCreateNewMessage() {
        String messageText = "barack";
        driver.findElement(By.id("usernameInput")).click();
        driver.findElement(By.id("usernameInput")).sendKeys("Botos");
        driver.findElement(By.id("passwordInput")).sendKeys("Botos123!");
        driver.findElement(By.id("loginButton")).click();
        driver.findElement(By.linkText("Üzenetek")).click();
        driver.findElement(By.linkText("Üzenet írás")).click();
        driver.findElement(By.id("exampleFormControlTextarea1")).click();
        driver.findElement(By.id("exampleFormControlTextarea1")).sendKeys(messageText);
        driver.findElement(By.cssSelector(".btn:nth-child(1)")).click();
        Assertions.assertTrue(driver.getPageSource().contains(messageText));
        driver.findElement(By.cssSelector(".btn:nth-child(2)")).click();


    }

    @Test
    public void setForDeletion() {
        String messageText = "spagetti";
        driver.findElement(By.id("usernameInput")).click();
        driver.findElement(By.id("usernameInput")).sendKeys("Botos");
        driver.findElement(By.id("passwordInput")).sendKeys("Botos123!");
        driver.findElement(By.id("loginButton")).click();
        driver.findElement(By.linkText("Üzenetek")).click();
        driver.findElement(By.linkText("Üzenetek olvasás")).click();
        driver.findElement(By.linkText("Üzenetek")).click();
        driver.findElement(By.linkText("Üzenet írás")).click();
        driver.findElement(By.id("exampleFormControlTextarea1")).click();
        driver.findElement(By.id("exampleFormControlTextarea1")).sendKeys(messageText);
        driver.findElement(By.cssSelector(".btn:nth-child(1)")).click();
        driver.findElement(By.id(messageText+"Delete")).click();
        System.out.println("olvass");
        System.out.println(driver.findElement(By.id(messageText + "Status")).getText());
        Assert.assertTrue("Törölt".equals(driver.findElement(By.id(messageText+"Status")).getText()));

    }

    @Test
    public void newMessageExists() {
        String messageText = "Orsos";
        driver.findElement(By.id("usernameInput")).click();
        driver.findElement(By.id("usernameInput")).sendKeys("Botos");
        driver.findElement(By.id("passwordInput")).sendKeys("Botos123!");
        driver.findElement(By.id("loginButton")).click();
        driver.findElement(By.linkText("Üzenetek")).click();
        driver.findElement(By.linkText("Üzenet írás")).click();
        driver.findElement(By.id("exampleFormControlTextarea1")).click();
        driver.findElement(By.id("exampleFormControlTextarea1")).sendKeys(messageText);
        driver.findElement(By.cssSelector(".btn:nth-child(1)")).click();
        Assert.assertTrue("Text not found!", driver.getPageSource().contains(messageText));
    }

    @Test
    public void createNewUserAndPromoteToAdmin() throws InterruptedException {
        String username = "Salata";
        driver.findElement(By.linkText("Regisztráció")).click();
        driver.findElement(By.id("username")).click();
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys("Lapat123!");
        driver.findElement(By.id("passwordConfirm")).sendKeys("Lapat123!");
        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).sendKeys(username+"@gmail.com");
        driver.findElement(By.id("birthday")).sendKeys("1992/10/22");
        driver.findElement(By.cssSelector("input:nth-child(7)")).click();
        Thread.sleep(3000);
        driver.findElement(By.id("usernameInput")).click();
        Thread.sleep(3000);
        driver.findElement(By.id("usernameInput")).sendKeys("Botos");
        driver.findElement(By.id("passwordInput")).sendKeys("Botos123!");
        driver.findElement(By.id("loginButton")).click();
        Thread.sleep(3000);
        driver.findElement(By.linkText("Üzenetek")).click();
        Thread.sleep(3000);
        driver.findElement(By.cssSelector(".nav-item:nth-child(3) > .nav-link")).click();
        Thread.sleep(3000);
        driver.findElement(By.id(username+"x")).click();
        Assert.assertFalse("Text is still there!", driver.getPageSource().contains(username));

    }
}
