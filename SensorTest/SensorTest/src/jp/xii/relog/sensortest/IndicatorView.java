/**
 * 
 */
package jp.xii.relog.sensortest;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

/**
 * @author Iori
 *
 */
public class IndicatorView extends View {

	
	static final int NUM_BAR_NUM = 32;							//インジケータのバーの総数
	static final double NUM_DELTA = Math.PI * 2 / NUM_BAR_NUM;	//1本の進むシータ 
	static final int NUM_DIRECTION_FONT_SIZE = 16;				//方位のフォントサイズ
	static final int NUM_DIRECTION_FONT_SIZE_HALF = NUM_DIRECTION_FONT_SIZE / 2;	//方位のフォントサイズの半分
	
	static final double NUM_DISTANCE_EYE_TO_DISPLAY = 5.9055;	//めと画面の距離(inch) = 15cm 
	
	static final int NUM_NORTH_COUNT = 0;						//北向きになるカウント
	static final int NUM_EAST_COUNT = NUM_BAR_NUM / 4;			//東向きになるカウント
	static final int NUM_SOUTH_COUNT = NUM_BAR_NUM / 2;			//南向きになるカウント
	static final int NUM_WEST_COUNT = NUM_BAR_NUM * 3 / 4;		//西向きになるカウント
	
	private double _locationLatitude = 0.0;			//緯度
	private double _locationLongitude = 0.0;		//経度
	private double _locationSeaLevel = 0.0;			//標高
	
	private float _sensorOrientation = 0;			//方位
	private float _sensorOrientationPitch = 0;		//ピッチ
	private float _sensorOrientationRoll = 0;		//ロール

	
	private CharactorObject _mineObject = null;		//自分の位置情報
	private ArrayList<CharactorObject> _targetObjects = null;	//ターゲットの位置情報のリスト
	
	
	/**
	 * 緯度
	 * @param _locationLatitude the _locationLatitude to set
	 */
	public void setLocationLatitude(double _locationLatitude) {
		this._locationLatitude = _locationLatitude;
	}
	/**
	 * 緯度
	 * @return the _locationLatitude
	 */
	public double getLocationLatitude() {
		return _locationLatitude;
	}
	/**
	 * 経度
	 * @param _locationLongitude the _locationLongitude to set
	 */
	public void setLocationLongitude(double _locationLongitude) {
		this._locationLongitude = _locationLongitude;
	}
	/**
	 * 経度
	 * @return the _locationLongitude
	 */
	public double getLocationLongitude() {
		return _locationLongitude;
	}
	/**
	 * 標高
	 * @param _locationSeaLevel the _locationSeaLevel to set
	 */
	public void setLocationSeaLevel(double _locationSeaLevel) {
		this._locationSeaLevel = _locationSeaLevel;
	}
	/**
	 * 標高
	 * @return the _locationSeaLevel
	 */
	public double getLocationSeaLevel() {
		return _locationSeaLevel;
	}

	/**
	 * 方位
	 * @param _sensorOrientation the _sensorOrientation to set
	 */
	public void setSensorOrientation(float _sensorOrientation) {
		this._sensorOrientation = _sensorOrientation;
	}
	/**
	 * 方位
	 * @return the _sensorOrientation
	 */
	public float getSensorOrientation() {
		return _sensorOrientation;
	}
	/**
	 * ピッチ
	 * @param _sensorOrientationPitch the _sensorOrientationPitch to set
	 */
	public void setSensorOrientationPitch(float _sensorOrientationPitch) {
		this._sensorOrientationPitch = _sensorOrientationPitch;
	}
	/**
	 * ピッチ
	 * @return the _sensorOrientationPitch
	 */
	public float getSensorOrientationPitch() {
		return _sensorOrientationPitch;
	}
	/**
	 * ロール
	 * @param _sensorOrientationRoll the _sensorOrientationRoll to set
	 */
	public void setSensorOrientationRoll(float _sensorOrientationRoll) {
		this._sensorOrientationRoll = _sensorOrientationRoll;
	}
	/**
	 * ロール
	 * @return the _sensorOrientationRoll
	 */
	public float getSensorOrientationRoll() {
		return _sensorOrientationRoll;
	}

	
	/**
	 * コンストラクタ
	 * @param context
	 */
	public IndicatorView(Context context) {
		super(context);
		
		_mineObject = new CharactorObject();
		
		_targetObjects = new ArrayList<CharactorObject>();
		
		CharactorObject target = new CharactorObject();
		target.setName("モクモク会場");
		target.setLocationLatitude(35.1671122695654);
		target.setLocationLongitude(136.898309290409);
		target.setLocationSeaLevel(0.0);
		_targetObjects.add(target);
		
		target = new CharactorObject();
		target.setName("雛見沢神社");
		target.setLocationLatitude(36.2547510789974);
		target.setLocationLongitude(136.905784606934);
		target.setLocationSeaLevel(500.0);
		_targetObjects.add(target);
		
	}

	
	/**
	 * 描画処理
	 */
	@Override
	protected void onDraw(Canvas canvas){
		Paint paint = new Paint();
		
		//現在位置更新
		updateLocationInfo();
		
		//線
		paint.setColor(Color.argb(255, 128, 255, 227));
		paint.setStrokeWidth(3);

		//テキスト
		paint.setTextSize(NUM_DIRECTION_FONT_SIZE);

		
		//中心（縦横回転するので注意）
		float center_x = this.getHeight() / 2;
		float center_y = this.getWidth() / 2;
		
		//32本
		//360 / 32 = 11度に1本
		//RADにすると 2PI = 360だから
		//PI / 16 = dsi-ta だけ進む
		
		//画面を縦向きに回転
		canvas.rotate(-90);
		canvas.translate(-1 * this.getHeight(), 0);

		//方位のインジケータ
		drawOrientationIndicator(canvas, paint
							, center_x, center_y - center_y / 3
							, center_x * 3 / 4);
		
		//ピッチのインジケータ
		drawPitchIndicator(canvas, paint
							, center_x - center_x / 2, center_y
							, center_x / 2);
		
		//ターゲット
		for (CharactorObject target : _targetObjects) {
			drawTarge(canvas, paint	, target, _mineObject);
		}
		
	}


	/**
	 * 方位のインジケータを描く
	 * @param canvas
	 * @param paint
	 */
	private void drawOrientationIndicator(Canvas canvas, Paint paint
										, float center_x, float center_y
										, float amplitude){
		
		double base_rad = Math.PI * getSensorOrientation() / 180;	//描きはじめのオフセット
//		double base_ori = Math.PI * 2 * getSensorOrientation() / 360;	//描きはじめのオフセット
//		double base_ori = Math.PI * 2 * 45 / 360;	//描きはじめのオフセット
		float x = 0;
		float y = 0;
		float h = 0;
		double now_rad = 0.0;
		String direction_name = "";
		
		for(int i=0; i<NUM_BAR_NUM; i++){
			//現在の角度
			now_rad = NUM_DELTA * i - base_rad;
			
			//0～360度の間に居ないときは戻す
			now_rad = adjustRadian(now_rad);
			
			if((now_rad) > (Math.PI / 2) && (now_rad) < (Math.PI * 3 / 2)){
				//90-270度の間は表示しない
			}else{
				//座標計算
				x = (float) (amplitude *  Math.sin(now_rad) + center_x);
				y = (float) (-1 * 20 * Math.cos(now_rad) + center_y);
				//高さを選択
				switch(i){
				case NUM_NORTH_COUNT:
					h = 20;
					direction_name = "N";
					break;
				case NUM_EAST_COUNT:
					h = 20;
					direction_name = "E";
					break;
				case NUM_SOUTH_COUNT:
					h = 20;
					direction_name = "S";
					break;
				case NUM_WEST_COUNT:
					h = 20;
					direction_name = "W";
					break;
				default:
					direction_name = "";
					h = 10;
					break;
				}
				//方位が指定されている場合は描画
				if(direction_name.length() > 0){
					paint.setAntiAlias(true);
					canvas.drawText(direction_name, x - NUM_DIRECTION_FONT_SIZE_HALF
							, y - h - NUM_DIRECTION_FONT_SIZE_HALF
							, paint);
				}
				paint.setAntiAlias(false);
				canvas.drawLine(x, y - h,  x, y , paint);
			}
		}
	}
	
	/**
	 * ピッチのインジケータ
	 */
	private void drawPitchIndicator(Canvas canvas, Paint paint
									, float center_x, float center_y
									, float amplitude){
		double base_rad = Math.PI * (getSensorOrientationPitch() + 90) / 180;	//描きはじめのオフセット
//		double base_rad = Math.PI * 2 * (-90 + 90) / 360;	//描きはじめのオフセット
		float x = 0;
		float y = 0;
		float h = 10;
		double now_rad = 0.0;

		//基準線
		y = center_y;
		x = center_x;
		h = 20;
		canvas.drawLine(x - h, y, x, y , paint);
		
		//個々のメモリ
		for(int i=0; i<NUM_BAR_NUM; i++){
			//現在の角度
			now_rad = NUM_DELTA * i - base_rad;
			
			//0～360度の間に居ないときは戻す
			now_rad = adjustRadian(now_rad);
			
			if((now_rad) > (Math.PI / 2) && (now_rad) < (Math.PI * 3 / 2)){
				//90-270度の間は表示しない
			}else{
				//座標計算
				y = (float) (amplitude *  Math.sin(now_rad) + center_y);
				x = (float) (-1 * 20 * Math.cos(now_rad) + center_x);
				//高さを選択
				if(i == 0){
					h = 20;
					paint.setAntiAlias(true);
					canvas.drawText("0", x - h - 6, y - 6, paint);
				}else{
					h = 10;
				}
				paint.setAntiAlias(false);
				canvas.drawLine(x - h, y, x, y , paint);
			}
		}
	}
	
	
	/**
	 * ターゲットを描く
	 * @param canvas
	 * @param paint
	 * @param target
	 * @param me
	 */
	private void drawTarge(Canvas canvas, Paint paint
						, CharactorObject target, CharactorObject me){
		//ターゲットとの距離と角度を計算
		double distance = target.calcDistance(me);					//距離
		double angle = target.calcAngle(me);						//方位角
		double ele_angle = target.calcElevationAngle(me);			//仰角
		double base_rad = Math.PI * getSensorOrientation() / 180;	//描きはじめのオフセット
		double base_ele_rad = Math.PI * (getSensorOrientationPitch() + 90) / 180;	//現在のピッチ
		double in_degree = Math.PI / 12;

		//アングルを調節
		base_rad = adjustRadian2(base_rad);
		base_ele_rad = adjustRadian2(base_ele_rad);
		angle = adjustRadian2(angle);

		if((angle > (base_rad - in_degree) && angle < (base_rad + in_degree))
				&& (ele_angle > (base_ele_rad - in_degree) && (ele_angle < (base_ele_rad + in_degree)))){
			//既定の範囲内だったら描画
			float x = (float) (NUM_DISTANCE_EYE_TO_DISPLAY * Math.tan(angle - base_rad) * 160 + this.getHeight() / 2);
			float y = (float) (NUM_DISTANCE_EYE_TO_DISPLAY * Math.tan(ele_angle - base_ele_rad) * 160 + this.getHeight() / 2);
//			float x = (float) (this.getHeight() / 2 + this.getHeight() * (angle - base_rad) / (in_degree * 2));
//			float y = (float) (this.getWidth() / 2 + this.getWidth() * (ele_angle - base_ele_rad) / (in_degree * 2));
			RectF r = new RectF(x - 5, y - 5
								, x + 5, y + 5);
			paint.setStyle(Paint.Style.STROKE);
			paint.setAntiAlias(false);
			canvas.drawRect(r, paint);
			
			//文字情報
			paint.setStyle(Paint.Style.FILL);
			paint.setAntiAlias(true);
			
			//名前
			String info = String.format("%s", target.getName());
			canvas.drawText(info, x + 5, y - NUM_DIRECTION_FONT_SIZE, paint);
			//距離
			info = String.format(" %.1f(km)", distance);
			canvas.drawText(info, x + 5, y , paint);
			//高低差
			info = String.format(" %.1f(m)", target.getLocationSeaLevel() - me.getLocationSeaLevel());
			canvas.drawText(info, x + 5, y + NUM_DIRECTION_FONT_SIZE, paint);
		}else{
			//範囲外なので方向指示を描画
			
			//画面外に書いた時の座標（この時は原点は0,0）
			float x = (float) (NUM_DISTANCE_EYE_TO_DISPLAY * Math.tan(adjustRadian2(angle - base_rad)) * 160);
			float y = (float) (NUM_DISTANCE_EYE_TO_DISPLAY * Math.tan(ele_angle - base_ele_rad) * 160);
//			float x = (float) (this.getHeight() * (angle - base_rad) / (in_degree * 2));
//			float y = (float) (this.getWidth() * (ele_angle - base_ele_rad) / (in_degree * 2));
			
			if(adjustRadian2(angle - base_rad) > 0){
				//右にいる
				x = Math.abs(x);
			}else{
				//左にいる
				x = -1 * Math.abs(x);
			}

			//三角
			double mark_angle = Math.atan2(y, x);
			
			//三角形の座標
			float tri_x = 0;
			float tri_y = 0;
			float apex = 20;
			tri_x = this.getHeight() / 2 - 25;
			apex = 20;

			float[] pts = {tri_x, tri_y - 8, tri_x, tri_y + 8
					, tri_x, tri_y + 8, tri_x + apex, tri_y
					, tri_x + apex, tri_y, tri_x, tri_y - 8};
			for(int i=0; i<pts.length; i+= 2){
				double temp_x = pts[i];
				double temp_y = pts[i+1];
				pts[i] = (float) (calcRotationX(temp_x, temp_y, mark_angle) + this.getHeight() / 2);
				pts[i+1] = (float) (calcRotationY(temp_x, temp_y, mark_angle) + this.getWidth() / 2);
			}

			//三角を描く
			paint.setStrokeCap(Paint.Cap.ROUND);
			canvas.drawLines(pts, paint);

//			String info = String.format("%.1f %.1f", adjustRadian2(angle - base_rad) * 180 / Math.PI
//													, mark_angle * 180 / Math.PI);
//			canvas.drawText(info, 10, this.getHeight() - 5, paint);
		}
	}
	
	
	/**
	 * 0～360度の間の値に調節する
	 * @param base
	 * @return
	 */
	public double adjustRadian(double now_rad){
		
		if(now_rad < 0){
			while(now_rad < 0){
				now_rad += Math.PI * 2;
			}
		}else if(now_rad > (Math.PI * 2)){
			while(now_rad > (Math.PI * 2)){
				now_rad -= Math.PI * 2;
			}
		}

		return now_rad;
	}

	/**
	 * -180～180度の間に調節する
	 * @param now_rad
	 * @return
	 */
	public double adjustRadian2(double now_rad){
		if(now_rad < (-1 * Math.PI)){
			while(now_rad < (-1 * Math.PI)){
				now_rad += Math.PI * 2;
			}
		}else if(now_rad > Math.PI){
			while(now_rad > Math.PI){
				now_rad -= Math.PI * 2;
			}
		}
		return now_rad;
	}
	
	/**
	 * 回転した座標Xを計算する
	 * @param x 元のX
	 * @param y　元のY
	 * @param rad　回転する角度（ラジアン）
	 * @return 回転した後のX
	 */
	public double calcRotationX(double x, double y, double rad){
		double ret = 0.0;
		double alpha = Math.atan2(y, x);
		double r = Math.sqrt(x * x + y * y);
		
		ret = r * Math.cos(alpha + rad);
		
		return ret;
	}
	
	/**
	 * 回転した座標Yを計算する
	 * @param x 元のX
	 * @param y　元のY
	 * @param rad　回転する角度（ラジアン）
	 * @return 回転した後のY
	 */
	public double calcRotationY(double x, double y, double rad){
		double ret = 0.0;
		double alpha = Math.atan2(y, x);
		double r = Math.sqrt(x * x + y * y);
		
		ret = r * Math.sin(alpha + rad);
		
		return ret;
	}
	
	
	
	/**
	 * 位置情報を各オブジェクトに反映する
	 */
	private void updateLocationInfo(){
		
		//自分の位置情報更新
		if(_mineObject == null){
		}else{
			_mineObject.setLocationLatitude(getLocationLatitude());
			_mineObject.setLocationLongitude(getLocationLongitude());
			_mineObject.setLocationSeaLevel(getLocationSeaLevel());
		}
		
	}


}
