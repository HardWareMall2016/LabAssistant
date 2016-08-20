package net.oschina.app.v2.model;

import java.io.IOException;

import net.oschina.app.v2.AppException;
import net.oschina.app.v2.utils.StringUtils;

import org.json.JSONObject;

/**
 * @version 1.0
 * @created 2012-3-21
 */
public class User extends Base {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static int RELATION_ACTION_DELETE = 0x00;// 鍙栨秷鍏虫敞
	public final static int RELATION_ACTION_ADD = 0x01;// 鍔犲叧娉? public final
														// static int
														// RELATION_TYPE_BOTH =
														// 0x01;//鍙屾柟浜掍负绮変笣
	public final static int RELATION_TYPE_FANS_HIM = 0x02;// 浣犲崟鏂归潰鍏虫敞浠? public
															// final static int
															// RELATION_TYPE_NULL
															// = 0x03;//浜掍笉鍏虫敞
	public final static int RELATION_TYPE_FANS_ME = 0x04;// 鍙湁浠栧叧娉ㄦ垜

	private int id;
	private String clientId = "";
	private String nickname = "";
	private String head = "";
	private int realname_status;
	private int invitation;
	private int qnum;
	private int anum;
	private int cnum;
	private int fnum;
	private int gnum;//关注数
	private int integral;
	private int rank;
	private int supportednum;//被支持数

	private int sex;
	private int phone_status;
	private int isgetpush;
	private int isgethelp;
	private int isadopt;
	private int iscomment;
	private int isafter;
	private int status;
	private int tuijianuid;
	private int type;
	private String company = "";
	private String interest = "";
	private boolean isOnce;
	private int isattention;//是否已关注
	private int fansnum;//粉丝求助数目
	private String password;
	private String phone;
	private boolean issigned;
	private String info;//认证信息

	public boolean issigned() {
		return issigned;
	}

	public void setIssigned(boolean issigned) {
		this.issigned = issigned;
	}

	public int getSupportednum() {
		return supportednum;
	}

	public void setSupportednum(int supportednum) {
		this.supportednum = supportednum;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getFansnum() {
		return fansnum;
	}

	public void setFansnum(int fansnum) {
		this.fansnum = fansnum;
	}

	public int getIsattention() {
		return isattention;
	}

	public void setIsattention(int isattention) {
		this.isattention = isattention;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getclientId() {
		return clientId;
	}

	public void setclientId(String clientId) {
		this.clientId = clientId;
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

	public int getRealname_status() {
		return realname_status;
	}

	public void setRealname_status(int realname_status) {
		this.realname_status = realname_status;
	}

	public int getInvitation() {
		return invitation;
	}

	public void setInvitation(int invitation) {
		this.invitation = invitation;
	}

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

	public int getCnum() {
		return cnum;
	}

	public void setCnum(int cnum) {
		this.cnum = cnum;
	}

	public int getFnum() {
		return fnum;
	}

	public void setFnum(int fnum) {
		this.fnum = fnum;
	}
	
	public int getGnum() {
		return gnum;
	}

	public void setGnum(int gnum) {
		this.gnum = gnum;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getPhone_status() {
		return phone_status;
	}

	public void setPhone_status(int phone_status) {
		this.phone_status = phone_status;
	}

	public int getIsgetpush() {
		return isgetpush;
	}

	public void setIsgetpush(int isgetpush) {
		this.isgetpush = isgetpush;
	}

	public int getIsgethelp() {
		return isgethelp;
	}

	public void setIsgethelp(int isgethelp) {
		this.isgethelp = isgethelp;
	}

	public int getIsadopt() {
		return isadopt;
	}

	public void setIsadopt(int isadopt) {
		this.isadopt = isadopt;
	}

	public int getIscomment() {
		return iscomment;
	}

	public void setIscomment(int iscomment) {
		this.iscomment = iscomment;
	}

	public int getIsafter() {
		return isafter;
	}

	public void setIsafter(int isafter) {
		this.isafter = isafter;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTuijianuid() {
		return tuijianuid;
	}

	public void setTuijianuid(int tuijianuid) {
		this.tuijianuid = tuijianuid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public boolean isOnce() {
		return isOnce;
	}

	public void setOnce(boolean isOnce) {
		this.isOnce = isOnce;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public static User parse(JSONObject json) throws IOException, AppException {
		User user = new User();
		JSONObject userinfo = json;
		user.setId(StringUtils.toInt(userinfo.optString("id")));
		user.setNickname(userinfo.optString("nickname"));
		user.setHead(userinfo.optString("head"));
		user.setRealname_status(StringUtils.toInt(userinfo
				.optString("realname_status")));
		user.setInvitation(StringUtils.toInt(userinfo.optString("invitation")));
		user.setQnum(StringUtils.toInt(userinfo.optString("qnum")));
		user.setAnum(StringUtils.toInt(userinfo.optString("anum")));
		user.setCnum(StringUtils.toInt(userinfo.optString("cnum")));
		user.setFnum(StringUtils.toInt(userinfo.optString("fnum")));
		user.setGnum(StringUtils.toInt(userinfo.optString("gnum")));
		user.setIntegral(StringUtils.toInt(userinfo.optString("integral")));
		user.setRank(StringUtils.toInt(userinfo.optString("rank")));

		user.setSex(StringUtils.toInt(userinfo.optString("sex")));
		user.setPhone_status(StringUtils.toInt(userinfo
				.optString("phone_status")));
		user.setIsgetpush(StringUtils.toInt(userinfo.optString("isgetpush")));
		user.setIsgethelp(StringUtils.toInt(userinfo.optString("isgethelp")));
		user.setIsadopt(StringUtils.toInt(userinfo.optString("isadopt")));
		user.setIscomment(StringUtils.toInt(userinfo.optString("iscomment")));
		user.setIsafter(StringUtils.toInt(userinfo.optString("isafter")));
		user.setStatus(StringUtils.toInt(userinfo.optString("status")));
		user.setTuijianuid(StringUtils.toInt(userinfo.optString("tuijianuid")));
		user.setType(StringUtils.toInt(userinfo.optString("type")));
		user.setCompany(userinfo.optString("company"));
		user.setInterest(userinfo.optString("interest"));
		user.setIsattention(userinfo.optInt("isattention"));
		user.setFansnum(userinfo.optInt("fansnum"));
		user.setSupportednum(userinfo.optInt("supportednum"));
		user.setIssigned(userinfo.optBoolean("issigned"));
		user.setInfo(userinfo.optString("info"));

		return user;
	}
}
