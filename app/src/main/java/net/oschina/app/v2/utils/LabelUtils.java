package net.oschina.app.v2.utils;

import android.text.TextUtils;

import com.shiyanzhushou.app.R;

/**
 * Created by WuYue on 2016/5/26.
 */
public class LabelUtils {
    public static int getBgResIdByLabel(String label){
        String []strings=label.split(" ");
        if(strings!=null&&strings.length>1){
            String classStr=strings[0];
            if("食品".equals(classStr)){
                return R.drawable.bg_label_1;
            }else if("药品".equals(classStr)){
                return R.drawable.bg_label_2;
            } else if("医疗器械".equals(classStr)){
                return R.drawable.bg_label_3;
            } else if("仪器试剂耗材".equals(classStr)){
                return R.drawable.bg_label_4;
            } else if("职场与生活".equals(classStr)){
                return R.drawable.bg_label_5;
            } else if("临床医学".equals(classStr)){
                return R.drawable.bg_label_6;
            }else if("化妆品".equals(classStr)){
                return R.drawable.bg_label_7;
            }else{
                return R.drawable.bg_label_other;
            }
        }else{
            return R.drawable.bg_label_other;
        }
    }

    public static String parseLable(String label){
        if(TextUtils.isEmpty(label)){
            return "";
        }else{
            return label.replace(" ","/");
        }
    }
}
