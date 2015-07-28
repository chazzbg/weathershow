package weathershow;

import java.io.*;
import java.net.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class Weather {

	private Document dom;
	private final String apiKey = "8e00fd16a0130419122403";
	private String city;
	private String outputWeather = "";
	private URLConnection yc;
	private WeatherShowView wsv;
	private String location;

	public Weather(WeatherShowView wsv) {
		this.wsv = wsv;
	}

	public boolean weatherFileExists() {
		File f = new File("./weather.data");
		return f.exists();
	}

	public void init() {

		if (wsv.getWeatherLocation() == null) {
			if (!weatherFileExists()) {
				wsv.openSetLocationBox();
				return;
			} else {
				parseDataFile();
				City c = parceCity();
				wsv.setWeatherLocation(c.getCity());
			}

		}
		location = wsv.getWeatherLocation();
		
		if (location != null) {
			try {
				fetchData();
				if (!outputWeather.equals("")) {
					writeData();
				}

				if (weatherFileExists()) {
					parseDataFile();
					wsv.setCity(parceCity());
					wsv.setCc(parseCurrentConditions());
					wsv.setForecast(parceForecasts());
				}


			} catch (IOException ex) {
				System.out.println(ex);
			}
		}
	}

	private void parseDataFile() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setCoalescing(true);
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse("./weather.data");
		} catch (ParserConfigurationException ex) {
			System.out.println("problem s parsvaneto na faila: " + ex);
		} catch (SAXException ex) {
			System.out.println("SAX error: " + ex);

		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	private void fetchData() throws IOException {

		URL weather = new URL("http://free.worldweatheronline.com/feed/weather.ashx?q=" + location.trim().replace(" ", "+") + "&format=xml&num_of_days=4&extra=localObsTime&key=" + apiKey);

		yc = weather.openConnection();
		yc.setConnectTimeout(1500);

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				outputWeather += inputLine;
			}
		} catch (UnknownHostException ex) {
			wsv.showError("Cannot connect to the server!");
		} catch (SocketTimeoutException ex) {
			wsv.showError("Connection problem");
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	private void writeData() {
		try {
			FileWriter output = new FileWriter("./weather.data");
			if (!outputWeather.equals("")) {
				output.write(outputWeather.trim());
			}
			output.close();
		} catch (IOException ex) {
			System.out.println("Problem with closing data wite " + ex);
		}
	}

	public final City parceCity() {
		try {
			NodeList nodes = dom.getElementsByTagName("request");

			Node node = nodes.item(0);
			if (node.getNodeType() == 1) {
				Element element = (Element) node;
				return new City(getValue("query", element));
			}
		} catch (NullPointerException ex) {
			return null;
		}
		return null;
	}

	public CurrentConditions parseCurrentConditions() {
		try {
			NodeList nodes = dom.getElementsByTagName("current_condition");
			Node node = nodes.item(0);
			if (node.getNodeType() == 1) {
				Element element = (Element) node;

				CurrentConditions cc = new CurrentConditions();
				cc.setObservationTime(getValue("observation_time", element));
				cc.setLocalObsDateTime(getValue("localObsDateTime", element));
				cc.setTempC(getInt("temp_C", element));
				cc.setTempF(getInt("temp_F", element));
				cc.setWeatherCode(getInt("weatherCode", element));
				cc.setWeatherIconUrl(getValue("weatherIconUrl", element));
				cc.setWeatherDesc(getValue("weatherDesc", element));
				cc.setWindSpeedMiles(getInt("windspeedMiles", element));
				cc.setWindSpeedKmph(getInt("windspeedKmph", element));
				cc.setWindDirDegree(getInt("winddirDegree", element));
				cc.setWindDir16Point(getValue("winddir16Point", element));
				cc.setPrecipMM(Float.parseFloat(getValue("precipMM", element)));
				cc.setHumidity(getInt("humidity", element));
				cc.setVisibility(getInt("visibility", element));
				cc.setPressure(getInt("pressure", element));
				cc.setCloudCover(getInt("cloudcover", element));
				return cc;
			}
		} catch (NullPointerException ex) {

			return null;
		}
		return null;
	}

	public Forecast[] parceForecasts() {
		try {
			NodeList nodes = dom.getElementsByTagName("weather");

			if ((nodes != null) && (nodes.getLength() > 0)) {
				Forecast[] wf = new Forecast[nodes.getLength()];
				for (int i = 0; i < nodes.getLength(); i++) {
					Element element = (Element) nodes.item(i);
					wf[i] = new Forecast();
					wf[i].setDate(getValue("date", element));
					wf[i].setTempMaxC(getInt("tempMaxC", element));
					wf[i].setTempMinC(getInt("tempMinC", element));
					wf[i].setTempMaxF(getInt("tempMaxF", element));
					wf[i].setTempMinF(getInt("tempMinF", element));
					wf[i].setWeatherCode(getInt("weatherCode", element));
					
				}

				return wf;
			}
		} catch (NullPointerException ex) {
			return null;
		}
		return null;
	}

	private static String getValue(String tag, Element element) {
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = nodes.item(0);
		return node.getNodeValue().trim();
	}

	private static int getInt(String tag, Element element) {
		return Integer.parseInt(getValue(tag, element));
	}

	@Override
	public String toString() {
		return "Weather{" + "location=" + location + '}';
	}
}