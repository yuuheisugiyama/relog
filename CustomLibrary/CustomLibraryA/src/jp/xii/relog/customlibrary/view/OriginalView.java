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
package jp.xii.relog.customlibrary.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

public class OriginalView extends View {

	public OriginalView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 色情報を変換する
	 * @return
	 */
	protected int ColorToInt(String temp){
		int ret = 0;
		
		if(temp.indexOf("@color/") == 0){
			int id = getContext().getResources().getIdentifier(temp.replace("@color/",""), "color", getContext().getPackageName());
			ret = getContext().getResources().getColor(id);
		}else{
			ret = Color.parseColor(temp);
		}

		return ret;
	}
	
	/**
	 * dip値を座標値に変換する
	 * @param temp
	 * @return
	 */
	protected int DtoInt(String temp){
		int ret = 0;
		float density = getContext().getResources().getDisplayMetrics().density;
		
		if(temp.indexOf("dip") >= 0){
			temp = temp.replace("dip", "");
			ret = (int) (Float.valueOf(temp) * density);
		}else if(temp.indexOf("dp") >= 0){
			temp = temp.replace("dp", "");
			ret = (int) (Float.valueOf(temp) * density);
		}else if(temp.indexOf("sp") >= 0){
			temp = temp.replace("sp", "");
			ret = (int) (Float.valueOf(temp) * density);
		}else{
			try{
				ret = Integer.valueOf(temp);
			}catch(Exception e){
			}
		}

		return ret;
	}
	
	/**
	 * dip値を座標値に変換する
	 * @param temp
	 * @return
	 */
	protected int DtoInt(int temp){
		return (int) (temp * getContext().getResources().getDisplayMetrics().density);
	}

	
}
