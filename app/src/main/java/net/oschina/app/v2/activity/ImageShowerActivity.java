package net.oschina.app.v2.activity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.ui.photoview.PhotoView;
import net.oschina.app.v2.ui.photoview.PhotoViewAttacher;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

public class ImageShowerActivity extends Activity {
	private PhotoView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_image);
		imageView = (PhotoView) findViewById(R.id.image);

		Bitmap bitmap = getIntent().getParcelableExtra("bitmap");
		if(bitmap!=null){
			imageView.setImageBitmap(bitmap);
			imageView.setScaleType(ScaleType.CENTER_CROP);
		}else{
			String imagePath=getIntent().getStringExtra("imagePath");
			bitmap = BitmapFactory.decodeFile(imagePath);
			imageView.setImageBitmap(bitmap);
			imageView.setScaleType(ScaleType.CENTER_CROP);
		}

		String imageUri = getIntent().getStringExtra("imageUri");
		ImageLoader.getInstance().displayImage(
				ApiHttpClient.getImageApiUrl(imageUri), imageView);
		//imageView.setScaleType(ScaleType.CENTER);
		
		imageView.setScaleType(ScaleType.FIT_CENTER);

		// Thread thread = new Thread(new LoadImageThread(
		// ApiHttpClient.getImageApiUrl(imageUri)));
		// thread.start();
		imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
			@Override
			public void onPhotoTap(View view, float x, float y) {
				finish();
			}
		});
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bitmap bitmap = (Bitmap) msg.getData().get("pic");
			imageView.setImageBitmap(bitmap);
		}
	};

	class LoadImageThread implements Runnable {
		private String url;

		public LoadImageThread(String url) {
			this.url = url;
		}

		@Override
		public void run() {
			try {
				Bitmap bitmap = loadImageFromUrl(url);
				Message msg = new Message();
				msg.getData().putParcelable("pic", bitmap);
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Bitmap loadImageFromUrl(String url) throws Exception {
		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpGet getRequest = new HttpGet(url);

		HttpResponse response = client.execute(getRequest);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			Log.e("LoadImage", "Request URL failed, error code =" + statusCode);
		}

		HttpEntity entity = response.getEntity();
		if (entity == null) {
			Log.e("LoadImage", "HttpEntity is null");
		}
		InputStream is = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			is = entity.getContent();
			byte[] buf = new byte[1024];
			int readBytes = -1;
			while ((readBytes = is.read(buf)) != -1) {
				baos.write(buf, 0, readBytes);
			}
		} finally {
			if (baos != null) {
				baos.close();
			}
			if (is != null) {
				is.close();
			}
		}
		byte[] imageArray = baos.toByteArray();
		return BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
	}
}
