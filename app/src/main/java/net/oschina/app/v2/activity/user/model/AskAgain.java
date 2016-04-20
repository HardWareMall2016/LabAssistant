package net.oschina.app.v2.activity.user.model;

import java.io.IOException;
import java.io.Serializable;

import net.oschina.app.v2.AppException;

import org.json.JSONObject;

public class AskAgain implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private int qid;
	private int aid;
	private String image;
	private int askid;
	private int auid;
	private String content;
	private String acontent;
	private int type;
	private String intputtime;
	private String title;
	private String nickname;
	private String qtitle;
	private String qnickname;
	private String askname;
	private String qimage;
	private String aimage;
	private String head;
	private int rank;
	private String anickname;
	private String zcontent;
	private String znickname;
	
private String aftername;
	
	
	
	

	public String getAftername() {
		return aftername;
	}


	public void setAftername(String aftername) {
		this.aftername = aftername;
	}

	public String getZcontent() {
		return zcontent;
	}

	public void setZcontent(String zcontent) {
		this.zcontent = zcontent;
	}

	public String getAnickname() {
		return anickname;
	}

	public void setAnickname(String anickname) {
		this.anickname = anickname;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getIntputtime() {
		return intputtime;
	}

	public void setIntputtime(String intputtime) {
		this.intputtime = intputtime;
	}

	public int getAskid() {
		return askid;
	}

	public void setAskid(int askid) {
		this.askid = askid;
	}

	public String getQtitle() {
		return qtitle;
	}

	public void setQtitle(String qtitle) {
		this.qtitle = qtitle;
	}

	
	public String getZnickname() {
		return znickname;
	}

	public void setZnickname(String znickname) {
		this.znickname = znickname;
	}

	public String getQnickname() {
		return qnickname;
	}

	public void setQnickname(String qnickname) {
		this.qnickname = qnickname;
	}

	public String getQimage() {
		return qimage;
	}

	public void setQimage(String qimage) {
		this.qimage = qimage;
	}

	public String getAimage() {
		return aimage;
	}

	public void setAimage(String aimage) {
		this.aimage = aimage;
	}

	public String getAskname() {
		return askname;
	}

	public void setAskname(String askname) {
		this.askname = askname;
	}

	public String getAcontent() {
		return acontent;
	}

	public void setAcontent(String acontent) {
		this.acontent = acontent;
	}

	// 解析json得到一个实体的对象
	public static AskAgain parseAskMeAgain(JSONObject response) throws IOException,
			AppException {
		AskAgain askAgain = new AskAgain();
		askAgain.setId(response.optInt("id"));
		askAgain.setQid(response.optInt("qid"));
		askAgain.setAid(response.optInt("aid"));
		askAgain.setImage(response.optString("image"));
		askAgain.setAskid(response.optInt("askid"));
		askAgain.setAuid(response.optInt("auid"));
		askAgain.setContent(response.optString("content"));
		askAgain.setType(response.optInt("type"));
		askAgain.setIntputtime(response.optString("inputtime"));
		askAgain.setQtitle(response.optString("qtitle"));
		askAgain.setQimage(response.optString("qimage"));
		askAgain.setAimage(response.optString("aimage"));
		askAgain.setAcontent(response.optString("acontent"));
		askAgain.setAskname(response.optString("askname"));
		askAgain.setHead(response.optString("head"));
		askAgain.setQnickname(response.optString("qnickname"));
		askAgain.setRank(response.optInt("rank"));
		askAgain.setAnickname(response.optString("anickname"));
		askAgain.setZcontent(response.optString("zcontent"));
		askAgain.setZnickname(response.optString("znickname"));
		askAgain.setAftername(response.optString("aftername"));
		return askAgain;
	}

	public static AskAgain parseMyAnswerAfter(JSONObject response)
			throws IOException, AppException {
		AskAgain askAgain = new AskAgain();
		askAgain.setId(response.optInt("id"));
		askAgain.setQid(response.optInt("qid"));
		askAgain.setAid(response.optInt("aid"));
		askAgain.setImage(response.optString("image"));
		askAgain.setAskid(response.optInt("askid"));
		askAgain.setAuid(response.optInt("auid"));
		askAgain.setContent(response.optString("content"));
		askAgain.setType(response.optInt("type"));
		askAgain.setIntputtime(response.optString("inputtime"));
		askAgain.setQtitle(response.optString("qtitle"));
		askAgain.setQimage(response.optString("qimage"));
		askAgain.setAimage(response.optString("aimage"));
		askAgain.setAcontent(response.optString("acontent"));
		askAgain.setQnickname(response.optString("qnickname"));
		askAgain.setAskname(response.optString("askname"));
		askAgain.setAnickname(response.optString("anickname"));
		askAgain.setZcontent(response.optString("zcontent"));
		askAgain.setZnickname(response.optString("znickname"));
		askAgain.setAftername(response.optString("aftername"));
		return askAgain;
	}
}
