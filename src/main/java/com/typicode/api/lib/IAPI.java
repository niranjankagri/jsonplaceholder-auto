package com.typicode.api.lib;

/**
 * Defines API's method to implement.
 * Keeps the framework independent of the HTTP library; see
 * {@link com.typicode.api.lib.restassuredimpl.APIImpl} for the REST Assured implementation.
 *
 */
public interface IAPI {

	/**
	 * GET request to specified end-point
	 * @param relativeURL : location of end-point, relative to the base URL (may include a query string)
	 * @return : Response from end-point
	 */
    public IResponse requestGet(String relativeURL);
}
