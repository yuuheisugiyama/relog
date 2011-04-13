package jp.xii.relog.sensortest;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SensorTest extends Activity 
	implements LocationListener, SensorEventListener{
	
	private TickHandler _tickHandler;

	//カメラ
	private Camera _cameraPreview;
	
	//描画系
	private IndicatorView _indicatorView = null;
	private SurfaceView _surfaceView = null;

	//位置情報関連
	private LocationManager _locationManger = null;
	private double _locationLatitude = 0.0;		//緯度
	private double _locationLongitude = 0.0;	//経度
	private double _locationAltitude = 0.0;		//高度
	
	//センサー関係
	private SensorManager _sensorManager = null;
	private float _sensorOrientation = 0;
	private float _sensorOrientationPitch = 0;
	private float _sensorOrientationRoll = 0;
//	private float _sensorMagneticX = 0;
//	private float _sensorMagneticY = 0;
//	private float _sensorMagneticZ = 0;
	
	
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //フルスクリーン
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //タイトルバー非表示
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //ビューを確定
        setContentView(R.layout.main);
        
        
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.MainView);
        
        //カメラ
        _surfaceView = new SurfaceView(this);
        //サーフェスHolderにコールバックとタイプを指定
        SurfaceHolder holder = _surfaceView.getHolder();
        holder.addCallback(_SurfaceHolderCallback);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        
        
        //レイアウトにインジケータを表示するビューを追加する
        _indicatorView = new IndicatorView(this);
        _indicatorView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT
        														, RelativeLayout.LayoutParams.FILL_PARENT));
        layout.addView(_indicatorView,0);
        layout.addView(_surfaceView,0);
        
        //センサー
        _locationManger = (LocationManager)getSystemService(LOCATION_SERVICE);
        _sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
    }
    
    
    //アプリの再開
    @Override
    public void onResume(){
    	super.onResume();
    	
    	//位置情報のプロバイダ取得
    	String provider = _locationManger.getBestProvider(new Criteria(), true);
    	if(provider == null){
    		//なにもない
    	}else{
    		_locationManger.requestLocationUpdates(provider, 0, 0,  this);
    		//最後に取得した位置情報を取得
    		Location location = _locationManger.getLastKnownLocation(provider);
    		if(location != null){
    			onLocationChanged(location);
    		}
    	}
    	
    	//方位センサー
    	List<Sensor> list = _sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
    	if(list == null){
    	}else if(list.size() < 1){
    	}else{
    		//方位センサー取得
    		Sensor sensor = list.get(0);
    		//リスナー登録
    		_sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    	}
    	
    	//地磁気センサー
    	list = _sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
    	if(list == null){
    	}else if(list.size() < 1){
    	}else{
    		//磁気センサー取得
    		Sensor sensor = list.get(0);
    		//リスナー登録
    		_sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    	}
    	
    	//定期処理ハンドラ
    	_tickHandler = new TickHandler();
    	_tickHandler.sleep(0);
    }
    
    //アプリの一時停止
    @Override
    public void onPause(){
    	super.onPause();
    	
    	_tickHandler = null;
    	
    	//リスナを解除
    	_sensorManager.unregisterListener(this);
    	_locationManger.removeUpdates(this);
    }
    
    
    //定期処理ハンドラ
    public class TickHandler extends Handler{
    	//定期処理
    	@Override
    	public void handleMessage(Message msg){
    		//再描画
    		_indicatorView.invalidate();
    		updateView();
    		
    		if(_tickHandler != null){
    			_tickHandler.sleep(100);
    		}
    	}
    	
    	//スリープ
    	public void sleep(long delayMills){
    		removeMessages(0);
    		sendMessageDelayed(obtainMessage(0), delayMills);
    	}
    }

    /**
     * カメラのプレビュー関連のコールバック
     */
    private SurfaceHolder.Callback _SurfaceHolderCallback = new SurfaceHolder.Callback() {
		
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			//カメラービューを停止
			_cameraPreview.stopPreview();
			//カメラを開放
			_cameraPreview.release();
			_cameraPreview = null;
			
		}
		
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			//カメラを起動します
			_cameraPreview = Camera.open();
			try{
				_cameraPreview.setPreviewDisplay(holder);
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			//カメラのプレビュー開始
			_cameraPreview.startPreview();
		}
	};
    
    
    
    /**
     * 表示内容を更新
     */
    private void updateView(){
    	TextView text = null;
    	
    	//位置
    	text = (TextView)findViewById(R.id.LocationInfo);
    	text.setText("La : " + _locationLatitude 
    			+ " , Lo : " + _locationLongitude
    			+ " , Al : " + _locationAltitude);
    	
    	//方位
    	text = (TextView)findViewById(R.id.OrientationInfo);
    	text.setText("Ori : " + _sensorOrientation
    				+ "\nPitch : " + _sensorOrientationPitch
    				+ "\nRoll :  " + _sensorOrientationRoll);
    	
    	//地磁気
//    	text = (TextView)findViewById(R.id.MagneticInfo);
//    	double L = Math.sqrt(_sensorMagneticX * _sensorMagneticX + _sensorMagneticY * _sensorMagneticY);	//斜辺L
//    	text.setText("X : " + _sensorMagneticX + "\nY : " + _sensorMagneticY + "\nZ : " + _sensorMagneticZ
//    				+ "\natan(X/Y) : " + Math.atan2(_sensorMagneticX, _sensorMagneticY) * 180 / Math.PI
//    				+ "\natan(Z/L) : " + Math.atan2(_sensorMagneticZ, L) * 180 / Math.PI);
    }

    /**
     * 位置が変わったら
     */
	@Override
	public void onLocationChanged(Location location) {
		_locationLatitude = location.getLatitude();		//緯度
		_locationLongitude = location.getLongitude();	//経度
		_locationAltitude = location.getAltitude();		//高度
		_indicatorView.setLocationLatitude(_locationLatitude);
		_indicatorView.setLocationLongitude(_locationLongitude);
		_indicatorView.setLocationSeaLevel(_locationAltitude);
	}

	/**
	 * プロバイダが無効になったら
	 */
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * プロバイダが有効になったら
	 */
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * プロバイダの状態が変化したら呼び出される
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}


	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * センサーの値が変化したら
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		if(event.sensor.getType() == Sensor.TYPE_ORIENTATION){
			//方位取得
			_sensorOrientation = event.values[0];
			_sensorOrientationPitch = event.values[1];
			_sensorOrientationRoll = event.values[2];
			_indicatorView.setSensorOrientation(_sensorOrientation);
			_indicatorView.setSensorOrientationPitch(_sensorOrientationPitch);
			_indicatorView.setSensorOrientationRoll(_sensorOrientationRoll);
		}else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
			//地磁気
//			_sensorMagneticX = event.values[0];
//			_sensorMagneticY = event.values[1];
//			_sensorMagneticZ = event.values[2];
		}
	}

}