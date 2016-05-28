package net.oschina.app.v2.utils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.shiyanzhushou.app.R;

/**
 * Created by Administrator on 2016/5/27.
 */
public class ImageLoderOptionUtil {
    public static DisplayImageOptions buldDisplayImageOptionsForAvatar(){
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_default_pic)
                .showImageForEmptyUri(R.drawable.ic_default_pic)
                .showImageOnFail(R.drawable.ic_default_pic)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }
}
