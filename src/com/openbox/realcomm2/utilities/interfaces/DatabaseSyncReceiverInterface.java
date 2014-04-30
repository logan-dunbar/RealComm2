package com.openbox.realcomm2.utilities.interfaces;

import android.content.Intent;

public interface DatabaseSyncReceiverInterface
{
	void onCheckUpdateReceive(Intent intent);
	void onDownloadDatabaseReceive(Intent intent);
	void bindCheckUpdateReceiver();
	void unbindCheckUpdateReceiver();
	void bindDownloadDatabaseRecever();
	void unbindDownloadDatabaseReceiver();
}
