package jp.xii.relog.sensortest;

/**
 * 表示オブジェクトの基底クラス
 * @author Iori
 *
 */
public class CharactorObject {

	static final double NUM_1DEGREE_KM_ON_YOKO = 90.163292;		//1度の距離(km)赤道周り
	static final double NUM_1DEGREE_KM_ON_TATE = 110.968304;	//1度の距離(km)子午線周り（縦方向）
	
	//個体情報
	private String _name = "";					//名前
	
	//位置情報
	private double _locationLatitude = 0.0;		//緯度
	private double _locationLongitude = 0.0;	//経度
	private double _locationSeaLevel = 0.0;		//標高
	
	
	
	/**
	 * 名前
	 * @param _name the _name to set
	 */
	public void setName(String _name) {
		this._name = _name;
	}
	/**
	 * 名前
	 * @return the _name
	 */
	public String getName() {
		return _name;
	}
	
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
	 * このオブジェクトと指定したオブジェクトの距離を計算する
	 * @param src 比較対象
	 * @return 距離(km)
	 */
	public double calcDistance(CharactorObject src){
		double ret = 0.0;
		
		if(src == null){
			//NG
		}else{
			double d_lati = getLocationLatitude() - src.getLocationLatitude();
			double d_long = getLocationLongitude() - src.getLocationLongitude();
			
			ret = Math.sqrt(Math.pow(d_lati * NUM_1DEGREE_KM_ON_TATE, 2)
							+ Math.pow(d_long * NUM_1DEGREE_KM_ON_YOKO, 2));
		}
		
		return ret;
	}

	/**
	 * このオブジェクトと指定したオブジェクトの北を0度とした角度を計算する
	 * @param src 比較対象（基準）
	 * @return 角度（ラジアン）
	 */
	public double calcAngle(CharactorObject src){
		double ret = 0.0;
		
		if(src == null){
			//NG
		}else{
			double d_lati = getLocationLatitude() - src.getLocationLatitude();
			double d_long = getLocationLongitude() - src.getLocationLongitude();
			
			ret = Math.atan2(d_long, d_lati);
		}
		
		return ret;
	}

	/**
	 * このオブジェクトと指定したオブジェクトの仰角を計算する
	 * @param src 比較対象（基準）
	 * @return 角度（ラジアン）
	 */
	public double calcElevationAngle(CharactorObject src){
		double ret = 0.0;
		
		if(src == null){
			//NG
		}else{
			double d_sea = getLocationSeaLevel() - src.getLocationSeaLevel();
			d_sea *= 0.001;		//mをkmに直す
			
			ret = -1 * Math.atan2(d_sea, calcDistance(src));
		}
		
		return ret;
	}
}
