package net.oschina.app.v2.activity.favorite.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.oschina.app.v2.AppException;
import net.oschina.app.v2.model.Base;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuestionModel extends Base {

	private int id;
	private String title;
	private String description;
	private String content;
	private int catid;
	private int inputtime;

	private List<ProblemModel> dataList = new ArrayList<QuestionModel.ProblemModel>();

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getCatid() {
		return catid;
	}

	public void setCatid(int catid) {
		this.catid = catid;
	}

	public int getInputtime() {
		return inputtime;
	}

	public void setInputtime(int inputtime) {
		this.inputtime = inputtime;
	}

	public List<ProblemModel> getDataList() {
		return dataList;
	}

	public void addDataList(ProblemModel model) {
		this.dataList.add(model);
	}

	public static QuestionModel parse(JSONObject content) throws IOException,
			AppException {
		QuestionModel comment = new QuestionModel();

		try {
			JSONObject response = new JSONObject(content.optString("data"));
			comment.setId(response.getInt("id"));
			comment.setTitle(response.getString("title"));
			comment.setDescription(response.getString("description"));
			comment.setContent(response.getString("content"));
			comment.setCatid(response.getInt("catid"));
			comment.setInputtime(response.getInt("inputtime"));

			JSONArray array = new JSONArray(response.optString("problem"));
			if (array != null) {
				for (int i = 0; i < array.length(); i++) {
					ProblemModel object = ProblemModel.parse(array
							.getJSONObject(i));
					comment.addDataList(object);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return comment;
	}

	public static class ProblemModel {
		private int id;
		private String title;
		private String optiona;
		private String optionb;
		private String optionc;
		private String optiond;

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

		public String getOptiona() {
			return optiona;
		}

		public void setOptiona(String optiona) {
			this.optiona = optiona;
		}

		public String getOptionb() {
			return optionb;
		}

		public void setOptionb(String optionb) {
			this.optionb = optionb;
		}

		public String getOptionc() {
			return optionc;
		}

		public void setOptionc(String optionc) {
			this.optionc = optionc;
		}

		public String getOptiond() {
			return optiond;
		}

		public void setOptiond(String optiond) {
			this.optiond = optiond;
		}

		public static ProblemModel parse(JSONObject response)
				throws IOException, AppException {
			ProblemModel comment = new ProblemModel();
			comment.setId(response.optInt("id"));
			comment.setTitle(response.optString("title"));
			comment.setOptiona(response.optString("optiona"));
			comment.setOptionb(response.optString("optionb"));
			comment.setOptionc(response.optString("optionc"));
			comment.setOptiond(response.optString("optiond"));
			return comment;
		}
	}
}