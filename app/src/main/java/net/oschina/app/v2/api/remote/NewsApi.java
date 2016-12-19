package net.oschina.app.v2.api.remote;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.user.model.Intersted;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.model.Post;
import net.oschina.app.v2.model.Report;
import net.oschina.app.v2.model.Tweet;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class NewsApi extends BaseApi {
	//

	/**
	 * 获取支持列表
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */

	public static void subShiMingRenZhen(int uid, String realname,
			String danwei_info, String zhiwu_info, String shouji_info,
			String email_info, String tel, String renzhengxinxi_info, int type,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("uid", uid);
			jsonObject.put("realname", realname);
			jsonObject.put("company", danwei_info);
			jsonObject.put("position", zhiwu_info);
			jsonObject.put("email", email_info);
			jsonObject.put("tel", tel);
			jsonObject.put("info", renzhengxinxi_info);
			jsonObject.put("type", type);
		} catch (Exception e) {
		}
		ApiHttpClient.post("index.php/Api/Member/realname.html", jsonObject,
				handler);
	}

	/**
	 * 支持者列表
	 * 
	 * @param uid
	 * @param aid
	 * @param handler
	 */
	public static void getSupportList(int uid, int aid,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("aid", aid);
		} catch (Exception e) {
		}
		ApiHttpClient.post("index.php/Api/Answer/supportlist.html", jsonObject,
				handler);
	}

	/**
	 * 获取数目
	 * 
	 * @param uid
	 * @param handler
	 */
	public static void getActiveNum(int uid, JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("time", (System.currentTimeMillis() / 1000) + "");
		} catch (Exception e) {
		}
		ApiHttpClient.post("index.php/Api/Member/getnum.html", jsonObject,
				handler);
	}
	
	/**
	 * 更新时间
	 * 
	 * @param uid
	 * @param handler
	 */
	public static void updateTime(int uid, int type,JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("type", type);
		} catch (Exception e) {
		}
		ApiHttpClient.post("index.php/Api/Member/updatetime.html", jsonObject,
				handler);
	}
	

	/**
	 * 用户关注
	 * 
	 * @param uid
	 * @param tuid
	 * @param handler
	 */
	public static void addAttention(int uid, int tuid,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("tuid", tuid);
		} catch (Exception e) {
		}
		ApiHttpClient.post("index.php/Api/Member/attention.html", jsonObject,
				handler);
	}

	/**
	 * 取消用户关注
	 * 
	 * @param uid
	 * @param tuid
	 * @param handler
	 */
	public static void unAttention(int uid, int tuid,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("tuid", tuid);
		} catch (Exception e) {
		}
		ApiHttpClient.post("index.php/Api/Member/unattention.html", jsonObject,
				handler);
	}

	/**
	 * 获取高手列表
	 * 
	 * @param uid
	 * @param handler
	 */
	public static void getSupperlist(int uid,String keyword,JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("keyword", keyword);
		} catch (Exception e) {
		}
		ApiHttpClient.post("index.php/Api/Question/supperlist.html",
				jsonObject, handler);
	}

	public static void getQuestionnum(int uid, JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("uid", uid);
		} catch (Exception e) {
		}
		ApiHttpClient.post("index.php/Api/Tongji/getquestionnum.html",
				jsonObject, handler);
	}

	public static void getAnswernum(int uid, JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("uid", uid);
		} catch (Exception e) {
		}
		ApiHttpClient.post("index.php/Api/Tongji/getanswernum.html",
				jsonObject, handler);
	}

	/**
	 * 获取支持列表
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void getZhiChiList(int uid, int aid,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("uid", uid);
			jsonObject.put("aid", aid);
		} catch (Exception e) {
			// TODO: handle exception
		}
		ApiHttpClient.post("index.php/Api/Answer/supportlist.html", jsonObject,
				handler);
	}

	/**
	 * 添加支持
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void setCommentZhiChi(int uid, int aid,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("uid", uid);
			jsonObject.put("aid", aid);
		} catch (Exception e) {
			// TODO: handle exception
		}
		ApiHttpClient.post("index.php/Api/Answer/support.html", jsonObject,
				handler);
	}

	/**
	 * 搜索详情页
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void getSearchList(String content, int pageIndex,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("pid", pageIndex);
			jsonObject.put("num", 30);
			jsonObject.put("keyword", content);
		} catch (Exception e) {
			// TODO: handle exception
		}
		ApiHttpClient.post("index.php/Api/Question/search.html", jsonObject,
				handler);

		// ApiHttpClient.post("index.php/Api/Found/search.html",
		// jsonObject,handler);
	}

	//type   1 用户(助手ID/用户名) 2 文章  3 问题
	public static void getSearchList(String content,int userId,int type, int pageIndex, JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("pid", pageIndex);
			jsonObject.put("uid", userId);
			jsonObject.put("num", 30);
			jsonObject.put("type", type);
			jsonObject.put("keyword", content);
		} catch (Exception e) {
			// TODO: handle exception
		}
		ApiHttpClient.post("index.php/Api/Index/search.html", jsonObject,
				handler);

		// ApiHttpClient.post("index.php/Api/Found/search.html",
		// jsonObject,handler);
	}

	//收藏/取消收藏文章 flag 1收藏 0取消
	public static void collectArticle(int aid,int uid,int flag,int type, JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("aid", aid);
			jsonObject.put("uid", uid);
			jsonObject.put("flag", flag);
			jsonObject.put("type", type);
		} catch (Exception e) {

		}
		ApiHttpClient.post("index.php/Api/Collect/article.html", jsonObject, handler);
	}

	/***
	 * 签到
	 * @param aid
	 * @param uid
	 * @param flag
	 * @param handler
	 */
	public static void signIn(int uid,JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
		} catch (Exception e) {

		}
		ApiHttpClient.post("index.php/Api/Member/signin.html", jsonObject, handler);
	}

	/***
	 * 签到提醒接口
	 * @param uid
	 * @param remind//0 取消提醒 1提醒
	 * @param handler
	 */
	public static void signRemind(int uid,int remind,JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("signremind", remind);
		} catch (Exception e) {

		}
		ApiHttpClient.post("index.php/Api/member/signremind.html", jsonObject, handler);
	}

	//收藏/取消收藏提问 flag 1收藏 0取消
	public static void collectQuestion(int qid,int uid,int flag, JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("qid", qid);
			jsonObject.put("uid", uid);
			jsonObject.put("flag", flag);
		} catch (Exception e) {

		}
		ApiHttpClient.post("index.php/Api/Collect/question.html", jsonObject, handler);
	}

	/**
	 * 修改助手号详情
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void getAssistantInfo(int uid, JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("uid", uid);
		} catch (Exception e) {
			// TODO: handle exception
		}
		ApiHttpClient.post("index.php/Api/Member/assistantinfo.html",
				jsonObject, handler);
	}

	/**
	 * 修改昵称
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void subEditNickname(int uid, String content,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {

			jsonObject.put("uid", uid);
			jsonObject.put("new_nickname", content);
		} catch (Exception e) {
			// TODO: handle exception
		}
		ApiHttpClient.post("index.php/Api/Member/changenickname.html",
				jsonObject, handler);
	}

	/**
	 * 绑定手机号
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void subBangDingShouJi(int uid, String phone,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {

			jsonObject.put("uid", uid);
			jsonObject.put("phone", phone);
		} catch (Exception e) {
			// TODO: handle exception
		}
		ApiHttpClient.post("index.php/Api/Member/phone.html", jsonObject,
				handler);
	}

	public static void userAttention(int uid, int aid,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {

			jsonObject.put("uid", uid);
			jsonObject.put("aid", aid);
		} catch (Exception e) {
			// TODO: handle exception
		}
		ApiHttpClient.post("/index.php/Api/Answer/adopt.html", jsonObject,
				handler);
	}

	/**
	 * 修改昵称
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void subEditPassword(int uid, String old_password,
			String new_password, JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("uid", uid);
			jsonObject.put("old_password", old_password);
			jsonObject.put("new_password", new_password);
		} catch (Exception e) {
			// TODO: handle exception
		}
		ApiHttpClient.post("index.php/Api/Member/changepassword.html",
				jsonObject, handler);
	}

	/**
	 * 问题反馈
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void subFeedback(int uid, String content,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {

			jsonObject.put("uid", uid);
			jsonObject.put("content", content);
		} catch (Exception e) {
		}
		ApiHttpClient.post("index.php/Api/Member/feedback.html",
				jsonObject, handler);
	}

	/**
	 * 获取新闻列表
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void getBrandList(int uid, int page,

	JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("uid", uid);
			jsonObject.put("pid", page);
			jsonObject.put("num", 30);
		} catch (Exception e) {
			// TODO: handle exception
		}

		ApiHttpClient.post("index.php/Api/Found/brands.html", jsonObject,
				handler);
	}

	/**
	 * 获取新闻列表
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void getBrandList(int uid, String searchTxt, int page,

	JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("uid", uid);
			jsonObject.put("keyword", searchTxt);
			jsonObject.put("pid", page);
			jsonObject.put("num", 30);
		} catch (Exception e) {
			// TODO: handle exception
		}

		ApiHttpClient.post("index.php/Api/Found/searchbrands.html", jsonObject,
				handler);
	}

	public static void getAttentionList(int uid, JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("uid", uid);
		} catch (Exception e) {
		}

		ApiHttpClient.post("index.php/Api/Member/attentionlist.html",
				jsonObject, handler);
	}
	/**
	 * 我关注的 分页
	 * 
	 */
	public static void getAttentionList(int uid, int page,JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("uid", uid);
			jsonObject.put("pid", page);
			jsonObject.put("num", 30);
		} catch (Exception e) {
		}

		ApiHttpClient.post("index.php/Api/Member/attentionpage.html",
				jsonObject, handler);
	}

	/**
	 * 我关注的 分页
	 *
	 */
	public static void getCollectQuestionList(int uid, int page,JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("uid", uid);
			jsonObject.put("pid", page);
			jsonObject.put("num", 30);
		} catch (Exception e) {
		}

		ApiHttpClient.post("index.php/Api/Collect/questionlist.html", jsonObject, handler);
	}

	public static void getFansList(int uid, JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("uid", uid);
		} catch (Exception e) {
		}

		ApiHttpClient.post("index.php/Api/Member/fans.html", jsonObject,
				handler);
	}
	
	/**
	 * 关注我的 分页
	 * 
	 */
	public static void getFansList(int uid, int page,JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("uid", uid);
			jsonObject.put("pid", page);
			jsonObject.put("num", 30);
		} catch (Exception e) {
		}

		ApiHttpClient.post("index.php/Api/Member/fanspage.html", jsonObject,
				handler);
	}

	/**
	 * 关注我的 分页
	 *
	 */
	public static void getCollectArticleList(int uid, int page,JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("pid", page);
			jsonObject.put("num", 30);
		} catch (Exception e) {
		}

		ApiHttpClient.post("index.php/Api/Collect/articlelist.html", jsonObject, handler);
	}

	/**
	 * 获取新闻列表
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void getDailyList(int uid, int page,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("uid", uid);
			jsonObject.put("pid", page);
			jsonObject.put("num", 300);
		} catch (Exception e) {
			// TODO: handle exception
		}

		ApiHttpClient.post("index.php/Api/Found/daily.html", jsonObject,
				handler);
	}
	
	/**
	 * 分享到实验拳
	 * 
	 * @param uid
	 * 
	 * @param handler
	 */
	public static void shareToCircle(int uid, int id,int type,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("uid", uid);
			jsonObject.put("id", id);
			jsonObject.put("type", type);
		} catch (Exception e) {
			// TODO: handle exception
		}

		ApiHttpClient.post("index.php/Api/Found/sharetocircle.html", jsonObject,
				handler);
	}
	
	/**
	 * 分享统计
	 * 
	 * @param uid
	 * 
	 * @param handler
	 */
	public static void shareCount(int uid, int id,int type,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("uid", uid);
			jsonObject.put("id", id);
			jsonObject.put("type", type);
		} catch (Exception e) {
			// TODO: handle exception
		}

		ApiHttpClient.post("index.php/Api/Found/sharecount.html", jsonObject,
				handler);
	}

	public static void getSubmittask(int uid, String type,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("uid", uid);
			jsonObject.put("type", type);
		} catch (Exception e) {
		}

		ApiHttpClient.post("/index.php/Api/Member/submittask.html", jsonObject,
				handler);
	}

	/**
	 * 获取本周排行
	 * 
	 * @param uid
	 * @param page
	 * @param type
	 *            1 :正解，2:回答，3:助手
	 * @param handler
	 */
	public static void getWeekRankList(int uid, int page, int type,
			JsonHttpResponseHandler handler) {

		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("uid", uid);
			jsonObject.put("type", 1);
		} catch (Exception e) {
		}

		if (1 == type) {
			ApiHttpClient.post("index.php/Api/Tongji/getadoptranklist.html",
					jsonObject, handler);

		} else if (2 == type) {
			ApiHttpClient.post("index.php/Api/Tongji/getanswerranklist.html",
					jsonObject, handler);

		} else if (3 == type) {
			ApiHttpClient.post("index.php/Api/Tongji/invitationranklist.html",
					jsonObject, handler);
		}
	}

	/**
	 * 获取总排行
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void getTotalRankList(int uid, int page, int type,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("uid", uid);
			jsonObject.put("type", 0);

		} catch (Exception e) {
		}

		if (1 == type) {
			ApiHttpClient.post("index.php/Api/Tongji/getadoptranklist.html",
					jsonObject, handler);

		} else if (2 == type) {
			ApiHttpClient.post("index.php/Api/Tongji/getanswerranklist.html",
					jsonObject, handler);

		} else if (3 == type) {
			ApiHttpClient.post("index.php/Api/Tongji/invitationranklist.html",
					jsonObject, handler);
		}

	}

	/**
	 * 获取活动中心
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void getActivityCenterList(int uid, int page,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("pid", page);
			jsonObject.put("num", 30);
			/* jsonObject.put("uid", 1); */
		} catch (Exception e) {
		}

		// index.php/Api/Found/party.html
		ApiHttpClient.post("index.php/Api/Found/party.html", jsonObject,
				handler);
	}

	/**
	 * 培训信息
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void getTrainList(int uid, int page,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("pid", page);
			jsonObject.put("num", 30);
			/* jsonObject.put("uid", 1); */
		} catch (Exception e) {
		}

		// index.php/Api/Found/party.html
		ApiHttpClient.post("index.php/Api/Found/train.html", jsonObject,
				handler);
	}

	/**
	 * 获取 实验周刊列表
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void getWeekList(int uid, int page,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("uid", uid);
			jsonObject.put("pid", page);
			jsonObject.put("num", 20);
		} catch (Exception e) {
			// TODO: handle exception
		}

		ApiHttpClient.post("index.php/Api/Found/weekly.html", jsonObject,
				handler);
	}

	/**
	 * 获取法律文档
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void getLawsList(int uid, int page,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject1 = new JSONObject();

		try {
			jsonObject1.put("uid", uid);
			jsonObject1.put("pid", page);
			jsonObject1.put("num", 30);
		} catch (Exception e) {
			// TODO: handle exception
		}

		ApiHttpClient.post("index.php/Api/Found/documents.html", jsonObject1,
				handler);
	}
	
	/**
	 * 搜索法律文档
	 * 
	 */
	public static void searchLawsList( int page,String searchTxt,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject1 = new JSONObject();

		try {
			jsonObject1.put("pid", page);
			jsonObject1.put("num", 30);
			jsonObject1.put("keyword", searchTxt);
			
		} catch (Exception e) { 
			
		}

		ApiHttpClient.post("index.php/Api/Found/searchdocuments.html", jsonObject1,
				handler);
	}
	
	

	public static void getQustionList(int uid, int page,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("pid", page);
			jsonObject.put("num", 30);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Member/myquestionlist.html",
				jsonObject, handler);
	}
	public static void getPersonalQustionList(int uid, int page,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("pid", page);
			jsonObject.put("num", 30);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Member/personalquestion.html",
				jsonObject, handler);
	}

	public static void getHomeAdList(int id, JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("catid", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Ad/getad.html", jsonObject, handler);
	}

	/**
	 * 获取首页列表
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void getHomeList(int uid, int page,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("pid", page);
			jsonObject.put("num", 30);
			jsonObject.put("uid", uid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/index/experimentalring.html",
				jsonObject, handler);
	}

	/**
	 * 获取等你答列表
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void getAskList(int page, JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("pid", page);
			jsonObject.put("num", 20);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Question/questionlist.html",
				jsonObject, handler);
	}
	
	/**
	 * 根据问题ID获取问题
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void getAskById(int qid, JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			int uid=AppContext.instance().getLoginUid();
			jsonObject.put("id", qid);
			jsonObject.put("uid", uid);
			/*jsonObject.put("pid", 1);
			jsonObject.put("num", 30);*/
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//questionlist.html
		ApiHttpClient.post("index.php/Api/Question/getquestionbyid.html",
				jsonObject, handler);
	}

	/**
	 * 获取等你答列表粉丝求助
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void getFansAskList(int uid, int page,int status,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("pid", page);
			jsonObject.put("status", status);
			jsonObject.put("num", 20);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// ApiHttpClient.post("index.php/Api/Member/fansquestionlist.html",
		// jsonObject, handler);
		ApiHttpClient.post("index.php/Api/Member/fansquestionlist.html",
				jsonObject, handler);
	}

	/**
	 * 获取等你答列表
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void getCommentList(int qid, int page,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			//回答列表，返回已读未读 add by wuyue
			int uid=AppContext.instance().getLoginUid();
			jsonObject.put("uid", uid);
			jsonObject.put("pid", page);
			jsonObject.put("qid", qid);
			jsonObject.put("num", 20);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Answer/answerlist.html", jsonObject,
				handler);
	}

	/**
	 * 获取回复追问列表
	 * 
	 * @param answerId
	 *            回复id
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void getCommentReplyList(int answerId, int page,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("aid", answerId);
			jsonObject.put("pid", page);
			jsonObject.put("num", 300);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Answer/answerafterlist.html",
				jsonObject, handler);
	}

	/**
	 * 获取回复追问列表
	 * 
	 * @param answerId
	 *            回复id
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void getCommentReplyList(int answerId, int page, int num,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("aid", answerId);
			jsonObject.put("pid", page);
			jsonObject.put("num", num);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Answer/gethreefter.html",
				jsonObject, handler);
	}

	/**
	 * 获取获取目录
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void getLanmu(int id, JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			if (id != -1)
				jsonObject.put("id", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Question/categorylist.html",
				jsonObject, handler);
	}

	/**
	 * 筛选数据
	 * 
	 * @param id
	 * @param handler
	 */
	public static void getFilterList(int page, String catid, int isreward,
			int issolveed, int type, JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("pid", page);
			jsonObject.put("num", 20);//300
			if(!catid.contains("-1")&&!TextUtils.isEmpty(catid)){
				jsonObject.put("catid", catid);
			}
			jsonObject.put("isreward", isreward);
			jsonObject.put("issolveed", issolveed);
			jsonObject.put("type", type);
			if(type==2){
				jsonObject.put("istop", 1);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("/index.php/Api/Question/select.html", jsonObject,
				handler);
	}

	/**
	 * 举报问题
	 * 
	 * @param id
	 * @param handler
	 */
	public static void reportQuestion(int uid, int qid,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("qid", qid);
			jsonObject.put("info", "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("/index.php/Api/Question/report.html", jsonObject,
				handler);
	}
	
	/**
	 * 举报问题
	 * 
	 * @param id
	 * @param handler
	 */
	public static void reportAnswer(int uid, int aid,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("aid", aid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("/index.php/Api/Answer/report.html", jsonObject,
				handler);
	}

	/**
	 * 删除答复列表问题
	 *
	 * @param id
	 * @param handler
	 */
	public static void delectAnswer(int uid, int aid,
									JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("aid", aid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("/index.php/Api/Answer/answerdel.html", jsonObject,
				handler);
	}
	/**
	 * 删除自己提问的问题
	 *
	 * @param id
	 * @param handler
	 */
	public static void delectQuestion(int uid, int qid,
									JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("qid", qid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("/index.php/Api/Question/questiondel.html", jsonObject,
				handler);
	}


	/**
	 * 获取用户信息
	 * 
	 * @param uid
	 * @param handler
	 */
	public static void getUserInfo(int uid,int tuid, int isme,JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("tuid", tuid);
			jsonObject.put("isme", isme);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("/index.php/Api/Member/memberinfo.html", jsonObject,
				handler);
	}

	/**
	 * 获取提交提问
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void submitQuestion(int uid, int catid, String label,
			String content, int reward, String superlist, String image,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		JSONObject labels = new JSONObject();
		try {

			labels.put(String.valueOf(catid), label);
			jsonObject.put("uid", uid);
			jsonObject.put("catid", catid);
			jsonObject.put("lable", labels.toString());
			jsonObject.put("content", content);
			jsonObject.put("reward", reward);
			jsonObject.put("superlist", superlist);
			jsonObject.put("image", image);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Question/questionadd.html",
				jsonObject, handler);
	}
	
	/**
	 * 获取用户积分
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void refreshScore(int uid,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
	
		try {
			jsonObject.put("uid", uid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Member/getintegral.html",
				jsonObject, handler);
	}

	

	/**
	 * 获取注册
	 * 
	 * @param catalog
	 *            类别 （1，2，3）
	 * @param page
	 *            第几页
	 * @param handler
	 */
	public static void registerUser(String cid, String nickname,
			String password, String verify, int sex, String phone, int tuijian,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("cid", cid);
			jsonObject.put("nickname", nickname);
			jsonObject.put("password", password);
			jsonObject.put("telverify", verify);
			jsonObject.put("sex", sex);
			jsonObject.put("phone", phone);
			jsonObject.put("tuijian", tuijian);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient
				.post("index.php/Api/Member/reg.html", jsonObject, handler);
	}

	// NewsApi.registerUser("58f8643d1093fd2c75544d8c4589f6bb",nickname,password,verify,1,phone,tuijian);

	public static void subComment(int id, int uid, boolean isZhuiwen,
			String content, boolean relation, String superlist,
			JsonHttpResponseHandler handler) {
		subComment(id, uid, isZhuiwen, 0, content, relation, superlist, handler);
	}
	
	public static void subComment(int id, int uid, boolean isZhuiwen,int aid,
			String content, boolean relation, String superlist,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			int type = isZhuiwen ? 1 : 0;
			int isaskhigh = relation ? 1 : 2;
			jsonObject.put("aid", aid);
			jsonObject.put("id", id);
			jsonObject.put("uid", uid);
			jsonObject.put("type", type);
			jsonObject.put("content", content);
			jsonObject.put("isaskhigh", isaskhigh);
			jsonObject.put("superlist", superlist);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Answer/answeradd.html", jsonObject,
				handler);
	}

	public static void subComment(int id, int uid, boolean isZhuiwen,
			String content, String imagePath, boolean relation,
			String superlist, JsonHttpResponseHandler handler) {
		subComment(id, uid, isZhuiwen, 0, content, imagePath, relation, superlist, handler);
	}
	
	public static void subComment(int id, int uid, boolean isZhuiwen,int aid,
			String content, String imagePath, boolean relation,
			String superlist, JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			int type = isZhuiwen ? 1 : 0;
			int isaskhigh = relation ? 1 : 2;
			jsonObject.put("aid", aid);
			jsonObject.put("id", id);
			jsonObject.put("uid", uid);
			jsonObject.put("type", type);
			jsonObject.put("content", content);
			jsonObject.put("image", imagePath);
			jsonObject.put("isaskhigh", isaskhigh);
			jsonObject.put("superlist", superlist);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Answer/answeradd.html", jsonObject,
				handler);
	}

	public static void addCommentAfter(int qid, int askid, int uid, int aid,int answerID,
			String content, JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("qid", qid);
			jsonObject.put("askid", askid);
			jsonObject.put("uid", uid);
			jsonObject.put("aid", aid);
			jsonObject.put("answerID", answerID);
			jsonObject.put("content", content);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("/index.php/Api/Answer/answerafter.html",
				jsonObject, handler);

	}

	public static void addCommentAfter(int qid, int askid, int uid, int aid,int answerID,
			String content, String urlPath, JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("qid", qid);
			jsonObject.put("askid", askid);
			jsonObject.put("uid", uid);
			jsonObject.put("aid", aid);
			jsonObject.put("answerID", answerID);
			jsonObject.put("content", content);
			jsonObject.put("image", urlPath);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("/index.php/Api/Answer/answerafter.html",
				jsonObject, handler);

	}

	public static void getChatRecord(int aid, int page,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("aid", aid);
			jsonObject.put("pid", page);
			jsonObject.put("num", 300);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("/index.php/Api/Answer/answerlog.html", jsonObject,
				handler);

	}

	/**
	 * 礼品兑换
	 * 
	 * @param uid
	 * @param proid
	 * @param name
	 * @param phone
	 * @param address
	 * @param email
	 * @param company
	 * @param handler
	 */
	public static void recharge(int uid, int proid, String name, String phone,
			String address, String email, String company,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("uid", uid);
			jsonObject.put("proid", proid);
			jsonObject.put("name", name);
			jsonObject.put("phone", phone);
			jsonObject.put("address", address);
			jsonObject.put("email", email);
			jsonObject.put("company", company);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("/index.php/Api/Product/recharge.html", jsonObject,
				handler);
	}

	/**
	 * 获取问卷
	 * 
	 * @param newsId
	 * @param handler
	 *//*
	public static void getQuestionnairelist(int catid,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("catid", catid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("/index.php/Api/Found/show.html",
				jsonObject, handler);
	}*/
	
	/**
	 * 获取问卷
	 * 
	 * @param newsId
	 * @param handler
	 */
	public static void getQuestionnairelist(int uid,int id,int type,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("id", id);
			jsonObject.put("type", type);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("/index.php/Api/Found/show.html",
				jsonObject, handler);
	}

	/**
	 * 添加问卷
	 * 
	 * @param newsId
	 * @param handler
	 */
	public static void addQuestionnairelist(int uid, int qnid, String options,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("qnid", qnid);
			jsonObject.put("options", options);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("/index.php/Api/Found/questionnaireadd.html",
				jsonObject, handler);
	}

	public static void checkUpdate(JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("type", 1);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("/index.php/Api/Member/getversion.html", jsonObject,
				handler);
	}

	public static void updateUserPhoto(int uid, String path,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("head", path);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("/index.php/Api/Member/changehead.html", jsonObject,
				handler);
	}

	/**
	 * 上传图像
	 * 
	 * @param imagePath
	 * @param handler
	 */
	public static void updateImage(String imagePath,
			JsonHttpResponseHandler handler) {
		try {
			RequestParams params = new RequestParams();
			params.put("fileField", new File(imagePath));
			ApiHttpClient.post("index.php/api/member/uploadimg", params,
					handler);
		} catch (IOException e) {
		}
	}

	public static void uploadImage(int type, String imagePath, Handler handler) {
		new Thread(new UpLoadFileThread(type, imagePath, handler)).start();
	}

	/**
	 * 上传图片
	 * 
	 * @param type
	 * @param imagePath
	 * @param handler
	 */
	static class UpLoadFileThread implements Runnable {
		int type;
		String imagePath;
		Handler handler;

		public UpLoadFileThread(int type, String imagePath, Handler handler) {
			this.type = type;
			this.imagePath = imagePath;
			this.handler = handler;
		}

		@Override
		public void run() {
			URL u = null;
			HttpURLConnection con = null;
			FileInputStream fis = null;
			OutputStream outStream = null;
			BufferedReader bufferedReader = null;
			try {
				fis = new FileInputStream(new File(imagePath));
				byte[] buffer = new byte[fis.available()];
				fis.read(buffer);

				u = new URL(
						ApiHttpClient
								.getAbsoluteApiUrl("index.php/api/member/uploadimg"));
				con = (HttpURLConnection) u.openConnection();
				con.setRequestMethod("POST");	// 请求方式			
				con.setDoInput(true); // 允许输入流
				con.setDoOutput(true); // 允许输出流
				con.setUseCaches(false); // 不允许使用缓存
	            
				con.setRequestProperty("Content-Type", "binary/octet-stream");
				con.setRequestProperty("type", String.valueOf(type));
				outStream = con.getOutputStream();
				outStream.write(buffer);
				outStream.flush();
				if (con.getResponseCode() == 200) {
					bufferedReader = new BufferedReader(new InputStreamReader(
							con.getInputStream()));
					String returnStr = bufferedReader.readLine();
					Message msg = new Message();
					msg.what = 1;
					msg.getData().putString("return", returnStr);
					handler.sendMessage(msg);
				} else {
					handler.sendEmptyMessage(2);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if(fis!=null){
						fis.close();
					}
					if(outStream!=null){
						outStream.close();
					}
					if(con!=null){
						con.disconnect();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// ///////////////////
	public static void getNewsList(int catalog, int page,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("catalog", catalog);
		params.put("pageIndex", page);
		params.put("pageSize", DEF_PAGE_SIZE);
		params.put("dataType", "json");
		ApiHttpClient.get("action/api/news_list", params, handler);
	}

	public static void getBlogList(String type, int pageIndex,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("type", type);
		params.put("pageIndex", pageIndex);
		params.put("pageSize", DEF_PAGE_SIZE);
		ApiHttpClient.get("action/api/blog_list", params, handler);
	}

	public static void getPostList(int catalog, int page,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("catalog", catalog);
		params.put("pageIndex", page);
		params.put("pageSize", DEF_PAGE_SIZE);
		params.put("dataType", "json");
		ApiHttpClient.get("action/api/post_list", params, handler);
	}

	public static void getPostListByTag(String tag, int page,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("tag", tag);
		params.put("pageIndex", page);
		params.put("pageSize", DEF_PAGE_SIZE);
		ApiHttpClient.get("action/api/post_list", params, handler);
	}

	public static void getTweetList(int uid, int page,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		// params.put("access_token", "");
		params.put("uid", uid);
		params.put("pageIndex", page);
		params.put("pageSize", DEF_PAGE_SIZE);
		// params.put("dataType", "json");
		ApiHttpClient.get("action/api/tweet_list", params, handler);
	}

	public static void getActiveList(int uid, int catalog, int page,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("catalog", catalog);
		params.put("pageIndex", page);
		params.put("pageSize", DEF_PAGE_SIZE);
		ApiHttpClient.get("action/api/active_list", params, handler);
	}

	public static void getFriendList(int uid, int relation, int page,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("relation", relation);
		params.put("pageIndex", page);
		params.put("pageSize", DEF_PAGE_SIZE);
		ApiHttpClient.get("action/api/friends_list", params, handler);
	}

	public static void getFavoriteList(int uid, int type, int page,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("type", type);
		params.put("pageIndex", page);
		params.put("pageSize", DEF_PAGE_SIZE);
		ApiHttpClient.get("action/api/favorite_list", params, handler);
	}

	public static void getSoftwareCatalogList(int tag,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams("tag", tag);
		ApiHttpClient.get("action/api/softwarecatalog_list", params, handler);
	}

	public static void getSoftwareTagList(int searchTag, int page,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("searchTag", searchTag);
		params.put("pageIndex", page);
		params.put("pageSize", DEF_PAGE_SIZE);
		ApiHttpClient.get("action/api/softwaretag_list", params, handler);
	}

	public static void getSoftwareList(String searchTag, int page,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("searchTag", searchTag);
		params.put("pageIndex", page);
		params.put("pageSize", DEF_PAGE_SIZE);
		ApiHttpClient.get("action/api/software_list", params, handler);
	}

	/**
	 * 获取评论列表
	 * 
	 * @param id
	 * @param catalog
	 *            1新闻 2帖子 3动弹 4动态
	 * @param page
	 * @param handler
	 */
	public static void getCommentList(int id, int catalog, int page,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("catalog", catalog);
		params.put("id", id);
		params.put("pageIndex", page);
		params.put("pageSize", DEF_PAGE_SIZE);
		ApiHttpClient.get("action/api/comment_list", params, handler);
	}

	public static void getBlogCommentList(int id, int page,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("id", id);
		params.put("pageIndex", page);
		params.put("pageSize", DEF_PAGE_SIZE);
		ApiHttpClient.get("action/api/blogcomment_list", params, handler);
	}

	public static void getUserInformation(int uid, int hisuid, String hisname,
			int pageIndex, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("hisuid", hisuid);
		params.put("hisname", hisname);
		params.put("pageIndex", pageIndex);
		params.put("pageSize", DEF_PAGE_SIZE);
		ApiHttpClient.get("action/api/user_information", params, handler);
	}

	@SuppressWarnings("deprecation")
	public static void getUserBlogList(int authoruid, final String authorname,
			final int uid, final int pageIndex, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("authoruid", authoruid);
		params.put("authorname", URLEncoder.encode(authorname));
		params.put("uid", uid);
		params.put("pageIndex", pageIndex);
		params.put("pageSize", DEF_PAGE_SIZE);
		ApiHttpClient.get("action/api/userblog_list", params, handler);
	}

	public static void updateRelation(int uid, int hisuid, int newrelation,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("hisuid", hisuid);
		params.put("newrelation", newrelation);
		ApiHttpClient.post("action/api/user_updaterelation", params, handler);
	}

	public static void getMyInformation(int uid,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		ApiHttpClient.post("action/api/my_information", params, handler);
	}


	public static void getBlogDetail(int id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams("id", id);
		ApiHttpClient.get("action/api/blog_detail", params, handler);
	}

	public static void getSoftwareDetail(String ident,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams("ident", ident);
		ApiHttpClient.get("action/api/software_detail", params, handler);
	}

	public static void getPostDetail(int id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams("id", id);
		ApiHttpClient.get("action/api/post_detail", params, handler);
	}

	public static void getTweetDetail(int id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams("id", id);
		ApiHttpClient.get("action/api/tweet_detail", params, handler);
	}

	public static void publicComment(int catalog, int id, int uid,
			String content, int isPostToMyZone, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("catalog", catalog);
		params.put("id", id);
		params.put("uid", uid);
		params.put("content", content);
		params.put("isPostToMyZone", isPostToMyZone);
		ApiHttpClient.post("action/api/comment_pub", params, handler);
	}

	public static void replyComment(int id, int catalog, int replyid,
			int authorid, int uid, String content,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("catalog", catalog);
		params.put("id", id);
		params.put("uid", uid);
		params.put("content", content);
		params.put("replyid", replyid);
		params.put("authorid", authorid);
		ApiHttpClient.post("action/api/comment_reply", params, handler);
	}

	public static void publicBlogComment(int blog, int uid, String content,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("blog", blog);
		params.put("uid", uid);
		params.put("content", content);
		ApiHttpClient.post("action/api/blogcomment_pub", params, handler);
	}

	public static void replyBlogComment(int blog, int uid, String content,
			int reply_id, int objuid, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("blog", blog);
		params.put("uid", uid);
		params.put("content", content);
		params.put("reply_id", reply_id);
		params.put("objuid", objuid);
		ApiHttpClient.post("action/api/blogcomment_pub", params, handler);
	}

	public static void publicPost(Post post, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", post.getAuthorId());
		params.put("title", post.getTitle());
		params.put("catalog", post.getCatalog());
		params.put("content", post.getBody());
		params.put("isNoticeMe", post.getIsNoticeMe());
		ApiHttpClient.post("action/api/post_pub", params, handler);
	}

	public static void publicTweet(Tweet tweet, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", tweet.getAuthorId());
		params.put("msg", tweet.getBody());

		// Map<String, File> files = new HashMap<String, File>();
		if (!TextUtils.isEmpty(tweet.getImageFilePath())) {
			try {
				params.put("img", new File(tweet.getImageFilePath()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		// if (tweet.getAmrFile() != null)
		// files.put("amr", tweet.getAmrFile());
		// params.put("img", file)l
		ApiHttpClient.post("action/api/tweet_pub", params, handler);
	}

	public static void deleteTweet(int uid, int tweetid,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("tweetid", tweetid);
		ApiHttpClient.post("action/api/tweet_delete", params, handler);
	}

	public static void deleteComment(int id, int catalog, int replyid,
			int authorid, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("id", id);
		params.put("catalog", catalog);
		params.put("replyid", replyid);
		params.put("authorid", authorid);
		ApiHttpClient.post("action/api/comment_delete", params, handler);
	}

	public static void deleteBlog(int uid, int authoruid, int id,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("authoruid", authoruid);
		params.put("id", id);
		ApiHttpClient.post("action/api/userblog_delete", params, handler);
	}

	public static void deleteBlogComment(int uid, int blogid, int replyid,
			int authorid, int owneruid, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("blogid", blogid);
		params.put("replyid", replyid);
		params.put("authorid", authorid);
		params.put("owneruid", owneruid);
		ApiHttpClient.post("action/api/blogcomment_delete", params, handler);
	}

	/**
	 * 用户添加收藏
	 * 
	 * @param uid
	 *            用户UID
	 * @param objid
	 *            比如是新闻ID 或者问答ID 或者动弹ID
	 * @param type
	 *            1:软件 2:话题 3:博客 4:新闻 5:代码
	 */
	public static void addFavorite(int uid, int objid, int type,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("objid", objid);
		params.put("type", type);
		ApiHttpClient.post("action/api/favorite_add", params, handler);
	}

	public static void delFavorite(int uid, int objid, int type,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("objid", objid);
		params.put("type", type);
		ApiHttpClient.post("action/api/favorite_delete", params, handler);
	}

	public static void report(Report report, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("obj_id", report.getReportId());
		params.put("url", report.getLinkAddress());
		params.put("obj_type", report.getReason());
		if (!TextUtils.isEmpty(report.getOtherReason())) {
			params.put("memo", report.getOtherReason());
		} else {
			params.put("memo", "其他原因");
		}
		ApiHttpClient.post("action/communityManage/report", params, handler);
	}

	public static void publicMessage(int uid, int receiver, String content,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("receiver", receiver);
		params.put("content", content);
		ApiHttpClient.post("action/api/message_pub", params, handler);
	}

	public static void deleteMessage(int uid, int friendid,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("friendid", friendid);
		ApiHttpClient.post("action/api/message_delete", params, handler);
	}

	public static void forwardMessage(int uid, String receiverName,
			String content, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("receiverName", receiverName);
		params.put("content", content);
		ApiHttpClient.post("action/api/message_pub", params, handler);
	}

	public static void getMessageList(int uid, int pageIndex,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("pageIndex", pageIndex);
		params.put("pageSize", DEF_PAGE_SIZE);
		ApiHttpClient.get("action/api/message_list", params, handler);
	}

	public static void updatePortrait(int uid, File portrait,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		Map<String, File> files = new HashMap<String, File>();
		files.put("portrait", portrait);
		ApiHttpClient.post("action/api/portrait_update", params, handler);
	}

	public static void getNotices(int uid, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		ApiHttpClient.post("action/api/user_notice", params, handler);
	}

	public static void clearNotice(int uid, int type,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("type", type);
		ApiHttpClient.post("action/api/notice_clear", params, handler);
	}

	public static void getUserNotice(int uid, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		ApiHttpClient.post("action/api/user_notice", params, handler);
	}

	// 获取我回答的列表
	public static void getAnswerList(int isadopt,int uid, int page,
			JsonHttpResponseHandler mJsonHandler) {
		JSONObject jsonObject = new JSONObject();
		try {
			// 请求的参数
			// jsonObject.put("cid", "58f8643d1093fd2c75544d8c4589f6bb");
			jsonObject.put("uid", uid);
			jsonObject.put("pid", page);
			if(isadopt>=0){
				jsonObject.put("isadopt", isadopt);
			}
			jsonObject.put("num", 20);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("/index.php/Api/Member/myanswerlist.html",
				jsonObject, mJsonHandler);
	}
	
	// 获取我回答的列表
		public static void getPersonalAnswerList(int uid, int page,
				JsonHttpResponseHandler mJsonHandler) {
			JSONObject jsonObject = new JSONObject();
			try {
				// 请求的参数
				// jsonObject.put("cid", "58f8643d1093fd2c75544d8c4589f6bb");
				jsonObject.put("uid", uid);
				jsonObject.put("pid", page);
				jsonObject.put("num", 20);
			} catch (Exception e) {
				e.printStackTrace();
			}
			ApiHttpClient.post("/index.php/Api/Member/personalanswer.html",
					jsonObject, mJsonHandler);
		}

	/**
	 * 获取问题分享
	 * 
	 * @param qid
	 *            问题id
	 * @param handler
	 */
	public static void getQuestionShareUrl(int qid,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("qid", qid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Question/share.html", jsonObject,
				handler);
	}
	
	/**
	 * 分享
	 * @param qid
	 * @param uid
	 * @param handler
	 */
	public static void shareQuestion(int qid,int uid,
			JsonHttpResponseHandler handler){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("qid", qid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Question/sharequeiton.html", jsonObject,
				handler);
	}

	// 获取我回答的列表
	public static void getReplyCommunicatList(int uid, int page, int qid,int aid,
			JsonHttpResponseHandler mJsonHandler) {
		JSONObject jsonObject = new JSONObject();
		try {
			// 请求的参数
			jsonObject.put("aid", aid);
			jsonObject.put("auid", uid);
			jsonObject.put("pid", page);
			jsonObject.put("qid", qid);
			jsonObject.put("num", 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("/index.php/Api/Answer/myanswerlist.html",
				jsonObject, mJsonHandler);
	}

	// 追问的列表
	public static void getAskAgainList(int uid, int mCurrentPage,
			JsonHttpResponseHandler mJsonHandler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("pid", mCurrentPage);
			jsonObject.put("num", 20);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Member/myanswerafterlist.html",
				jsonObject, mJsonHandler);
	}

	// 追问我的列表
	public static void getAskMeAgainList(int uid, int mCurrentPage,
			JsonHttpResponseHandler mJsonHandler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("pid", mCurrentPage);
			jsonObject.put("num", 20);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Member/askmelist.html", jsonObject,
				mJsonHandler);
	}

	// 我的追问列表
	public static void getMyAnswerAfterList(int uid, int pid, int mCurrentPage,
			JsonHttpResponseHandler mJsonHandler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("pid", pid);
			jsonObject.put("num", 20);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Member/myquestionclosely.html",
				jsonObject, mJsonHandler);
	}

	// 获取系统消息的
	public static void getXiTongXiaoXi(int uid, int mCurrentPage,
			JsonHttpResponseHandler mJsonHandler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("pid", mCurrentPage);
			jsonObject.put("num", 20);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Member/mymessagelist.html",
				jsonObject, mJsonHandler);
	}

	// 支持列表
	public static void getSupportedinfo(int uid, int mCurrentPage,
									   JsonHttpResponseHandler mJsonHandler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("pid", mCurrentPage);
			jsonObject.put("num", 20);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Member/supportedinfo.html",
				jsonObject, mJsonHandler);
	}

	// 粉丝求助接口的请求
	public static void getFunsForHelpList(int uid, int mCurrentPage,
			JsonHttpResponseHandler mJsonHandler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("pid", mCurrentPage);
			jsonObject.put("num", 100);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Member/myfansquestion.html",
				jsonObject, mJsonHandler);
	}

	// 获取我感兴趣的
	public static void getMyInterstedList(int i,
			JsonHttpResponseHandler mJsonHandler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", i);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Member/interestcategory.html",
				jsonObject, mJsonHandler);
	}

	// 添加我感兴趣的
	public static void addMyInterstedList(List<Intersted> data,
			JsonHttpResponseHandler mJsonHandler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", AppContext.instance().getLoginUid());

			StringBuilder builder = new StringBuilder();
			builder.append("[");

			int size = data.size();
			for (int i = 0; i < size; i++) {
				if (i == size - 1) {
					builder.append(data.get(i).getId());

				} else {
					builder.append(data.get(i).getId() + ",");
				}
			}

			builder.append("]");

			jsonObject.put("catid", builder.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Member/interestadd.html", jsonObject,
				mJsonHandler);
	}

	// 请求获取实物
	public static void getShiWuList(int i, int mCurrentPage, int type,
			JsonHttpResponseHandler mJsonHandler) {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", i);
			jsonObject.put("pid", mCurrentPage);
			jsonObject.put("num", 30);
			jsonObject.put("type", type);

		} catch (Exception e) {
			e.printStackTrace();
		}

		ApiHttpClient.post("index.php/Api/Member/myproduct.html", jsonObject,
				mJsonHandler);
	}

	// 用户登陆
	public static void loginUser(String clientId, String nickname,
			String password, JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("clientid", clientId);
			jsonObject.put("phone", nickname);
			jsonObject.put("password", password);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Member/login.html", jsonObject,
				handler);
	}

	// 获取讨论
	public static void getDiscuss(int uid, int articleId, int page,
			JsonHttpResponseHandler mJsonHandler) {
		JSONObject jsonObject = new JSONObject();
		try {

			jsonObject.put("id", articleId);
			jsonObject.put("uid", uid);
			jsonObject.put("pid", page);
			jsonObject.put("num", 30);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Found/discusslist.html", jsonObject,
				mJsonHandler);
	}

	// 添加讨论
	public static void addDiscuss(int articleId, String content,
			JsonHttpResponseHandler mJsonHandler) {
		JSONObject jsonObject = new JSONObject();
		try {

			jsonObject.put("uid", AppContext.instance().getLoginUid());
			jsonObject.put("id", articleId);
			jsonObject.put("content", content);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Found/discussadd.html", jsonObject,
				mJsonHandler);
	}

	public static void baoming(int id, String realname, String danwei_info,
			String shouji_info, String email_info,
			JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {

			jsonObject.put("uid", AppContext.instance().getLoginUid());
			jsonObject.put("wid", id);
			jsonObject.put("name", realname);
			jsonObject.put("company", danwei_info);
			jsonObject.put("phone", shouji_info);
			jsonObject.put("email", email_info);
			

		} catch (Exception e) {
			// TODO: handle exception
		}
		ApiHttpClient.post("index.php/Api/Found/registration.html", jsonObject,
				handler);
	}

	// 找回密码
	public static void findPassword(String phone, String telverify,
			String newPassword, JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {

			jsonObject.put("phone", phone);
			jsonObject.put("telverify", telverify);
			jsonObject.put("new_password", newPassword);

		} catch (Exception e) {
			e.printStackTrace();
		}

		ApiHttpClient.post("index.php/Api/Member/findpassword.html",
				jsonObject, handler);
	}

	//
	public static void getRealName(int uid, JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Member/getrealname", jsonObject,
				handler);
	}

	// 讨论里面点赞
	public static void doHit(int uid, int did, JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();

		try {

			jsonObject.put("uid", uid);
			jsonObject.put("did", did);

		} catch (Exception e) {
			e.printStackTrace();
		}

		ApiHttpClient.post("index.php/Api/Found/hit.html", jsonObject, handler);
	}

	public static void getGiftList(int i, int mCurrentPage, int type,
			JsonHttpResponseHandler mJsonHandler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", i);
			jsonObject.put("pid", mCurrentPage);
			jsonObject.put("num", 30);
			jsonObject.put("type", type);

		} catch (Exception e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Product/productlist.html",
				jsonObject, mJsonHandler);
	}

	public static void getGiftDetail(int i, int proid,
			JsonHttpResponseHandler mJsonHandler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", i);
			jsonObject.put("proid", proid);

		} catch (Exception e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Product/getshow.html", jsonObject,
				mJsonHandler);
	}

	public static void getFansData(int loginUid, JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", loginUid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Member/fans.html", jsonObject,
				handler);
	}

	public static void addMyInterstedList(int id, List<Intersted> data,
			JsonHttpResponseHandler addInterestedHandler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", AppContext.instance().getLoginUid());

			StringBuilder builder = new StringBuilder();
			builder.append("{");

			int size = data.size();
			for (int i = 0; i < size; i++) {
				if (i == size - 1) {
					builder.append(data.get(i).getId());

				} else {
					builder.append(data.get(i).getId() + ",");
				}
			}

			builder.append("}");

			jsonObject.put("catid", builder.toString());

			AppContext.showToast("UID" + AppContext.instance().getLoginUid()
					+ "...catid:" + builder.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("index.php/Api/Member/interestadd.html", jsonObject,
				addInterestedHandler);
	}

	/**
	 * 问题补充
	 */
	public static void addAskSupply(int uid, int qid,String content,JsonHttpResponseHandler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid", uid);
			jsonObject.put("qid", qid);
			jsonObject.put("content", content);
		} catch (Exception e) {
		}
		ApiHttpClient.post("index.php/Api/Question/quesedit.html", jsonObject,
				handler);
	}

	// 获取我回答的列表
	public static void updateCheck(JsonHttpResponseHandler mJsonHandler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("type", 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("/index.php/Api/Member/getversion.html", jsonObject, mJsonHandler);
	}

	// 修改手机号
	public static void updatephone(String telverify,String phone,JsonHttpResponseHandler mJsonHandler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("uid",AppContext.instance().getLoginUid());
			jsonObject.put("telverify", telverify);
			jsonObject.put("phone", phone);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ApiHttpClient.post("/index.php/Api/Member/updatephone.html", jsonObject, mJsonHandler);
	}
}
