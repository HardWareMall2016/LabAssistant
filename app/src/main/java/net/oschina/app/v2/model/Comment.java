package net.oschina.app.v2.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.oschina.app.v2.AppException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * 评论实体类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class Comment extends Entity {

	public static final String BUNDLE_KEY_COMMENT = "bundle_key_comment";
	public static final String BUNDLE_KEY_ID = "bundle_key_id";
	public static final String BUNDLE_KEY_CATALOG = "bundle_key_catalog";
	public static final String BUNDLE_KEY_BLOG = "bundle_key_blog";
	public static final String BUNDLE_KEY_OPERATION = "bundle_key_operation";
	
	public static final int OPT_ADD = 1;
	public static final int OPT_REMOVE = 2;
	
	protected static final long serialVersionUID = -3376380441354446400L;
	public final static int CLIENT_MOBILE = 2;
	public final static int CLIENT_ANDROID = 3;
	public final static int CLIENT_IPHONE = 4;
	public final static int CLIENT_WINDOWS_PHONE = 5;

	protected int qid;
	protected int auid;
	protected String content;
	protected int type;
	protected int isadopt;
	protected String inputtime;
	protected int snum;//支持数
	protected int anum;//追问数
	protected int answerNum;//互动数
	protected String nickname;
	protected String head;
	protected String image;
	protected String hits;
	protected String isread;

	
	protected int aid;
	protected String title;
	protected int rank;
	protected String info;
	protected List<CommentItem> itemList=new ArrayList<CommentItem>();
	protected String ansupperlist;
	protected int count;
	
	private String aftername;


	public String getIsread() {
		return isread;
	}

	public void setIsread(String isread) {
		this.isread = isread;
	}

	public String getAftername() {
		return aftername;
	}


	public void setAftername(String aftername) {
		this.aftername = aftername;
	}



	protected boolean zhuiWen;
	
	public Comment(){
	
	}

	public String getHits() {
		return hits;
	}

	public void setHits(String hits) {
		this.hits = hits;
	}

	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public boolean isZhuiWen() {
		return zhuiWen;
	}
	public void setZhuiWen(boolean zhuiWen) {
		this.zhuiWen = zhuiWen;
	}
	public int getqid() {
		return qid;
	}
	public void setqid(int qid) {
		this.qid = qid;
	}
	public int getauid() {
		return auid;
	}
	public void setauid(int auid) {
		this.auid = auid;
	}
	public String getcontent() {
		return content;
	}
	public void setcontent(String content) {
		this.content = content;
	}
	public int gettype() {
		return type;
	}
	public void settype(int type) {
		this.type = type;
	}
	public int getisadopt() {
		return isadopt;
	}
	public void setisadopt(int isadopt) {
		this.isadopt = isadopt;
	}
	public String getinputtime() {
		return inputtime;
	}
	public void setinputtime(String inputtime) {
		this.inputtime = inputtime;
	}
	
	public int getsnum() {
		return snum;
	}
	public void setsnum(int snum) {
		this.snum = snum;
	}
	
	public int getanum() {
		return anum;
	}
	public void setanum(int anum) {
		this.anum = anum;
	}
	
	public int getAnswerNum() {
		return answerNum;
	}
	public void setAnswerNum(int answerNum) {
		this.answerNum = answerNum;
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
	
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public List<CommentItem> getItemList() {
		return itemList;
	}
	public void addItemList(CommentItem object) {
		this.itemList.add(object);
	}
	
	

	public String getAnsupperlist() {
		return ansupperlist;
	}
	public void setAnsupperlist(String ansupperlist) {
		this.ansupperlist = ansupperlist;
	}



	protected List<Reply> replies = new ArrayList<Reply>();
	protected List<Refer> refers = new ArrayList<Refer>();

	public static class Reply implements Serializable, Parcelable {
		protected static final long serialVersionUID = 1L;
		public String rauthor;
		public String rpubDate;
		public String rcontent;

		public Reply() {
		}

		public Reply(Parcel source) {
			rauthor = source.readString();
			rpubDate = source.readString();
			rcontent = source.readString();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(rauthor);
			dest.writeString(rpubDate);
			dest.writeString(rcontent);
		}

		@Override
		public int describeContents() {
			return 0;
		}

		public static final Parcelable.Creator<Reply> CREATOR = new Creator<Reply>() {

			@Override
			public Reply[] newArray(int size) {
				return new Reply[size];
			}

			@Override
			public Reply createFromParcel(Parcel source) {
				return new Reply(source);
			}
		};
	}

	public static class Refer implements Serializable, Parcelable {
		protected static final long serialVersionUID = 1L;
		public String refertitle;
		public String referbody;

		public Refer() {
		}

		public Refer(Parcel source) {
			referbody = source.readString();
			refertitle = source.readString();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(referbody);
			dest.writeString(refertitle);
		}

		@Override
		public int describeContents() {
			return 0;
		}

		public static final Parcelable.Creator<Refer> CREATOR = new Creator<Comment.Refer>() {

			@Override
			public Refer[] newArray(int size) {
				return new Refer[size];
			}

			@Override
			public Refer createFromParcel(Parcel source) {
				return new Refer(source);
			}
		};
	}


	public static Comment parse(JSONObject response) throws IOException, AppException {
		Comment comment = new Comment();
		//{"content":"欧奈雅也可以啊","id":"2","auid":"2","nickname":"xixi","isadopt":"0","snum":"1","aid":"0","head":null,"anum":"1","type":"0","inputtime":"1418361333"}
		try {
			comment.setId(response.getInt("id"));
			comment.setqid(response.getInt("qid"));
			comment.setauid(response.getInt("auid"));
			comment.setcontent(response.getString("content"));
			comment.settype(response.getInt("type"));
			comment.setisadopt(response.getInt("isadopt"));
			comment.setinputtime(response.getString("inputtime"));
			comment.setsnum(response.getInt("snum"));
			comment.setanum(response.getInt("anum"));
			comment.setAnswerNum(response.getInt("answernum"));
			comment.setnickname(response.getString("nickname"));
			comment.sethead(response.getString("head"));
			comment.setTitle(response.optString("title"));
			comment.setAid(response.optInt("aid"));
			comment.setRank(response.optInt("rank"));
			comment.setImage(response.optString("image"));
			comment.setIsread(response.optString("isread"));
			String hits=response.optString("hits");
			if(TextUtils.isEmpty(hits)){
				hits="0";
			}
			comment.setHits(hits);
		
			try{
				comment.setInfo(response.optString("info"));
			}catch(Exception e){
				
			}
			
			try{
				comment.setAnsupperlist(response.getString("ansupperlist"));
			}catch(Exception e){
				
			}
			
			String afterdata=response.optString("afterdata");
			if (!TextUtils.isEmpty(afterdata) && !"null".equals(afterdata)) {
				JSONArray array=new JSONArray(afterdata);
				for (int i=0;i<array.length();i++) {
					CommentItem object=CommentItem.parse(array.getJSONObject(i));
					comment.addItemList(object);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
     
        return comment;       
	}
}
