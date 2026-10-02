package com.typicode.api;

import org.hamcrest.Matcher;
import org.testng.Assert;

import com.typicode.Util;

/**
 * This class contains all the verifications functions related to API Response.
 * Every check works on the latest response of {@link APIUser} and returns it,
 * so checks can be chained after a request.
 *
 */
public class APIUserChecker {

    private APIUser apiUser;

    /**
     * Initializes {@link APIUser} class
     * @param apiUser : {@link APIUser}
     */
    public APIUserChecker(APIUser apiUser) {
        this.apiUser = apiUser;
    }

    /**
     * Validates the response code from end-point with the expected response.
     * Fails the test immediately on mismatch.
     * @param responseCode : Expected Response code
     * @param message : Message to be logged
     * @return : {@link APIUser}
     */
    public APIUser responseCode(int responseCode, String message) {
        Assert.assertEquals(apiUser.getResponse().getStatusCode(), responseCode, message + ". Unexpected response code");
        Util.getLogger().info("PASSED: " + message + ". Response code is " + responseCode);

        return this.apiUser;
    }

    /**
     * Validates response body as per defined path and throws exception immediately if occurs
     * @param path : location to response body
     * @param matcher : {@link Matcher} to compare
     * @param message : Message to be displayed
     * @return : {@link APIUser}
     */
    public <T> APIUser responseBody(String path, Matcher<T> matcher, String message) {
    	apiUser.getAssertUtil().verify(matcher.matches(apiUser.getResponse().getResposeBody(path)), true, message);
    	Util.getLogger().info("PASSED: " + message);

        return this.apiUser;
    }

    /**
     * Validates response body as per defined path and throws exception at the end of test
     * (when {@code throwAssertionOnFailure()} is called)
     * @param path : location to response body
     * @param matcher : {@link Matcher} to compare
     * @param message : Message to be displayed
     * @return : {@link APIUser}
     */
    public <T> APIUser responseBodySafely(String path, Matcher<T> matcher, String message) {
    	boolean isMatched = matcher.matches(apiUser.getResponse().getResposeBody(path));
    	apiUser.getAssertUtil().verifySafely(isMatched, true, message);

    	if(isMatched) {
    		Util.getLogger().info("PASSED: " + message);
    	} else {
    		Util.getLogger().info("FAILED: " + message);
    	}

        return this.apiUser;
    }
}
