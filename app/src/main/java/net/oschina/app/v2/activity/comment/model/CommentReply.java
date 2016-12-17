package net.oschina.app.v2.activity.comment.model;

import java.io.IOException;
import java.io.Serializable;

import net.oschina.app.v2.AppException;
import net.oschina.app.v2.model.Entity;

import org.json.JSONObject;

/**
 * 评论实体类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class CommentReply extends Entity implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private int qid;
	private int auid;
	private int aid;
	private int askid;
	private String image;
	private String content;
	private int type;
	private String intputtime;

	private String nickname;
	private String askname;
	private String head;
	private int rank;

	private String question;
	private String qnickname;
	private String qhead;
	private int count;
	
	private int sign;

	private String fileurl;
	private String filename;
	
	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

	public String getFileurl() {
		return fileurl;
	}

	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQid() {
		return qid;
	}

	public void setQid(int qid) {
		this.qid = qid;
	}

	public int getAuid() {
		return auid;
	}

	public void setAuid(int auid) {
		this.auid = auid;
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public int getAskid() {
		return askid;
	}

	public void setAskid(int askid) {
		this.askid = askid;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getIntputtime() {
		return intputtime;
	}

	public void setIntputtime(String intputtime) {
		this.intputtime = intputtime;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAskname() {
		return askname;
	}

	public void setAskname(String askname) {
		this.askname = askname;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
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

	public static CommentReply parseReply(JSONObject response)
			throws IOException, AppException {
		CommentReply comment = new CommentReply();
		comment.setId(response.optInt("id"));
		comment.setQid(response.optInt("qid"));
		comment.setAuid(response.optInt("auid"));
		comment.setAid(response.optInt("aid"));
		comment.setImage(response.optString("image"));
		comment.setSign(response.optInt("sign"));
		comment.setContent(response.optString("content"));
		comment.setType(response.optInt("type"));
		comment.setIntputtime(response.optString("inputtime"));
		comment.setRank(response.optInt("rank"));
		comment.setNickname(response.optString("nickname"));
		comment.setAskname(response.optString("askname"));
		comment.setHead(response.optString("head"));
		comment.setQnickname(response.optString("qnickname"));
		comment.setQhead(response.optString("qhead"));
		comment.setQuestion(response.optString("question"));

		comment.setFilename(response.optString("filename"));
		comment.setFileurl(response.optString("fileurl"));
		return comment;
	}

	public static CommentReply parseAnswerAfter(JSONObject response)
			throws IOException, AppException {
		CommentReply comment = new CommentReply();
		comment.setId(response.optInt("id"));
		comment.setQid(response.optInt("qid"));
		comment.setAuid(response.optInt("auid"));
		comment.setAid(response.optInt("aid"));
		comment.setAskid(response.optInt("askid"));
		comment.setSign(response.optInt("sign"));
		comment.setImage(response.optString("image"));
		comment.setContent(response.optString("content"));
		comment.setType(response.optInt("type"));
		comment.setIntputtime(response.optString("inputtime"));
		comment.setRank(response.optInt("rank"));
		comment.setNickname(response.optString("nickname"));
		comment.setAskname(response.optString("askname"));
		comment.setHead(response.optString("head"));
		comment.setQnickname(response.optString("qnickname"));
		comment.setQhead(response.optString("qhead"));
		comment.setQuestion(response.optString("question"));
		comment.setCount(response.optInt("count"));

		comment.setFilename(response.optString("filename"));
		comment.setFileurl(response.optString("fileurl"));
		return comment;
	}

}
