package com.shanchain.shandata.receiver;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import java.io.File;
/**
 * Created by flyye on 2017/11/24.
 */

public class DownloadCompleteReceiver extends BroadcastReceiver{

        private DownloadManager manager ;
        @Override
        public void onReceive(Context context, Intent intent) {
            manager =(DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
            if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                Query query = new Query();
                query.setFilterById(downloadId);
                Cursor myDownload = manager.query(query);
                if (myDownload.moveToFirst()) {
                    int fileNameIdx = myDownload.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                    String fileName = myDownload.getString(fileNameIdx);
                    installAPK(fileName,context);
                }
            }
        }

        private void installAPK(String  filePath,Context context) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(new File(filePath)),"application/vnd.android.package-archive");
            context.startActivity(intent);
        }

}
