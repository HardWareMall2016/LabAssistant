package net.oschina.app.v2.activity.image;

import com.shiyanzhushou.app.R;

import android.view.View;
import android.widget.ImageView;

/**
 * 加V工具类，根据type加载不同的加v图片
 * @author JasonMarzw
 *
 */
public class IvSignUtils {

	//认证用户:20，商户：21，软件小编:22,  专家:30，内测组：31，BOSS:32
	/**
	 * 根据用户类型，加载不同的v标志
	 * @param type
	 * @param image
	 */
	public static void displayIvSignByType(int type ,ImageView image,ImageView imageBg){
		int resId=0;
		switch(type){
		case 20://认证用户
			resId=R.drawable.user_v_sign;
			imageBg.setVisibility(View.INVISIBLE);
			image.setVisibility(View.VISIBLE);
			break;
		case 21://商户
			resId=R.drawable.user_v_sign_blue;
			imageBg.setVisibility(View.INVISIBLE);
			image.setVisibility(View.VISIBLE);
			break;
		case 4://小编
			resId=R.drawable.user_v_sign_edit;
			imageBg.setVisibility(View.INVISIBLE);
			image.setVisibility(View.VISIBLE);
			break;
		case 5://专家
			resId=R.drawable.user_v_sign_expert;
			imageBg.setVisibility(View.VISIBLE);
			imageBg.setImageResource(R.drawable.iv_expert_bg);
			image.setVisibility(View.INVISIBLE);
			break;
		case 6://内测组
			resId=R.drawable.user_v_sign_test;
			imageBg.setVisibility(View.VISIBLE);
			imageBg.setImageResource(R.drawable.iv_test_bg);
			image.setVisibility(View.INVISIBLE);
			break;
		case 7://BOSS
			resId=R.drawable.user_v_sign_boss;
			imageBg.setVisibility(View.VISIBLE);
			imageBg.setImageResource(R.drawable.iv_boss_bg);
			image.setVisibility(View.INVISIBLE);
			break;
		default:
			imageBg.setVisibility(View.INVISIBLE);
			image.setVisibility(View.INVISIBLE);
			break;
		}
		image.setImageResource(resId);
	}
}
