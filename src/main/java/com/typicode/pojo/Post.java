package com.typicode.pojo;

/**
 * Defines Post class, mapped from a JSON item of the /posts end-point
 *
 */
public class Post {

	// Id of the user who created the post
	private int userId;
	private int id;
	private String title;
	private String body;

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
}
