package net.oschina.app.v2.activity.user.model;

import java.io.IOException;
import java.io.Serializable;

import net.oschina.app.v2.AppException;

import org.json.JSONException;
import org.json.JSONObject;

public class RenWuChengJiu implements Serializable{
	private static final long serialVersionUID = 1L;
	private int code;
	private String desc;
	private boolean data;
	private int id;
	private String image;
	private String catname;
	//
	public static Intersted parse(JSONObject response) throws IOException, AppException {
		Intersted intersted=new Intersted();
		try {
			//问题ID
			intersted.setId(response.getInt("id"));//问题的内容
			intersted.setCatname(response.getString("catname"));//问题的分类
			intersted.setImage(response.getString("image"));//兴趣的图片
		} catch (JSONException e) {
			e.printStackTrace();
		}
        return intersted;       
	}
}
