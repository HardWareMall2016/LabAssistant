package net.oschina.app.v2.model;

import java.io.IOException;
import java.io.Serializable;

import net.oschina.app.v2.AppException;

import org.json.JSONObject;

public class MessageNum implements Serializable {

	public int getQnum() {
		return qnum;
	}

	public void setQnum(int qnum) {
		this.qnum = qnum;
	}

	public int getAnum() {
		return anum;
	}

	public void setAnum(int anum) {
		this.anum = anum;
	}

	public int getZnum() {
		return znum;
	}

	public void setZnum(int znum) {
		this.znum = znum;
	}

	public int getFnum() {
		return fnum;
	}

	public void setFnum(int fnum) {
		this.fnum = fnum;
	}

	public int getMnum() {
		return mnum;
	}

	public void setMnum(int mnum) {
		this.mnum = mnum;
	}

	public int getGnum() {
		return gnum;
	}

	public void setGnum(int gnum) {
		this.gnum = gnum;
	}

	public int getDnum() {
		return dnum;
	}

	public void setDnum(int dnum) {
		this.dnum = dnum;
	}

	public int getWnum() {
		return wnum;
	}

	public void setWnum(int wnum) {
		this.wnum = wnum;
	}

	public int getNnum() {
		return nnum;
	}

	public void setNnum(int nnum) {
		this.nnum = nnum;
	}

	public int getTnum() {
		return tnum;
	}

	public void setTnum(int tnum) {
		this.tnum = tnum;
	}

	public int getPnum() {
		return pnum;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getStatus() {
		return status;
	}

	public void setPnum(int pnum) {
		this.pnum = pnum;
	}

	public int getBnum() {
		return bnum;
	}

	public void setBnum(int bnum) {
		this.bnum = bnum;
	}

	public int getCurdayintegral() {
		return curdayintegral;
	}

	public void setCurdayintegral(int curdayintegral) {
		this.curdayintegral = curdayintegral;
	}

	private int qnum;
	private int anum;
	private int znum;
	private int fnum;
	private int mnum;
	private int gnum;
	private int dnum;
	private int wnum;
	private int nnum;
	private int tnum;
	private int pnum;
	private int bnum;
	private int chartnum;
	private int afternum;//追问我的
	private int status;  //用户状态 【0禁用 1未禁用】
	private int curdayintegral;//当天积分数

	public static MessageNum parse(JSONObject response) throws IOException,
			AppException {
		System.out.println("获取信息字段:" + response);
		MessageNum messageNum = new MessageNum();
		messageNum.setQnum(response.optInt("qnum"));
		messageNum.setAnum(response.optInt("anum"));
		messageNum.setZnum(response.optInt("znum"));
		messageNum.setFnum(response.optInt("fnum"));
		messageNum.setMnum(response.optInt("mnum"));
		messageNum.setGnum(response.optInt("gnum"));
		messageNum.setDnum(response.optInt("dnum"));
		messageNum.setWnum(response.optInt("wnum"));
		messageNum.setNnum(response.optInt("nnum"));
		messageNum.setTnum(response.optInt("tnum"));
		messageNum.setPnum(response.optInt("pnum"));
		messageNum.setBnum(response.optInt("bnum"));
		messageNum.setStatus(response.optInt("status"));
		messageNum.setChartnum(response.optInt("chartnum"));
		messageNum.setAfternum(response.optInt("afternum"));
		messageNum.setCurdayintegral(response.optInt("curdayintegral"));
		return messageNum;
	}
	
	

	
	
	public int getAfternum() {
		return afternum;
	}

	public void setAfternum(int afternum) {
		this.afternum = afternum;
	}

	public int getChartnum() {
		return chartnum;
	}

	public void setChartnum(int chartnum) {
		this.chartnum = chartnum;
	}

	@Override
	public String toString() {
		return (qnum + anum + znum + fnum + tnum) + "";
	}

	public int getActiveNum() {
		return qnum + anum + znum +afternum+ fnum + mnum+gnum;
	}

	public int getFindNum() {
		return dnum + wnum +  tnum + pnum+chartnum;
	}

}
