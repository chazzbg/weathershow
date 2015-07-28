package weathershow;

public class Forecast {

	private String date;
	private int tempMaxC;
	private int tempMaxF;
	private int tempMinC;
	private int tempMinF;
	private int windSpeedMiles;
	private int windSpeedKmph;
	private String windDirection;
	private String windDir16Point;
	private int windDirDegree;
	private int weatherCode;
	private String weatherIconUrl;
	private String weatherDesc;
	private float precipMM;

	public Forecast() {
	}

	public Forecast(String date, int tempMaxC, int tempMaxF, int tempMinC, int tempMinF, int windSpeedMiles, int windSpeedKmph, String windDirection, String windDir16Point, int windDirDegree, int weatherCode, String weatherIconUrl, String weatherDesc, float precipMM) {
		this.date = date;
		this.tempMaxC = tempMaxC;
		this.tempMaxF = tempMaxF;
		this.tempMinC = tempMinC;
		this.tempMinF = tempMinF;
		this.windSpeedMiles = windSpeedMiles;
		this.windSpeedKmph = windSpeedKmph;
		this.windDirection = windDirection;
		this.windDir16Point = windDir16Point;
		this.windDirDegree = windDirDegree;
		this.weatherCode = weatherCode;
		this.weatherIconUrl = weatherIconUrl;
		this.weatherDesc = weatherDesc;
		this.precipMM = precipMM;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public float getPrecipMM() {
		return this.precipMM;
	}

	public void setPrecipMM(float precipMM) {
		this.precipMM = precipMM;
	}

	public int getTempMaxC() {
		return this.tempMaxC;
	}

	public void setTempMaxC(int tempMaxC) {
		this.tempMaxC = tempMaxC;
	}

	public int getTempMaxF() {
		return this.tempMaxF;
	}

	public void setTempMaxF(int tempMaxF) {
		this.tempMaxF = tempMaxF;
	}

	public int getTempMinC() {
		return this.tempMinC;
	}

	public void setTempMinC(int tempMinC) {
		this.tempMinC = tempMinC;
	}

	public int getTempMinF() {
		return this.tempMinF;
	}

	public void setTempMinF(int tempMinF) {
		this.tempMinF = tempMinF;
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

	public String getWindDirection() {
		return this.windDirection;
	}

	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
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
		return "Forecast{tempMaxC=" + this.tempMaxC + ", tempMinC=" + this.tempMinC + '}';
	}
}