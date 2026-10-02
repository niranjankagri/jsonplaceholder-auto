package com.typicode.api.lib;

import java.util.List;

/**
 * Defines functions to return as response.
 * Wraps the HTTP library's response so tests never depend on it directly.
 *
 */
public interface IResponse {

    /**
     * Outputs response body to the log
     */
    public void print();
    
    /**
     * Status code of response
     * @return : Response code
     */
    public int getStatusCode();
    
    /**
     * Returns value at specified path.
     * For a JSON array response, a field name such as "id" returns that field from every item.
     * @param path : location in response body (JSON path)
     * @return : values found at the path
     */
    public <T> List<T> getResposeBody(String path);
    
    /**
     * Converts response JSON body as java object
     * @param t : Empty array of the target type, e.g. new User[]{}, used only for its class
     * @return : response body as object (an array of the same type as t; cast it by the caller)
     */
    public <T> Object getResponseBody(T[] t);
}
