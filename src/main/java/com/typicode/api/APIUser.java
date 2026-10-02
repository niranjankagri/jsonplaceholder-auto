package com.typicode.api;

import com.typicode.AssertUtil;
import com.typicode.api.app.UserApi;
import com.typicode.api.app.post.PostApi;
import com.typicode.api.app.post.comment.CommentApi;
import com.typicode.api.lib.IAPI;
import com.typicode.api.lib.IResponse;
import com.typicode.api.lib.restassuredimpl.APIImpl;

/**
 * Entry point for API operations, used fluently by the tests, e.g.
 * {@code api().users().fetchAllUsers().verify().responseCode(StatusCode.OK, "...")}.
 * It holds the current relative URL, the latest response and the assertion utility,
 * and gives access to the resource APIs ({@link #users()}, {@link #posts()}, {@link #comments()}).
 *
 */
public class APIUser {

    private APIUserChecker apiUserChecker;
    private CommentApi commentApi;
    private AssertUtil assertUtil;
    // Sends the HTTP requests (Rest-Assured implementation)
    private IAPI apiService;
    // Response of the latest request
    private IResponse response;
    private PostApi postApi;
    // Path used by the next request; set by the resource APIs before each call
    private String relativeUrl;
    private UserApi userApi;

    /**
     * Initialize Base URL of AUT
     * @param baseUrl : Base URL of the application, e.g. https://jsonplaceholder.typicode.com
     */
    public APIUser(String baseUrl) {
        this.apiService = new APIImpl(baseUrl);
        this.apiUserChecker = new APIUserChecker(this);
    }

    /**
     * Verifies the API result
     * @return {@link APIUserChecker} to verify API operations
     */
    public APIUserChecker verify() {
        return apiUserChecker;
    }

    /**
     * Response from end-point
     * @return {@link IResponse} of the latest request, or null if no request was sent yet
     */
    public IResponse getResponse() {
        return this.response;
    }

    /**
     * Sets the defined URL relative to Base URL
     * @param relativeUrl : Path to resource after Base URL
     * @return {@link APIUser}
     */
    public APIUser setRelativeUrl(String relativeUrl) {
    	this.relativeUrl = relativeUrl;
    	return this;
    }

    /**
     * @return : Relative Path to resource in Application
     * @throws IllegalStateException if no relative URL has been set
     */
    public String getRelativeUrl() {
    	if(this.relativeUrl != null) {
    		return this.relativeUrl;
    	}

    	throw new IllegalStateException("Relative URL is not set");
    }

    /**
     * Prints the response body of end-point to the log
     * @return : {@link APIUser}
     */
    public APIUser printResponse() {
    	this.response.print();
    	return this;
    }

    /**
     * GET response of end-point at the current relative URL; the result is kept for {@link #getResponse()}
     * @return : {@link APIUser}
     */
    public APIUser requestGet() {
        this.response = apiService.requestGet(getRelativeUrl());
        return this;
    }

    /**
     * Initializes assertion utility (only once; later calls keep the collected soft assertions)
     * @return : {@link APIUser}
     */
    public APIUser initAssertUtil() {
    	if(assertUtil == null) {
    		assertUtil = new AssertUtil();
    		assertUtil.initSoftAssert();
    	}

    	return this;
    }

    /**
     * Instance of {@link AssertUtil}
     * @return {@link AssertUtil}
     * @throws IllegalStateException if {@link #initAssertUtil()} was not called first
     */
    public AssertUtil getAssertUtil() {
    	if(assertUtil != null) {
    		return this.assertUtil;
    	}

    	throw new IllegalStateException("Assertion Utility is not initialized yet");
    }

    /**
     * Creates a new object of {@link UserApi} class if not created before
     * @return {@link UserApi}
     */
    public UserApi users() {
    	if(userApi == null) {
    		userApi = new UserApi(this);
    	}
    	return userApi;
    }

    /**
     * Creates a new object of {@link PostApi} class if not created before
     * @return {@link PostApi}
     */
    public PostApi posts() {
    	if(postApi == null) {
    		postApi = new PostApi(this);
    	}
    	return postApi;
    }

    /**
     * Creates a new object of {@link CommentApi} class if not created before
     * @return {@link CommentApi}
     */
    public CommentApi comments() {
    	if(commentApi == null) {
    		commentApi = new CommentApi(this);
    	}
    	return commentApi;
    }
}
