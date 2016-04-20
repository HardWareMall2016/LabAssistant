package net.oschina.app.v2.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class DownloadHelper {

	public static final String LOG_TAG = "test";

	private File rootDir;
	private Context context;
	private ProgressDialog mProgressDialog;

	public DownloadHelper(Context context) {
		this.context = context;
		this.rootDir = Environment.getExternalStorageDirectory();
	}

	public void startDownload(String fileName, String fileUrl) {
		DownloadFileAsync download = new DownloadFileAsync();
		download.execute(fileName, fileUrl);
	}

	class DownloadFileAsync extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				// 连接地址
				URL u = new URL(params[1]);
				HttpURLConnection c = (HttpURLConnection) u.openConnection();
				c.setRequestMethod("GET");
				c.setDoOutput(true);
				c.connect();

				// 计算文件长度
				int lenghtOfFile = c.getContentLength();

				FileOutputStream f = new FileOutputStream(new File(rootDir
						+ "/my_downloads/", params[0]));

				InputStream in = c.getInputStream();

				// 下载的代码
				byte[] buffer = new byte[1024];
				int len1 = 0;
				long total = 0;

				while ((len1 = in.read(buffer)) > 0) {
					total += len1; // total = total + len1
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
					f.write(buffer, 0, len1);
				}
				f.close();

			} catch (Exception e) {
				Log.d(LOG_TAG, e.getMessage());
			}

			return null;
		}

		protected void onProgressUpdate(String... progress) {
			Log.d(LOG_TAG, progress[0]);
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		@Override
		protected void onPostExecute(String unused) {
			mProgressDialog.dismiss();
		}
	}

	public void checkAndCreateDirectory(String dirName) {
		File new_dir = new File(rootDir + dirName);
		if (!new_dir.exists()) {
			new_dir.mkdirs();
		}
	}

	private void showDialog() {
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setMessage("Downloading file...");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(true);
		mProgressDialog.show();

	}
}
