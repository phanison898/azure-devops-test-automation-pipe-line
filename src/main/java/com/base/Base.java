package com.base;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class Base {

	public static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();

	private final String URL = "https://www.google.com/";
	private final String HOME = System.getProperty("user.dir");
	private final String SCREENSHOTS_PATH = HOME + "/x-output/report/screenshots/";
	private final String REPORT_PATH = HOME + "/x-output/report/report.html";
	public ExtentReports extent = null;
	public ExtentTest test = null;

	public WebDriver getDriver() {
		return driver.get();
	}

	public String screenshot() {
		TakesScreenshot ss = (TakesScreenshot) getDriver();
		File src = ss.getScreenshotAs(OutputType.FILE);
		String destPath = SCREENSHOTS_PATH + System.currentTimeMillis() + ".png";
		File dest = new File(destPath);
		try {
			FileUtils.copyFile(src, dest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return destPath;
	}

	@BeforeSuite
	public void beforeSuite() {

		File file = new File(SCREENSHOTS_PATH);

		if (file.exists()) {
			System.out.println("Cleaning the directory '" + file.getName() + "' ...");
			try {
				FileUtils.cleanDirectory(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("No directory found with the name '" + file.getName() + "'");
		}

		extent = new ExtentReports();
		ExtentSparkReporter html = new ExtentSparkReporter(REPORT_PATH);
		html.config().setDocumentTitle("Test Report");
		html.config().setReportName("Test Report");
		extent.attachReporter(html);
	}

	@BeforeMethod
	public void initialization() {

		test = extent.createTest("sampleTest");

		driver.set(new ChromeDriver());
		getDriver().manage().window().maximize();
		getDriver().manage().deleteAllCookies();
		getDriver().get(URL);
		test.log(Status.INFO, "Launched URL = " + URL);
	}

	@Test
	public void sampleTest() {
		WebElement input = getDriver().findElement(By.name("q"));
		input.clear();
		input.sendKeys("time");
		test.info("Entered \'Date\' into search box",
				MediaEntityBuilder.createScreenCaptureFromPath(screenshot()).build());
		input.submit();
		test.log(Status.PASS, MediaEntityBuilder.createScreenCaptureFromPath(screenshot()).build());
	}

	@AfterMethod
	public void closeBrowser() {
		getDriver().close();
		getDriver().quit();
	}

	@AfterSuite
	public void afterSuite() {
		extent.flush();
	}

}
