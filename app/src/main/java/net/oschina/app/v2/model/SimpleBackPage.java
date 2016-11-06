package net.oschina.app.v2.model;

import net.oschina.app.v2.activity.comment.fragment.CommentFrament;
import net.oschina.app.v2.activity.comment.fragment.CommentReplyFragment;
import net.oschina.app.v2.activity.daily.DailyEnglishFragment;
import net.oschina.app.v2.activity.favorite.fragment.FavoriteViewPagerFragment;
import net.oschina.app.v2.activity.find.fragment.ActivityCenterFragment;
import net.oschina.app.v2.activity.find.fragment.BillBoaodViewPagerFragment;
import net.oschina.app.v2.activity.find.fragment.DailyFragment;
import net.oschina.app.v2.activity.find.fragment.MallFragment;
import net.oschina.app.v2.activity.find.fragment.NewBrandFragment;
import net.oschina.app.v2.activity.find.fragment.ReferencesFragment;
import net.oschina.app.v2.activity.find.fragment.TrainFragment;
import net.oschina.app.v2.activity.find.fragment.WeeklyFragment;
import net.oschina.app.v2.activity.friend.fragment.FriendViewPagerFragment;
import net.oschina.app.v2.activity.home.fragment.SearchFragment;
import net.oschina.app.v2.activity.message.fragment.MessageDetailFragment;
import net.oschina.app.v2.activity.message.fragment.MessageForwardFragment;
import net.oschina.app.v2.activity.message.fragment.MessagePublicFragment;
import net.oschina.app.v2.activity.settings.fragment.AboutSoftFragment;
import net.oschina.app.v2.activity.settings.fragment.BangDingShouJiFragment;
import net.oschina.app.v2.activity.settings.fragment.EditNicknameFragment;
import net.oschina.app.v2.activity.settings.fragment.EditPasswordFragment;
import net.oschina.app.v2.activity.settings.fragment.FeedbackFragment;
import net.oschina.app.v2.activity.settings.fragment.HelpFragment;
import net.oschina.app.v2.activity.settings.fragment.LisenceFragment;
import net.oschina.app.v2.activity.settings.fragment.MassageSettingFragment;
import net.oschina.app.v2.activity.settings.fragment.NotifIcationSettingsFragment;
import net.oschina.app.v2.activity.settings.fragment.RenZhengShuoMingFragment;
import net.oschina.app.v2.activity.settings.fragment.SaoYiSaoFragment;
import net.oschina.app.v2.activity.settings.fragment.SettingsFragment;
import net.oschina.app.v2.activity.settings.fragment.ShiMingRenZhengFragment;
import net.oschina.app.v2.activity.settings.fragment.UserShuoMingFragment;
import net.oschina.app.v2.activity.tweet.fragment.TweetPublicFragment;
import net.oschina.app.v2.activity.user.fragment.AnswerAfterPagerFragment;
import net.oschina.app.v2.activity.user.fragment.AskMeAgainFragment;
import net.oschina.app.v2.activity.user.fragment.CaoGaoXiangFragment;
import net.oschina.app.v2.activity.user.fragment.FunsForHelpFragment;
import net.oschina.app.v2.activity.user.fragment.JifenXiangQingFragment;
import net.oschina.app.v2.activity.user.fragment.MyAnswerFragment;
import net.oschina.app.v2.activity.user.fragment.MyFavoriteViewPageFragment;
import net.oschina.app.v2.activity.user.fragment.MyFavoritesViewPageFragment;
import net.oschina.app.v2.activity.user.fragment.MyInterstFragment;
import net.oschina.app.v2.activity.user.fragment.MyQuestionFragment;
import net.oschina.app.v2.activity.user.fragment.MyZhuShouIDFragment;
import net.oschina.app.v2.activity.user.fragment.TaskChengjiuFragment;
import net.oschina.app.v2.activity.user.fragment.UserCenterFragment;
import net.oschina.app.v2.activity.user.fragment.UserProfileFragment;
import net.oschina.app.v2.activity.user.fragment.WoDeWuPinViewPageFragment;
import net.oschina.app.v2.activity.user.fragment.XiTongXiaoXiFragment;
import net.oschina.app.v2.activity.user.fragment.ZhiChiWoDeFragment;

import com.shiyanzhushou.app.R;

public enum SimpleBackPage {

	SETTINGS(0, R.string.actionbar_title_settings, SettingsFragment.class), 
	ABOUT(1, R.string.actionbar_title_about, RenZhengShuoMingFragment.class),
	PROFILE(2, R.string.actionbar_title_profile, UserProfileFragment.class),
	FRIENDS(3, R.string.actionbar_title_friends, FriendViewPagerFragment.class),
	FAVORITES(4, R.string.actionbar_title_favorites, FavoriteViewPagerFragment.class),
	COMMENT(6, R.string.actionbar_title_comment, CommentFrament.class),
	USER_CENTER(8, R.string.actionbar_title_user_center, UserCenterFragment.class),
	TWEET_PUBLIC(10, R.string.actionbar_title_tweet_public, TweetPublicFragment.class),
	REPLY_COMMENT(11, R.string.actionbar_title_reply_comment, CommentReplyFragment.class),
	MESSAGE_PUBLIC(12, R.string.actionbar_title_message_public, MessagePublicFragment.class),
	MESSAGE_DETAIL(13, R.string.actionbar_title_message_detail, MessageDetailFragment.class),
	SEARCH(14, R.string.actionbar_title_search, SearchFragment.class),
	LISENCE(16, R.string.actionbar_title_lisence, LisenceFragment.class),
	MESSAGE_FORWARD(17, R.string.actionbar_title_message_forward, MessageForwardFragment.class),
	NOTIFICATION_SETTINGS(18, R.string.actionbar_title_notification_settings, NotifIcationSettingsFragment.class),
	DAILY_ENGLISH(19, R.string.actionbar_title_daily_english, DailyEnglishFragment.class),

	DAILY(20, R.string.find_shoushouribao, DailyFragment.class),
	WEEKLY(21, R.string.find_shiyanzhoukan, WeeklyFragment.class),
	REFERENCES(22, R.string.find_faguiwenxian, ReferencesFragment.class),
	ACTIVITYCENTER(23, R.string.find_huodongzhongxin, ActivityCenterFragment.class),
	BRAND(24, R.string.find_pinpaiku, NewBrandFragment.class),
	TRAIN(25, R.string.find_peixunxinxi, TrainFragment.class),
	MALL(26, R.string.find_jifenshangcheng, MallFragment.class),
//	BILLBOARD(27, R.string.find_paihangbang, BillboardFragment.class),
	BILLBOARD(27, R.string.find_paihangbang, BillBoaodViewPagerFragment.class),
	
	Question(28,R.string.actionbar_title_myquestion,MyQuestionFragment.class),
	Answer(29,R.string.actionbar_title_myanswer,MyAnswerFragment.class), 
	AskmeAgain(30,R.string.active_zhuiwenwode,AskMeAgainFragment.class),
	Fensiqiuzhu(31,R.string.active_fensiqiuzhu,FunsForHelpFragment.class),
	xitongxiaoxi(32,R.string.activite_xitongxiaoxi,XiTongXiaoXiFragment.class), 
	guanzhuxiaoxi(33,R.string.activity_guanzhuxiaoxi,MyFavoriteViewPageFragment.class), 
	woganxingqude(34,R.string.activity_woganxingqde,MyInterstFragment.class),
	renwuchengjiu(35,R.string.activity_renwuchengjiu,/*RenWuChengJiuFragment.class*/TaskChengjiuFragment.class), 
	wodewupin(36,R.string.activity_wodewupin,WoDeWuPinViewPageFragment.class),
	
	EDITNICKNAME(37,R.string.settings_xiugainicheng,EditNicknameFragment.class),
	SHIMINGRENZHENG(38,R.string.settings_shimingrenzheng,ShiMingRenZhengFragment.class),
	//修改密码
	EDITPASSWORD(39,R.string.settings_xiugaimima,EditPasswordFragment.class),
	MASSAGESETTING(40,R.string.settings_xiaoxishezhi,MassageSettingFragment.class),
	SAOYISAO(41,R.string.settings_saoyisaoxiazai,SaoYiSaoFragment.class),
	BANGDINGSHOUJI(42,R.string.settings_bangdingshoujihao,BangDingShouJiFragment.class),
	ABOUTSOFT(43,R.string.settings_guanyuruanjian,AboutSoftFragment.class),
	HELP(44,R.string.settings_bangzhu,HelpFragment.class),
	FEEDBACK(45,R.string.me_left,FeedbackFragment.class),
	RENZHENGSHUOMING(46,R.string.settings_shimingrenzheng,RenZhengShuoMingFragment.class),
	MYZHUSHOUID(47,R.string.active_wodezhushouhao,MyZhuShouIDFragment.class),
	ANSWERAFTER(48,R.string.active_zhuiwenwode,AnswerAfterPagerFragment.class),
	UserSHUOMING(49,R.string.active_userDescription,UserShuoMingFragment.class),
	wodesoucang(50,R.string.active_wodesoucang,MyFavoritesViewPageFragment.class),
	jifenxiangqing(51,R.string.activite_jifenxiangqing,JifenXiangQingFragment.class),
	zhichiwode(52,R.string.activite_zhichiwode,ZhiChiWoDeFragment.class),
	caogaoxiang(53,R.string.active_caogaoxiang,CaoGaoXiangFragment.class);

	private int title;
	private Class<?> clz;
	private int value;
	private SimpleBackPage(int value, int title, Class<?> clz) {
		this.value = value;
		this.title = title;
		this.clz = clz;
	}

	public int getTitle() {
		return title;
	}

	public void setTitle(int title) {
		this.title = title;
	}

	public Class<?> getClz() {
		return clz;
	}

	public void setClz(Class<?> clz) {
		this.clz = clz;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static SimpleBackPage getPageByValue(int val) {
		for (SimpleBackPage p : values()) {
			if (p.getValue() == val)
				return p;
		}
		return null;
	}
}
