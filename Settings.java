package weathershow;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Chazz
 */
public class Settings {
	private String laf = "Nimbus";
	private String distUnits = "Kilometers";
	private String tempUnits = "Celsius";
	private long refreshInterval = 10800000;
	private boolean hideOnClose = true;
	private boolean hideOnMinimize = true;
	private WeatherShowView wsv;
	private Document dom;
	public Settings(WeatherShowView wsv) {
		this.wsv = wsv;
		File f = new File("./settings");
		if(!f.exists()){
			wsv.showError("Settings file missing.\nDefault settings restored!");
			write();
		}
	}

	public void save(String laf,String distUnits,String tempUnits,long refreshInterval,boolean hideOnClose,boolean hideOnMinimize){
		this.laf = laf;
		this.distUnits = distUnits;
		this.tempUnits = tempUnits;
		this.refreshInterval = refreshInterval;
		this.hideOnClose = hideOnClose;
		this.hideOnMinimize = hideOnMinimize;
		
		write();
	}
	private void write(){
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			dom = db.newDocument();
			Element rootSettings = dom.createElement("settings");
			dom.appendChild(rootSettings);
			
			Element lafElement = dom.createElement("laf");
			lafElement.appendChild(dom.createTextNode(laf));
			rootSettings.appendChild(lafElement);
			
			Element distUnitsElement = dom.createElement("distUnits");
			distUnitsElement.appendChild(dom.createTextNode(distUnits));
			rootSettings.appendChild(distUnitsElement);
			
			Element tempUnitsElement = dom.createElement("tempUnits");
			tempUnitsElement.appendChild(dom.createTextNode(tempUnits));
			rootSettings.appendChild(tempUnitsElement);
			
			Element refreshIntervalElement = dom.createElement("refreshInterval");
			refreshIntervalElement.appendChild(dom.createTextNode(String.valueOf(refreshInterval)));
			rootSettings.appendChild(refreshIntervalElement);
			
			Element hideOnCloseElement = dom.createElement("hideOnClose");
			hideOnCloseElement.appendChild(dom.createTextNode(String.valueOf(hideOnClose)));
			rootSettings.appendChild(hideOnCloseElement);
			
			Element hideOnMinimizeElement = dom.createElement("hideOnMinimize");
			hideOnMinimizeElement.appendChild(dom.createTextNode(String.valueOf(hideOnMinimize)));
			rootSettings.appendChild(hideOnMinimizeElement);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(dom);
			StreamResult result = new StreamResult(new File("settings"));
			transformer.transform(source, result);
			
			
		} catch (ParserConfigurationException ex ){
			System.out.println(ex);
		} catch (TransformerException ex){
			System.out.println(ex);
		}
		
	}
	
	public void load(){
		parseSettingsFile();
		parseSettings();
	}
	private void parseSettingsFile() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setCoalescing(true);
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse("./settings");
		} catch (ParserConfigurationException ex) {
			System.out.println("problem s parsvaneto na faila: " + ex);
		} catch (SAXException ex) {
			write();
			wsv.showError("Settings file corrupted.\nDefault settings restored!");

		} catch (IOException ex) {
			System.out.println(ex);
		}
	}
		private static String getValue(String tag, Element element) {
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = nodes.item(0);
		return node.getNodeValue().trim();
	}

	
	public final void parseSettings() {
		try {
			NodeList nodes = dom.getElementsByTagName("settings");

			Node node = nodes.item(0);
			if (node.getNodeType() == 1) {
				Element element = (Element) node;
				laf = getValue("laf", element);
				distUnits = getValue("distUnits", element);
				tempUnits = getValue("tempUnits", element);
				refreshInterval = Long.parseLong(getValue("refreshInterval", element));
				hideOnClose = Boolean.parseBoolean(getValue("hideOnClose", element));
				hideOnMinimize = Boolean.parseBoolean(getValue("hideOnMinimize", element));
			}
		} catch (NullPointerException ex) {
			write();
			wsv.showError("Settings file corrupted.\nDefault settings restored!");
		}
		
	}
	
	public String getLaf() {
		return laf;
	}

	public void setLaf(String laf) {
		this.laf = laf;
	}

	public boolean isHideOnClose() {
		return hideOnClose;
	}

	public void setHideOnClose(boolean hideOnClose) {
		this.hideOnClose = hideOnClose;
	}

	public boolean isHideOnMinimize() {
		return hideOnMinimize;
	}

	public void setHideOnMinimize(boolean hideOnMinimize) {
		this.hideOnMinimize = hideOnMinimize;
	}

	

	public long getRefreshInterval() {
		return refreshInterval;
	}

	public void setRefreshInterval(long refreshInterval) {
		this.refreshInterval = refreshInterval;
	}

	public String getDistUnits() {
		return distUnits;
	}

	public void setDistUnits(String distUnits) {
		this.distUnits = distUnits;
	}

	public String getTempUnits() {
		return tempUnits;
	}

	public void setTempUnits(String tempUnits) {
		this.tempUnits = tempUnits;
	}

	@Override
	public String toString() {
		return "Settings{" + "laf=" + laf + ", distUnits=" + distUnits + ", tempUnits=" + tempUnits + ", refreshInterval=" + refreshInterval + ", hideOnClose=" + hideOnClose + ", hideOnMinimize=" + hideOnMinimize + '}';
	}

	
	

}
