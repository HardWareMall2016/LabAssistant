package net.oschina.app.v2.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.oschina.app.v2.AppException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 收藏列表实体类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class FavoriteList extends Entity implements ListEntity {
	public final static int TYPE_ALL = 0x00;
	public final static int TYPE_SOFTWARE = 0x01;
	public final static int TYPE_POST = 0x02;
	public final static int TYPE_BLOG = 0x03;
	public final static int TYPE_NEWS = 0x04;
	public final static int TYPE_CODE = 0x05;
	
	private int code;
	private String desc;
	private List<Favorite> favoritelist = new ArrayList<Favorite>();

	/**
	 * 收藏实体类
	 */
	public static class Favorite implements Serializable {
		private int id;
		private int tuid;
		private int inputtime;
		private String nickname;
		private String head;
		private String company;
		private int realname_status;
		private int rank;
		
		private int fuid;
		private int same;//是否互相关注
		private int type;//用户类型
		
		
		
		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public int getSame() {
			return same;
		}

		public void setSame(int same) {
			this.same = same;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getTuid() {
			return tuid;
		}

		public void setTuid(int tuid) {
			this.tuid = tuid;
		}

		public int getInputtime() {
			return inputtime;
		}

		public void setInputtime(int inputtime) {
			this.inputtime = inputtime;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public String getHead() {
			return head;
		}

		public void setHead(String head) {
			this.head = head;
		}

		public String getCompany() {
			return company;
		}

		public void setCompany(String company) {
			this.company = company;
		}

		public int getRealname_status() {
			return realname_status;
		}

		public void setRealname_status(int realname_status) {
			this.realname_status = realname_status;
		}

		public int getRank() {
			return rank;
		}

		public void setRank(int rank) {
			this.rank = rank;
		}

		
		
		public int getFuid() {
			return fuid;
		}

		public void setFuid(int fuid) {
			this.fuid = fuid;
		}

		public static Favorite parse(JSONObject response) throws IOException,
				AppException {
			Favorite question = new Favorite();
			question.setId(response.optInt("id"));
			question.setTuid(response.optInt("tuid"));
			question.setFuid(response.optInt("fuid"));
			question.setRealname_status(response.optInt("realname_status"));
			question.setInputtime(response.optInt("inputtime"));
			question.setNickname(response.optString("nickname"));
			question.setHead(response.optString("head"));
			question.setRank(response.optInt("rank"));
			question.setCompany(response.optString("company"));
			question.setSame(response.optInt("same"));
			question.setType(response.optInt("type"));
			return question;
		}
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<Favorite> getFavoritelist() {
		return favoritelist;
	}

	public void setFavoritelist(List<Favorite> favoritelist) {
		this.favoritelist = favoritelist;
	}

	public static FavoriteList parse(String jsonStr) throws IOException,
			AppException {
		FavoriteList questionList = new FavoriteList();
		Favorite favorite = null;
		try {
			// 把解析的String转换成Json
			JSONObject json = new JSONObject(jsonStr);
			questionList.setCode(json.getInt("code"));
			questionList.setDesc(json.getString("desc"));
			JSONArray dataList = new JSONArray(json.getString("data"));
			// 遍历jsonArray
			for (int i = 0; i < dataList.length(); i++) {
				favorite = Favorite.parse(dataList.getJSONObject(i));
				questionList.getFavoritelist().add(favorite);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return questionList;
	}

	@Override
	public List<?> getList() {
		return favoritelist;
	}
}
