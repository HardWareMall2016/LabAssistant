package net.oschina.app.v2.model;

import java.io.IOException;

import net.oschina.app.v2.AppException;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * 新闻实体类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class Home_bk extends Entity {

	private int id;
	private int uid;
	private String content;
	private int inputtime;
	private int status;
	private int value;
	private String valname;
	private String nickname;
	private String head;
	private String label;
	private int anum;

	private String question;
	private String image;
	private String superlist;
	private int questiontime;
	private String answer;
	private int isadopt;
	private int answertime;

	public Home_bk() {

	}

	public int getisadopt() {
		return isadopt;
	}

	public void setisadopt(int isadopt) {
		this.isadopt = isadopt;
	}

	public int getanswertime() {
		return answertime;
	}

	public void setanswertime(int answertime) {
		this.answertime = answertime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getanum() {
		return anum;
	}

	public void setanum(int anum) {
		this.anum = anum;
	}

	public int getquestiontime() {
		return questiontime;
	}

	public void setquestiontime(int questiontime) {
		this.questiontime = questiontime;
	}

	public int getuid() {
		return uid;
	}

	public void setuid(int uid) {
		this.uid = uid;
	}

	public String getanswer() {

		if (TextUtils.isEmpty(answer) || answer.equalsIgnoreCase("null")) {
			return " ";
		}

		return answer;
	}

	public void setanswer(String answer) {

		this.answer = answer;

	}

	public String getlabel() {
		return label;
	}

	public void setlabel(String label) {
		this.label = label;
	}

	public String getquestion() {
		return question;
	}

	public void setquestion(String question) {
		this.question = question;
	}

	public String getsuperlist() {
		return superlist;
	}

	public void setsuperlist(String superlist) {
		this.superlist = superlist;
	}

	public String getimage() {
		return image;
	}

	public void setimage(String image) {
		this.image = image;
	}

	public String getcontent() {
		return content;
	}

	public void setcontent(String content) {
		this.content = content;
	}

	public int getinputtime() {
		return inputtime;
	}

	public void setinputtime(int inputtime) {
		this.inputtime = inputtime;
	}

	public int getstatus() {
		return status;
	}

	public void setstatus(int status) {
		this.status = status;
	}

	public int getvalue() {
		return value;
	}

	public void setvalue(int value) {
		this.value = value;
	}

	public String getvalname() {
		return valname;
	}

	public void setvalname(String valname) {
		this.valname = valname;
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

	public static Home_bk parse(JSONObject response) throws IOException,
			AppException {
		/*
		 * private int id; private int uid; private String content; private int
		 * inputtime; private int status; private int value; private String
		 * valname; private String nickname; private String head; private String
		 * label; private int anum;
		 * 
		 * private String question; private String image; private String
		 * superlist; private int questiontime; private String answer; private
		 * int isadopt; private int answertime;
		 */

		Home_bk daily = new Home_bk();

		try {
			daily.setId(response.getInt("id"));
			daily.setuid(response.getInt("uid"));
			daily.setcontent(response.getString("content"));
			daily.setinputtime(response.getInt("inputtime"));
			daily.setstatus(response.getInt("status"));
			daily.setvalue(response.getInt("value"));
			daily.setvalname(response.getString("valname"));
			daily.setnickname(response.getString("nickname"));
			daily.sethead(response.getString("head"));
			if (response.has("label"))
				daily.setlabel(response.getString("label"));
			if (response.has("question"))
				daily.setquestion(response.getString("question"));
			if (response.has("image"))
				daily.setimage(response.getString("image"));
			if (response.has("superlist"))
				daily.setsuperlist(response.getString("superlist"));
			if (response.has("answer"))
				daily.setanswer(response.getString("answer"));
			if (response.has("anum"))
				daily.setanum(response.getInt("anum"));
			if (response.has("questiontime"))
				daily.setquestiontime(response.getInt("questiontime"));
			if (response.has("isadopt"))
				daily.setisadopt(response.getInt("isadopt"));
			if (response.has("answertime"))
				daily.setisadopt(response.getInt("answertime"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return daily;
	}
}
