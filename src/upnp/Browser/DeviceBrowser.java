/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upnp.Browser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.message.header.STAllHeader;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.fourthline.cling.controlpoint.SubscriptionCallback;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.state.StateVariableValue;
import org.fourthline.cling.model.types.UDAServiceId;
import upnp.device.DeviceObj;
import upnp.main.Controller;
import upnp.main.RemoteCase;
/**
 *
 * @author Admins
 */
public class DeviceBrowser extends JFrame implements ListSelectionListener, ActionListener{
    private Controller controller;
    
    public DeviceBrowser()
    {
        upnpService = new UpnpServiceImpl(listener);
        upnpService.getControlPoint().search(new STAllHeader());
        controller = new Controller(upnpService);
        init();
    }
    
    UpnpService upnpService;
    
    RegistryListener listener = new RegistryListener() {

        public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {}//"Discovery started: " + device.getDisplayString()
        
//                        "Discovery failed: " + device.getDisplayString()
        public void remoteDeviceDiscoveryFailed(Registry registry, RemoteDevice device, Exception ex) {}

        public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
            System.out.println("Remote device available: " + device.getDisplayString());
            if(DeviceObj.isPhone(device))
            {
                listmodel.addElement(device); repaint();
                SubscriptionCallback subscriptionCallback = new SubscriptionCallback(device.findService(
                    new UDAServiceId("SwitchStatus")
                )) {
                    @Override
                    protected void failed(GENASubscription genaSubscription, UpnpResponse upnpResponse, Exception e, String s) {}

                    @Override
                    protected void established(GENASubscription genaSubscription) {}

                    @Override
                    protected void ended(GENASubscription genaSubscription, CancelReason cancelReason, UpnpResponse upnpResponse) {}

                    @Override
                    protected void eventReceived(GENASubscription genaSubscription) {
                        Map<String, StateVariableValue> values = genaSubscription.getCurrentValues();
                        StateVariableValue status = values.get("Status");
                        int value = Integer.parseInt(status.toString());
                        if(controller.getRemotedDevice(device) != null)
                        {
                            if(value == 0 ) controller.getRemotedDevice(device).eachDo(true);
                            if(value == 1 ) controller.getRemotedDevice(device).eachDo(false);
                        }
                    }

                    @Override
                    protected void eventsMissed(GENASubscription genaSubscription, int i) {}
                };
                upnpService.getControlPoint().execute(subscriptionCallback);
            }
            if(DeviceObj.isCDPlayer(device))
            {
                listmodel2.addElement(device); repaint();
            }
        }

        public void remoteDeviceUpdated(Registry registry, RemoteDevice device) {}//"Remote device updated: " + device.getDisplayString()

        public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
            System.out.println("Remote device removed: " + device.getDisplayString());
            listmodel.removeElement(device); repaint();
        }

        public void localDeviceAdded(Registry registry, LocalDevice device){} //"Local device added: " + device.getDisplayString()

        public void localDeviceRemoved(Registry registry, LocalDevice device) {}//"Local device removed:

        public void beforeShutdown(Registry registry) {}//"Before shutdown, the registry has devices: " + registry.getDevices().size()

        public void afterShutdown() {}//"Shutdown of registry complete!
    };
    
    private void init()
    {
        setSize(500, 500); setLayout(null); setLocationRelativeTo(null);
        setVisible(true); setDefaultCloseOperation(EXIT_ON_CLOSE);
        listBrowsedDevice = new JList(); listRemotedDevice = new JList();
        listmodel = new DefaultListModel(); listmodel2 = new DefaultListModel();
        listBrowsedDevice.setModel(listmodel); listRemotedDevice.setModel(listmodel2);
        JScrollPane pane = new JScrollPane(); pane.setBounds(25, 25, 200, 200);
        JScrollPane pane2 = new JScrollPane(); pane2.setBounds(250, 25, 200, 200);
        add(pane); pane.setViewportView(listBrowsedDevice);
        add(pane2); pane2.setViewportView(listRemotedDevice);
        listBrowsedDevice.addListSelectionListener(this); listRemotedDevice.addListSelectionListener(this);
        JLabel label = new JLabel("Device : ");
        label.setBounds(25, 250, 50, 20); add(label);
        deviceNamelb = new JLabel();
        deviceNamelb.setBounds(100, 250, 300, 20); add(deviceNamelb);
        JLabel label2 = new JLabel("Device : ");
        label2.setBounds(25, 300, 50, 20); add(label2);
        deviceNamelb2 = new JLabel();
        deviceNamelb2.setBounds(100, 300, 300, 20); add(deviceNamelb2);
        remoteBtn = new JButton("Remote"); remoteBtn.setBounds(200, 370, 100, 40); add(remoteBtn);
        remoteBtn.addActionListener(this);
        repaint();
    }
    
    private JList listBrowsedDevice;
    private DefaultListModel listmodel;
    private JLabel deviceNamelb;
    private JList listRemotedDevice;
    private DefaultListModel listmodel2;
    private JLabel deviceNamelb2;
    private JButton remoteBtn;
    
    @Override
    public void valueChanged(ListSelectionEvent lse) {
        Object obj = lse.getSource();
        if(obj == listBrowsedDevice)
        {
            int index = listBrowsedDevice.getSelectedIndex();
            deviceK = (Device) listmodel.get(index);
            deviceNamelb.setText(deviceK.getDetails().getFriendlyName());
        }
        if(obj == listRemotedDevice)
        {
            int index = listRemotedDevice.getSelectedIndex();
            deviceR = (Device) listmodel2.get(index);
            deviceNamelb2.setText(deviceR.getDetails().getFriendlyName());
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object obj = ae.getSource();
        if(obj == remoteBtn)
        {
            String device1 = deviceNamelb.getText(); String device2 = deviceNamelb2.getText();
            if(device1.compareTo("")==0 || device2.compareTo("")==0)
            { JOptionPane.showMessageDialog(null, "Please select device"); return; }
            if(remoteFrame != null && remoteFrame.isVisible()) return;
            
            remoteFrame = new RemoteCase(deviceK, deviceR, controller);
        }
    }
    
    private Device deviceK;
    private Device deviceR;
    private RemoteCase remoteFrame;
}