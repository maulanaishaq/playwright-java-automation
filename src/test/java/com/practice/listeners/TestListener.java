package com.practice.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.microsoft.playwright.Page;
import com.practice.utils.PlaywrightFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Listener kustom TestNG untuk mengatur logging, screenshot otomatis saat kegagalan,
 * dan pembuatan laporan tes berbasis HTML dengan ExtentReports secara thread-safe.
 */
public class TestListener implements ITestListener {
    private static final Logger log = LogManager.getLogger(TestListener.class);

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        log.info("Test Suite '{}' dimulai. Menyiapkan ExtentReports...", context.getName());

        // Membuat direktori laporan jika belum ada
        File reportDir = new File("reports");
        if (!reportDir.exists()) {
            reportDir.mkdirs();
        }

        // Inisialisasi ExtentSparkReporter
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("reports/ExtentReport.html");
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle("Automation Test Report");
        sparkReporter.config().setReportName("Practice Test Automation Results");
        sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Application", "Practice Test Automation");
        extent.setSystemInfo("Operating System", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("Browser", "Playwright Configured Browser");
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("Test Suite selesai. Menulis ExtentReports ke file...");
        if (extent != null) {
            extent.flush();
        }
        log.info("ExtentReports berhasil dibuat di: reports/ExtentReport.html");
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        log.info("=== Memulai Test Case: {} ===", testName);

        // Membuat entry test di ExtentReports secara thread-safe
        ExtentTest test = extent.createTest(testName, result.getMethod().getDescription());
        extentTest.set(test);
        extentTest.get().log(Status.INFO, "Test case '" + testName + "' dimulai.");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        log.info("=== Test Case Sukses: {} ===", testName);

        extentTest.get().log(Status.PASS, "Test case '" + testName + "' BERHASIL.");
        extentTest.remove(); // Bersihkan thread-local
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        log.error("=== Test Case GAGAL: {} ===", testName);
        log.error("Penyebab kegagalan: ", result.getThrowable());

        extentTest.get().log(Status.FAIL, "Test case '" + testName + "' GAGAL.");
        extentTest.get().fail(result.getThrowable());

        // Mengambil screenshot via Playwright
        Page page = PlaywrightFactory.getPage();
        if (page != null) {
            try {
                // Membuat nama file screenshot yang unik
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String screenshotName = testName + "_" + timestamp + ".png";
                
                // Path absolut untuk penyimpanan file, path relatif untuk referensi laporan HTML
                Path screenshotPath = Paths.get("reports", "screenshots", screenshotName);
                
                // Buat folder screenshots jika belum ada
                Files.createDirectories(screenshotPath.getParent());

                // Mengambil screenshot halaman penuh (full page)
                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(screenshotPath)
                        .setFullPage(true));

                log.info("Screenshot kegagalan disimpan di: {}", screenshotPath.toAbsolutePath());

                // Melampirkan screenshot ke Extent Report
                // Karena file ExtentReport.html berada di folder 'reports',
                // maka path relatif ke file screenshot adalah 'screenshots/nama_file.png'
                String relativePathToReport = "screenshots/" + screenshotName;
                extentTest.get().fail("Screenshot Kegagalan:",
                        MediaEntityBuilder.createScreenCaptureFromPath(relativePathToReport).build());

            } catch (IOException e) {
                log.error("Gagal membuat direktori untuk screenshot: ", e);
            } catch (Exception e) {
                log.error("Gagal mengambil screenshot: ", e);
            }
        } else {
            log.warn("Tidak dapat mengambil screenshot karena instansi Playwright Page bernilai null.");
        }
        extentTest.remove(); // Bersihkan thread-local
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        log.warn("=== Test Case DILEWATI (SKIPPED): {} ===", testName);

        extentTest.get().log(Status.SKIP, "Test case '" + testName + "' DILEWATI.");
        extentTest.remove(); // Bersihkan thread-local
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Tidak diimplementasikan
    }
}
