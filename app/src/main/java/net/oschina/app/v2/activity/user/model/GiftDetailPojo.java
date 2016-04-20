package net.oschina.app.v2.activity.user.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.text.TextUtils;

public class GiftDetailPojo {

	private int id;
	private String title;
	private String thumb;
	private int isHot;
	private int intergal;
	private int rank;
	private int rechargestatus;
	private int type;
	private int num;
	private List<String> imgs = new ArrayList<String>();
	private long inputtime;
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
	public int getIsHot() {
		return isHot;
	}
	public void setIsHot(int isHot) {
		this.isHot = isHot;
	}
	public int getIntergal() {
		return intergal;
	}
	public void setIntergal(int intergal) {
		this.intergal = intergal;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getRechargestatus() {
		return rechargestatus;
	}
	public void setRechargestatus(int rechargestatus) {
		this.rechargestatus = rechargestatus;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public List<String> getImgs() {
		return imgs;
	}
	public void setImgs(List<String> imgs) {
		this.imgs = imgs;
	}
	public long getInputtime() {
		return inputtime;
	}
	public void setInputtime(long inputtime) {
		this.inputtime = inputtime;
	}
	
	
	public GiftDetailPojo  parse(JSONObject jObj)
	{
		GiftDetailPojo pojo = new GiftDetailPojo();
		
		pojo.id = jObj.optInt("id");
		pojo.title = jObj.optString("title");
		pojo.thumb = jObj.optString("thumb");
		pojo.isHot = jObj.optInt("isHot");
		pojo.inputtime = jObj.optLong("inputtime");
		pojo.intergal = jObj.optInt("intergal");
		pojo.num = jObj.optInt("num");
		pojo.rechargestatus = jObj.optInt("rechargestatus");
		pojo.type = jObj.optInt("type");
		pojo.rank = jObj.optInt("rank");
		
		String imglist = jObj.optString("imglist");
		if(!TextUtils.isEmpty(imglist))
		{
			String[] arr = imglist.split("|");
			for(int i = 0; i< arr.length; i++)
			{
				imgs.add(arr[i]);
			}
			
		}
		
		return pojo;
	}
	
}
