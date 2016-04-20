package net.oschina.app.v2.activity.tweet.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import net.oschina.app.v2.AppContext;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.MediaColumns;
import android.text.format.DateFormat;
import android.widget.ArrayAdapter;

public class ExtendMediaPicker {
	private static final int REQUEST_CODE_PICK_IMAGE = 2001;
	private static final int REQUEST_CODE_TAKE_PHOTO = 2002;

	private Uri imageUri;
	private String imagePath;
	private File tempFile;
	private Activity mActivity;
	private OnMediaPickerListener mMediaPickerListener;

	public ExtendMediaPicker(Activity activity) {
		this.mActivity = activity;
		this.tempFile = createTempFile();
	}
	
	

	public String getImagePath() {
		return imagePath;
	}



	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}



	public void showPickerView() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setAdapter(new ArrayAdapter<String>(mActivity,
				android.R.layout.simple_list_item_1, new String[] { "使用相机拍照",
						"从手机相册选择" }), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					openSystemCamera();
				} else {
					openSystemPickImage();
				}
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	int resultCOde;
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		resultCOde=requestCode;
		switch (requestCode) {
		case REQUEST_CODE_PICK_IMAGE:
			
			String path = MediaUtils.getPath(mActivity, data.getData());
			cropImageUri(Uri.fromFile(new File(path)));
			break;
		case REQUEST_CODE_TAKE_PHOTO:
			if (imageUri == null) {
				imageUri = Uri.fromFile(new File(imagePath));
			}
			cropImageUri(imageUri);
			break;
		}
	}

	@SuppressLint("InlinedApi")
	private void openSystemPickImage() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
		photoPickerIntent.setType("image/*");
		mActivity.startActivityForResult(photoPickerIntent,
				REQUEST_CODE_PICK_IMAGE);
	}

	private void openSystemCamera() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			imagePath = Environment.getExternalStorageDirectory()
					+ File.separator + System.currentTimeMillis() + ".jpg";
			File dirPath = new File(imagePath);
			imageUri = Uri.fromFile(dirPath);

			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			mActivity.startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
		} else {
			AppContext
					.showToast("Before you take photos please insert SD card");
		}
	}

	private File createTempFile() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return new File(Environment.getExternalStorageDirectory(),
					"image.jpg");
		} else {
			return new File(mActivity.getFilesDir(), "image.jpg");
		}
	}

	private void cropImageUri(Uri uri) {
		if(uri==null)
			return ;
		if (mMediaPickerListener != null) {
			// 控制图片质量
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				Bitmap bmp = BitmapFactory.decodeFile(uri.getPath(), options);
				int longSide = options.outHeight > options.outWidth ? options.outHeight
						: options.outWidth;
				options.inSampleSize = 1;
				if (longSide < 1000) {
					options.inSampleSize = 1;
				} else if (longSide < 2000) {
					options.inSampleSize = 2;
				} else {
					options.inSampleSize = 4;
				}
				// 解析图片
				options.inJustDecodeBounds = false;
				bmp = BitmapFactory.decodeFile(uri.getPath(), options).copy(
						Bitmap.Config.ARGB_8888, true);
				// 自动旋转规避
				int degree = getBitmapDegree(uri.getPath());
				if (degree != 0) {
					Matrix matrix = new Matrix();
					matrix.postRotate(degree);
					bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
							bmp.getHeight(), matrix, true);
				}
				if (bmp != null) {
					OutputStream out = new FileOutputStream(uri.getPath()
							+ "send");
					bmp.compress(CompressFormat.JPEG, 100, out);
					out.close();
					
					if(REQUEST_CODE_TAKE_PHOTO==resultCOde){//拍照才保存到相册
						String fname = DateFormat.format("yyyyMMddhhmmss", new Date())
								.toString();

						String otherFilePath = insertImage(mActivity.getContentResolver(), bmp,
								fname, "");
						
						try {
							mActivity.sendBroadcast(
									new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
											.parse("file://" + otherFilePath)));

						} catch (Exception e) {
							e.printStackTrace();
						}

						try {
							String path = getFilePathByContentResolver(
									mActivity, Uri.parse(otherFilePath));
							File tempFile = new File(path);
							;
							Uri uri2 = Uri.fromFile(tempFile);
							Intent localIntent = new Intent(
									Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
							mActivity.sendBroadcast(localIntent);

							MediaScannerConnection.scanFile(mActivity,
									new String[] { tempFile.getAbsolutePath() }, null,
									null);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			mMediaPickerListener.onSelectedMediaChanged(uri.getPath() + "send");
			return;
		}
	}
	
	private String getFilePathByContentResolver(Context context, Uri uri) {
		if (null == uri) {
			return null;
		}
		Cursor c = context.getContentResolver().query(uri, null, null, null,
				null);
		String filePath = null;
		if (null == c) {
			throw new IllegalArgumentException("Query on " + uri
					+ " returns null result.");
		}
		try {
			if ((c.getCount() != 1) || !c.moveToFirst()) {
			} else {
				filePath = c.getString(c
						.getColumnIndexOrThrow(MediaColumns.DATA));
			}
		} finally {
			c.close();
		}
		return filePath;
	}
	
	public String insertImage(ContentResolver cr, Bitmap source,
			String title, String description) {
		ContentValues values = new ContentValues();
		long taken = System.currentTimeMillis();
		values.put(Images.Media.TITLE, title);
		values.put(Images.Media.DESCRIPTION, description);
		values.put(Images.Media.MIME_TYPE, "image/jpeg");
		values.put(Images.Media.DISPLAY_NAME, title);
		values.put(Images.Media.DATE_ADDED, taken);
		values.put(Images.Media.DATE_TAKEN, taken);
		values.put(Images.Media.DATE_MODIFIED, taken);

		Uri url = null;
		String stringUrl = null; /* value to be returned */

		try {
			url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					values);

			if (source != null) {
				OutputStream imageOut = cr.openOutputStream(url);
				try {
					source.compress(Bitmap.CompressFormat.JPEG, 100,
							imageOut);
				} finally {
					imageOut.close();
				}

				long id = ContentUris.parseId(url);
				// Wait until MINI_KIND thumbnail is generated.
				Bitmap miniThumb = Images.Thumbnails.getThumbnail(cr, id,
						Images.Thumbnails.MINI_KIND, null);
				// This is for backward compatibility.
				Bitmap microThumb = StoreThumbnail(cr, miniThumb, id, 50F,
						50F, Images.Thumbnails.MICRO_KIND);
			} else {
				cr.delete(url, null, null);
				url = null;
			}
		} catch (Exception e) {
			if (url != null) {
				cr.delete(url, null, null);
				url = null;
			}
		}

		if (url != null) {
			stringUrl = url.toString();
		}

		return stringUrl;
	}

	private final Bitmap StoreThumbnail(ContentResolver cr, Bitmap source,
			long id, float width, float height, int kind) {
		// create the matrix to scale it
		Matrix matrix = new Matrix();

		float scaleX = width / source.getWidth();
		float scaleY = height / source.getHeight();

		matrix.setScale(scaleX, scaleY);

		Bitmap thumb = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
				source.getHeight(), matrix, true);

		ContentValues values = new ContentValues(4);
		values.put(Images.Thumbnails.KIND, kind);
		values.put(Images.Thumbnails.IMAGE_ID, (int) id);
		values.put(Images.Thumbnails.HEIGHT, thumb.getHeight());
		values.put(Images.Thumbnails.WIDTH, thumb.getWidth());

		Uri url = cr.insert(Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

		try {
			OutputStream thumbOut = cr.openOutputStream(url);
			try {
				thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
			} finally {
				thumbOut.close();
			}
			return thumb;
		} catch (FileNotFoundException ex) {
			return null;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}

	}


	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {
		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * 读取图片的旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return 图片的旋转角度
	 */
	private int getBitmapDegree(String path) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 旋转图片
	 * 
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	public void setOnMediaPickerListener(OnMediaPickerListener listener) {
		this.mMediaPickerListener = listener;
	}

	public interface OnMediaPickerListener {

		void onSelectedMediaChanged(String mediaUri);
	}
}
