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
package jp.xii.relog.customlibrary;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.http.protocol.HTTP;

public class DoSuCommand {

	Process _process = null;				//suプロセス
	DataOutputStream _outputStream = null;	//出力ストリーム
	DataInputStream _inputStream = null;	//入力ストリーム
	
	
	public boolean init(){
		boolean ret = false;
		
		try {
			//su実行
			_process = Runtime.getRuntime().exec("su");
			
			//入出力ストリーム取得
			_outputStream = new DataOutputStream(_process.getOutputStream());
			_inputStream = new DataInputStream(_process.getInputStream());
			
			//バージョンを取得する
			ret = true;
//			if(!write("su -v\n")){
//			}else{
//				String[] results = read().split("\n");
//				for (String line : results) {
//					if(line.length() > 0){
//						//バージョンがとれたので成功
//						ret = true;
//					}
//				}
//			}
		} catch (IOException e) {
		}
		
		return ret;
	}
	
	public void deinit(){
		if(_inputStream != null){
			try {
				_inputStream.close();
			} catch (IOException e) {
			}
		}
		if(_outputStream != null){
			try {
				if(_process != null){
					//プロセスと出力ストリームがある場合は
					//シェルを終了する
					_outputStream.writeBytes("exit\n");
					_outputStream.flush();
					try {
						//シェルの終了を待つ
						_process.waitFor();
					} catch (InterruptedException e) {
					}
				}
				_outputStream.close();
			} catch (IOException e) {
			}
		}
		
		if(_process != null){
			_process.destroy();
		}
		
		_outputStream = null;
		_inputStream = null;
		_process = null;
	}
	
	/**
	 * コマンドをsuシェルへ投げる
	 * 最後に\nを付けるまで実行されない
	 * @param command
	 */
	public boolean write(String command){
		boolean ret = false;
		if(_outputStream == null){
		}else{
			try {
				_outputStream.writeBytes(command);
				_outputStream.flush();
				ret = true;
			} catch (IOException e) {
			}
		}
		return ret;
	}
	
	/**
	 * suシェルの出力を文字列にする
	 * かならず結果が戻ってくる時に使う
	 * それ以外で使うともどってこなくなるので注意
	 * もちろん結果は標準出力でエラー出力はダメ
	 * コマンドの結果が複数行になる場合は
	 * splitなどを使ってバラして使う
	 * @param timeout
	 * @return
	 */
	public String read(){//int timeout){
		String ret = "";
		
		if(_inputStream == null){
		}else{
			int size = 0;
			byte[] buffer = new byte[1024];
//			long start_time = System.currentTimeMillis();
			
			try {
				do {
					size = _inputStream.read(buffer);
					if(size > 0){
						ret += new String(buffer, 0, size, HTTP.UTF_8);
					}
				}while(_inputStream.available() > 0);
//				do{
//					while(_inputStream.available() > 0){
//						size = _inputStream.read(buffer);
//						if(size > 0){
//							ret += new String(buffer, 0, size, HTTP.UTF_8);
//						}
//					}
//
//					//なにか取得できてたら終了
//					if(ret.length() > 0){
//						break;
//					}else{
//						try {
//							Thread.sleep(10);
//						} catch (InterruptedException e) {
//						}
//					}
//					
//					//タイムアウト時間がきてなければ繰り返す
//				}while((System.currentTimeMillis() - start_time) < timeout);
			} catch (IOException e) {
			}
		}

		return ret;
	}
}
