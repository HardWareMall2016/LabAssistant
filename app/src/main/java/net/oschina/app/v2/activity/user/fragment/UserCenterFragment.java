package net.oschina.app.v2.activity.user.fragment;

import java.io.IOException;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.tweet.view.ExtendMediaPicker;
import net.oschina.app.v2.activity.tweet.view.ExtendMediaPicker.OnMediaPickerListener;
import net.oschina.app.v2.activity.user.adapter.UserCenterPagerAdapter;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseFragment;
import net.oschina.app.v2.model.User;
import net.oschina.app.v2.model.event.ModifyUserHeadComplete;
import net.oschina.app.v2.ui.pagertab.PagerSlidingTabStrip;
import net.oschina.app.v2.utils.StringUtils;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

public class UserCenterFragment extends BaseFragment {

	private static final String USER_CENTER_SCREEN = "user_center_screen";

	private int mHisUid;

	private ImageView itemAvatar;
	private ImageView itemSign;
	private TextView itemName;
	private TextView itemCompany;
	private TextView itemGNum;
	private TextView itemFNum;
	private TextView itemInterest;
	private TextView itemQuestion;
	private TextView itemAnswer;
	private TextView itemUser;
	private TextView itemNotice;
	private PagerSlidingTabStrip mTabStrip;
	private ViewPager mViewPager;

	private ExtendMediaPicker picker;
	private UserCenterPagerAdapter mTabAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.user_center_layout, container,
				false);
		Bundle args = getArguments();
		mHisUid = args.getInt("his_id", 0);
		if (mHisUid != AppContext.instance().getLoginUid()) {
			isme=2;
		}
		picker = new ExtendMediaPicker(getActivity());

		ensureUi(view);
		sendGetUserInfomation();
		return view;
	}

	private void ensureUi(View view) {
		itemAvatar = (ImageView) view.findViewById(R.id.item_avatar);
		itemSign = (ImageView) view.findViewById(R.id.item_sign);
		itemName = (TextView) view.findViewById(R.id.item_name);
		itemCompany = (TextView) view.findViewById(R.id.item_company);
		itemGNum = (TextView) view.findViewById(R.id.item_gnam);
		itemFNum = (TextView) view.findViewById(R.id.item_fnam);
		itemInterest = (TextView) view.findViewById(R.id.item_interest);
		itemAvatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateUserPhoto();
			}
		});

		itemQuestion = (TextView) view.findViewById(R.id.create_question);
		itemAnswer = (TextView) view.findViewById(R.id.answer_question);
		itemUser = (TextView) view.findViewById(R.id.btnUse);
		itemNotice = (TextView) view.findViewById(R.id.btnNotice);
		itemNotice.setOnClickListener(this);

		mTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
		mViewPager = (ViewPager) view.findViewById(R.id.main_tab_pager);

		mTabAdapter = new UserCenterPagerAdapter(getActivity(),
				getChildFragmentManager(), mHisUid);
		mViewPager.setAdapter(mTabAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				mTabStrip.onPageSelected(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				mTabStrip.onPageScrolled(arg0, arg1, arg2);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				mTabStrip.onPageScrollStateChanged(arg0);
			}
		});
		mTabStrip.setViewPager(mViewPager);
	}

	private void setUserPhoto(String head) {
		String path = ApiHttpClient.getImageApiUrl(head);
		ImageLoader.getInstance().displayImage(path, itemAvatar);
	}

	private int isme=1;//1自己，2他人
	
	private void fillData(User user) {
		setUserPhoto(user.getHead());

		if (user.getRealname_status() == 1) {
			itemSign.setVisibility(View.VISIBLE);
		} else {
			itemSign.setVisibility(View.INVISIBLE);
		}

		itemName.setText(user.getNickname());

		if (user.getSex() == 1) {
			itemName.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.userinfo_icon_male, 0);
		} else {
			itemName.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.userinfo_icon_female, 0);
		}

		itemCompany.setText(user.getCompany());
		itemGNum.setText("关注 " + user.getGnum());
		itemFNum.setText("粉丝 " + user.getFnum());
		String tip="TA感兴趣的：";
		int uid = AppContext.instance().getLoginUid();
		if(user.getId()==uid){
			tip="我感兴趣的：";
		}
		if (!StringUtils.isEmpty(user.getInterest())
				&& !"null".equals(user.getInterest())) {
			itemInterest.setText(tip + user.getInterest());
		} else {
			itemInterest.setText(tip+"暂无");
		}

		itemQuestion.setText(""+user.getQnum());
		itemAnswer.setText(""+user.getAnum());
		itemUser.setText(""+user.getCnum());

		if (mHisUid == AppContext.instance().getLoginUid()) {
			itemNotice.setVisibility(View.INVISIBLE);
			
		} else {
			
			itemNotice.setVisibility(View.VISIBLE);
			if(user.getIsattention()==1){
				itemNotice.setText("已关注");
				itemNotice.setEnabled(false);
			}
		}
		
	}

	private void updateUserPhoto() {
		if (mHisUid == AppContext.instance().getLoginUid()) {
			picker.setOnMediaPickerListener(new OnMediaPickerListener() {
				@Override
				public void onSelectedMediaChanged(String mediaUri) {
					uploadImage(mediaUri);
				}
			});
			picker.showPickerView();
		}
	}

	private void uploadImage(String imagePath) {
		NewsApi.uploadImage(1, imagePath, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				try {
					switch (msg.what) {
					case 1:
						String returnStr = msg.getData().getString("return");
						JSONObject jsonObject = new JSONObject(returnStr);
						String imageUrl = jsonObject.getString("url");
						updateFace(imageUrl);
						setUserPhoto(imageUrl);
						break;
					case 2:
						AppContext.showToast("修改图像失败");
						break;
					default:
						break;
					}
					System.out.println(msg.what);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		// NewsApi.updateImage(imagePath, new JsonHttpResponseHandler(){
		// @Override
		// public void onSuccess(int statusCode, Header[] headers, JSONObject
		// response) {
		// if (response.optInt("code")==88) {
		// String head=response.optString("url");
		// updateFace(head);
		// setUserPhoto(head);
		// } else {
		// AppContext.showToast("修改图像失败");
		// }
		// }
		// });
	}

	private void updateFace(final String url) {
		int uid = AppContext.instance().getLoginUid();
		NewsApi.updateUserPhoto(uid, url, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (response.optInt("code") == 88) {
					AppContext.showToast("修改图像成功");
					User user=(User)AppContext.instance().getLoginInfo();
					user.setHead(url);
					AppContext.instance().saveLoginInfo(user);
				} else {
					AppContext.showToast("修改图像失败");
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnNotice) {
			addUserAttention(mHisUid);
		}
	}

	void addUserAttention(int tuid) {
		if (!AppContext.instance().isLogin()) {
			UIHelper.showLoginView(getActivity());
			return;
		}
		int uid = AppContext.instance().getLoginUid();
		NewsApi.addAttention(uid, tuid, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				AppContext.showToast(response.optString("desc", ""));
				itemNotice.setText("已关注");
				itemNotice.setEnabled(false);
			}
		});

	}

	private void sendGetUserInfomation() {
		NewsApi.getUserInfo(AppContext.instance().getLoginUid(),mHisUid, isme,new JsonHttpResponseHandler() {
			public void onSuccess(int statusCode, Header[] headers,
					org.json.JSONObject response) {
				try {
					if (response.optInt("code") == 88) {
						JSONObject userinfo = new JSONObject(response
								.getString("data"));
						fillData(User.parse(userinfo));
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(USER_CENTER_SCREEN);
		MobclickAgent.onResume(getActivity());
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(USER_CENTER_SCREEN);
		MobclickAgent.onPause(getActivity());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		picker.onActivityResult(requestCode, resultCode, data);
	}
}
