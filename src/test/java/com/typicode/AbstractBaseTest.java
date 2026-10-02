package com.typicode;

import com.typicode.api.APIUser;

/**
 * Base class for all the tests. This class contains all the common functions required to all tests
 *
 */
public abstract class AbstractBaseTest {

	// One APIUser per test class instance, created on first use
	private APIUser apiUser;

	/**
	 * Initializes {@link APIUser} class with the baseUrl from application-data.properties
	 * @return : {@link APIUser}
	 */
	protected APIUser api() {
		if(apiUser == null) {
			apiUser = new APIUser(Util.readApplicationData("baseUrl"));
		}
		apiUser.initAssertUtil();

		return apiUser;
	}

	/**
	 * Throws assertion error if there's any assertion failure,
	 * then resets the soft assertions for the next test. Call it at the end of every test.
	 */
	public void throwAssertionOnFailure() {
		try {
			apiUser.getAssertUtil().throwAssertionOnFailure();
		} finally {
			apiUser.getAssertUtil().initSoftAssert();
		}
	}
}
