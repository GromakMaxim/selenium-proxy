package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v107.network.Network;
import org.openqa.selenium.devtools.v107.network.model.Request;
import org.openqa.selenium.devtools.v107.network.model.Response;

import java.time.Duration;
import java.util.Optional;

public class BaseTest {
    public ChromeDriver driver;
    private DevTools devTools;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();

        this.driver = new ChromeDriver();
        this.driver.manage().window().setSize(new Dimension(1920, 1080));
        this.driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        this.devTools = driver.getDevTools();
        devTools.createSession();

        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.requestWillBeSent(), requestWillBeSent -> {
            Request request = requestWillBeSent.getRequest();
            System.out.println("url: " + request.getUrl());
            System.out.println("method: " + request.getMethod());
        });

        devTools.addListener(Network.responseReceived(), responseReceived -> {
            Response response = responseReceived.getResponse();
            System.out.println("url:" + response.getUrl());
            System.out.println("status: " + response.getStatus());
            System.out.println(response.toString());
        });
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
