package net.oschina.app.v2.model;

import java.io.IOException;

import net.oschina.app.v2.AppException;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityCenter extends Entity {
	private int id;
	private String title;
	private String thumb;
	private String inputtime;
	private int allow_share;
	private String imglist;
	private String description;

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

	public int getAllow_share() {
		return allow_share;
	}

	public void setAllow_share(int allow_share) {
		this.allow_share = allow_share;
	}

	public String getImglist() {
		return imglist;
	}

	public void setImglist(String imglist) {
		this.imglist = imglist;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static ActivityCenter parse(JSONObject response) throws IOException,
			AppException {
		ActivityCenter ac = new ActivityCenter();
		try {
			ac.setId(response.getInt("id"));
			ac.setTitle(response.getString("title"));
			ac.setThumb(response.getString("thumb"));
			ac.setInputtime(response.getString("inputtime"));
			ac.setAllow_share(response.getInt("allow_share"));
			ac.setImglist(response.getString("imglist"));
			ac.setDescription(response.getString("description"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ac;
	}
}
