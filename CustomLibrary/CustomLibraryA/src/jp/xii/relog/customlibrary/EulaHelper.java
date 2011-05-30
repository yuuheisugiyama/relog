package jp.xii.relog.customlibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * EULA表示ヘルパークラス
 * @author Iori
 *
 */
public class EulaHelper {

	private Activity _parentActivity = null;	//呼び出し元アクティビティ
	private boolean _isAgreed = false;			//同意してるか
	private String _appName = "";				//アプリの名称
	private String _eulaMessage = "";			//メッセージ
	
	/**
	 * 同意しているか
	 * @param _isAgreed the _isAgreed to set
	 */
	public void setIsAgreed(boolean _isAgreed) {
		this._isAgreed = _isAgreed;
	}
	/**
	 * 同意しているか
	 * @return the _isAgreed
	 */
	public boolean isAgreed() {
		return _isAgreed;
	}
	
	/**
	 * アプリの名称を設定する
	 * @param _appName the _appName to set
	 */
	public void setAppName(String _appName) {
		this._appName = _appName;
	}
	/**
	 * アプリの名称を取得する
	 * @return the _appName
	 */
	public String getAppName() {
		return _appName;
	}
	
	/**
	 * EULA本文を設定する
	 * まるごと書き換えるので注意
	 * @param _eulaMessage the _eulaMessage to set
	 */
	public void setEulaMessage(String _eulaMessage) {
		this._eulaMessage = _eulaMessage;
	}
	/**
	 * EULA本文を取得する
	 * @return the _eulaMessage
	 */
	public String getEulaMessage() {
		return _eulaMessage;
	}
	/**
	 * EULA本文を初期状態にします
	 * activityが指定してないと消えるだけです
	 */
	public void clearEulaMessage(){
		if(_parentActivity == null){
			_eulaMessage = "";
		}else{
			try{
				_eulaMessage = String.format(_parentActivity.getString(R.string.eula_message)
						, getAppName()
						, _parentActivity.getString(R.string.eula_developer_name)
						, _parentActivity.getString(R.string.eula_cout)
						);
			}catch(Exception e){
			}
		}
		
	}

	
	/**
	 * コンストラクタ
	 * @param activity
	 */
	public EulaHelper(Activity activity, String app_name){
		_parentActivity = activity;
		_appName = app_name;
		clearEulaMessage();
	}
	
	/**
	 * 表示して結果を反映する
	 */
	public void show(){
		//読み込む
		load(_parentActivity);
		//実処理開始
		if(_parentActivity == null){
		}else if(isAgreed()){
			//既に同意している
		}else{
			AlertDialog.Builder ad = new AlertDialog.Builder(_parentActivity);
			ad.setTitle(_parentActivity.getString(R.string.eula_title));
			ad.setMessage(getEulaMessage());
			//OKボタン
			ad.setPositiveButton(R.string.eula_agree
							, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//同意したことを保存する
					save(_parentActivity, true);
				}
			});
			//Cancelボタン
			ad.setNegativeButton(R.string.eula_disagree
							, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//同意しないので終了する
					_parentActivity.finish();
				}
			});
			ad.create();
			ad.show();
		}
	}
	
	/**
	 * 同意したことを保存する
	 */
	private void save(Context context, boolean is_agreed){
		if(context == null){
		}else{
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
			if(pref == null){
			}else{
	    		SharedPreferences.Editor editor = pref.edit();
	    		editor.putBoolean(context.getString(R.string.eula_is_agreed_key), is_agreed);
	    		editor.commit();
			}
		}
	}
	/**
	 * 同意状況を読み込む
	 */
	public void load(Context context){
		if(context == null){
		}else{
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
			if(pref == null){
			}else{
				setIsAgreed(pref.getBoolean(context.getString(R.string.eula_is_agreed_key), false));
			}
		}
	}
	
}
