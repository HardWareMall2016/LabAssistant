package net.oschina.app.v2.activity.user.model;

import net.oschina.app.v2.AppException;

import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author acer 系统消息实体
 */
public class ZhiChiWoDe implements Serializable {
	private String uid;
	private String id;

	private String title;
	private String content;
	private String inputtime;
	private String nickname;
	private String info;
	private String group_id;
	private int type;
	private String head;
	private String questiontotal;
	private String rank;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getInputtime() {
		return inputtime;
	}

	public void setInputtime(String inputtime) {
		this.inputtime = inputtime;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getQuestiontotal() {
		return questiontotal;
	}

	public void setQuestiontotal(String questiontotal) {
		this.questiontotal = questiontotal;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public static ZhiChiWoDe parse(JSONObject response) throws IOException,
			AppException {
		ZhiChiWoDe zhiChiWoDe = new ZhiChiWoDe();
		zhiChiWoDe.setUid(response.optString("uid"));
		zhiChiWoDe.setId(response.optString("id"));
		zhiChiWoDe.setTitle(response.optString("title"));
		zhiChiWoDe.setContent(response.optString("content"));
		zhiChiWoDe.setInputtime(response.optString("inputtime"));
		zhiChiWoDe.setNickname(response.optString("nickname"));
		zhiChiWoDe.setInfo(response.optString("info"));
		zhiChiWoDe.setGroup_id(response.optString("group_id"));
		zhiChiWoDe.setType(response.optInt("type"));
		zhiChiWoDe.setHead(response.optString("head"));
		zhiChiWoDe.setQuestiontotal(response.optString("questiontotal"));
		zhiChiWoDe.setRank(response.optString("rank"));

		return zhiChiWoDe;
	}
}
