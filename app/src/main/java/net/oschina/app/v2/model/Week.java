package net.oschina.app.v2.model;

import java.io.IOException;

import net.oschina.app.v2.AppException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 实验周刊 实体类
 * 
 * @author jaysi
 * 
 */
public class Week extends Entity {
	public static final int TYPE_MAIN = 1;
	public static final int TYPE_SUB = 0;
	
	private int id;
	private String title;
	private String description;
	private String thumb;
	private String imglist;
	private int allow_share;
	private String inputtime;
	
	private boolean isHeader;

	public String getInputtime() {
		return inputtime;
	}

	public void setInputtime(String inputtime) {
		this.inputtime = inputtime;
	}

	public Week() {

	}

	public int getAllow_share() {
		return allow_share;
	}

	public void setAllow_share(int allow_share) {
		this.allow_share = allow_share;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getImglist() {
		return imglist;
	}

	public void setImglist(String imglist) {
		this.imglist = imglist;
	}

	public boolean isHeader() {
		return isHeader;
	}

	public void setHeader(boolean isHeader) {
		this.isHeader = isHeader;
	}

	public static Week parse(JSONObject respose) throws IOException,
			AppException {
		Week week = new Week();
		try {
			week.setId(respose.getInt("id"));
			week.setTitle(respose.getString("title"));
			week.setDescription(respose.getString("description"));
			week.setThumb(respose.getString("thumb"));
			week.setImglist(respose.getString("imglist"));
			week.setAllow_share(respose.getInt("allow_share"));
			week.setInputtime(respose.getString("inputtime"));
			week.setHeader(true);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return week;
	}

	public static Week parseSubData(JSONObject respose) throws IOException,
			AppException {
		Week week = new Week();
		try {
			week.setId(respose.getInt("id"));
			week.setTitle(respose.getString("title"));
			week.setThumb(respose.getString("thumb"));
			week.setAllow_share(respose.getInt("allow_share"));
			week.setInputtime(respose.getString("inputtime"));
			week.setHeader(false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return week;
	}
}
