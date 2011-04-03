package jp.xii.relog.customlibrary.mediastore;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

/**
 * ‰¹Šy‚Æ‚©‰æ‘œ‚Æ‚©‚ð’[––‚ÌDB‚É“o˜^‚·‚é
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
