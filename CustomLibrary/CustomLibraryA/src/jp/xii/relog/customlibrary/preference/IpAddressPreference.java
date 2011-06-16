/*
 * Copyright 2011 IoriAYANE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.xii.relog.customlibrary.preference;

import java.util.ArrayList;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;


public class IpAddressPreference extends OriginalDialogPreference 
	implements View.OnFocusChangeListener
			, View.OnClickListener{

	//パラメータ
	LinearLayout _layoutIp = null;
	ArrayList<EditText> _textNumbers = null;
	String _addressNumbers[] = {"0", "0", "0", "0"};

	/**
	 * レイアウトを取得
	 * @return
	 */
	public LinearLayout getLayoutIp(){
		if(_layoutIp == null){
			_layoutIp = new LinearLayout(getContext());
		}
		return _layoutIp;
	}

	/**
	 * IPアドレスの数値の配列
	 * @return
	 */
	public ArrayList<EditText> getTextNumbers(){
		if(_textNumbers == null){
			_textNumbers = new ArrayList<EditText>();
			for(int i=0; i<4; i++){
				EditText child = new EditText(getContext());
				child.setOnFocusChangeListener(this);
				child.setOnClickListener(this);
				_textNumbers.add(child);
				getLayoutIp().addView(child);
			}
		}
		return _textNumbers;
	}
	
	
	/**
	 * IPアドレスを取得する
	 * @return
	 */
	public String getIp(){
		return _addressNumbers[0] + "." 
			+ _addressNumbers[1] + "."
			+ _addressNumbers[2] + "."
			+ _addressNumbers[3];
	}
	
	/**
	 * IPを設定する
	 * @param ip
	 */
	public void setIp(String ip){
		//ピリオドで４つに分解
		String numbers[] = ip.split("\\.");
		if(getTextNumbers().size() != 4){
			//エディットが用意できてない
		}else if(numbers.length != 4){
			//IPアドレスじゃない
			ip = "0.0.0.0";
			for (int i=0; i<4; i++) {
				_addressNumbers[i] = "";
			}
		}else{
			//エディットにバラして設定
			if((numbers[0].compareTo("0") == 0)
					&& (numbers[1].compareTo("0") == 0)
					&& (numbers[2].compareTo("0") == 0)
					&& (numbers[3].compareTo("0") == 0)){
				//全部０の場合は空白
				for (int i=0; i<4; i++) {
					_addressNumbers[i] = "";
				}
			}else{
				//通常通り配置
				int i = 0;
				for (String number : numbers) {
					if(!isInt(number)){
						_addressNumbers[i] = "";
					}else{
						_addressNumbers[i] = number;
					}
					i++;
				}
			}
		}
	}
	
	/**
	 * コンストラクタ
	 * @param context
	 * @param attrs
	 */
	public IpAddressPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	
	
	/**
	 * 表示したときに呼ばれる
	 */
	@Override
	protected void onBindView(View view) {

		String ip = "0.0.0.0";
		
		//設定を読み込み
		SharedPreferences pref = getSharedPreferences();
		if(pref == null){
		}else{
			ip = pref.getString(getKey(), ip);
			
			setIp(ip);

		}

		//サマリーに現在値を設定
		setSummary((ip));

		//これはなぜか最後じゃないとイケないらしい
		super.onBindView(view);
	}

	/**
	 * プリファレンスのクリックイベント
	 */
	@Override
	protected void onClick(){

		//レイアウトを毎回造り直さないとダメ
		_layoutIp = null;
		_textNumbers = null;

		//レイアウト調節
		getLayoutIp().setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT
													, LayoutParams.WRAP_CONTENT));
		getLayoutIp().setWeightSum(1);
		getLayoutIp().setPadding(5, 0, 0, 5);
		getLayoutIp().setOrientation(LinearLayout.HORIZONTAL);
		for (EditText text : getTextNumbers()) {
			text.setInputType(InputType.TYPE_CLASS_NUMBER);
			text.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT
												, LayoutParams.FILL_PARENT
												, (float) 0.25));
		}

		//値を設定
		for(int i=0; i<4; i++){
			getTextNumbers().get(i).setText(_addressNumbers[i]);
		}

		//ダイアログ表示
		showCustumDialog(getContext(), (String)getDialogTitle(), (String)getDialogMessage()
							, (View)getLayoutIp(), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 設定保存
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
				SharedPreferences.Editor editor = pref.edit();
				int num[] = new int[4];
				for(int i=0; i<4; i++){
					//数値にいったん変換する
					num[i] = toInt(getTextNumbers().get(i).getText().toString());
					//範囲外チェック
					if(num[i] < 0){
						num[i] = 0;
					}
					if(num[i] > 255){
						num[i] = 255;
					}
				}
				editor.putString(getKey(), num[0] + "."	+ num[1] + "."
						+ num[2] + "." + num[3]
						);
				editor.commit();


				//表示を更新
				notifyChanged();
			}
		});

	}

	/**
	 * エディットにフォーカスがきたら内容を選択する
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(v.getClass() == EditText.class){
			if(hasFocus){
				EditText edit = (EditText)v;
//				InputMethodManager inputMethodManager = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//				inputMethodManager.showSoftInput(edit, 0);
				edit.selectAll();
			}
			
//			Log.d("mpremocon", "edit focus=" + hasFocus);
		}
	}

	/**
	 * エディットのクリックイベント
	 */
	@Override
	public void onClick(View v) {
		if(v.getClass() == EditText.class){
			EditText edit = (EditText)v;
			if(edit.isSelected()){
			}else{
				edit.selectAll();
			}
//			Log.d("mpremocon", "edit click");
		}
	}
	
}
