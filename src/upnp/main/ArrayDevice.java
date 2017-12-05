/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upnp.main;

import java.util.ArrayList;
import upnp.device.DeviceObj;

/**
 *
 * @author Admins
 */
public class ArrayDevice extends ArrayList<DeviceObj>{
    public ArrayDevice() {}
    
    public void removeExistDevice(DeviceObj d)
    {
        for(int i=0; i< size(); i++)
        {
            if(get(i).getDevice() == d.getDevice())
            {
                remove(i);
                return;
            }
        }
    }
    
    public void removeDevice(DeviceObj d)
    {
        remove(d);
    }
    
    public void addDevice(DeviceObj d)
    {
        removeExistDevice(d);
        add(d);
        System.out.println(size());
    }
    
    public void eachDo(boolean b)
    {
        for(DeviceObj temp: this)
            temp.remoteDevice(b);
    }
}
