/**
 * Project Name:Houlai
 * File Name:BitmapLoaderUtil.java
 * Package Name:cn.xinlishuo.chulaihi.common.utils.view
 * Date:2015-4-15下午8:59:17
 * Copyright (c) 2015, bo.wang@xinlishuo.cn All Rights Reserved.
 *
 */

package net.oschina.app.v2.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.InputStream;

/**
 * ClassName:BitmapLoaderUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2015-4-15 下午8:59:17 <br/>
 * 
 * @author 95
 * @version
 * @since JDK 1.6
 * @see
 */
public class BitmapLoaderUtil {

	/**
	 * 以最省内存的方式读取本地资源的图片
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 */
	public static Bitmap readBitMapInSize(Context context, int resId, int sampleSize) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		opt.inSampleSize = sampleSize;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 */
	public static BitmapDrawable readBitMapDrawable(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return new BitmapDrawable(BitmapFactory.decodeStream(is, null, opt));
	}

	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	// 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响
	private static Bitmap createScaleBitmap(Bitmap src, int dstWidth, int dstHeight) {
		Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
		if (src != dst) { // 如果没有缩放，那么不回收
			src.recycle(); // 释放Bitmap的native像素数组
		}
		return dst;
	}

	// 从Resources中加载图片
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options); // 读取图片长款
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight); // 计算inSampleSize
		options.inJustDecodeBounds = false;
		Bitmap src = BitmapFactory.decodeResource(res, resId, options); // 载入一个稍大的缩略图
		return createScaleBitmap(src, reqWidth, reqHeight); // 进一步得到目标大小的缩略图
	}

	// 从sd卡上加载图片
	public static Bitmap decodeSampledBitmapFromFd(String pathName, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;
		Bitmap src = BitmapFactory.decodeFile(pathName, options);
		return createScaleBitmap(src, reqWidth, reqHeight);
	}

	/**
	 * 计算inSampleSize，用于压缩图片
	 *
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSizeByScreenWidth(BitmapFactory.Options options,
									  int reqWidth, int reqHeight)
	{
		// 源图片的宽度
		int width = options.outWidth;
		int height = options.outHeight;
		int inSampleSize = 1;

		if (width > reqWidth && height > reqHeight)
		{
			// 计算出实际宽度和目标宽度的比率
			int widthRatio = Math.round((float) width / (float) reqWidth);
			int heightRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = Math.max(widthRatio, heightRatio);
		}
		return inSampleSize;
	}

	/**
	 * 根据计算的inSampleSize，得到压缩后图片
	 *
	 * @param pathName

	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromResourceByScreen(Resources res,Integer pathName)
	{

		int reqWidth,  reqHeight;
		reqWidth=res.getDisplayMetrics().widthPixels;
		reqHeight=reqWidth;
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, pathName,options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSizeByScreenWidth(options, reqWidth,
				reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		return BitmapFactory.decodeResource(res, pathName, options);
	}

	public static Bitmap decodeTest(Resources res, int resId) {
		InputStream input = res.openRawResource(resId);
		// 通过InputStream创建BitmapDrawable对象<br>
		final BitmapDrawable girl = new BitmapDrawable(input);
		// 通过BitmapDrawable对象获取Bitmap对象<br>
		Bitmap bitmap = girl.getBitmap();
		// 利用Bitmpa对象创建缩略图<br>
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, 51, 50);

		return bitmap;
	}

	/**
	 * 加载imageLoader默认配置
	 * loadDisplayImageOptions:(这里用一句话描述这个方法的作用). <br/>
	 *
	 * @author 95
	 * @return
	 * @since JDK 1.6
	 */
	public static DisplayImageOptions loadDisplayImageOptions() {

		return new DisplayImageOptions.Builder()/*.showImageOnLoading(R.drawable.soh_1)*/ // 设置图片下载期间显示的图片
				/*.showImageForEmptyUri(R.drawable.soh_1) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.soh_1) // 设置图片加载或解码过程中发生错误显示的图片*/
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
				.build();
	}

	/**
	 * 加载imageLoader默认配置
	 * loadDisplayImageOptions:(这里用一句话描述这个方法的作用). <br/>
	 *
	 * @author 95
	 * @return
	 * @since JDK 1.6
	 */
	public static DisplayImageOptions loadDisplayImageOptions(int emptyUri) {
		return new DisplayImageOptions.Builder()/*.showImageOnLoading(R.drawable.soh_1)*/ // 设置图片下载期间显示的图片
				/*.showImageForEmptyUri(R.drawable.soh_1) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.soh_1) // 设置图片加载或解码过程中发生错误显示的图片*/
				.showImageForEmptyUri(emptyUri)
				.showImageOnFail(emptyUri)
				.showImageOnLoading(emptyUri)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
				.build();
	}
}
