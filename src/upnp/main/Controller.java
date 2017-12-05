/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upnp.main;

import java.util.HashMap;
import method.player.AudioController;
import method.player.PlayController;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.model.meta.Device;
import upnp.device.DeviceObj;

/**
 *
 * @author Admins
 */
public class Controller {
    private UpnpService upnpService;
    private HashMap<Device, ArrayDevice> mapDevice;
    
    public Controller(UpnpService upnpService)
    {
        this.upnpService = upnpService;
        mapDevice = new HashMap<Device, ArrayDevice>();
    }
    
    public ArrayDevice getRemotedDevice(Device key)
    {
        return mapDevice.get(key);
    }
    
    public void add(Device key, DeviceObj value)
    {
        if(getRemotedDevice(key)== null)
        {
            ArrayDevice ar = new ArrayDevice();
            ar.addDevice(value);
            mapDevice.put(key, ar);
        } else {
            getRemotedDevice(key).addDevice(value);
        }
    }
    
    public void remove(Device key)
    {
        mapDevice.remove(key);
    }
    
    public DeviceObj createPlayService(Device deviceR)
    {
        DeviceObj device = new PlayController(deviceR, upnpService);
        return device;
    }
    
    public DeviceObj createAudioService(Device deviceR)
    {
        DeviceObj device = new AudioController(deviceR, upnpService);
        return device;
    }
}
