/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upnp.device;

import method.player.AudioController;
import method.player.DoService;
import method.player.PlayController;
import method.player.Services;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.types.UDAServiceId;

/**
 *
 * @author Admins
 */
public class CDPlayer extends DeviceObj implements DoService{
    org.fourthline.cling.model.meta.Service service;
    
    public boolean isOn;
    protected Services services;
    
    public CDPlayer(Device device, UpnpService upnpService) {
        super(device, upnpService);
        service  = device.findService(new UDAServiceId("SwitchPower"));
        isOn = getStatus();
    }
    
    public void addService(boolean b)
    {
    }

    @Override
    public void SwitchPower() {
        ActionInvocation invocation = new ActionInvocation(service.getAction("GetStatus"));
        invocation.setInput("newTargetValue", !isOn);
        new ActionCallback.Default(invocation, upnpService.getControlPoint()).run();
        isOn = !isOn;
    }

    @Override
    public boolean getStatus() {
        ActionInvocation invocation = new ActionInvocation(service.getAction("GetStatus"));
        new ActionCallback.Default(invocation, upnpService.getControlPoint()).run();
        Boolean status = (Boolean) invocation.getOutput("ResultStatus").getValue();
        return status.booleanValue();
    }

    @Override
    public void DoService(boolean b) {
        if(this instanceof AudioController)
        {
            AudioController audio = (AudioController) this;
            this.services = audio;
            this.services.remote(b);
        }
        if(this instanceof PlayController)
        {
            PlayController player = (PlayController) this;
            this.services = player;
            this.services.remote(b);
        }
    }
}
