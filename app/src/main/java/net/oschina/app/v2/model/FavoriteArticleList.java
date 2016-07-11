package net.oschina.app.v2.model;

import net.oschina.app.v2.AppException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 收藏列表实体类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class FavoriteArticleList extends Entity implements ListEntity {

	private int code;
	private String desc;
	private List<FavoriteArticle> favoritelist = new ArrayList<FavoriteArticle>();

	/**
	 * 收藏实体类
	 */
	public static class FavoriteArticle implements Serializable {
		private String id;
		private String qnid;
		private String catid;
		private String title;
		private String thumb;
		private String date;
		private String content;
		private String catname;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getQnid() {
			return qnid;
		}

		public void setQnid(String qnid) {
			this.qnid = qnid;
		}

		public String getCatid() {
			return catid;
		}

		public void setCatid(String catid) {
			this.catid = catid;
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

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getCatname() {
			return catname;
		}

		public void setCatname(String catname) {
			this.catname = catname;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public static FavoriteArticle parse(JSONObject response) throws IOException,
				AppException {
			FavoriteArticle question = new FavoriteArticle();
			question.setId(response.optString("id"));
			question.setQnid(response.optString("qnid"));
			question.setCatid(response.optString("catid"));
			question.setTitle(response.optString("title"));
			question.setThumb(response.optString("thumb"));
			question.setDate(response.optString("date"));
			question.setContent(response.optString("content"));
			question.setCatname(response.optString("catname"));
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

	public List<FavoriteArticle> getFavoritelist() {
		return favoritelist;
	}

	public void setFavoritelist(List<FavoriteArticle> favoritelist) {
		this.favoritelist = favoritelist;
	}

	public static FavoriteArticleList parse(String jsonStr) throws IOException,
			AppException {
		FavoriteArticleList questionList = new FavoriteArticleList();
		FavoriteArticle favorite = null;
		try {
			// 把解析的String转换成Json
			JSONObject json = new JSONObject(jsonStr);
			questionList.setCode(json.getInt("code"));
			questionList.setDesc(json.getString("desc"));
			JSONArray dataList = new JSONArray(json.getString("data"));
			// 遍历jsonArray
			for (int i = 0; i < dataList.length(); i++) {
				favorite = FavoriteArticle.parse(dataList.getJSONObject(i));
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
