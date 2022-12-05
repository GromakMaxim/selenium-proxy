package org.example;


import com.browserup.harreader.model.Har;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.time.Duration;

public class AppTest extends BaseTest{
    @Test
    @SneakyThrows
    public void shouldAnswerWithTrue() {
        proxy.newHar("demoqa.com");
        driver.get("https://demoqa.com/text-box");

        driver.findElement(By.id("userName")).sendKeys("qwerty_name");
        driver.findElement(By.id("userEmail")).sendKeys("qwe@mail.ru");
        driver.findElement(By.id("currentAddress")).sendKeys("qa_qa_qa");
        driver.findElement(By.id("permanentAddress")).sendKeys("aaaaaaaaaaaaddddddddddddddreeeeeeeeeeeessssss");

        driver.findElement(By.id("submit")).click();
        new Actions(driver).pause(Duration.ofSeconds(2)).perform();

        Har har = proxy.getHar();
        File harFile = new File("demoqa.com");
        har.writeTo(harFile);
    }
}
