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

import java.io.File;
import java.util.Date;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class MediaStoreAccessAudio extends MediaStoreAccess {

	
	public class AudioInfo{
		private int _id = -1;
		private int _audioId = -1;
		private int _playlistId = -1;
		private int _artistId = -1;
		private int _albumId = -1;
		private int _playOrder = -1;

		private String _title = "";
		private String _name = "";
		private String _artist = "";
		private String _album = "";
		private String _year = "";
		private String _composer = "";
		
		private Date _date = null;
		private Date _modifiedDate = null;

		private File _file = null;
		private String _displayName = "";
		private long _size = 0;
		private String _mimeType = "";
		private int _hash = 0;

		public int getId() {
			return _id;
		}
		public void setId(int _id) {
			this._id = _id;
		}
		public int getAudioId() {
			return _audioId;
		}
		public void setAudioId(int _audioId) {
			this._audioId = _audioId;
		}
		public int getPlaylistId() {
			return _playlistId;
		}
		public void setPlaylistId(int _playlistId) {
			this._playlistId = _playlistId;
		}
		public int getArtistId() {
			return _artistId;
		}
		public void setArtistId(int _artistId) {
			this._artistId = _artistId;
		}
		public int getAlbumId() {
			return _albumId;
		}
		public void setAlbumId(int _albumId) {
			this._albumId = _albumId;
		}
		public int getPlayOrder() {
			return _playOrder;
		}
		public void setPlayOrder(int _playOrder) {
			this._playOrder = _playOrder;
		}
		public String getTitle() {
			return _title;
		}
		public void setTitle(String _title) {
			this._title = _title;
		}
		/**
		 * @param _name the _name to set
		 */
		public void setName(String _name) {
			this._name = _name;
		}
		/**
		 * @return the _name
		 */
		public String getName() {
			return _name;
		}
		public String getArtist() {
			return _artist;
		}
		public void setArtist(String _artist) {
			this._artist = _artist;
		}
		public String getAlbum() {
			return _album;
		}
		public void setAlbum(String _album) {
			this._album = _album;
		}
		public String getYear() {
			return _year;
		}
		public void setYear(String _year) {
			this._year = _year;
		}
		public String getComposer() {
			return _composer;
		}
		public void setComposer(String _composer) {
			this._composer = _composer;
		}
		public Date getDate() {
			if(_date == null){
				_date = new Date();
			}
			return _date;
		}
		public void setDate(Date _date) {
			this._date = _date;
		}
		public Date getModifiedDate() {
			if(_modifiedDate == null){
				_modifiedDate = new Date();
			}
			return _modifiedDate;
		}
		public void setModifiedDate(Date _modifiedDate) {
			this._modifiedDate = _modifiedDate;
		}
		public File getFile() {
			if(_file == null){
				_file = new File("/");
			}
			return _file;
		}
		public void setFile(File _file) {
			this._file = _file;
		}
		public String getDisplayName() {
			return _displayName;
		}
		public void setDisplayName(String _displayName) {
			this._displayName = _displayName;
		}
		public long getSize() {
			return _size;
		}
		public void setSize(long size) {
			this._size = size;
		}
		public String getMimeType() {
			return _mimeType;
		}
		public void setMimeType(String _mimeType) {
			this._mimeType = _mimeType;
		}
		/**
		 * @param _hash the _hash to set
		 */
		public void setHash(int _hash) {
			this._hash = _hash;
		}
		/**
		 * @return the _hash
		 */
		public int getHash() {
			return _hash;
		}
	}
	
	
	public static final int INVALID_USER_PLAYLIST_ID = -1;	//不正なプレイリストID
	public static final int INVALID_AUDIO_ID = -1;			//不正なオーディオID
	
	ContentResolver _contentResolver = null;
	
	public MediaStoreAccessAudio(Activity activity) {
		super(activity);
		
		_contentResolver = activity.getContentResolver();
	}

	
	/**
	 * カーソルからオーディオ情報を取得する
	 * @param cursor
	 * @return
	 */
	public AudioInfo getAudioInfo(Cursor cursor){
		AudioInfo info = null;
		
		if(cursor == null){
		}else{
			info = new AudioInfo();
			String name;
			boolean is_first_id = true;
			
			for(int i=0;i<cursor.getColumnCount();i++){
				try{
					name = cursor.getColumnName(i);
					
					if(is_first_id && name.compareTo("_id") == 0){
						info.setId(cursor.getInt(i));
						is_first_id = false;
					}else if(name.compareTo("audio_id") == 0){
						info.setAlbumId(cursor.getInt(i));
					}else if(name.compareTo("playlist_id") == 0){
						info.setPlaylistId(cursor.getInt(i));
					}else if(name.compareTo("play_order") == 0){
						info.setPlayOrder(cursor.getInt(i));
					}else if(name.compareTo("_data") == 0){
						info.setFile(new File(cursor.getString(i)));
					}else if(name.compareTo("_display_name") == 0){
						info.setDisplayName(cursor.getString(i));
					}else if(name.compareTo("_data_hashcode") == 0){
						info.setHash(cursor.getInt(i));
					}else if(name.compareTo("_size") == 0){
						info.setSize(cursor.getLong(i));
					}else if(name.compareTo("mime_type") == 0){
						info.setMimeType(cursor.getString(i));
					}else if(name.compareTo("date_added") == 0){
						info.setDate(new Date(cursor.getInt(i)));
					}else if(name.compareTo("date_modified") == 0){
						info.setModifiedDate(new Date(cursor.getInt(i)));
					}else if(name.compareTo("title") == 0){
						info.setTitle(cursor.getString(i));
					}else if(name.compareTo("name") == 0){
						info.setName(cursor.getString(i));
					}else if(name.compareTo("artist_id") == 0){
						info.setAlbumId(cursor.getInt(i));
					}else if(name.compareTo("album_id") == 0){
						info.setAlbumId(cursor.getInt(i));
					}else if(name.compareTo("year") == 0){
						info.setYear(cursor.getString(i));
					}else if(name.compareTo("artist") == 0){
						info.setArtist(cursor.getString(i));
					}else if(name.compareTo("album") == 0){
						info.setAlbum(cursor.getString(i));
					}else if(name.compareTo("composer") == 0){
						info.setComposer(cursor.getString(i));
					}else{
					}
					
					Log(" " + name + " : " + cursor.getString(i));
				}catch(Exception e){
					Log(" " + cursor.getColumnName(i) + " : error");
				}
			}
		}
				
		return info;
	}
	
	
	/**
	 * プレイリストを作成する
	 * @param name
	 * @param images_uri
	 * @param thumb
	 */
	public int createUserPlaylist(String name, Uri images_uri, String thumb){
		int ret = INVALID_USER_PLAYLIST_ID;
		ContentValues contentvalues;
		Uri result_uri = null;
        
		if(_contentResolver == null){
			//NG
		}else if(isExistUserPlaylist(name)){
			//すでに登録されてる
			ret = getUserPlaylistId(name);
		}else{
			int image_index = -1;
			if(images_uri != null){
				image_index = (int)ContentUris.parseId(images_uri);
			}
			//データ作成
			switch(getPhoneType()){
			default:
			case Standard:
				contentvalues = new ContentValues(1);
				contentvalues.put("name", name);
				break;
			case GalaxyS:
				contentvalues = new ContentValues(3);
				contentvalues.put("name", name);
				Integer integer = Integer.valueOf(image_index);
				contentvalues.put("images_id", integer);
				contentvalues.put("thumbnail_uri", thumb);
				break;
			}
			
			//追加
			Uri playlist_uri = getPlaylistContentsUri(StoragePlaceType.External);
			result_uri = _contentResolver.insert(playlist_uri, contentvalues);
			
			if(result_uri == null){
				//NG
				Log("fail add playlist : " + name + ", is null");
			}else if((ret = (int)ContentUris.parseId(result_uri)) == -1){
				//NG
				Log("fail add playlist : " + name + ", " + result_uri.toString());
			}else{
				//OK
				Log("add playlist : " + name + "," + ret);
			}
		}

		return ret;
	}
	
	/**
	 * プレイリストの名前を変更する
	 * @param id
	 * @param name
	 * @return
	 */
	public boolean updateUserPlaylist(int id, String name){
		boolean ret = false;
		
		if(_contentResolver == null){
			//NG
		}else if(isExistUserPlaylist(name)){
			//すでに登録されてる
			Log("already exist playlist : " + name);
		}else{
			Uri uri = null;
			uri = getPlaylistContentsUri(StoragePlaceType.External);
	        if(uri == null){
	        }else{
				ContentValues contentvalues;
				String as[];
				int k = -1;
				contentvalues = new ContentValues(1);
				contentvalues.put("name", name);
				as = new String[1];
				as[0] = Integer.toString(id);
				k = _contentResolver.update(uri, contentvalues, "_id = ?", as);
				if(k != 1){
					Log("fail update playlist : " + name + ", " + k);
				}else{
					ret = true;
				}
	        }
		}
		
		return ret;
	}
	
	/**
	 * プレイリストに曲を追加する
	 * @param playlist_id
	 * @param audio_id
	 * @return
	 */
	public boolean addItemToUserPlaylist(int playlist_id, int audio_id){
		boolean ret = false;
		
		if(_contentResolver == null){
		}else if(isExistItemInUserPlaylist(playlist_id, audio_id)){
			//既にある
			ret = true;
		}else{
			Uri uri = getPlaylistsMembersContentsUri(StoragePlaceType.External, playlist_id);
			if(uri == null){
			}else{
				Uri result_uri = null;
				ContentValues contentvalues = new ContentValues();
                contentvalues.put("play_order", getUserListMaxPlayOrder(playlist_id) + 1);
				
				switch(getPhoneType()){
				default:
				case Standard:
	                //SQLパラメータを設定する
	                contentvalues.put("audio_id", Integer.valueOf(audio_id));
					break;
				case GalaxyS:
					String data = "";
					int data_hash = 0;
					//目的のIDの曲情報を取得する
					Uri music_uri = getMediaContentsUri(StoragePlaceType.External);
					String as[] = new String[2];
					as[0] = "_data";
					as[1] = "_data_hashcode";
					String where = "_id = " + audio_id;
					Cursor cursor = null;
					try{
						cursor = _contentResolver.query(music_uri, as, where, null, null);
					}catch(Exception e){
					}
					if(cursor == null){
						Log("fail query music : " + audio_id );
					}else{
						cursor.moveToFirst();
						data = cursor.getString(0);
						data_hash = cursor.getInt(1);
						cursor.close();
					}
					
					//SQLパラメータを設定する
					contentvalues.put("audio_data", data);
					contentvalues.put("audio_data_hashcode", data_hash);
					break;
				}

				//追加
				result_uri = _contentResolver.insert(uri, contentvalues);
				if(result_uri == null){
					//NG
					Log("fail insert music : " + playlist_id + ", " + audio_id + ", is null");
				}else if(((int)ContentUris.parseId(result_uri)) == -1){
					//NG
					Log("fail insert music : " + playlist_id + ", " + audio_id + ", " + result_uri.toString());
				}else{
					//OK
					ret = true;
				}

			}
		}
		
		return ret;
	}

	
	/**
	 * プレイリストを削除する
	 * @param ids
	 * @return
	 */
	public boolean removeUserPlaylists(int[] ids){
		boolean ret = false;
		
		if(_contentResolver == null){
		}else if(ids == null){
		}else{
			Uri uri = getPlaylistContentsUri(StoragePlaceType.External);
			ret = removeItems(uri, ids);
		}		
		
		return ret;
	}
	

	/**
	 * 登録されているアイテムを削除する
	 * @param uri
	 * @param ids
	 * @return
	 */
	protected boolean removeItems(Uri uri, int[] ids){
		boolean ret = false;
		
		if(_contentResolver == null){
		}else if(uri == null || ids == null){
		}else{
			String where = "_id IN(";
			for(int i=0; i<ids.length; i++){
				where += Integer.valueOf(ids[i]);
				if(i < (ids.length -1)){
					where += ", ";
				}
			}
			where += ")";
			
			int nums = _contentResolver.delete(uri, where, null);
			if(nums > 0){
				ret = true;
			}
		}		
		
		return ret;
	}
	
	/**
	 * プレイリストの中身をクリアする
	 * @param playlist_id
	 */
	public boolean clearItemsInPlaylist(int playlist_id){
		boolean ret = false;
		if(_contentResolver == null){
		}else{
			Uri uri = getPlaylistsMembersContentsUri(StoragePlaceType.External, playlist_id);
			if(uri == null){
			}else{
				_contentResolver.delete(uri, null, null);
				ret = true;
			}
		}
		return ret;
	}

	
	/**
	 * プレイリストが登録されているか確認
	 * @param name
	 * @return
	 */
    public boolean isExistUserPlaylist(String name)
    {
		boolean ret = false;
		
		if(_contentResolver == null){
		}else{
			Uri uri = getPlaylistContentsUri(StoragePlaceType.External);
			String as[] = new String[1];
			as[0] = "_id";
			String as1[] = new String[1];
			as1[0] = name;
			Cursor cursor = _contentResolver.query(uri, as, "name= ?", as1, "name");
			if(cursor != null){
				if(cursor.getCount() > 0){
					ret = true;
				}
				cursor.close();
			}
		}

		return ret;
    }
    
    /**
     * プレイリストに該当の曲が含まれているか
     * @param id
     * @return
     */
    public boolean isExistItemInUserPlaylist(int playlist_id, int audio_id){
    	boolean ret = false;
    	
		if(_contentResolver == null){
		}else{
			Uri uri = getPlaylistsMembersContentsUri(StoragePlaceType.External, playlist_id);
			String as[] = new String[1];
			String where = "";
			
			switch(getPhoneType()){
			default:
			case Standard:
				as[0] = "_id";
				where = "audio_id=" + audio_id;
				break;
			case GalaxyS:
				//通常のリストからハッシュを取得して再検索じゃないとダメかな・・・？
				as[0] = "music_audio_playlists_map._id";
				where = "music_audio_playlists_map._id=" + audio_id;
				break;
			}
			
			Cursor cursor = null;
			try{
				cursor = _contentResolver.query(uri, as, where, null, null);
			}catch(Exception e){
				Log( e.getMessage());
			}
			if(cursor != null){
				if(cursor.getCount() > 0){
					cursor.moveToFirst();
					ret = true;
				}
				cursor.close();
			}
		}
    	
    	return ret;
    }

	/**
	 * プレイリストのIDを取得する
	 * @param name
	 * @return
	 */
	public int getUserPlaylistId(String name){
		int ret = INVALID_USER_PLAYLIST_ID;
		
		if(_contentResolver == null){
		}else{
			Uri uri = getPlaylistContentsUri(StoragePlaceType.External);
			String as[] = new String[1];
			as[0] = "_id";
			String as1[] = new String[1];
			as1[0] = name;
			Cursor cursor = _contentResolver.query(uri, as, "name= ?", as1, "name");
			if(cursor == null){
			}else{
				if(cursor.getCount() == 0){
				}else{
					//目的のプレイリストIDを含むカーソルを探す
					cursor.moveToFirst();
					while(!cursor.isAfterLast()){
						
                    	try{
                    		if(cursor.getColumnName(0).toString().compareTo("_id") != 0){
                    		}else{
                    			ret = cursor.getInt(0);
                    		}
                    	}catch(Exception e){
                        	Log( cursor.getColumnName(0) + " : error");
                    	}
						cursor.moveToNext();
					}
				}
				cursor.close();
			}
		}

		return ret;
	}

	/**
	 * audio_idを取得する
	 * @param path フルパス
	 * @return
	 */
	public int getAudioId(String path){
		int ret = INVALID_AUDIO_ID;
		if(_contentResolver == null){
		}else{
			Uri music_uri = getMediaContentsUri(StoragePlaceType.External);
			String as[] = new String[1];
			as[0] = "_id";
			String where = "_data = ?";
			String as1[] = new String[1];
			as1[0] = path;
			Cursor cursor = null;
			try{
				cursor = _contentResolver.query(music_uri, as, where, as1, null);
			}catch(Exception e){
			}
			if(cursor == null){
				Log("fail query music : " + path );
			}else{
				cursor.moveToFirst();
				ret = cursor.getInt(0);
				cursor.close();
			}
		}
		return ret;
	}

	/**
	 * プレイリストの最大のplay_orderを取得する
	 * @param i
	 * @return
	 */
	public int getUserListMaxPlayOrder(int i){
		int ret = -1;
		
		if(_contentResolver == null){
		}else{
			Uri uri = getPlaylistsMembersContentsUri(StoragePlaceType.External, i);
			String as[] = new String[1];
			as[0] = " max(play_order)";
			String s = "playlist_id = " + i;
			Cursor cursor = _contentResolver.query(uri, as, s, null, null);
			if(cursor == null){
			}else{
				cursor.moveToFirst();
				ret = cursor.getInt(0);
				cursor.close();
			}
		}
		return ret;
	}
	
}
