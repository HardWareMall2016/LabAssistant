package net.oschina.app.v2.model;

import net.oschina.app.v2.api.ApiHttpClient;

import org.json.JSONObject;

public class WeekRank extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int id;
	private int num;
	private String userIcon;
	private String userName;
	private String companyName;
	private int rank;
	private int type;//用户类型
	private int explain;//1被采纳数,2回答数,3邀请数
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
	

	public int getExplain() {
		return explain;
	}

	public void setExplain(int explain) {
		this.explain = explain;
	}

	public WeekRank() {

	}

	public static WeekRank parse(JSONObject response) {
		WeekRank weekRank = new WeekRank();

		weekRank.setId(response.optInt("id"));
		weekRank.setNum(response.optInt("num"));
		weekRank.setCompanyName(response.optString("company"));
		weekRank.setUserIcon(response.optString("head"));
		weekRank.setUserName(response.optString("nickname"));
		weekRank.setRank(response.optInt("rank"));
		weekRank.setType(response.optInt("type"));
		weekRank.setExplain(response.optInt("explain"));
		
		
		weekRank.setUserIcon(String.format(ApiHttpClient.DEV_API_IMAGE_URL, weekRank.getUserIcon()));

		
		
		return weekRank;

	}
}
