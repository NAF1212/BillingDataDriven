package testCases;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import pages.AddBill;
import pages.ExcelUtils;
import pages.LoginPage;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

public class FastFrateTest {

    private WebDriver driver;
    private LoginPage loginPage;
    private AddBill addBillPage;

    private static final String URL = "https://atm.fastfrate.com/dev/login";
    //private static final String USERNAME = "raunak.jain@atomicnorth.com";
    //private static final String PASSWORD = "Abc8094@";
    private static final String USERNAME = "Jitendra.kumar@atomicnorth.com";
    private static final String PASSWORD = "Jeetay@123";
    private static final String SCREENSHOT_PATH = "C:\\Users\\MdNafisAhmad\\git\\repository8\\DataDrivenFramework\\target.Screenshot";
    private static final String EXCEL_PATH = "C:\\Users\\MdNafisAhmad\\git\\repository8\\DataDrivenFramework\\src\\test\\java\\utils\\AutomationTest2.xlsx";
    private static final String SHEET_NAME = "Sheet1"; // Update to your actual sheet name

    private ExtentReports extent;
    private ExtentTest test;

    @BeforeMethod
    public void setupExtentReports() {
        // Generate a timestamp for the report file name
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String reportPath = System.getProperty("user.dir") + "/reports/ExtentReport_" + timeStamp + ".html";

        // Configure the ExtentReports HTML reporter
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setDocumentTitle("Automation Test Report");
        sparkReporter.config().setReportName("Billing Test Execution");
        sparkReporter.config().setTheme(Theme.STANDARD);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // Add system information to the report
        extent.setSystemInfo("Host Name", "Localhost");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("User Name", "Jitendra Kumar");
    }

    @Test
    public void executeBillingTests() throws IOException, InterruptedException {
        // Read data from the Excel file
        List<Object[]> testData = ExcelUtils.readExcelData(EXCEL_PATH, SHEET_NAME);

        for (Object[] data : testData) {
            String tripDetails = (String) data[0];
            String billNumber = (String) data[1];
            String term = (String) data[2];
            String sequence = (String) data[3];
            String shipperId = (String) data[4];
            String consigneeId = (String) data[5];
            String thirdParty = (String) data[6];
            String checkboxName = (String) data[7];
            String quantity = (String) data[8];
            String pieces = (String) data[9];
            String cuft = (String) data[10];
            String description = (String) data[11];
            String weight = (String) data[12];

            try {
                // Reinitialize the browser before each test execution
                setupBrowser();

                // Create a test in the report
                test = extent.createTest("Billing Test for Bill Number: " + billNumber);

                // Perform billing operations
                loginPage.login(USERNAME, PASSWORD);
                addBillPage.navigateToBillings();
                addBillPage.selectBillingOption();
                addBillPage.enterTripDetails(tripDetails);
                addBillPage.enterBillNumber(billNumber);
                addBillPage.selectTerm(term);
                addBillPage.enterSequence(sequence);
                addBillPage.enterShipmentDetails(shipperId);
                addBillPage.enterConsigneeDetails(consigneeId);
                addBillPage.enterThirdPartyDetails(thirdParty);
                addBillPage.selectCheckbox(checkboxName);
                addBillPage.enterQuantity(quantity);
                addBillPage.enterDescription(description);
                addBillPage.enterPieces(pieces);
                addBillPage.enterCubicFeet(cuft);
                addBillPage.enterWeight(weight);
                addBillPage.calculateAndConfirm();
                addBillPage.clickPublished();
                addBillPage.testToastMessageAfterPublish();
                
                // Capture screenshot after test execution
                captureScreenshot(billNumber, "screenshot");

                // Log the test as passed
                test.log(Status.PASS, "Test passed");

            } catch (Exception e) {
                // Log the exception and capture an error screenshot
                test.log(Status.FAIL, "Test failed: " + e.getMessage());
                captureScreenshot(billNumber, "error_screenshot");
            } finally {
                // Close the browser after each test
                tearDownBrowser();
            }
        }
    }

    private void setupBrowser() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get(URL);
        loginPage = new LoginPage(driver);
        addBillPage = new AddBill(driver);
    }

    private void tearDownBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void captureScreenshot(String billNumber, String type) throws IOException {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		String screenshotFilePath = SCREENSHOT_PATH + type + "_" + billNumber + "_" + timestamp + ".png";
		Shutterbug.shootPage(driver, Capture.FULL, true).save(screenshotFilePath);
		test.addScreenCaptureFromPath(screenshotFilePath);
    }

    @AfterMethod()
    public void tearDownExtentReports() {
        if (extent != null) {
            extent.flush();
        }
    }
}