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
package jp.xii.relog.customlibrary.mediastore;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

/**
 * 音楽とか画像とかを端末のDBに登録する
 * @author Iori
 *
 */
public class MediaScannerNotifier 
	implements MediaScannerConnectionClient{

	private MediaScannerConnection _Connection = null; 
	private String _Path = null;
	private String _MimeType = null;
	private onMediaScannerNotifierListener _listener = null;
	
	public MediaScannerNotifier(Context context
								, String path, String mimeType
								, onMediaScannerNotifierListener listener) { 
        _Path = path;
        _MimeType = mimeType;
        _listener = listener;
        _Connection = new MediaScannerConnection(context, this);
        if(_Connection == null){
        }else{
            _Connection.connect(); 
        }
    } 	
	
	@Override
	public void onMediaScannerConnected() {
		if(_Connection == null){
		}else{
			_Connection.scanFile(_Path, _MimeType);
		}
	}

	@Override
	public void onScanCompleted(String path, Uri uri) {
		if(_Connection == null){
		}else{
			_Connection.disconnect(); 
		}
		if(_listener == null){
		}else{
			_listener.onScanCompleted(path, uri);
		}
	}
	
	
	public interface onMediaScannerNotifierListener{
		public void onScanCompleted(String path, Uri uri);
	}

}
