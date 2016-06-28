package net.oschina.app.v2.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.shiyanzhushou.app.R;

import net.oschina.app.v2.base.BaseApplication;

import org.apache.http.Header;

import java.io.File;

public class HttpRequestUtils {
    private final static String TAG = "HttpRequestUtils";

    public static RequestHandle downloadFile(String serviceFileUrl, String saveFilePath, final FileDownloadCallback callback) {
        Context context= BaseApplication.context();
        final Resources mResources = context.getResources();
        Log.i(TAG, "downloadFile = " + serviceFileUrl);

        callback.onPrepare();

        if (TextUtils.isEmpty(serviceFileUrl)) {
            callback.onDownloadFailed(mResources.getString(R.string.comm_network_download_path_null));
            return null;
        }

        if (!Connectivity.isConnected(context)) {
            callback.onNoNetwork();
            return null;
        }

        File saveFile = new File(saveFilePath);
        FileAsyncHttpResponseHandler responseHandler = new FileAsyncHttpResponseHandler(saveFile) {

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Log.i(TAG, "onFailure statusCode = " + statusCode);
                if (statusCode == 0) {
                    callback.onTimeout();
                } else {
                    callback.onDownloadFailed(mResources.getString(R.string.comm_download_fialed));
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                Log.i(TAG, "onSuccess statusCode = " + statusCode);
                Log.i(TAG, "onSuccess file length = " + file.length());
                if (file.length() == 0) {
                    callback.onDownloadFailed(mResources.getString(R.string.comm_download_fialed));
                } else {
                    callback.onDownloadSuccess(file);
                }
            }

            @Override
            public void onCancel() {
                callback.onCanceled();
            }
        };
        return HttpClientUtils.get(serviceFileUrl, responseHandler);
    }

    public static void releaseRequest(RequestHandle request) {
        if (request != null && !request.isFinished()) {
            request.cancel(true);
        }
    }
}
