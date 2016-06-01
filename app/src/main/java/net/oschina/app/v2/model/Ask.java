package net.oschina.app.v2.model;

import android.text.TextUtils;

import java.io.IOException;

import net.oschina.app.v2.AppException;

import org.json.JSONObject;

/**
 * 新闻实体类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class Ask extends Entity {

	public int id;
	private int uid;
	private int catid;
	private String content;
	private String label;
	private int reward;
	private String superlist;
	private int issolveed;
	private int allow_comment;
	private String inputtime;
	private int anum;
	private int rank;
	private String nickname;
	private String head;
	private int type;
	private String image;
	private int istop;
	private String company;
	private int isanswer;
	private int from;
	private String hits;

	public Ask() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getrank() {
		return rank;
	}

	public void setrank(int rank) {
		this.rank = rank;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getCatid() {
		return catid;
	}

	public void setCatid(int catid) {
		this.catid = catid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getreward() {
		return reward;
	}

	public void setreward(int reward) {
		this.reward = reward;
	}

	public String getsuperlist() {
		return superlist;
	}

	public void setsuperlist(String superlist) {
		this.superlist = superlist;
	}

	public int getissolveed() {
		return issolveed;
	}

	public void setissolveed(int issolveed) {
		this.issolveed = issolveed;
	}

	public String getinputtime() {
		return inputtime;
	}

	public void setinputtime(String inputtime) {
		this.inputtime = inputtime;
	}

	public int getanum() {
		return anum;
	}

	public void setanum(int anum) {
		this.anum = anum;
	}

	public int getallow_comment() {
		return allow_comment;
	}

	public void setallow_comment(int allow_comment) {
		this.allow_comment = allow_comment;
	}

	public String getnickname() {
		return nickname;
	}

	public void setnickname(String nickname) {
		this.nickname = nickname;
	}

	public String gethead() {
		return head;
	}

	public void sethead(String head) {
		this.head = head;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getIstop() {
		return istop;
	}

	public void setIstop(int istop) {
		this.istop = istop;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}


	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getIsanswer() {
		return isanswer;
	}

	public void setIsanswer(int isanswer) {
		this.isanswer = isanswer;
	}
	public String getHits() {
		return hits;
	}

	public void setHits(String hits) {
		this.hits = hits;
	}

	public static Ask parse(JSONObject response) throws IOException,
			AppException {
		Ask ask = new Ask();
		ask.setId(response.optInt("id"));
		ask.setUid(response.optInt("uid"));
		ask.setCatid(response.optInt("catid"));
		ask.setnickname(response.optString("nickname"));
		ask.sethead(response.optString("head"));
		ask.setrank(response.optInt("rank"));
		ask.setContent(response.optString("content"));
		ask.setLabel(response.optString("lable"));
		ask.setreward(response.optInt("reward"));
		ask.setsuperlist(response.optString("superlist"));
		ask.setinputtime(response.optString("inputtime"));
		ask.setissolveed(response.optInt("issolveed"));
		ask.setanum(response.optInt("anum"));
		ask.setallow_comment(response.optInt("allow_comment"));
		ask.setType(response.optInt("type"));
		ask.setImage(response.optString("image"));
		ask.setIstop(response.optInt("istop"));
		ask.setCompany(response.optString("company"));
		ask.setIsanswer(response.optInt("isanswer"));
		ask.setFrom(response.optInt("from"));
		String hits=response.optString("hits");
		if(TextUtils.isEmpty(hits)){
			hits="0";
		}
		ask.setHits(hits);
		return ask;
	}

}
