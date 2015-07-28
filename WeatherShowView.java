package weathershow;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Chazz
 */
public class WeatherShowView extends javax.swing.JFrame {

	private CurrentConditions cc;
	private City city;
	private Forecast[] forecast;
	private Weather weather;
	public static JDialog aboutBox;
	public static JDialog setLocationBox;
	public static JDialog settingsBox;
	private String location;
	private WeatherShowTray tray;
	private Settings settings;


	/**
	 * Creates new form WeatherShowView
	 */
	public WeatherShowView() {
		settings = new Settings(this);
		settings.load();
		
		setLAF(settings.getLaf());
		SwingUtilities.updateComponentTreeUI(this);
		tray = new WeatherShowTray(this);
		initComponents();
		setTimer();
		
		
		
		
		
	}

	// <editor-fold defaultstate="collapsed" desc="UI Update">
	private void updateUI() {
		
		String windSpeed, visibility,tempSign;
		
		if(settings.getTempUnits().equals("Celsius"))
			tempSign = "°C";
		else
			tempSign = "°F";
		
		if(settings.getDistUnits().equals("Kilometers")){
			 windSpeed = cc.getWindSpeedKmph() + " km\\h ";
			 visibility = cc.getVisibility() + " km";
		} else {
			 windSpeed = cc.getWindSpeedMiles() + " miles\\h ";
			 visibility = Math.round((float)cc.getVisibility()*0.62f) + " miles";
		}
		
		
		cityLabel.setText(city.getCity());
		tempLabel.setText((settings.getTempUnits().equals("Celsius") ? cc.getTempC() :cc.getTempF()) + tempSign);
		
		
		windLabel.setText("Wind: " + windSpeed + cc.getWindDir16Point());
		humidityLabel.setText("Humidity: " + cc.getHumidity() + "%");
		visibilityLabel.setText("Visibility: " + visibility);
		cloudCoverLabel.setText("Cloud cover: " + cc.getCloudCover() + "%");
		precipLabel.setText("Precip: " + cc.getPrecipMM() + " mm");
		pressureLabel.setText("Pressure: " + cc.getPressure() + " mb");
		weatherDescLabel.setText(cc.getWeatherDesc());
		weatherIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/weathershow/resources/images/" + cc.getWeatherCode() + "_day_lg.png")));
		DateFormat formatOrig = new SimpleDateFormat("y-m-d h:mm a", Locale.US);
		DateFormat format = new SimpleDateFormat("h:mm a", Locale.US);
		try {
			Date obsDate = formatOrig.parse(cc.getLocalObsDateTime());

			lastObservedLabel.setText("Last observation: " + format.format(obsDate));
		} catch (ParseException ex) {
			System.out.println("Parse date error " + ex);
		}

		try {
			DateFormat[] formatForecast = new SimpleDateFormat[forecast.length];
			DateFormat[] formatForecastDay = new SimpleDateFormat[forecast.length];
			Date[] day = new Date[forecast.length];
			String[] days = new String[forecast.length];

			for (int i = 0; i < forecast.length; i++) {
				formatForecast[i] = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
				formatForecastDay[i] = new SimpleDateFormat("EEEE", Locale.US);
				day[i] = formatForecast[i].parse(forecast[i].getDate());
				days[i] = formatForecastDay[i].format(day[i]);
			}

			day1Label.setText(days[0]);
			day2Label.setText(days[1]);
			day3Label.setText(days[2]);
			day4Label.setText(days[3]);

			day1H.setText("H: " + (settings.getTempUnits().equals("Celsius")? forecast[0].getTempMaxC() : forecast[0].getTempMaxF()) + tempSign);
			day1L.setText("L: " + (settings.getTempUnits().equals("Celsius")? forecast[0].getTempMinC() : forecast[0].getTempMinF()) + tempSign);

			day2H.setText("H: " + (settings.getTempUnits().equals("Celsius")? forecast[1].getTempMaxC() : forecast[1].getTempMaxF()) + tempSign);
			day2L.setText("L: " + (settings.getTempUnits().equals("Celsius")? forecast[1].getTempMinC() : forecast[1].getTempMinF()) + tempSign);

			day3H.setText("H: " + (settings.getTempUnits().equals("Celsius")? forecast[2].getTempMaxC() : forecast[2].getTempMaxF()) + tempSign);
			day3L.setText("L: " + (settings.getTempUnits().equals("Celsius")? forecast[2].getTempMinC() : forecast[2].getTempMinF()) + tempSign);

			day4H.setText("H: " + (settings.getTempUnits().equals("Celsius")? forecast[3].getTempMaxC() : forecast[3].getTempMaxF())+ tempSign);
			day4L.setText("L: " + (settings.getTempUnits().equals("Celsius")? forecast[3].getTempMinC() : forecast[3].getTempMinF()) + tempSign);

			day1Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/weathershow/resources/images/" + forecast[0].getWeatherCode() + "_day_sm.png")));
			day2Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/weathershow/resources/images/" + forecast[1].getWeatherCode() + "_day_sm.png")));
			day3Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/weathershow/resources/images/" + forecast[2].getWeatherCode() + "_day_sm.png")));
			day4Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/weathershow/resources/images/" + forecast[3].getWeatherCode() + "_day_sm.png")));
			tray.setTrayIcon(cc.getWeatherCode());
			tray.setTrayTitle(city.getCity() + "\n" + cc.getWeatherDesc() + "\n" + (settings.getTempUnits().equals("Celsius") ? cc.getTempC() :cc.getTempF()) + tempSign);
		} catch (ParseException ex) {
			System.out.println("Parse date error " + ex);
		} catch (NullPointerException ex) {
			System.out.println(ex);
		}
	}// </editor-fold>

	private void weatherFetch() {
		weather = new Weather(this);

		Runnable newTask = new Runnable() {

			@Override
			public void run() {
				progressBar.setValue(100);
				progressBar.setIndeterminate(true);
				weather.init();
				
				if (city != null
						&& cc != null
						&& forecast != null) {
					updateUI();
				}
				progressBar.setIndeterminate(false);
				progressBar.setValue(0);
			}
		};

		new Thread(newTask).start();
	}

	private void setTimer() {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				weatherFetch();
			}
		};
		timer.scheduleAtFixedRate(task, 0l, settings.getRefreshInterval());
	}

	public void showError(String err) {
		JOptionPane.showMessageDialog(rootPane, err, "Error", 0);
	}

	public void debug(Object obj) {
		System.out.println(obj);
	}

	/**
	 * method is called from within the constructor to initialize the form. WARNING: Do NOT modify code. The content of method is always regenerated
	 * by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        currentWeatherPanel = new javax.swing.JPanel();
        cityLabel = new javax.swing.JLabel();
        tempLabel = new javax.swing.JLabel();
        weatherDescLabel = new javax.swing.JLabel();
        lastObservedLabel = new javax.swing.JLabel();
        windLabel = new javax.swing.JLabel();
        precipLabel = new javax.swing.JLabel();
        humidityLabel = new javax.swing.JLabel();
        cloudCoverLabel = new javax.swing.JLabel();
        weatherIcon = new javax.swing.JLabel();
        visibilityLabel = new javax.swing.JLabel();
        pressureLabel = new javax.swing.JLabel();
        setLocationButton = new javax.swing.JButton();
        aboutButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        forecastPanel = new javax.swing.JPanel();
        day1Pane = new javax.swing.JPanel();
        day1Label = new javax.swing.JLabel();
        day1Icon = new javax.swing.JLabel();
        day1H = new javax.swing.JLabel();
        day1L = new javax.swing.JLabel();
        day2Pane = new javax.swing.JPanel();
        day2Label = new javax.swing.JLabel();
        day2Icon = new javax.swing.JLabel();
        day2H = new javax.swing.JLabel();
        day2L = new javax.swing.JLabel();
        day3Pane = new javax.swing.JPanel();
        day3Label = new javax.swing.JLabel();
        day3Icon = new javax.swing.JLabel();
        day3H = new javax.swing.JLabel();
        day3L = new javax.swing.JLabel();
        day4Pane = new javax.swing.JPanel();
        day4Label = new javax.swing.JLabel();
        day4Icon = new javax.swing.JLabel();
        day4H = new javax.swing.JLabel();
        day4L = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        refreshButton = new javax.swing.JButton();
        settingsButton = new javax.swing.JButton();

        setDefaultCloseOperation(settings.isHideOnClose() ? HIDE_ON_CLOSE : EXIT_ON_CLOSE);
        setTitle("Weather Show ");
        setBackground(null);
        setIconImage(new ImageIcon(getClass().getResource("/weathershow/resources/images/icon.png")).getImage());
        setLocationByPlatform(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowIconified(java.awt.event.WindowEvent evt) {
                formWindowIconified(evt);
            }
        });

        currentWeatherPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Current weather"));
        currentWeatherPanel.setToolTipText(null);
        currentWeatherPanel.setName("Current Weather"); // NOI18N
        currentWeatherPanel.setOpaque(false);

        cityLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        cityLabel.setText("City");

        tempLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        tempLabel.setText("0°C");

        weatherDescLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        weatherDescLabel.setText("WeatherDesc");

        lastObservedLabel.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lastObservedLabel.setText("Last observed: 10:15 PM");

        windLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        windLabel.setText("Wind: 0 kmp/h N");

        precipLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        precipLabel.setText("Precip: 0 MM");

        humidityLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        humidityLabel.setText("Humidity: 0%");

        cloudCoverLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cloudCoverLabel.setText("Cloud Cover: 0%");

        weatherIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/weathershow/resources/images/na.png"))); // NOI18N

        visibilityLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        visibilityLabel.setText("Visibility: 0km");

        pressureLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        pressureLabel.setText("Pressure: 0 MP");

        javax.swing.GroupLayout currentWeatherPanelLayout = new javax.swing.GroupLayout(currentWeatherPanel);
        currentWeatherPanel.setLayout(currentWeatherPanelLayout);
        currentWeatherPanelLayout.setHorizontalGroup(
            currentWeatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(currentWeatherPanelLayout.createSequentialGroup()
                .addGroup(currentWeatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(currentWeatherPanelLayout.createSequentialGroup()
                        .addComponent(tempLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(weatherDescLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lastObservedLabel))
                .addContainerGap())
            .addGroup(currentWeatherPanelLayout.createSequentialGroup()
                .addGroup(currentWeatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(currentWeatherPanelLayout.createSequentialGroup()
                        .addGroup(currentWeatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cloudCoverLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(windLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(currentWeatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(currentWeatherPanelLayout.createSequentialGroup()
                                .addComponent(humidityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(visibilityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, currentWeatherPanelLayout.createSequentialGroup()
                                .addComponent(precipLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pressureLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(weatherIcon))
        );
        currentWeatherPanelLayout.setVerticalGroup(
            currentWeatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(currentWeatherPanelLayout.createSequentialGroup()
                .addGroup(currentWeatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(currentWeatherPanelLayout.createSequentialGroup()
                        .addComponent(cityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(currentWeatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tempLabel)
                            .addComponent(weatherDescLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(currentWeatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(windLabel)
                            .addComponent(humidityLabel)
                            .addComponent(visibilityLabel))
                        .addGap(3, 3, 3)
                        .addGroup(currentWeatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cloudCoverLabel)
                            .addComponent(precipLabel)
                            .addComponent(pressureLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lastObservedLabel))
                    .addComponent(weatherIcon))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        setLocationButton.setText("Set location");
        setLocationButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        setLocationButton.setMargin(new java.awt.Insets(1, 1, 1, 1));
        setLocationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setLocationButtonActionPerformed(evt);
            }
        });

        aboutButton.setText("About");
        aboutButton.setMargin(new java.awt.Insets(1, 1, 1, 1));
        aboutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutButtonActionPerformed(evt);
            }
        });

        exitButton.setText("Exit");
        exitButton.setMargin(new java.awt.Insets(1, 1, 1, 1));
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        forecastPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("4 day forecast"));

        day1Pane.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        day1Pane.setPreferredSize(new java.awt.Dimension(120, 100));

        day1Label.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        day1Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        day1Label.setText("Day");

        day1Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/weathershow/resources/images/na_small.png"))); // NOI18N

        day1H.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        day1H.setText("H: 0°C");

        day1L.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        day1L.setText(" L: 0°C");

        javax.swing.GroupLayout day1PaneLayout = new javax.swing.GroupLayout(day1Pane);
        day1Pane.setLayout(day1PaneLayout);
        day1PaneLayout.setHorizontalGroup(
            day1PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(day1Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(day1PaneLayout.createSequentialGroup()
                .addComponent(day1Icon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(day1PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(day1H, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(day1L, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        day1PaneLayout.setVerticalGroup(
            day1PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(day1PaneLayout.createSequentialGroup()
                .addComponent(day1Label)
                .addGroup(day1PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(day1PaneLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(day1Icon))
                    .addGroup(day1PaneLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(day1H)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(day1L)))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        day2Pane.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        day2Pane.setPreferredSize(new java.awt.Dimension(120, 100));

        day2Label.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        day2Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        day2Label.setText("Day");
        day2Label.setFocusCycleRoot(true);
        day2Label.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        day2Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/weathershow/resources/images/na_small.png"))); // NOI18N

        day2H.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        day2H.setText("H: 0°C");

        day2L.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        day2L.setText("L: 0°C");

        javax.swing.GroupLayout day2PaneLayout = new javax.swing.GroupLayout(day2Pane);
        day2Pane.setLayout(day2PaneLayout);
        day2PaneLayout.setHorizontalGroup(
            day2PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(day2Label, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
            .addGroup(day2PaneLayout.createSequentialGroup()
                .addComponent(day2Icon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(day2PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(day2H, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addComponent(day2L, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)))
        );
        day2PaneLayout.setVerticalGroup(
            day2PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(day2PaneLayout.createSequentialGroup()
                .addComponent(day2Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(day2PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(day2Icon)
                    .addGroup(day2PaneLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(day2H)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(day2L)))
                .addGap(0, 13, Short.MAX_VALUE))
        );

        day3Pane.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        day3Pane.setPreferredSize(new java.awt.Dimension(120, 100));

        day3Label.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        day3Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        day3Label.setText("Day");

        day3Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/weathershow/resources/images/na_small.png"))); // NOI18N

        day3H.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        day3H.setText("H:0°C");

        day3L.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        day3L.setText("L: 0°C");

        javax.swing.GroupLayout day3PaneLayout = new javax.swing.GroupLayout(day3Pane);
        day3Pane.setLayout(day3PaneLayout);
        day3PaneLayout.setHorizontalGroup(
            day3PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(day3Label, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
            .addGroup(day3PaneLayout.createSequentialGroup()
                .addComponent(day3Icon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(day3PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(day3H, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addComponent(day3L, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)))
        );
        day3PaneLayout.setVerticalGroup(
            day3PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(day3PaneLayout.createSequentialGroup()
                .addComponent(day3Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(day3PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(day3Icon)
                    .addGroup(day3PaneLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(day3H)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(day3L)))
                .addGap(0, 13, Short.MAX_VALUE))
        );

        day4Pane.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        day4Pane.setPreferredSize(new java.awt.Dimension(120, 100));

        day4Label.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        day4Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        day4Label.setText("Day");

        day4Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/weathershow/resources/images/na_small.png"))); // NOI18N

        day4H.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        day4H.setText("H: 0°C");

        day4L.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        day4L.setText("L: 0°C");

        javax.swing.GroupLayout day4PaneLayout = new javax.swing.GroupLayout(day4Pane);
        day4Pane.setLayout(day4PaneLayout);
        day4PaneLayout.setHorizontalGroup(
            day4PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(day4Label, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
            .addGroup(day4PaneLayout.createSequentialGroup()
                .addComponent(day4Icon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(day4PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(day4H, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addComponent(day4L, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)))
        );
        day4PaneLayout.setVerticalGroup(
            day4PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(day4PaneLayout.createSequentialGroup()
                .addComponent(day4Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(day4PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(day4Icon)
                    .addGroup(day4PaneLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(day4H)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(day4L)))
                .addGap(0, 13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout forecastPanelLayout = new javax.swing.GroupLayout(forecastPanel);
        forecastPanel.setLayout(forecastPanelLayout);
        forecastPanelLayout.setHorizontalGroup(
            forecastPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(forecastPanelLayout.createSequentialGroup()
                .addComponent(day1Pane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(day2Pane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(day3Pane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(day4Pane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        forecastPanelLayout.setVerticalGroup(
            forecastPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(forecastPanelLayout.createSequentialGroup()
                .addGroup(forecastPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(day3Pane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(day4Pane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(day1Pane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(day2Pane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        refreshButton.setText("Refresh");
        refreshButton.setMargin(new java.awt.Insets(1, 1, 1, 1));
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        settingsButton.setText("Settings");
        settingsButton.setMargin(new java.awt.Insets(1, 1, 1, 1));
        settingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(setLocationButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(settingsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(refreshButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(aboutButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exitButton))
            .addComponent(currentWeatherPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(forecastPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(currentWeatherPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(forecastPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(setLocationButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(aboutButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(refreshButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(settingsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(exitButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
		System.exit(0);
	}//GEN-LAST:event_exitButtonActionPerformed

	private void aboutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutButtonActionPerformed
		openAboutBox();
	}//GEN-LAST:event_aboutButtonActionPerformed

	private void setLocationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setLocationButtonActionPerformed
		openSetLocationBox();
	}//GEN-LAST:event_setLocationButtonActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
		weatherFetch();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void formWindowIconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowIconified
		if(settings.isHideOnMinimize())
			setVisible(false);
		
    }//GEN-LAST:event_formWindowIconified

	private void settingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsButtonActionPerformed
		openSettingsBox();
	}//GEN-LAST:event_settingsButtonActionPerformed
	public void openSetLocationBox() {

		setLocationBox = new WeatherShowSetLocation(this);
		setLocationBox.setLocationRelativeTo(rootPane);
		setLocationBox.setVisible(true);
		if (location != null) {
			weatherFetch();
		} else if (location == null && !weather.weatherFileExists()) {
			showError("You must select location");
		}


	}

	public void openAboutBox() {
		if (aboutBox == null) {
			aboutBox = new WeatherShowAbout(this);
			aboutBox.setLocationRelativeTo(rootPane);
			aboutBox.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosing(WindowEvent e) {
					aboutBox.dispose();

				}
			});
			aboutBox.setVisible(true);
		}
		aboutBox.dispose();
		aboutBox = null;
	}
	
	public void openSettingsBox(){
		settingsBox = new WeatherShowSettings(this);
		settingsBox.setLocationRelativeTo(rootPane);
		settingsBox.setVisible(true);
	}
	
	public void setWeatherLocation(String location) {
		this.location = location;
	}

	public String getWeatherLocation() {
		return this.location;
	}

	public void setCc(CurrentConditions cc) {
		this.cc = cc;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public void setForecast(Forecast[] forecast) {
		this.forecast = forecast;
	}

	public void hideShowToggle() {
		int state = getExtendedState();
		System.out.println(state);
		// Clear the iconified bit
		state &= ~ICONIFIED;
		System.out.println(state);
		// Deiconify the frame
		setExtendedState(state);

		setVisible(!isVisible());
		if(isVisible())toFront();
	}
	
	public static void setLAF(Object laf){
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				
				
				if (laf.equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(WeatherShowView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(WeatherShowView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(WeatherShowView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(WeatherShowView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
	}
	
	public void refresh(){
		
		weatherFetch();
	}
	
	public void setNewSettings(){
		settings.load();
		setDefaultCloseOperation(settings.isHideOnClose()? HIDE_ON_CLOSE : EXIT_ON_CLOSE);
		refresh();
	}
	/**
	 * @param args the command line arguments
	 */
	public static void main(final String args[]) {
		/*
		 * Set the Nimbus look and feel
		 */
		//setLAF("Nimbus");
		/*
		 * Create and display the form
		 */

		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {

				new WeatherShowView().setVisible(true);
			}
		});
	}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aboutButton;
    private javax.swing.JLabel cityLabel;
    private javax.swing.JLabel cloudCoverLabel;
    private javax.swing.JPanel currentWeatherPanel;
    private javax.swing.JLabel day1H;
    private javax.swing.JLabel day1Icon;
    private javax.swing.JLabel day1L;
    private javax.swing.JLabel day1Label;
    private javax.swing.JPanel day1Pane;
    private javax.swing.JLabel day2H;
    private javax.swing.JLabel day2Icon;
    private javax.swing.JLabel day2L;
    private javax.swing.JLabel day2Label;
    private javax.swing.JPanel day2Pane;
    private javax.swing.JLabel day3H;
    private javax.swing.JLabel day3Icon;
    private javax.swing.JLabel day3L;
    private javax.swing.JLabel day3Label;
    private javax.swing.JPanel day3Pane;
    private javax.swing.JLabel day4H;
    private javax.swing.JLabel day4Icon;
    private javax.swing.JLabel day4L;
    private javax.swing.JLabel day4Label;
    private javax.swing.JPanel day4Pane;
    private javax.swing.JButton exitButton;
    private javax.swing.JPanel forecastPanel;
    private javax.swing.JLabel humidityLabel;
    private javax.swing.JLabel lastObservedLabel;
    private javax.swing.JLabel precipLabel;
    private javax.swing.JLabel pressureLabel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton setLocationButton;
    private javax.swing.JButton settingsButton;
    private javax.swing.JLabel tempLabel;
    private javax.swing.JLabel visibilityLabel;
    private javax.swing.JLabel weatherDescLabel;
    private javax.swing.JLabel weatherIcon;
    private javax.swing.JLabel windLabel;
    // End of variables declaration//GEN-END:variables
}
