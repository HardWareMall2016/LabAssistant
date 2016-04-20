package net.oschina.app.v2.activity.comment.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.oschina.app.v2.AppException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CommentModel {

	private int qid;
	private String question;
	private String qnickname;
	private String qhead;

	private List<CommentReply> dataList = new ArrayList<CommentReply>();

	public int getQid() {
		return qid;
	}

	public void setQid(int qid) {
		this.qid = qid;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getQnickname() {
		return qnickname;
	}

	public void setQnickname(String qnickname) {
		this.qnickname = qnickname;
	}

	public String getQhead() {
		return qhead;
	}

	public void setQhead(String qhead) {
		this.qhead = qhead;
	}

	public List<CommentReply> getDataList() {
		return dataList;
	}

	public void setDataList(List<CommentReply> dataList) {
		this.dataList = dataList;
	}

	public static CommentModel parse(JSONObject json) throws IOException,
			AppException {
		CommentModel commentlist = new CommentModel();
		CommentReply comment = null;
		try {
//			comment.setQid(response.optInt("qid"));
//			comment.setQuestion(response.optString("question"));
//			comment.setQnickname(response.optString("qnickname"));
//			comment.setQhead(response.optString("qhead"));
			
			JSONArray dataList = new JSONArray(json.getString("data"));
			// 遍历jsonArray
			for (int i = 0; i < dataList.length(); i++) {
				comment = CommentReply.parseReply(dataList.getJSONObject(i));
				commentlist.getDataList().add(comment);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return commentlist;
	}

}
