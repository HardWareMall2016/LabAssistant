package net.oschina.app.v2.utils;

import com.shiyanzhushou.app.R;

import net.oschina.app.v2.model.MessageNum;
import android.view.View;
import android.widget.TextView;

/**
 * 消息类型对应代码
 * @author 95
 *
 */
public class ActiveNumType {
	public static final int assist_type=1 ;//助手日报
	public static final int lab_type=2 ;//实验周刊
	public static final int active_type=3 ;//活动中心
	public static final int study_type=4 ;//培训信息
	public static final int top_type=5 ;//排行榜
	
	public static final int ask_type=11;//我的提问
	public static final int answer_type=12;//我的回答
	public static final int fansforhelp_type=13;//粉丝求助
	public static final int askafter_type=14;//我的追问
	public static final int askmeafter_type=15;//追问我的
	public static final int sysMessage_type=16;//系统消息
	public static final int attention_type=17;//关注我的
	
	/**
	 * 根据类型,数量更新消息label
	 * @param type
	 * @param tv
	 * @param num
	 */
	public static void updateMessageLabel(int num,TextView tv){
		if (num > 0) {
			tv.setVisibility(View.VISIBLE);
			//大于99显示99+
			String findStr=num>99?num+"+":num+"";
			tv.setText(findStr);
			tv.setBackgroundResource(R.drawable.g_unread_messages_bg);
		} else {
			tv.setText("");
			tv.setBackgroundResource(R.drawable.ic_item_goto_right_tip);
		}
	}
	
	
	/**
	 * 根据类型,数量更新消息label
	 * @param type
	 * @param tv
	 * @param num
	 */
	public static void updateMessageNum(int num,int type,MessageNum messageNum){
		switch(type){
		case assist_type:
			messageNum.setDnum(num);
			break;
		case lab_type:
			messageNum.setWnum(num);
			break;
		case active_type:
			messageNum.setTnum(num);
			break;
		case study_type:
			messageNum.setPnum(num);
			break;
		case top_type:
			messageNum.setChartnum(num);
			break;
		case ask_type			://11;//我的提问
			messageNum.setQnum(num);
			break;
		case answer_type		://=12;//我的回答
			messageNum.setAnum(num);
			break;
		case fansforhelp_type	://=13;//粉丝求助
			messageNum.setFnum(num);
			break;
		case askafter_type		://=14;//我的追问
		case askmeafter_type	://=15;//追问我的
			messageNum.setZnum(num);
			messageNum.setAfternum(num);
			break;
		case sysMessage_type	://=16;//系统消息
			messageNum.setMnum(num);
			break;
		case attention_type		://=17;//关注我的
			messageNum.setGnum(num);
			break;
		}
	}
}
