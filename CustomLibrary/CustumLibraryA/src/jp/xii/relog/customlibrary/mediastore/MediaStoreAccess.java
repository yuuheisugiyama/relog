package jp.xii.relog.customlibrary.mediastore;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

public class MediaStoreAccess {

	public static final String DEBUG_TAG_NAME = "MediaStoreAccess";
	
	private static final String VOLUME_NAME_INTERNAL = "internal";
	private static final String VOLUME_NAME_EXTERNAL = "external";

	public static final String PHONE_GALAXY_S = "GT-I9000";
	
	/**
	 * 電話の種類（機種依存処理用情報）
	 * @author Iori
	 *
	 */
	enum PhoneType{
		Standard		//標準端末
		, GalaxyS		//ぎゃらくしー
	}
	
	/**
	 * 対象メディア
	 * @author Iori
	 *
	 */
	public enum StoragePlaceType{
		Internal
		,External
	}
	
	/**
	 * 対象情報
	 * @author Iori
	 *
	 */
	public enum InfomationType{
		Media
		, Album
		, Artist
		, Genre
		, Playlist
		, PlaylistMember
	}
	

	private Activity _activityParent = null;
	private PhoneType _phoneType = PhoneType.Standard;	//機種依存情報
	
	private boolean _isDebuging = false;				//デバッグするか
	private ArrayList<String> _debugLog = null;						//デバッグログ
	
	/**
	 * 機種依存情報
	 * @return
	 */
	protected PhoneType getPhoneType(){
		return _phoneType;
	}
	
	public MediaStoreAccess(Activity activity){
		_activityParent = activity;
		
		checkPhoneType();
	}

	/**
	 * 機種タイプを決定する
	 */
	public void checkPhoneType(){
		
		//とりあえずプレイリストを取得してみて判断する
		PhoneType[] types = {PhoneType.GalaxyS
							, PhoneType.Standard
							};
		Cursor cursor = null;
		for (PhoneType type : types) {
			_phoneType = type;
	        cursor = getMediaStoreInfo(StoragePlaceType.External
					, InfomationType.Playlist
					, null, null, null, null);
			if(cursor == null){
			}else{
				//OKなので終了
				break;
			}
		}

//		if(Build.DEVICE.compareTo(PHONE_GALAXY_S) == 0){
//			_phoneType = PhoneType.GalaxyS;
//		}else{
//			_phoneType = PhoneType.Standard;
//		}

		if(isDebuging()){
	    	Log("//-- Device info --");
			Log( " BOARD:" + Build.BOARD);
			Log( " BRAND:" + Build.BRAND);
			
			Log( " CPU_ABI:" + Build.CPU_ABI);
			Log( " DEVICE:" + Build.DEVICE);
			Log( " DISPLAY:" + Build.DISPLAY);
			Log( " FINGERPRINT:" + Build.FINGERPRINT);
			Log( " HOST:" + Build.HOST);
			Log( " ID:" + Build.ID);
			Log( " MANUFACTURER:" + Build.MANUFACTURER);
			Log( " MODEL:" + Build.MODEL);
			Log( " PRODUCT:" + Build.PRODUCT);
			Log( " TAGS:" + Build.TAGS);
			Log( " TYPE:" + Build.TYPE);
			Log( " USER:" + Build.USER);
			Log( " TIME:" + Build.TIME);
			Log( " CODENAME:" + Build.VERSION.CODENAME);
			Log( " INCREMENTAL:" + Build.VERSION.INCREMENTAL);
			Log( " RELEASE:" + Build.VERSION.RELEASE);
			Log( " SDK:" + Build.VERSION.SDK);
			Log( " SDK_INT:" + Build.VERSION.SDK_INT);
		}
	}
	
	/**
	 * デバッグするか
	 * @param _isDebuging the _isDebuging to set
	 */
	public void setIsDebuging(boolean _isDebuging) {
		this._isDebuging = _isDebuging;
	}
	/**
	 * デバッグするか
	 * @return the _isDebuging
	 */
	public boolean isDebuging() {
		return _isDebuging;
	}

	/**
	 * デバッグログ
	 * @return the _debugLog
	 */
	public ArrayList<String> getDebugLog() {
		if(_debugLog == null){
			_debugLog = new ArrayList<String>();
		}
		return _debugLog;
	}
	public String getDebugLogAll(){
		String log = "";
		for (String item : getDebugLog()) {
			log += item + "\n";
		}
		return log;
	}
	
	

	/**
	 * メディア情報をカーソルで受け取る
	 * @param place 対象の保存メディア
	 * @param info 取得する情報
	 * @param projection 取得するカラム
	 * @param selection 検索対象
	 * @param selectionArgs 検索条件の値
	 * @param sortOrder ソート条件
	 * @return
	 */
	public Cursor getMediaStoreInfo(StoragePlaceType place
									, InfomationType info
									, String[] projection
									, String selection
									, String[] selectionArgs
									, String sortOrder){
		Cursor cursor = null;
		Uri uri = null;
		
		switch(info){
		default:
		case Media:
			uri = getMediaContentsUri(place);
			break;
		case Album:
			uri = getAlbumContentsUri(place);
			break;
		case Artist:
			uri = getArtistContentsUri(place);
			break;
		case Genre:
			uri = getGenreContentsUri(place);
			break;
		case Playlist:
			uri = getPlaylistContentsUri(place);
			break;
		case PlaylistMember:
			uri = null;
			break;
		}
		if(uri == null){
			Log("uri:null");
		}else{
//			Log( "uri:" + uri.toString());
		}
		
		//取得
		if(_activityParent != null){
			try{
				cursor = _activityParent.managedQuery(uri, projection, selection
													, selectionArgs, sortOrder);
			}catch(Exception e){
	        	Log("managedQuery : " + e.getMessage());
			}
		}else{
        	Log("_activityParent == null");
		}
		
		return cursor;
	}


	/**
	 * プレイリストのメンバーを取得
	 * @param place
	 * @param playlist_id
	 * @return
	 */
	public Cursor getPlaylistMembers(StoragePlaceType place
									, int playlist_id
									, String[] projection
									, String selection
									, String[] selectionArgs
									, String sortOrder){

		Cursor cursor = null;
		Uri uri = null;

		//URI
		uri = getPlaylistsMembersContentsUri(place, playlist_id);	//標準
		
		//取得
		if(_activityParent != null){
			try{
				cursor = _activityParent.managedQuery(uri, projection, selection, selectionArgs, sortOrder);
			}catch(Exception e){
			}
		}
		
		return cursor;
	}

	/**
	 * メディア用のURIを取得する
	 * @param place
	 * @return
	 */
	protected Uri getMediaContentsUri(StoragePlaceType place){
		Uri uri = null;
		if(place == StoragePlaceType.Internal){
			uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
		}else{
			uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		}
		return uri;
	}
	/**
	 * アルバムのURIを取得する
	 * @param place
	 * @return
	 */
	protected Uri getAlbumContentsUri(StoragePlaceType place){
		Uri uri = null;
		if(place == StoragePlaceType.Internal){
			uri = MediaStore.Audio.Albums.INTERNAL_CONTENT_URI;
		}else{
			uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
		}
		return uri;
	}
	
	/**
	 * アーティストのURIを取得する
	 * @param place
	 * @return
	 */
	protected Uri getArtistContentsUri(StoragePlaceType place){
		Uri uri = null;
		if(place == StoragePlaceType.Internal){
			uri = MediaStore.Audio.Artists.INTERNAL_CONTENT_URI;
		}else{
			uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
		}
		return uri;
	}
	/**
	 * ジャンルのURIを取得する
	 * @param place
	 * @return
	 */
	protected Uri getGenreContentsUri(StoragePlaceType place){
		Uri uri = null;
		if(place == StoragePlaceType.Internal){
			uri = MediaStore.Audio.Genres.INTERNAL_CONTENT_URI;
		}else{
			uri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;
		}
		return uri;
	}
	
	/**
	 * プレイリストのURIを取得する
	 * @param place
	 * @return
	 */
	protected Uri getPlaylistContentsUri(StoragePlaceType place){
		Uri uri = null;
		
		switch(_phoneType){
		default:
		case Standard:
			//標準端末
			if(place == StoragePlaceType.Internal){
				uri = MediaStore.Audio.Playlists.INTERNAL_CONTENT_URI;
			}else{
				uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
			}
			break;
		case GalaxyS:
			//ギャラクシー
			String volumeName = "";
			if(place == StoragePlaceType.Internal){
				volumeName = VOLUME_NAME_INTERNAL;
			}else{
				volumeName = VOLUME_NAME_EXTERNAL;
			}
			uri = Uri.parse("content://media/" + volumeName + "/audio/music_playlists");
			break;
		}

		return uri;
	}
	
	/**
	 * プレイリストを取得する時のURIを取得する
	 * @param volumeName
	 * @param genreId
	 * @return
	 */
	protected Uri getPlaylistsMembersContentsUri(StoragePlaceType place, int genreId){
		Uri uri = null;
		String volumeName = "";
		
		if(place == StoragePlaceType.Internal){
			volumeName = VOLUME_NAME_INTERNAL;
		}else{
			volumeName = VOLUME_NAME_EXTERNAL;
		}
		
		switch(_phoneType){
		default:
		case Standard:
			//標準端末
			uri = MediaStore.Audio.Playlists.Members.getContentUri(volumeName, genreId);
			break;
		case GalaxyS:
			//ギャラクシー
			uri = Uri.parse("content://media/" + volumeName + "/audio/music_playlists/" + genreId + "/members");
			break;
		}
		return uri;
	}


	/**
	 * ログを出力
	 * @param message
	 */
	public void Log(String message){
		if(message == null){
		}else if(message.length() == 0){
		}else{
			Log.d(DEBUG_TAG_NAME, message);
			
			if(isDebuging()){
				getDebugLog().add(message);
				if(getDebugLog().size() > 400){
					getDebugLog().remove(0);
				}
			}
		}
	}
}
