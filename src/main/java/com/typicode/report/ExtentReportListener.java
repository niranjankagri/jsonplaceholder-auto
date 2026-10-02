package com.typicode.report;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.typicode.Util;

/**
 * TestNG listener that writes an Extent HTML report of the test run.
 * Every test becomes one entry in the report; its log messages are added as steps
 * by {@link ExtentLogAppender}. Registered on {@code AbstractBaseTest}, so it runs
 * for every test class, from Maven or from the IDE.
 *
 */
public class ExtentReportListener implements ITestListener {

	// Location of the report, relative to the working directory (the project root)
	public static final String REPORT_PATH = "target/extent-reports/ExtentReport.html";

	// Name of the ITestResult attribute that holds the test's report entry
	private static final String EXTENT_TEST = "extentTest";

	// One report for the whole run, created on first use
	private static ExtentReports extentReports;
	// Report entry of the test running on the current thread
	private static final ThreadLocal<ExtentTest> currentTest = new ThreadLocal<>();

	/**
	 * Creates the report (only once) and starts sending log messages to it
	 * @return : {@link ExtentReports}
	 */
	private static synchronized ExtentReports getExtentReports() {
		if(extentReports == null) {
			ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_PATH);
			sparkReporter.config().setDocumentTitle("jsonplaceholder-auto");
			sparkReporter.config().setReportName("JSONPlaceholder API Test Report");
			sparkReporter.config().setTheme(Theme.STANDARD);
			sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

			extentReports = new ExtentReports();
			extentReports.attachReporter(sparkReporter);
			extentReports.setSystemInfo("Base URL", Util.readApplicationData("baseUrl"));
			extentReports.setSystemInfo("Java", System.getProperty("java.version"));
			extentReports.setSystemInfo("OS", System.getProperty("os.name"));

			ExtentLogAppender.register();
		}

		return extentReports;
	}

	/**
	 * Report entry of the test running on the current thread
	 * @return : {@link ExtentTest}, or null if no test is running
	 */
	public static ExtentTest getCurrentTest() {
		return currentTest.get();
	}

	/**
	 * Creates the report entry for the test, named after the test method and grouped by test class
	 * @param result : {@link ITestResult} of the test
	 * @return : {@link ExtentTest}
	 */
	private ExtentTest createTest(ITestResult result) {
		String description = result.getMethod().getDescription();
		ExtentTest test = description != null
				? getExtentReports().createTest(result.getMethod().getMethodName(), description)
				: getExtentReports().createTest(result.getMethod().getMethodName());
		test.assignCategory(result.getTestClass().getRealClass().getSimpleName());
		result.setAttribute(EXTENT_TEST, test);

		return test;
	}

	/**
	 * Report entry of the test, created if the test was skipped before it started
	 * @param result : {@link ITestResult} of the test
	 * @return : {@link ExtentTest}
	 */
	private ExtentTest getTest(ITestResult result) {
		ExtentTest test = (ExtentTest)result.getAttribute(EXTENT_TEST);
		return test != null ? test : createTest(result);
	}

	@Override
	public void onTestStart(ITestResult result) {
		currentTest.set(createTest(result));
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		getTest(result).pass("Test passed");
		currentTest.remove();
	}

	@Override
	public void onTestFailure(ITestResult result) {
		// The throwable lists every failed soft assertion, or the hard assertion that stopped the test
		if(result.getThrowable() != null) {
			getTest(result).fail(result.getThrowable());
		} else {
			getTest(result).fail("Test failed");
		}
		currentTest.remove();
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		if(result.getThrowable() != null) {
			getTest(result).skip(result.getThrowable());
		} else {
			getTest(result).skip("Test skipped");
		}
		currentTest.remove();
	}

	/**
	 * Writes the report to {@link #REPORT_PATH} after each &lt;test&gt; of the suite
	 */
	@Override
	public void onFinish(ITestContext context) {
		getExtentReports().flush();
		Util.getLogger().info("Extent report: " + REPORT_PATH);
	}
}
