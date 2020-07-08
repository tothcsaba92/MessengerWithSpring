package edu.progmatic.messenger.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
        System.setProperty("webdriver.chrome.driver","C:\\Users\\Csaba\\Downloads\\chromedriver_win32\\chromedriver.exe");
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
        driver.findElement(By.id("usernameInput")).click();
        driver.findElement(By.id("usernameInput")).sendKeys("Botos");
        driver.findElement(By.id("passwordInput")).sendKeys("Botos123!");
        driver.findElement(By.id("loginButton")).click();
        driver.findElement(By.linkText("Üzenetek")).click();
        driver.findElement(By.linkText("Üzenet írás")).click();
        driver.findElement(By.id("exampleFormControlTextarea1")).click();
        driver.findElement(By.id("exampleFormControlTextarea1")).sendKeys("Automata tesztek");
        driver.findElement(By.cssSelector(".btn:nth-child(1)")).click();
        driver.findElement(By.cssSelector(".btn:nth-child(2)")).click();
    }

    @Test
    public void deleteMessage() {
        driver.findElement(By.id("usernameInput")).click();
        driver.findElement(By.id("usernameInput")).sendKeys("Botos");
        driver.findElement(By.id("passwordInput")).sendKeys("Botos123!");
        driver.findElement(By.id("loginButton")).click();
        driver.findElement(By.linkText("Üzenetek")).click();
        driver.findElement(By.linkText("Üzenetek olvasás")).click();
        driver.findElement(By.cssSelector(".row100:nth-child(1) .btn")).click();
        driver.findElement(By.cssSelector(".btn:nth-child(2)")).click();
        driver.findElement(By.cssSelector(".container > .d-flex")).click();
    }
}
