package net.oschina.app.v2.model;

import java.io.IOException;

import net.oschina.app.v2.AppException;

import org.json.JSONObject;

public class CommentItem extends Base {

	private String id;
	private String auid;
	private String content;
	private int islouzhu;
	private String nickname;
	
	private int qid;
	private int aid;
	
	public int getQid() {
		return qid;
	}

	public void setQid(int qid) {
		this.qid = qid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuid() {
		return auid;
	}

	public void setAuid(String auid) {
		this.auid = auid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getIslouzhu() {
		return islouzhu;
	}

	public void setIslouzhu(int islouzhu) {
		this.islouzhu = islouzhu;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public static CommentItem parse(JSONObject response) throws IOException, AppException {
		CommentItem comment = new CommentItem();
		comment.setId(response.optString("id"));
		comment.setAuid(response.optString("auid"));
		comment.setContent(response.optString("content"));
		comment.setIslouzhu(response.optInt("islouzhu"));
		comment.setNickname(response.optString("nickname"));
        return comment;
	}

}
