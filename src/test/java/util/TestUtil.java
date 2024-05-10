package util;

import base.BaseTest;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

public class TestUtil {
    public static long sysTime = System.currentTimeMillis();;
    public static void takeScreenshotAtEndOfTest(String fileName, WebDriver driver) throws IOException {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String currentDir = System.getProperty("user.dir");
        FileUtils.copyFile(scrFile, new File(currentDir +"/reports/extent-reports/screenshots/" + fileName + ".png"));
    }
    public String getScreenShotPath(String testCaseName, WebDriver driver) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destinationFile = "/reports/extent-reports/screenshots/" + testCaseName + ".png";
        FileUtils.copyFile(source, new File(destinationFile));
        return destinationFile;
    }
    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }
    public static  String getDate() {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        df.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        return df.format(new Date());
    }
    public static String convertSecondToHHMMSSString(int nSecondTime) {
        return LocalTime.MIN.plusSeconds(nSecondTime).toString();
    }
    //The download directory is reset and used to verify when songs download to it. If the file extension contains ".mp3" then it completed downloading.  If it contains ".crdownload", then it is in the process of downloading
    public static boolean checkDownloadedFiles() {
        File rootFolder = new File(System.getProperty("user.dir") + "/downloads");
        File[] downloadedFiles = rootFolder.listFiles(file ->
                file.getName().contains(".mp3") || file.getName().contains(".com.google.Chrome")
        );
        return downloadedFiles != null;
    }
    public static void deleteFiles() {
        File rootFolder = new File(System.getProperty("user.dir") + "/downloads");
        Optional<File[]> deletedFiles = Optional.ofNullable(rootFolder.listFiles());
        if (deletedFiles.isPresent()) {
            for(File f : deletedFiles.get()) {
                Reporter.log("Deleting downloaded file: " + f.getName(), true);
                f.delete();
            }
        } else {
            Reporter.log("There were no downloaded files present to delete", true);
        }
    }

}