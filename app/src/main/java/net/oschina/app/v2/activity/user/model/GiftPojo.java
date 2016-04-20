package net.oschina.app.v2.activity.user.model;

import java.util.ArrayList;
import java.util.List;

import net.oschina.app.v2.api.ApiHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;

public class GiftPojo {

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
	private String inputtime;

	private String content;
	
	private int islearn;

	public int getIslearn() {
		return islearn;
	}

	public void setIslearn(int islearn) {
		this.islearn = islearn;
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

	public String getInputtime() {
		return inputtime;
	}

	public void setInputtime(String inputtime) {
		this.inputtime = inputtime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public static GiftPojo parse(JSONObject jObj) {
		GiftPojo pojo = new GiftPojo();

		pojo.id = jObj.optInt("id");
		pojo.title = jObj.optString("title");
		pojo.thumb = ApiHttpClient.getImageApiUrl(jObj.optString("thumb"));
		pojo.isHot = jObj.optInt("ishot");
		pojo.intergal = jObj.optInt("integral");
		pojo.num = jObj.optInt("num");
		pojo.rechargestatus = jObj.optInt("rechargestatus");
		pojo.type = jObj.optInt("type");
		
		
		return pojo;
	}

	public static GiftPojo parseDetail(JSONObject jObj) {
		GiftPojo pojo = new GiftPojo();

		pojo.id = jObj.optInt("id");
		pojo.title = jObj.optString("title");
		pojo.thumb = ApiHttpClient.getImageApiUrl(jObj.optString("thumb"));
		pojo.isHot = jObj.optInt("ishot");
		pojo.intergal = jObj.optInt("integral");
		pojo.rank = jObj.optInt("rank");
		pojo.rechargestatus = jObj.optInt("rechargestatus");
		pojo.type = jObj.optInt("type");
		pojo.num = jObj.optInt("num");
		pojo.islearn=jObj.optInt("islearn");
//		String imglist = jObj.optString("imglist");
//		if (!TextUtils.isEmpty(imglist) && !"null".equalsIgnoreCase(imglist)) {
//			String[] arr = imglist.split("\\|");
//			for (int i = 0; i < arr.length; i++) {
//				pojo.imgs.add(ApiHttpClient.getImageApiUrl(arr[i]));
//			}
//
//		}
		JSONArray jsonArray = jObj.optJSONArray("imglist");
		for (int i=0;i<jsonArray.length();i++) {
			String img = jsonArray.optString(i);
			if(TextUtils.isEmpty(img)){
				continue;
			}
			pojo.imgs.add(ApiHttpClient.getImageApiUrl(img));
		}
		if(pojo.islearn==1){
			pojo.getImgs().add(pojo.getThumb());
		}
		pojo.content = jObj.optString("content");
		pojo.inputtime = jObj.optString("inputtime");
		
		return pojo;
	}

}
