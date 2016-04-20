package net.oschina.app.v2.activity.user.model;

import java.io.IOException;

import net.oschina.app.v2.AppException;

import org.json.JSONException;
import org.json.JSONObject;

public class SingleAnswer {
	private int id;
	private String name;
	private String head;
	private String content;
	private int intputtime;
	private int surportnumber;
	private int askagainnumber;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getIntputtime() {
		return intputtime;
	}
	public void setIntputtime(int intputtime) {
		this.intputtime = intputtime;
	}
	public int getSurportnumber() {
		return surportnumber;
	}
	public void setSurportnumber(int surportnumber) {
		this.surportnumber = surportnumber;
	}
	public int getAskagainnumber() {
		return askagainnumber;
	}
	public void setAskagainnumber(int askagainnumber) {
		this.askagainnumber = askagainnumber;
	}
	
	public static SingleAnswer parse(JSONObject response) throws IOException, AppException {
	/*	Daily daily = new Daily();*/
		//Answer answer=new Answer();
		SingleAnswer answer=new SingleAnswer();
		try {
			//问题ID
/*			answer.setId(response.getInt("id"));//问题的内容
			answer.setContent(response.getString("content"));//问题内容
			answer.setInputtime(response.getInt("inputtime"));//问题回答的时间*/
			answer.setId(response.getInt("id"));
			answer.setHead(response.getString("head"));
			answer.setIntputtime(response.getInt("inputtime"));
			answer.setSurportnumber(response.getInt("surportnum"));
			answer.setName(response.getString("name"));
			answer.setContent(response.getString("content"));
			answer.setAskagainnumber(response.getInt("askagainnumber"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
        return answer;       
	}

}
