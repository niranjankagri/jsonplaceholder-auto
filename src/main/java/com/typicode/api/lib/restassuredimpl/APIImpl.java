package com.typicode.api.lib.restassuredimpl;

import com.typicode.api.lib.IAPI;
import com.typicode.api.lib.IResponse;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

/**
 * 
 * Implements {@link IAPI} via Rest-Assured
 *
 */
public class APIImpl implements IAPI {
	
	// Request specification of the latest request
	private RequestSpecification httpRequest;
    
	/**
	 * Sets the Base URL used by every request.
	 * Note: RestAssured.baseURI is global, so it applies to the whole JVM.
	 * @param baseURL : Base URL of AUT, e.g. https://jsonplaceholder.typicode.com
	 */
    public APIImpl(String baseURL) {
        RestAssured.baseURI = baseURL;
    }
    
    @Override
    public IResponse requestGet(String relativeURL) {
    	httpRequest = RestAssured.given();
        return new ResponseImpl(httpRequest.get(relativeURL));
    }
}
