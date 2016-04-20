package net.oschina.app.v2.model;

import java.io.IOException;

import net.oschina.app.v2.AppException;

import org.json.JSONException;
import org.json.JSONObject;

public class Brand extends Entity implements Comparable<Brand> {
	protected int id;
	protected String title;
	protected String thumb;
	protected String inputtime;
	protected String description;

	public Brand() {

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

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getInputtime() {
		return inputtime;
	}

	public void setInputtime(String inputtime) {
		this.inputtime = inputtime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static Brand parse(JSONObject response) throws IOException,
			AppException {
		Brand brand = new Brand();
		try {
			brand.setId(response.getInt("id"));
			brand.setTitle(response.getString("title"));
			brand.setThumb(response.getString("thumb"));
			brand.setInputtime(response.getString("inputtime"));
			brand.setDescription(response.getString("descripiton"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return brand;

	}

	@Override
	public int compareTo(Brand another) {
		return this.title.compareTo(another.getTitle());
	}
}
