package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.testng.Reporter;
import org.testng.annotations.*;
import util.listeners.TestListener;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

public class BaseTest{
    public static WebDriver driver;
    private static final ThreadLocal <WebDriver> threadDriver = new ThreadLocal<>();
    public static WebDriver getDriver() {
        return threadDriver.get();
    }
    public static void navigateTo(String baseURL) {
        getDriver().get(baseURL);
    }

    public static void setupBrowser(String baseURL) throws MalformedURLException {
        threadDriver.set(pickBrowser(System.getProperty("browser")));
        Reporter.log("browser is: " + System.getProperty("browser"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        getDriver().manage().deleteAllCookies();
        navigateTo(baseURL);
    }
    public static  WebDriver pickBrowser(String browser) throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        String gridURL = "http://192.168.0.224:4444";
        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions optionsFirefox = new FirefoxOptions();
                optionsFirefox.addArguments("-private");
                return driver = new FirefoxDriver(optionsFirefox);
            //gradle clean test -Dbrowser=MicrosoftEdge
            case "MicrosoftEdge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
//                edgeOptions.addArguments(new String[]{"--remote-allow-origins=*", "--disable-notifications", "--start-maximized"});
                return driver = new EdgeDriver();
            //gradle clean test -Dbrowser=grid-edge
            case "grid-edge":
                caps.setCapability("browserName", "MicrosoftEdge");
                return driver = new RemoteWebDriver(URI.create(gridURL).toURL(), caps);
            //gradle clean test -Dbrowser=grid-firefox
            case "grid-firefox":
                caps.setCapability("browserName", "firefox");
                return driver = new RemoteWebDriver(URI.create(gridURL).toURL(), caps);
            //gradle clean test -Dbrowser=grid-chrome
            case "grid-chrome":
                ChromeOptions options1 = new ChromeOptions();
                options1.addArguments("--remote-allow-origins=*", "--disable-notifications", "--start-maximized", "--incognito");
                caps.setCapability("browserName", "chrome");
                caps.setCapability(ChromeOptions.CAPABILITY, options1);
                return driver = new RemoteWebDriver(URI.create(gridURL).toURL(), caps);
            case "cloud":
                return lambdaTest();
            default:
                return setupDefaultBrowser();
        }
    }
    public static WebDriver lambdaTest() throws MalformedURLException {
        String username = System.getProperty("lambdaTestUser");
        String authKey = System.getProperty("lambdaTestKey");
        String hub = "@hub.lambdatest.com/wd/hub";
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platform", "windows 10");
        caps.setCapability("browserName", "Chrome");
        caps.setCapability("version", "120.0");
        caps.setCapability("resolution", "1024X768");
        caps.setCapability("build", "TestNG with Java");
        caps.setCapability("name", BaseTest.class.getName());
        caps.setCapability("plugin", "java-testNG");
        return new RemoteWebDriver(new URL("https://" + username + ":" + authKey + hub), caps);
    }
    private static WebDriver setupDefaultBrowser() {
        WebDriverManager.chromedriver().setup();
        ChromeDriverService service = new ChromeDriverService.Builder().usingAnyFreePort().build();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*", "--disable-notifications", "--start-maximized", "--incognito");
        options.setExperimentalOption("prefs", setDownloadDir());
        TestListener eventListener = new TestListener();
        driver = new ChromeDriver(service, options);
        Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
        System.setProperty("browserName", cap.getBrowserName());
        System.setProperty("browserVersion", cap.getBrowserVersion());
        EventFiringDecorator<WebDriver> decorator = new EventFiringDecorator<>(eventListener);
        return decorator.decorate(driver);
    }
    public static HashMap<String, Object> setDownloadDir() {
        HashMap<String, Object> chromePref = new HashMap<>();
        chromePref.put("profile.default_content_settings.popups", 0);
        chromePref.put("download.default_directory", System.getProperty("java.io.tmpdir"));
        return chromePref;
    }
    public static void loadEnv() {
        Dotenv dotenv = Dotenv.configure().directory("./src/test/resources").load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
    }
    public static void closeBrowser() {
        if (getDriver() == null) {
            threadDriver.get().close();
            threadDriver.remove();
        }
        threadDriver.get().quit();
    }
}


