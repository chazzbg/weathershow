/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package weathershow;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 *
 * @author Chazz
 */
public class WeatherShowTray {

    private SystemTray tray;
    private TrayIcon icon;
    private PopupMenu menu;
    private MenuItem about;
    private MenuItem refresh;
    private MenuItem hideShowToggle;
    private MenuItem exit;
    private MenuItem setLocation;
	private MenuItem settings;
    private WeatherShowView wsv;
    public WeatherShowTray(WeatherShowView wsv) {
         this.wsv = wsv;
        initComponents();
       
    }

    private void initComponents() {
        if (!SystemTray.isSupported()) {
            return;
        }
       
        tray = SystemTray.getSystemTray();
        menu = new PopupMenu();
        icon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/weathershow/resources/images/icon.png")), "Weather Show");
        about = new MenuItem("About");
        refresh = new MenuItem("Refresh");
        exit = new MenuItem("Exit");
        setLocation = new MenuItem("Set location");
        hideShowToggle = new MenuItem(wsv.isVisible()?"Hide":"Show");
        settings = new MenuItem("Settings");
		
        icon.setPopupMenu(menu);
        icon.setImageAutoSize(true);
        
        menu.add(refresh);
        menu.add(setLocation);
		
        menu.add(hideShowToggle);
		menu.add(settings);
        menu.addSeparator();
        menu.add(about);
        menu.add(exit);
       
        

        icon.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                trayActionPerformed(ae);
            }
        });
        about.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                aboutActionPerformed(e);
            }
        });
        
        refresh.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                 refreshActionPerformed(e);
            }
        });
        
        hideShowToggle.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                hideShowToggleActionPerformed(e);
            }
        });
        setLocation.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setLocationActionPerformed(e);
            }
        });
        exit.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                exitActionPerformed(e);
            }
        });
		
		settings.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				settingsActionPerformed(ae);
			}

		});
        try {
            tray.add(icon);

        } catch (AWTException ex) {
            System.out.println(ex);
        }
       
    }

    private void trayActionPerformed(ActionEvent evt) {
        wsv.hideShowToggle();
        if(wsv.isVisible()){ 
            hideShowToggle.setLabel("Hide");
        } else {
            hideShowToggle.setLabel("Show");
        }
    }
    
    private void aboutActionPerformed(ActionEvent evt){
       wsv.openAboutBox();
    }
    private void hideShowToggleActionPerformed(ActionEvent evt) {
        wsv.hideShowToggle();
        if(wsv.isVisible()){ 
            hideShowToggle.setLabel("Hide");
        } else {
            hideShowToggle.setLabel("Show");
        }
    }
    private void refreshActionPerformed(ActionEvent evt){
		 wsv.refresh();
    }
    
    private void setLocationActionPerformed(ActionEvent evt) {
        wsv.openSetLocationBox();
    }
    
    private void exitActionPerformed(ActionEvent evt){
        System.exit(0);
    }
    public void setTrayIcon(int weatherCode){
        icon.setImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/weathershow/resources/images/"+weatherCode+"_day_sm.png")));
    }
    
    public void setTrayTitle(String weather){
        icon.setToolTip(weather);
    }
	
	
	private void settingsActionPerformed(ActionEvent ae) {
		wsv.openSettingsBox();
	}
   
    
}
