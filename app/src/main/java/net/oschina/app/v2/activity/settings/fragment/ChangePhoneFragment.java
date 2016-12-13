package net.oschina.app.v2.activity.settings.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseFragment;
import net.oschina.app.v2.emoji.Emoji;
import net.oschina.app.v2.utils.TDevice;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.lang.ref.SoftReference;

public class ChangePhoneFragment extends BaseFragment {
    private EditText mEtPhoneNumber;
    private EditText mEtCheckCode;
    private Button mGetCode;
    private Button mSubmit;

    private String phoneNumber="";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.v2_fragment_change_phone, container, false);
        mEtPhoneNumber = (EditText) view.findViewById(R.id.et_obatin_verify);
        mEtCheckCode = (EditText) view.findViewById(R.id.et_cons);
        mSubmit = (Button) view.findViewById(R.id.btn_submit);
        mGetCode = (Button) view.findViewById(R.id.bt_obtain_cons);
        mGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVerifyCode();
            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!phoneNumber.matches("1[3|5|7|8|][0-9]{9}")) {
                    Toast.makeText(getActivity(), R.string.phonenumber_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                // 验证码
                String verify = null;
                if (mEtCheckCode.getText() != null)
                    verify = mEtCheckCode.getText().toString();
                else {
                    AppContext.showToast("请输入验证码！");
                    return;
                }
                phoneUpdate(verify,phoneNumber);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        TDevice.hideSoftKeyboard(getActivity().getCurrentFocus());
    }

    public boolean onBackPressed() {
        TDevice.hideSoftKeyboard(getActivity().getCurrentFocus());
        return false;
    }


    private void getVerifyCode() {
        phoneNumber=mEtPhoneNumber.getText().toString();
        if (!phoneNumber.matches("1[3|5|7|8|][0-9]{9}")) {
            Toast.makeText(getActivity(), R.string.phonenumber_error, Toast.LENGTH_SHORT).show();
            return;
        }

        StringEntity entity;
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", phoneNumber);
            entity = new StringEntity(jsonObject.toString());
        } catch (Exception e) {
           return;
        }
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
        };
        // partUrl，请求的url
        String partUrl = "index.php/Api/Member/sendchecknum.html";
        client.post(AppContext.instance(), ApiHttpClient.getAbsoluteApiUrl(partUrl), entity, "application/json", handler);

        // 60000是总的延时的时间，1000是延时的间隔
        TimeCount timeCount = new TimeCount(60000, 1000);
        timeCount.start();
    }

    // 延时的内部类
    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        // 结束的时候调用方法
        @Override
        public void onFinish() {
            // 1.设置按钮点击为true
            mGetCode.setText("再次验证");
            mGetCode.setClickable(true);
            mGetCode.setBackgroundResource(R.drawable.common_btn_selector);
        }

        // 延时开始的时候调用的方法
        @SuppressLint("ResourceAsColor")
        @Override
        public void onTick(long millisUntilFinished) {
            mGetCode.setClickable(false);
            mGetCode.setBackgroundResource(R.drawable.verify_timecount);
            mGetCode.setText(String.format("%d秒", millisUntilFinished / 1000));
        }
    }

    private void phoneUpdate(String code,String phone) {
        NewsApi.updatephone(code,phone,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.optInt("code") == 88) {
                        AppContext.showToast("手机号修改成功");
                        getActivity().finish();
                    }else{
                        AppContext.showToast("手机号修改失败");
                    }
                } catch (Exception e) {
                }
            }
        });
    }
}
