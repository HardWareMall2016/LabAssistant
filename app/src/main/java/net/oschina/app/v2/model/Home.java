package net.oschina.app.v2.model;

import java.io.IOException;

import net.oschina.app.v2.AppException;
import net.oschina.app.v2.GlobalConstants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 新闻实体类
 * 
 * @author johnny
 * @version 1.0
 */
public class Home extends Entity {
	private int type;

	private int id;
	private int catid;
	private String label1;
	private String label2;
	private String title;
	private String url;
	private int islink;
	private String copyfrom;// 来源
	private String thumb;
	private String bewrite;
	private String questiontitle;
	private String inputtime;
	private String qinputtime;
	private String head;
	private String nickname;
	private String qnickname;
	private String anickname;
	private int quid;
	private int uid;
	private int qid;
	private int aid;
	private int auid;
	private String content;
	private int status;
	private int isadopt;
	private String superlist;
	private String lable;
	private int reward;
	private int issolveed;
	private String allow_comment;
	private int anum;
	private int findid;
	private String image;
	private String qimage;
	private int mark;
	private String answernickname;
	private int isanswer; //我是否回答过
	

	public String getAnswernickname() {
		return answernickname;
	}

	public void setAnswernickname(String answernickname) {
		this.answernickname = answernickname;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public String getLabel1() {
		return label1;
	}

	public void setLabel1(String label1) {
		this.label1 = label1;
	}

	public String getLabel2() {
		return label2;
	}

	public void setLabel2(String label2) {
		this.label2 = label2;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getIslink() {
		return islink;
	}

	public void setIslink(int islink) {
		this.islink = islink;
	}

	public String getCopyfrom() {
		return copyfrom;
	}

	public void setCopyfrom(String copyfrom) {
		this.copyfrom = copyfrom;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getBewrite() {
		return bewrite;
	}

	public void setBewrite(String bewrite) {
		this.bewrite = bewrite;
	}

	public String getInputtime() {
		return inputtime;
	}

	public void setInputtime(String inputtime) {
		this.inputtime = inputtime;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getQid() {
		return qid;
	}

	public void setQid(int qid) {
		this.qid = qid;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getIsadopt() {
		return isadopt;
	}

	public void setIsadopt(int isadopt) {
		this.isadopt = isadopt;
	}

	public String getSuperlist() {
		return superlist;
	}

	public void setSuperlist(String superlist) {
		this.superlist = superlist;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public String getQuestiontitle() {
		return questiontitle;
	}

	public void setQuestiontitle(String questiontitle) {
		this.questiontitle = questiontitle;
	}

	public int getReward() {
		return reward;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	public int getIssolveed() {
		return issolveed;
	}

	public void setIssolveed(int issolveed) {
		this.issolveed = issolveed;
	}

	public String getAllow_comment() {
		return allow_comment;
	}

	public void setAllow_comment(String allow_comment) {
		this.allow_comment = allow_comment;
	}

	public int getAnum() {
		return anum;
	}

	public void setAnum(int anum) {
		this.anum = anum;
	}
	
	public int getIsanswer() {
		return isanswer;
	}

	public void setIsanswer(int isanswer) {
		this.isanswer = isanswer;
	}

	

	public int getFindid() {
		return findid;
	}

	public void setFindid(int findid) {
		this.findid = findid;
	}

	public int getQuid() {
		return quid;
	}

	public void setQuid(int quid) {
		this.quid = quid;
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

	public String getQimage() {
		return qimage;
	}

	public void setQimage(String qimage) {
		this.qimage = qimage;
	}

	public String getQinputtime() {
		return qinputtime;
	}

	public void setQinputtime(String qinputtime) {
		this.qinputtime = qinputtime;
	}

	public String getQnickname() {
		return qnickname;
	}

	public void setQnickname(String qnickname) {
		this.qnickname = qnickname;
	}

	public String getAnickname() {
		return anickname;
	}

	public void setAnickname(String anickname) {
		this.anickname = anickname;
	}

	public static Home parse(JSONObject response) throws IOException,
			AppException {
		Home home = new Home();
		try {
			home.setType(response.getInt("type"));
			switch (home.getType()) {
			case GlobalConstants.HOME_TYPE_MY_QUESTION:
			
			case GlobalConstants.HOME_TYPE_SHAREFROMME:// 我的提问
				home.setId(response.getInt("id"));
				home.setCatid(response.getInt("catid"));
				home.setUid(response.getInt("uid"));
				home.setImage(response.getString("image"));
				home.setContent(response.getString("content"));
				home.setLable(response.getString("lable"));
				home.setReward(response.getInt("reward"));
				home.setSuperlist(response.getString("superlist"));
				home.setIssolveed(response.getInt("issolveed"));
				home.setAllow_comment(response.getString("allow_comment"));
				home.setFindid(response.getInt("findid"));
				home.setBewrite(response.getString("bewrite"));
				home.setAnum(response.getInt("anum"));
				home.setHead(response.getString("head"));
				home.setNickname(response.getString("nickname"));
				home.setInputtime(response.getString("inputtime"));
				break;
			case GlobalConstants.HOME_TYPE_MY_ANSWER:// 我的回答
				home.setId(response.getInt("id"));
				home.setQid(response.getInt("qid"));
				home.setAid(response.getInt("aid"));
				home.setQuid(response.getInt("quid"));
				home.setAuid(response.getInt("auid"));
				home.setContent(response.getString("content"));
				home.setStatus(response.getInt("status"));
				home.setIsadopt(response.getInt("isadopt"));
				home.setFindid(response.getInt("findid"));
				home.setBewrite(response.getString("bewrite"));
				home.setQuestiontitle(response.getString("questiontitle"));
				home.setQimage(response.getString("qimage"));
				home.setLable(response.getString("lable"));
				home.setUid(response.getInt("uid"));
				home.setHead(response.getString("head"));
				home.setNickname(response.getString("nickname"));
				home.setInputtime(response.getString("inputtime"));
				home.setQnickname(response.getString("qnickname"));
				home.setQinputtime(response.getString("qinputtime"));
			
				if(home.getAid()==0){
					home.setAid(home.getId());
				}
				break;
			case GlobalConstants.HOME_TYPE_BE_HELP:// 我的粉丝求助
				home.setId(response.getInt("id"));
				home.setQimage(response.getString("qimage"));
				home.setCatid(response.getInt("catid"));
				home.setUid(response.getInt("uid"));
				home.setContent(response.getString("content"));
				home.setLable(response.getString("lable"));
				home.setReward(response.getInt("reward"));
				home.setSuperlist(response.getString("superlist"));
				home.setIssolveed(response.getInt("issolveed"));
				home.setAllow_comment(response.getString("allow_comment"));
				home.setFindid(response.getInt("findid"));
				home.setBewrite(response.getString("bewrite"));
				home.setAnum(response.getInt("anum"));
				home.setIsanswer(response.getInt("isanswer"));
				home.setHead(response.getString("head"));
				home.setNickname(response.getString("nickname"));
				home.setInputtime(response.getString("inputtime"));
				break;
			case GlobalConstants.HOME_TYPE_ANSWER_CHOICE:// 我的答案被采纳
				home.setAuid(response.getInt("auid"));
				home.setId(response.getInt("id"));
				home.setQid(response.getInt("qid"));
				home.setQuid(response.getInt("quid"));
				home.setQimage(response.getString("qimage"));
				home.setContent(response.getString("content"));
				home.setIsadopt(response.getInt("isadopt"));
				home.setFindid(response.getInt("findid"));
				home.setBewrite(response.getString("bewrite"));
				home.setTitle(response.getString("title"));
				home.setLable(response.getString("lable"));
				home.setSuperlist(response.getString("superlist"));
				home.setUid(response.getInt("uid"));
				home.setHead(response.getString("head"));
				home.setNickname(response.getString("nickname"));
				home.setInputtime(response.getString("inputtime"));
				home.setQnickname(response.getString("qnickname"));
				home.setQinputtime(response.getString("qinputtime"));
				if(home.getAid()==0){
					home.setAid(home.getId());
				}
				break;
			case GlobalConstants.HOME_TYPE_ASKME:
			case GlobalConstants.HOME_TYPE_MY_ANSWERAFTER:// 我的追问
				home.setQid(response.getInt("qid"));
				home.setQuid(response.getInt("quid"));
				home.setQimage(response.getString("qimage"));
				home.setAid(response.getInt("aid"));
				home.setContent(response.getString("content"));
				home.setStatus(response.getInt("status"));
				home.setIsadopt(response.getInt("isadopt"));
				home.setType(response.getInt("type"));
				home.setBewrite(response.getString("bewrite"));
				home.setQuestiontitle(response.getString("questiontitle"));
				home.setLable(response.getString("lable"));
				home.setSuperlist(response.getString("superlist"));
				home.setUid(response.getInt("uid"));
				home.setHead(response.getString("head"));
				home.setNickname(response.getString("nickname"));
				home.setInputtime(response.getString("inputtime"));
				home.setQnickname(response.getString("qnickname"));
				home.setQinputtime(response.getString("qinputtime"));
				home.setAnickname(response.getString("anickname"));
				home.setAnswernickname(response.getString("answernickname"));
				if(home.getAid()==0){
					home.setAid(home.getId());
				}
				break;
			case GlobalConstants.HOME_TYPE_MY_ATTENER_QUESTION:// 我关注的人提问
				home.setId(response.getInt("id"));
				home.setQimage(response.getString("qimage"));
				home.setCatid(response.getInt("catid"));
				home.setUid(response.getInt("uid"));
				home.setContent(response.getString("content"));
				home.setLable(response.getString("lable"));
				home.setReward(response.getInt("reward"));
				home.setSuperlist(response.getString("superlist"));
				home.setIssolveed(response.getInt("issolveed"));
				home.setAllow_comment(response.getString("allow_comment"));
				home.setType(response.getInt("type"));
				home.setFindid(response.getInt("findid"));
				home.setQuid(response.getInt("quid"));
				home.setBewrite(response.getString("bewrite"));
				home.setHead(response.getString("head"));
				home.setNickname(response.getString("nickname"));
				home.setAnum(response.getInt("anum"));
				home.setInputtime(response.getString("inputtime"));
				break;
			case GlobalConstants.HOME_TYPE_MY_ATTENER_ANSWER:// 我关注的人回答
				home.setAuid(response.getInt("auid"));
				home.setQid(response.getInt("qid"));
				home.setQimage(response.getString("qimage"));
				home.setAid(response.getInt("aid"));
				home.setTitle(response.getString("qcontent"));
				home.setContent(response.getString("acontent"));
				home.setStatus(response.getInt("status"));
				home.setIsadopt(response.getInt("isadopt"));
				home.setType(response.getInt("type"));
				home.setFindid(response.getInt("findid"));
				home.setBewrite(response.getString("bewrite"));
				home.setUid(response.getInt("uid"));
				home.setHead(response.getString("head"));
				home.setNickname(response.getString("nickname"));
				home.setQuid(response.getInt("quid"));
				home.setInputtime(response.getString("inputtime"));
				home.setLable(response.getString("lable"));
				home.setSuperlist(response.getString("superlist"));
				home.setReward(response.getInt("reward"));
				home.setAnum(response.getInt("anum"));
				if(home.getAid()==0){
					home.setAid(home.getId());
				}
				break;
			case GlobalConstants.HOME_TYPE_BE_ATTENTION:// XX关注了我
			case GlobalConstants.HOME_TYPE_MY_ATTENTION:// 我关注了XX
				home.setId(response.getInt("id"));
				home.setHead(response.getString("head"));
				home.setFindid(response.getInt("findid"));
				home.setNickname(response.getString("nickname"));
				home.setInputtime(response.getString("inputtime"));
				break;
			case GlobalConstants.HOME_TYPE_LESS_AND_MORE:// //XXX受我邀请注册了实验助手并关注了我

				break;
			case GlobalConstants.HOME_TYPE_PUSH:// 活动中心培训信息的主动推送内容
				home.setId(response.getInt("id"));
				home.setCatid(response.getInt("catid"));
				home.setLabel1(response.getString("label1"));
				home.setLabel2(response.getString("label2"));
				home.setTitle(response.getString("title"));
				home.setUrl(response.getString("url"));
				home.setIslink(response.getInt("islink"));
				home.setCopyfrom(response.getString("copyfrom"));
				home.setThumb(response.getString("thumb"));
				home.setBewrite(response.getString("bewrite"));
				home.setInputtime(response.getString("inputtime"));
				home.setFindid(response.getInt("findid"));
				break;
			case GlobalConstants.HOME_TYPE_SHARETOCIRCLE:// 分享到实验圈
				final int catId=response.getInt("catid");
				home.setId(response.getInt("id"));
				home.setCatid(catId);
				home.setThumb(response.getString("thumb"));
				home.setLabel1(response.getString("label1"));
				home.setLabel2(response.getString("label2"));
				home.setTitle(response.getString("title"));
				home.setUrl(response.getString("url"));
				home.setIslink(response.getInt("islink"));
				home.setCopyfrom(response.getString("copyfrom"));
				home.setBewrite(response.getString("bewrite"));
				home.setInputtime(response.getString("inputtime"));
				home.setFindid(response.getInt("findid"));
				
				//实验助手
				if(catId==5){
					home.setMark(response.getInt("mark"));
				}
				
				break;
			default:
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return home;
	}
}
