package jp.xii.relog.customlibrary;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotifyControl {
	
	private Activity _activity = null;
	private int _icon_resourse_id = android.R.drawable.stat_sys_warning;
	
	//通知領域表示関係
	private NotificationManager _notificationManager = null;	
	private Notification _notification = null;
	private Intent _notifyIntent = null;
	private PendingIntent _contentIntent = null;

	/**
	 * コンテキストの取得
	 * @return
	 */
	public Context getContext() {
		return _activity;
	}

	/**
	 * 親のアクティビティの取得
	 * @return
	 */
	public Activity getActivity(){
		return _activity;
	}
	
	/**
	 * アイコンのリソースIDの指定
	 * @param icon
	 */
	public void setIcon(int icon) {
		_icon_resourse_id = icon;
	}


	/**
	 * コンストラクタ
	 * @param context
	 */
	public NotifyControl(Activity activity, int icon){
		_activity = activity;
		setIcon(icon);
	}
	
	/**
	 * Notifyの初期化
	 * @param context
	 */
	public void initNotify(String message){
		
		_notificationManager = (NotificationManager)getActivity().getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		_notification = new Notification(
					 	_icon_resourse_id,
						message,
						System.currentTimeMillis());
		_notifyIntent = getActivity().getIntent();
		_contentIntent = PendingIntent.getActivity(getContext(), 0, _notifyIntent, 0);
	}
	
	/**
	 * Notifyを表示する
	 * @param context
	 * @param message
	 */
	public void viewNotify(String title, String message, int app_name){

		if(_notification == null && _notificationManager == null){
		}else{
			_notification.when = System.currentTimeMillis();
			_notification.setLatestEventInfo(
					getContext(),
					title,
					message,
					_contentIntent);
			
			_notificationManager.notify(app_name, _notification);
		}
	}

	/**
	 * Notifyを消す
	 */
	public void clearNotify(){
		if(_notificationManager != null){
			_notificationManager.cancelAll();
		}
	}
	
}
