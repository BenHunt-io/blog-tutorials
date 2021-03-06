package de.rieckpil.blog;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.junit5.ScreenShooterExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.codeborne.selenide.Selenide.*;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookStoreYouTubeWT {

  @LocalServerPort
  private Integer port;

  @Container
  public static BrowserWebDriverContainer<?> webDriverContainer =
    new BrowserWebDriverContainer<>()
      .withCapabilities(new ChromeOptions()
        .addArguments("--no-sandbox")
        .addArguments("--disable-dev-shm-usage"));

  @RegisterExtension
  public static ScreenShooterExtension screenShooterExtension =
    new ScreenShooterExtension().to("target/selenide");

  @Test
  public void shouldDisplayBook() {

    Configuration.timeout = 2000;
    Configuration.baseUrl = "http://172.17.0.1:" + port;

    RemoteWebDriver remoteWebDriver = webDriverContainer.getWebDriver();
    WebDriverRunner.setWebDriver(remoteWebDriver);

    open("/book-store");

    $(By.id("all-books")).shouldNot(Condition.exist);

    screenshot("pre-book-fetch");

    $(By.id("fetch-books")).click();

    screenshot("post-book-fetch");

    $(By.id("all-books")).shouldBe(Condition.visible);
  }
}
