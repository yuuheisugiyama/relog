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

import jp.xii.relog.customlibrary.widget.RangeSelectBar;
import jp.xii.relog.customlibrary.widget.RangeSelectBar.onRangeSelectBarChangeListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class RangeSelectPreference extends OriginalDialogPreference {

	public final static String STR_ATTR_UNIT = "unit";		//単位
	public final static String STR_ATTR_VALUE_FORMAT = "value_format";	//値のフォーマット書式
	
	private int _max = 100;		//最大値
	private int _min = 0;		//最小値
	private int _step = 10;		//可変値
	
	private int _first = 40;		//最初の値
	private int _last = 80;			//最後の値
	private boolean _isLoop = true;		//左右がループするか

	private String _unitString = "";	//単位の文字列（サマリの表示用）
	private String _valueFormatString = "";	//値のフォーマット書式指定
	
	/**
	 * 最大値
	 * @param _max the _max to set
	 */
	public void setMax(int _max) {
		if(_max <= getMin()){
			_max = getMin() + 1; 
		}
		this._max = _max;
	}
	/**
	 * 最大値
	 * @return the _max
	 */
	public int getMax() {
		return _max;
	}

	/**
	 * 最小値
	 * @param _min the _min to set
	 */
	public void setMin(int _min) {
		if(_min >= getMax()){
			_min = getMax() - 1;
		}if(_min < 0){
			_min = 0;
		}
		this._min = _min;
	}
	/**
	 * 最小値
	 * @return the _min
	 */
	public int getMin() {
		return _min;
	}
		
	/**
	 * 可変値
	 * @param _step the _step to set
	 */
	public void setStep(int _step) {
		this._step = _step;
	}
	/**
	 * 可変値
	 * @return the _step
	 */
	public int getStep() {
		return _step;
	}
	
	
	/**
	 * 最初の値
	 * @param _first the _first to set
	 */
	public void setFirst(int _first) {
		this._first = _first;
	}
	/**
	 * 最初の値
	 * @return the _first
	 */
	public int getFirst() {
		return _first;
	}
	/**
	 * 最後の値
	 * @param _last the _last to set
	 */
	public void setLast(int _last) {
		this._last = _last;
	}
	/**
	 * 最後の値
	 * @return the _last
	 */
	public int getLast() {
		return _last;
	}
	/**
	 * 左右がループするか
	 * @param _isLoop the _isLoop to set
	 */
	public void setIsLoop(boolean _isLoop) {
		this._isLoop = _isLoop;
	}
	/**
	 * 左右がループするか
	 * @return the _isLoop
	 */
	public boolean isLoop() {
		return _isLoop;
	}

	/**
	 * 単位の文字列（サマリの表示用）
	 * @param _unitString the _unitString to set
	 */
	public void setUnitString(String _unitString) {
		this._unitString = _unitString;
	}
	/**
	 * 単位の文字列（サマリの表示用）
	 * @return the _unitString
	 */
	public String getUnitString() {
		return _unitString;
	}
	
	/**
	 * 値のフォーマット書式指定
	 * @param _valueFormatString the _valueFormatString to set
	 */
	public void setValueFormatString(String _valueFormatString) {
		this._valueFormatString = _valueFormatString;
	}
	/**
	 * 値のフォーマット書式指定
	 * @return the _valueFormatString
	 */
	public String getValueFormatString() {
		return _valueFormatString;
	}
	
	
	/**
	 * コンストラクタ
	 * @param context
	 * @param attrs
	 */
	public RangeSelectPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		String temp = null;

		//最大値
		temp = attrs.getAttributeValue(null, RangeSelectBar.STR_ATTR_MAX);
		if(temp != null){
			setMax(Integer.parseInt(temp));
		}
		//最小値
		temp = attrs.getAttributeValue(null, RangeSelectBar.STR_ATTR_MIN);
		if(temp != null){
			setMin(Integer.parseInt(temp));
		}
		//可変値
		temp = attrs.getAttributeValue(null, RangeSelectBar.STR_ATTR_STEP);
		if(temp != null){
			setStep(Integer.parseInt(temp));
		}
		
		//最初の値の初期値
		temp = attrs.getAttributeValue(null, RangeSelectBar.STR_ATTR_DEFAULT_FIRST);
		if(temp != null){
			setFirst(Integer.parseInt(temp));
		}
		//最後の値の初期値
		temp = attrs.getAttributeValue(null, RangeSelectBar.STR_ATTR_DEFAULT_LAST);
		if(temp != null){
			setLast(Integer.parseInt(temp));
		}

		//単位
		temp = attrs.getAttributeValue(null, STR_ATTR_UNIT);
		if(temp != null){
			setUnitString(temp);
		}
		
		//書式指定文字列
		temp = attrs.getAttributeValue(null, STR_ATTR_VALUE_FORMAT);
		if(temp != null){
			setValueFormatString(temp);
		}
		

		//不正な値を治す
		if(getFirst() < getMin()){
			setFirst(getMin());
		}
		if(getLast() > getMax()){
			setLast(getMax());
		}
	}
	

	/**
	 * 表示したときに呼ばれる
	 */
	@Override
	protected void onBindView(View view) {
		
		//設定読込
		SharedPreferences pref = getSharedPreferences();
		if(pref == null){
		}else{
			String temp = "";
			String[] values = null;
			temp = pref.getString(getKey(), temp);
			values = temp.split(",");
			if(values == null){
			}else if(values.length < 2){
			}else{
				setFirst(Integer.valueOf(values[0]));
				setLast(Integer.valueOf(values[1]));
			}
		}
		
		//サマリに表示する
		if(getUnitString().length() == 0){
			setSummary("Range : " + getFirst() + " - " + getLast());
		}else{
			setSummary("Range(" + getUnitString() + ") : " + getFirst() + " - " + getLast());
		}
		
		super.onBindView(view);
	}

	/**
	 * クリックイベント
	 */
	@Override
	protected void onClick() {


	    //inflaterを使ってxmlのレイアウトをViewに反映する
	    LayoutInflater inflater = (LayoutInflater)getContext()
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View v = inflater.inflate(jp.xii.relog.customlibrary.R.layout.range_select_preference_dialog, null);
		final RangeSelectBar bar = (RangeSelectBar)v.findViewById(jp.xii.relog.customlibrary.R.id.RangeSelect_RangeSelectBar);
		final TextView text_first = (TextView)v.findViewById(jp.xii.relog.customlibrary.R.id.LavelFirst_TextView);
		final TextView text_last = (TextView)v.findViewById(jp.xii.relog.customlibrary.R.id.LavelLast_TextView);
		TextView text_min = (TextView)v.findViewById(jp.xii.relog.customlibrary.R.id.LavelMin_TextView);
		TextView text_max = (TextView)v.findViewById(jp.xii.relog.customlibrary.R.id.LavelMax_TextView);
		TextView text_center = (TextView)v.findViewById(jp.xii.relog.customlibrary.R.id.LavelCenter_TextView);
		bar.setMax(getMax());
		bar.setMin(getMin());
		bar.setFirst(getFirst());
		bar.setLast(getLast());
		bar.setStep(getStep());
		setFormatText(text_first, getFirst());
		setFormatText(text_last, getLast());
		setFormatText(text_max, getMax());
		setFormatText(text_min, getMin());
		setFormatText(text_center, (getMax() + getMin()) / 2);
		
		//値変更時のイベント
		bar.setOnRangeSelectBarChangeListener(new onRangeSelectBarChangeListener() {
			@Override
			public void onProgressChanged(RangeSelectBar rangeSelectBar, int first,
					int last) {
				//ダイアログの現在の値を設定する
				setFormatText(text_first, first);
				setFormatText(text_last, last);
			}
		});
		
		
	    //ダイアログ表示
		showCustumDialog(getContext()
						, (String)getDialogTitle(), (String)getDialogMessage()
						, v, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//ダイアログのビューから結果を取得
				if(bar != null){
					setFirst(bar.getFirst());
					setLast(bar.getLast());
				}
				// 設定保存
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
				SharedPreferences.Editor editor = pref.edit();
				editor.putString(getKey(), getFirst() + "," + getLast());
				editor.commit();
				
				//表示を更新
				notifyChanged();
			}
		});
	}
	
	/**
	 * 書式指定の文字列をテキストビューに指定する
	 * @param v
	 * @param value
	 */
	private void setFormatText(TextView v, int value){
		if(v == null){
		}else{
			try{
				v.setText(String.format(getValueFormatString(), value));
			}catch(Exception e){
				v.setText(String.valueOf(value));
			}
		}
	}
	
}
