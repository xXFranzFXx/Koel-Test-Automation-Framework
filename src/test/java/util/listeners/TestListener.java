package util.listeners;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.events.WebDriverListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import util.extentReports.ExtentManager;
import util.logs.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Set;


public class TestListener  implements ITestListener, WebDriverListener {
    static ExtentReports extent = ExtentManager.getInstance();
    static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    public static void logPassDetails(String log) {
        test.get().pass(MarkupHelper.createLabel(log, ExtentColor.GREEN));
    }
    public static void logRsDetails(String log) {
        test.get().info(MarkupHelper.createLabel(log, ExtentColor.WHITE));
    }
    public static void logAssertionDetails(String log) {
        test.get().info(MarkupHelper.createLabel(log, ExtentColor.PURPLE));
    }
    public  static void logFailureDetails(String log) {
        test.get().fail(MarkupHelper.createLabel(log, ExtentColor.RED));
    }
    public static void logExceptionDetails(String log) {
        test.get().fail(log);
    }
    public static void logInfoDetails(String log) {
        test.get().info(MarkupHelper.createLabel(log, ExtentColor.GREY));
    }

    @Override
    public synchronized void onStart(ITestContext context) {
       Log.info("Extent Reports for Koel Automation Test Suite started!");
    }
    @Override
    public synchronized void onFinish(ITestContext context) {
        Log.info("Extent Reports for Koel Automation Test Suite ending!");
        extent.flush();
    }
    @Override
    public synchronized void onTestStart(ITestResult result) {
        Log.info(result.getMethod().getMethodName() + " started!");
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName(),result.getMethod().getDescription());
        test.set(extentTest);
    }
    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        Log.info(result.getMethod().getMethodName() + " passed!");
        test.get().pass(MarkupHelper.createLabel("Test Passed", ExtentColor.GREEN));
    }
    @Override
    public synchronized void onTestFailure(ITestResult result) {
        Log.error(result.getMethod().getMethodName() + " failed!");
        test.get().log(Status.FAIL, "fail ❌").addScreenCaptureFromPath("/reports/extent-reports/screenshots/" + result.getMethod().getMethodName() + ".png");
        Log.info("screen shot taken for failed test " + result.getMethod().getMethodName());
        test.get().fail(result.getThrowable());
    }
    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        Log.warn(result.getMethod().getMethodName() + " skipped");
        test.get().skip(MarkupHelper.createLabel("Skipped", ExtentColor.AMBER));
        test.get().log(Status.SKIP,  result.getThrowable());
    }
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        Log.info("onTestFailedButWithinSuccessPercentage for " + result.getMethod().getMethodName());

    }
      @Override
    public void beforeAnyCall(Object target, Method method, Object[] args) {
        Log.debug( "Before calling method: " + method.getName());
    }

    public void afterAnyCall(Object target, Method method, Object[] args, Object result) {
        Log.debug("After calling method: " + method.getName());
    }

    public void onError(Object target, Method method, Object[] args, InvocationTargetException e) {
        Log.fatal( "Error while calling method: " + method.getName() + " - " + e.getMessage());
    }

    public void beforeAnyWebDriverCall(WebDriver driver, Method method, Object[] args) {
        Log.debug("Before calling WebDriver method: " + method.getName());
    }

    public void afterAnyWebDriverCall(WebDriver driver, Method method, Object[] args, Object result) {
        Log.debug("After calling WebDriver method: " + method.getName());
    }
}