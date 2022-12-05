package org.example;

import com.browserup.bup.BrowserUpProxyServer;
import com.browserup.bup.client.ClientUtil;
import com.browserup.bup.proxy.CaptureType;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.Inet4Address;
import java.time.Duration;

public class BaseTest {
    public WebDriver driver;
    public BrowserUpProxyServer proxy;
    public Proxy seleniumProxy;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        proxy = new BrowserUpProxyServer();
        proxy.start();

        seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

        DesiredCapabilities seleniumCapabilities = new DesiredCapabilities();
        seleniumCapabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

        ChromeOptions options = new ChromeOptions();
        options.addArguments(                             "--window-size=1920,1080");
        options.merge(seleniumCapabilities);

        WebDriverManager.chromedriver()
                .capabilities(seleniumCapabilities)
                .setup();

        this.driver = new ChromeDriver(options);
        this.driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(40));

    }

    @AfterEach
    void tearDown() {
        proxy.stop();
        if (driver != null){
            driver.quit();
        }
    }
}
