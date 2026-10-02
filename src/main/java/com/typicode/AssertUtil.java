package com.typicode;

import org.testng.Assert;
import org.testng.asserts.SoftAssert;

/**
 * Assertion Util for validation of results.
 * Supports hard assertions (fail immediately) and soft assertions
 * (collected and reported together by {@link #throwAssertionOnFailure()}).
 */
public class AssertUtil {
	
	// Collects soft assertion failures until throwAssertionOnFailure() is called
	private SoftAssert softAssert;
	
	/**
	 * Initializes {@link SoftAssert}, discarding any failures collected so far
	 */
	public void initSoftAssert() {
		softAssert = new SoftAssert();
	}

	/**
	 * Verifies actual and expected result safely i.e. it will not throw assertion immediately on failure
	 * @param actual : Actual Result
	 * @param expected : Expected Result
	 * @param message : Message to be displayed
	 */
	public void verifySafely(Object actual, Object expected, String message) {
		softAssert.assertEquals(actual, expected, message);
	}
	
	/**
	 * Verifies all the safe assertions and throw exception if there's any failure
	 * @throws AssertionError listing every failed soft assertion
	 */
	public void throwAssertionOnFailure() {
		softAssert.assertAll();
	}
	
	/**
	 * Verifies actual and expected result, throws exception immediately if there's any failure
	 * @param actual : Actual Result
	 * @param expected : Expected Result
	 * @param message : Message to be displayed
	 */
	public void verify(Object actual, Object expected, String message) {
		Assert.assertEquals(actual, expected, message);
	}
}
