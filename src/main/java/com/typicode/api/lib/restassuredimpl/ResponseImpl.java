package com.typicode.api.lib.restassuredimpl;

import java.util.List;

import com.typicode.Util;
import com.typicode.api.lib.IResponse;

import io.restassured.response.Response;

/**
 * Implements {@link IResponse} for API implementation using Rest-Assured 
 *
 */
public class ResponseImpl implements IResponse {

	// Response returned by Rest-Assured
    private Response response;
    
    /**
     * Wraps Rest-Assured response
     * @param response : {@link Response} returned by Rest-Assured
     */
    public ResponseImpl(Response response) {
        this.response = response;
    }
    
    @Override
    public void print() {
        Util.getLogger().info("Response Body: \n" + response.asString());
    }

    @Override
    public int getStatusCode() {
        return response.getStatusCode();
    }

    @Override
    public <T> List<T> getResposeBody(String path) {
        return response.body().jsonPath().get(path);
    }

	@Override
	public <T> Object getResponseBody(T[] t) {
		// Deserializes the JSON (with Gson) into an array of the same type as t
		return response.body().as(t.getClass());
	}
}
