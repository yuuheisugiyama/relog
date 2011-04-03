package jp.xii.relog.customlibrary.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.KeyEvent;

public abstract class OriginalPreferenceActivity extends PreferenceActivity {

	/**
	 * 設定をサマリーに表示する
	 */
	protected abstract void updateSettingSummary();	
	
	
	/**
	 * 停止から再開
	 */
	@Override
	protected void onResume() {
	  super.onResume();
	  getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(_listener);
	}
	/**
	 * 一時停止
	 */
	@Override
	protected void onPause() {
	  super.onPause();
	  getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(_listener);
	}
	/**
	 * プリファレンスの変更時のイベントハンドラ
	 */
	private SharedPreferences.OnSharedPreferenceChangeListener _listener = 
	  new SharedPreferences.OnSharedPreferenceChangeListener() {
	    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
	    	updateSettingSummary();
	    }
	};

	/**
	 *キーイベント処理 
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event){
		//BACKキー
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
				//パラメータ保存（受け渡し用）

				//アクティビティの終了
				setResult(RESULT_OK);
				finish();
			}
		}
		return super.dispatchKeyEvent(event);
	}

	
	/**
	 * まとめてクリックリスナーを登録する
	 * @param context 呼び出しパッケージのコンテキスト
	 * @param res_ids プリファレンスkeyにしている文字列のリソースID
	 * @param listener リスナー
	 */
	protected void setOnPreferenceClickListener(Context context, int[] res_ids, OnPreferenceClickListener listener){
		if((context == null) || (res_ids == null) || (listener == null)){
		}else{
			Preference pref;

			for (int id : res_ids) {
				pref = this.findPreference(context.getString(id));
				if(pref != null){
					pref.setOnPreferenceClickListener(listener);
				}
			}
		}
	}
	
	/**
	 * まとめて有効無効を設定する
	 * @param context 呼び出しパッケージのコンテキスト
	 * @param res_ids プリファレンスkeyにしている文字列のリソースID
	 * @param enabled 有効にするか無効にするか
	 */
	protected void setPreferenceEnabled(Context context, int[] res_ids, boolean enabled){
		if((context == null) || (res_ids == null)){
		}else{
			Preference pref;

			for (int id : res_ids) {
				pref = this.findPreference(context.getString(id));
				if(pref != null){
					pref.setEnabled(enabled);
				}
			}
		}
	}

	
	/**
	 * リストプリファレンスのサマリーを選択内容でセットする
	 * @param pref
	 * @param values_id
	 * @param entries_id
	 */
	protected void setListPreferenceSummary(Preference pref
										, int values_id, int entries_id
										, String set_value){
		if(pref == null){
		}else{
			String[] values = getResources().getStringArray(values_id);
			String[] entries = getResources().getStringArray(entries_id);
			for(int i=0; i<values.length; i++){
				if(values[i].compareTo(set_value) == 0){
					pref.setSummary(entries[i]);
				}
			}
		}
	}

}
