package net.oschina.app.v2.activity.model;

import java.io.IOException;
import java.io.Serializable;

import net.oschina.app.v2.AppException;

import org.json.JSONObject;

public class Question implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;//主键的id，也就是问题的id
	private int catid;//分类id
	private int uid;//用户id
	private String content;//用户提问的内容
	private String lable;//标签
	private int reward;//悬赏
	private String superlist;//高手
	private int issolved;//是否解决 返回1是解决
	private int allow_comment;//是否允许回答
	private String intputtime;//发布时间
	private int anum;//回答数
	private String nickname;//昵称
	private String head;//头像
	private int rank;//等级
	
	private int newreply;//新回答数
	private String newtime;//回答时间
	private String image;
	private int hits;

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}
	
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


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getCatid() {
		return catid;
	}


	public void setCatid(int catid) {
		this.catid = catid;
	}


	public int getUid() {
		return uid;
	}


	public void setUid(int uid) {
		this.uid = uid;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getLable() {
		return lable;
	}


	public void setLable(String lable) {
		this.lable = lable;
	}


	public int getReward() {
		return reward;
	}


	public void setReward(int reward) {
		this.reward = reward;
	}


	public String getSuperlist() {
		return superlist;
	}


	public void setSuperlist(String superlist) {
		this.superlist = superlist;
	}


	public int getIssolved() {
		return issolved;
	}


	public void setIssolved(int issolved) {
		this.issolved = issolved;
	}


	public int getAllow_comment() {
		return allow_comment;
	}


	public void setAllow_comment(int allow_comment) {
		this.allow_comment = allow_comment;
	}


	public String getIntputtime() {
		return intputtime;
	}


	public void setIntputtime(String intputtime) {
		this.intputtime = intputtime;
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


	//解析json得到一个实体的对象 
	public static Question parse(JSONObject response) throws IOException, AppException {
		Question question=new Question();
		question.setId(response.optInt("id"));
		question.setCatid(response.optInt("catid"));
		question.setUid(response.optInt("uid"));
		question.setContent(response.optString("content"));
		question.setLable(response.optString("lable"));
		question.setReward(response.optInt("reward"));
		question.setSuperlist(response.optString("superlist"));
		question.setIssolved(response.optInt("issolveed"));
		question.setAllow_comment(response.optInt("allow_comment"));
		question.setIntputtime(response.optString("inputtime"));
		question.setAnum(response.optInt("anum"));
		question.setNickname(response.optString("nickname"));
		question.setHead(response.optString("head"));
		question.setRank(response.optInt("rank"));
		question.setNewreply(response.optInt("newreply"));
		question.setNewtime(response.optString("newtime"));
		question.setImage(response.optString("image"));
		question.setHits(response.optInt("hits"));
        return question;       
	}

	@Override
	public String toString() {
		return "Question [id=" + id + ", catid=" + catid + ", uid=" + uid
				+ ", content=" + content + ", lable=" + lable + ", reward="
				+ reward + ", superlist=" + superlist + ", issolved="
				+ issolved + ", allow_comment=" + allow_comment
				+ ", intputtime=" + intputtime + ", anum=" + anum
				+ ", nickname=" + nickname + ", head=" + head + ", rank="
				+ rank + "]";
	}
	
}
