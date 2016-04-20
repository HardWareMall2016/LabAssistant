package net.oschina.app.v2.activity.user.model;
import java.io.IOException;
import java.io.Serializable;

import net.oschina.app.v2.AppException;

import org.json.JSONException;
import org.json.JSONObject;
public class Intersted implements Serializable{
	private static final long serialVersionUID = 1L;
	private int code;
	private String desc;
	private boolean data;
	private int id;
	private String image;
	private String catname;
	private int categoryId = -1;
	
	private boolean isBigCategory = false;
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getCatname() {
		return catname;
	}

	public void setCatname(String catname) {
		this.catname = catname;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean isData() {
		return data;
	}

	public void setData(boolean data) {
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	
	public boolean isBigCategory() {
		return isBigCategory;
	}

	public void setBigCategory(boolean isBigCategory) {
		this.isBigCategory = isBigCategory;
	}
	

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

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
