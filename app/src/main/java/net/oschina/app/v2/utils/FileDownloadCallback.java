package net.oschina.app.v2.utils;

import java.io.File;

public interface FileDownloadCallback {
    void onPrepare();

    void onDownloadFailed(String errorMsg);

    void onTimeout();

    void onNoNetwork();

    void onCanceled();

    void onProgress(long bytesWritten, long totalSize);

    void onDownloadSuccess(File downFile);
}
