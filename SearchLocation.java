package weathershow;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import javax.swing.JDialog;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SearchLocation {

	private String apiKey = "8e00fd16a0130419122403";
	private String query;
	private URLConnection yc;
	private Document dom;
	private WeatherShowSetLocation wsst;
	public SearchLocation(JDialog wsst) {
		this.wsst = (WeatherShowSetLocation) wsst;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String[] getResults() {
		parseXml();
		try {
		
			NodeList nodes = dom.getElementsByTagName("result");
			
			if ((nodes != null) && (nodes.getLength() > 0)) {
				String[] results = new String[nodes.getLength()];
				for (int i = 0; i < nodes.getLength(); i++) {
					Element element = (Element) nodes.item(i);
					results[i] = (getValue("areaName", element) + "," + getValue("country", element));
				}
				return results;
			}
		} catch (NullPointerException ex) {
			
			return null;
		}
		return null;
	}

	private void parseXml() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setCoalescing(true);
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream results = fetchResults();
				
				if(results!=null)
					dom = db.parse(results);
				
		} catch (ParserConfigurationException ex) {
			System.out.println("problem s parsvaneto na faila: " + ex);
		} catch (SAXException ex) {
			System.out.println("SAX error: " + ex);
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	private InputStream fetchResults() throws IOException {
		
			URL weather = new URL("http://www.worldweatheronline.com/feed/search.ashx?q=" + query + "&key="+apiKey+"&popular=no&format=xml");
			yc = weather.openConnection();
			yc.setConnectTimeout(15000);
		try {
			return yc.getInputStream();
		} catch (UnknownHostException ex) {
			wsst.showError("Cannot connect to the server!");
		} catch (SocketTimeoutException ex) {
			wsst.showError("Connection problem");
		} catch (IOException ex) {
			System.out.println(ex);
		}
		return null;
	}

	private static String getValue(String tag, Element element) {
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = nodes.item(0);
		return node.getNodeValue().trim();
	}
}