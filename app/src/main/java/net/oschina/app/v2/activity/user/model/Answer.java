package net.oschina.app.v2.activity.user.model;

import java.io.IOException;
import java.io.Serializable;

import net.oschina.app.v2.AppException;

import org.json.JSONObject;

public class Answer implements Serializable {
	private static final long serialVersionUID = 2412766324644175931L;
	private int code;// 状态码
	private String desc;// 描述
	private boolean data;// 返回数据内容

	private int id;// 返回主键ID
	private int qid;// 问题的id
	private int quid;// 发布问题用户id
	private String content;// 问题的内容
	private String newContent;
	private int isadopt;// 是否采纳
	private String inputtime;// 回答时间
	private String label;// 标签
	private String superlist;// 邀请人

	private String title;
	private int aid;
	private int auid;
	private int type;
	private int snum;
	private int anum;
	private String nickname;
	private String qnickname;
	private String head;
	private int rank;
	
	private int newreply;
	private String newtime;
	private String image;
	
	
	

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getNewreply() {
		return newreply;
	}

	public void setNewreply(int newreply) {
		this.newreply = newreply;
	}

	public String getNewtime() {
		return newtime;
	}

	public void setNewtime(String newtime) {
		this.newtime = newtime;
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

	public boolean isData() {
		return data;
	}

	public void setData(boolean data) {
		this.data = data;
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

	public int getQuid() {
		return quid;
	}

	public void setQuid(int quid) {
		this.quid = quid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getIsadopt() {
		return isadopt;
	}

	public void setIsadopt(int isadopt) {
		this.isadopt = isadopt;
	}

	public String getInputtime() {
		return inputtime;
	}

	public void setInputtime(String inputtime) {
		this.inputtime = inputtime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public int getAuid() {
		return auid;
	}

	public void setAuid(int auid) {
		this.auid = auid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSnum() {
		return snum;
	}

	public void setSnum(int snum) {
		this.snum = snum;
	}

	public int getAnum() {
		return anum;
	}

	public void setAnum(int anum) {
		this.anum = anum;
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

	public String getNewContent() {
		return newContent;
	}

	public void setNewContent(String newContent) {
		this.newContent = newContent;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSuperlist() {
		return superlist;
	}

	public void setSuperlist(String superlist) {
		this.superlist = superlist;
	}

	public String getQnickname() {
		return qnickname;
	}

	public void setQnickname(String qnickname) {
		this.qnickname = qnickname;
	}

	// 解析json得到一个实体的对象
	public static Answer parse(JSONObject response) throws IOException,
			AppException {
		Answer answer = new Answer();
		// 问题ID
		answer.setId(response.optInt("id"));
		answer.setContent(response.optString("content"));
		answer.setNewContent(response.optString("newcontent"));
		answer.setInputtime(response.optString("inputtime"));
		answer.setQid(response.optInt("qid"));
		answer.setQuid(response.optInt("quid"));
		answer.setIsadopt(response.optInt("isadopt"));
		answer.setTitle(response.optString("title"));
		answer.setAid(response.optInt("aid"));
		answer.setAuid(response.optInt("auid"));
		answer.setType(response.optInt("type"));
		answer.setSnum(response.optInt("snum"));
		answer.setAnum(response.optInt("anum"));
		answer.setSuperlist(response.optString("superlist"));
		answer.setNickname(response.optString("nickname"));
		answer.setQnickname(response.optString("qnickname"));
		answer.setHead(response.optString("head"));
		answer.setRank(response.optInt("rank"));
		answer.setLabel(response.optString("lable"));
		answer.setNewreply(response.optInt("newreply"));
		answer.setNewtime(response.optString("newtime"));
		answer.setImage(response.optString("image"));
		return answer;
	}
}
