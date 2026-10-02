package com.typicode.apitests;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.not;

import org.testng.annotations.Test;

import com.typicode.AbstractBaseTest;
import com.typicode.api.APIUser;
import com.typicode.api.StatusCode;

/**
 * Test Class to validate {@link APIUser}.
 * Not part of smoke-testng.xml; run it from the IDE or add it to a suite.
 *
 */
public class APITest extends AbstractBaseTest {

	/**
	 * Fetches all users and validates response code & body as not empty or null
	 */
	@Test
	public void fetchUsers() {
		api()
			.users()
			.fetchAllUsers()
			.printResponse()
			.verify().responseCode(StatusCode.OK, "Fetching User Details")
			.verify().responseBodySafely("id", everyItem(not(emptyOrNullString())), "User ID should not be null or empty");
		throwAssertionOnFailure();
	}
}
