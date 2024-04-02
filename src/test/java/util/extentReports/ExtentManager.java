package util.extentReports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentReporter;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.Platform;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExtentManager {
    private static ExtentReports extent;
    private static ExtentReports failedExtent;
    private static ExtentReports passedExtent;
    private static Platform platform;
    private static Map<String, String> fileMap = Map.of(
            "all", "Koel-Test-Automation-Extent-Report.html",
            "passed","Koel-Test-Automation-Extent-Report-Passed.html",
            "failed", "Koel-Test-Automation-Extent-Report-Failed.html"

    );
    private static String reportFileName = "Koel-Test-Automation-Extent-Report.html";
    private static String macPath = System.getProperty("user.dir") + "/reports/extent-reports";
    private static String windowsPath = System.getProperty("user.dir") + "\\reports\\extent-reports";
    private static List<String> macLocs = fileMap.keySet()
            .stream()
            .map(s -> macPath + "/" + fileMap.get(s))
            .toList();
    private static List<String> windowsLocs = fileMap.keySet()
            .stream()
            .map(s -> windowsPath + "\\" + fileMap.get(s))
            .toList();

    private static String macReportFileLoc = macPath + "/" + reportFileName;
    private static String winReportFileLoc = windowsPath + "\\" + reportFileName;
    public static ExtentReports getInstance(String status) {
        switch(status) {
            case "all" :
                if (extent == null)
                    createInstance();
                return extent;
            case "failed":
                if (failedExtent == null)
                    createFailed();
                return failedExtent;
            case "passed":
                if (passedExtent == null)
                    createPassed();
                return passedExtent;
            default:
                return extent;
        }
    }
    public static void createAll() {
        createInstance();
        createPassed();
        createFailed();
    }
    public static ExtentReports createInstance() {
        platform = getCurrentPlatform();
        String fileName = getReportFileLocations(platform, "all");
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter(fileName);
        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setDocumentTitle("Extent Report");
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName("Koel Automation Tests");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("Environment", "TEST");
        extent.setSystemInfo("Author", "Franz Fernando");
        return extent;
    }
    public static ExtentReports createPassed() {
        String passedFileName = getReportFileLocations(platform, "passed");
        ExtentSparkReporter passedHtmlReporter = new ExtentSparkReporter(passedFileName);
        passedHtmlReporter.filter().statusFilter().as(new Status[]{Status.PASS}).apply();
        passedHtmlReporter.config().setTheme(Theme.DARK);
        passedHtmlReporter.config().setDocumentTitle("Extent Report");
        passedHtmlReporter.config().setEncoding("utf-8");
        passedHtmlReporter.config().setReportName("Koel Automation Tests");
        passedExtent = new ExtentReports();
        passedExtent.attachReporter(passedHtmlReporter);
        passedExtent.setSystemInfo("Status Filter", "Passed");
        passedExtent.setSystemInfo("Author", "Franz Fernando");
        return passedExtent;
    }
    public static ExtentReports createFailed() {
        String failedFileName = getReportFileLocations(platform, "failed");
        ExtentSparkReporter failedHtmlReporter = new ExtentSparkReporter(failedFileName);
        failedHtmlReporter.filter().statusFilter().as(new Status[]{Status.FAIL, Status.SKIP}).apply();
        failedHtmlReporter.config().setTheme(Theme.DARK);
        failedHtmlReporter.config().setDocumentTitle("Extent Report");
        failedHtmlReporter.config().setEncoding("utf-8");
        failedHtmlReporter.config().setReportName("Koel Automation Tests");
        failedExtent = new ExtentReports();
        failedExtent.attachReporter(failedHtmlReporter);
        failedExtent.setSystemInfo("Status Filter", "Failed/Skipped");
        failedExtent.setSystemInfo("Author", "Franz Fernando");
        return failedExtent;
    }
    private static String getReportFileLocations (Platform platform, String status) {
        String reportFileLocation = null;
        switch (platform) {
            case MAC:
                reportFileLocation = macPath + "/" + fileMap.get(status);
                createReportPath(macPath);
                System.out.println("ExtentReport Path for MAC: " + macPath + "\n");
                break;
            case WINDOWS:
                reportFileLocation = windowsPath + "\\" + fileMap.get(status);
                createReportPath(windowsPath);
                System.out.println("ExtentReport Path for WINDOWS: " + windowsPath + "\n");
                break;
            default:
                System.out.println("ExtentReport path has not been set! There is a problem!\n");
                break;
        }
        return reportFileLocation;
    }
    //Select the extent report file location based on platform
//    private static String getReportFileLocation (Platform platform) {
//        String reportFileLocation = null;
//        switch (platform) {
//            case MAC:
//                reportFileLocation = macReportFileLoc;
//                createReportPath(macPath);
//                System.out.println("ExtentReport Path for MAC: " + macPath + "\n");
//                break;
//            case WINDOWS:
//                reportFileLocation = winReportFileLoc;
//                createReportPath(windowsPath);
//                System.out.println("ExtentReport Path for WINDOWS: " + windowsPath + "\n");
//                break;
//            default:
//                System.out.println("ExtentReport path has not been set! There is a problem!\n");
//                break;
//        }
//        return reportFileLocation;
//    }
    //Create the report path if it does not exist
    private static void createReportPath (String path) {
        File testDirectory = new File(path);
        if (!testDirectory.exists()) {
            if (testDirectory.mkdir()) {
                System.out.println("Directory: " + path + " is created!" );
            } else {
                System.out.println("Failed to create directory: " + path);
            }
        } else {
            System.out.println("Using current reports directory: " + path);
        }
    }
    //Get current platform
    private static Platform getCurrentPlatform () {
        if (platform == null) {
            String operSys = System.getProperty("os.name").toLowerCase();
            if (operSys.contains("win")) {
                platform = Platform.WINDOWS;
            } else if (operSys.contains("nix") || operSys.contains("nux")
                    || operSys.contains("aix")) {
                platform = Platform.LINUX;
            } else if (operSys.contains("mac")) {
                platform = Platform.MAC;
            }
        }
        return platform;
    }
}
