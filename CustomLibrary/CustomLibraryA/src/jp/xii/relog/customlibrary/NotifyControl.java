package jp.xii.relog.customlibrary;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

public class NotifyControl {
	
	private Activity _activity = null;
	private int _icon_resourse_id = android.R.drawable.stat_sys_warning;
	
	//アップデート間隔の制御
	private long _lastUpdateTime = 0;		//前回アップデートした時間
	private long _updateInterval = 500;		//更新間隔(デフォルト500msec)
	
	//通知領域表示関係
	private NotificationManager _notificationManager = null;	
	private Notification _notification = null;
	private Intent _notifyIntent = null;
	private PendingIntent _contentIntent = null;


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
	 * 更新間隔
	 * @param _updateInterval the _updateInterval to set
	 */
	public void setUpdateInterval(long _updateInterval) {
		this._updateInterval = _updateInterval;
	}
	/**
	 * 更新間隔
	 * @return the _updateInterval
	 */
	public long getUpdateInterval() {
		return _updateInterval;
	}

	/**
	 * コンストラクタ
	 * @param context
	 */
	public NotifyControl(Activity activity, int icon){
		_activity = activity;
		setIcon(icon);
	}
	
	public NotifyControl(){
		
	}
	
	/**
	 * Notifyの初期化
	 * @param context
	 */
	public void initNotify(String message){
		initNotify(getActivity(), _icon_resourse_id, message);
	}

	/**
	 * Notifyの初期化
	 * @param activity
	 * @param icon
	 * @param message
	 */
	public void initNotify(Activity activity, int icon, String message){
		if((activity == null) || (message == null)){
		}else{
			_notificationManager = (NotificationManager)activity.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			_notification = new Notification(
						 	icon,
							message,
							System.currentTimeMillis());
			_notifyIntent = activity.getIntent();
			_contentIntent = PendingIntent.getActivity(activity, 0, _notifyIntent, 0);
			
			//登録したらクリア
			_lastUpdateTime = System.currentTimeMillis();
		}
	}
	
	/**
	 * Notifyを表示する
	 * @param title
	 * @param message
	 * @param app_name
	 */
	public void viewNotify(String title, String message, int app_name){
		viewNotify(getActivity(), title, message, app_name);
	}	
	
	/**
	 * Notifyを表示する
	 * @param activity
	 * @param title
	 * @param message
	 * @param app_name
	 */
	public void viewNotify(Activity activity, String title, String message, int app_name){
		long now = System.currentTimeMillis();
		
		if((_notification == null) || (_notificationManager == null)
				|| (activity == null)){
		}else if((now - _lastUpdateTime) < getUpdateInterval()){
			//呼出が早過ぎる
		}else{
			_notification.when = now;
			_notification.setLatestEventInfo(
					activity,
					title,
					message,
					_contentIntent);
			
			_notificationManager.notify(app_name, _notification);

			//前回の更新時間を保存
			_lastUpdateTime = now;
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
