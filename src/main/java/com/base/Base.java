package com.base;

import org.openqa.selenium.By;
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
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class Base {

	public static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();

	private final String URL = "https://www.google.com/";
	public ExtentReports extent = null;
	public ExtentTest test = null;

	public WebDriver getDriver() {
		return driver.get();
	}

	@BeforeSuite
	public void beforeSuite() {
		extent = new ExtentReports();
		ExtentSparkReporter html = new ExtentSparkReporter("index.html");
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
		test.log(Status.INFO, "Entered \'Date\' into search box");
		input.submit();
		test.log(Status.PASS, "Displayed results");
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
