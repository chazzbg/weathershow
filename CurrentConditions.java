package weathershow;

public class CurrentConditions {

	private String observationTime;
	private String localObsDateTime;
	private int tempC;
	private int tempF;
	private int weatherCode;
	private String weatherIconUrl;
	private String weatherDesc;
	private int windSpeedMiles;
	private int windSpeedKmph;
	private int windDirDegree;
	private String windDir16Point;
	private float precipMM;
	private int humidity;
	private int visibility;
	private int pressure;
	private int cloudCover;

	public CurrentConditions(String observationTime, String localObsDateTime, int tempC, int tempF, int weatherCode, String weatherIconUrl, String weatherDesc, int windSpeedMiles, int windSpeedKmph, int windDirDegree, String windDir16Point, float precipMM, int humidity, int visibility, int pressure, int cloudCover) {
		this.observationTime = observationTime;
		this.localObsDateTime = localObsDateTime;
		this.tempC = tempC;
		this.tempF = tempF;
		this.weatherCode = weatherCode;
		this.weatherIconUrl = weatherIconUrl;
		this.weatherDesc = weatherDesc;
		this.windSpeedMiles = windSpeedMiles;
		this.windSpeedKmph = windSpeedKmph;
		this.windDirDegree = windDirDegree;
		this.windDir16Point = windDir16Point;
		this.precipMM = precipMM;
		this.humidity = humidity;
		this.visibility = visibility;
		this.pressure = pressure;
		this.cloudCover = cloudCover;
	}

	public CurrentConditions() {
	}

	public int getCloudCover() {
		return this.cloudCover;
	}

	public void setCloudCover(int cloudCover) {
		this.cloudCover = cloudCover;
	}

	public int getHumidity() {
		return this.humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public String getLocalObsDateTime() {
		return this.localObsDateTime;
	}

	public void setLocalObsDateTime(String localObsDateTime) {
		this.localObsDateTime = localObsDateTime;
	}

	public String getObservationTime() {
		return this.observationTime;
	}

	public void setObservationTime(String observationTime) {
		this.observationTime = observationTime;
	}

	public float getPrecipMM() {
		return this.precipMM;
	}

	public void setPrecipMM(float precipMM) {
		this.precipMM = precipMM;
	}

	public int getPressure() {
		return this.pressure;
	}

	public void setPressure(int pressure) {
		this.pressure = pressure;
	}

	public int getTempC() {
		return this.tempC;
	}

	public void setTempC(int tempC) {
		this.tempC = tempC;
	}

	public int getTempF() {
		return this.tempF;
	}

	public void setTempF(int tempF) {
		this.tempF = tempF;
	}

	public int getVisibility() {
		return this.visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

	public int getWeatherCode() {
		return this.weatherCode;
	}

	public void setWeatherCode(int weatherCode) {
		this.weatherCode = weatherCode;
	}

	public String getWeatherDesc() {
		return this.weatherDesc;
	}

	public void setWeatherDesc(String weatherDesc) {
		this.weatherDesc = weatherDesc;
	}

	public String getWeatherIconUrl() {
		return this.weatherIconUrl;
	}

	public void setWeatherIconUrl(String weatherIconUrl) {
		this.weatherIconUrl = weatherIconUrl;
	}

	public String getWindDir16Point() {
		return this.windDir16Point;
	}

	public void setWindDir16Point(String windDir16Point) {
		this.windDir16Point = windDir16Point;
	}

	public int getWindDirDegree() {
		return this.windDirDegree;
	}

	public void setWindDirDegree(int windDirDegree) {
		this.windDirDegree = windDirDegree;
	}

	public int getWindSpeedKmph() {
		return this.windSpeedKmph;
	}

	public void setWindSpeedKmph(int windSpeedKmph) {
		this.windSpeedKmph = windSpeedKmph;
	}

	public int getWindSpeedMiles() {
		return this.windSpeedMiles;
	}

	public void setWindSpeedMiles(int windSpeedMiles) {
		this.windSpeedMiles = windSpeedMiles;
	}

	public String toString() {
		return "CurrentConditions{observationTime=" + this.observationTime + ", localObsDateTime=" + this.localObsDateTime + ", tempC=" + this.tempC + ", tempF=" + this.tempF + ", weatherCode=" + this.weatherCode + ", weatherIconUrl=" + this.weatherIconUrl + ", weatherDesc=" + this.weatherDesc + ", windSpeedMiles=" + this.windSpeedMiles + ", windSpeedKmph=" + this.windSpeedKmph + ", windDirDegree=" + this.windDirDegree + ", windDir16Point=" + this.windDir16Point + ", precipMM=" + this.precipMM + ", humidity=" + this.humidity + ", visibility=" + this.visibility + ", pressure=" + this.pressure + ", cloudCover=" + this.cloudCover + '}';
	}
}