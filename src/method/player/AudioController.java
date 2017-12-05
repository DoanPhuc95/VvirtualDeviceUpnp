/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package method.player;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.types.UDAServiceId;
import upnp.device.CDPlayer;

/**
 *
 * @author Admins
 */
public class AudioController extends CDPlayer implements Services.AudioService, Services{
    
    org.fourthline.cling.model.meta.Service service;
    
    protected int currentVolume;
    protected boolean isMute = false;
    
    public AudioController(Device device, UpnpService upnpService) {
        super(device, upnpService);
        service = device.findService(new UDAServiceId("Audio"));
        currentVolume = getVolume();
    }

    @Override
    public int getVolume() {
        ActionInvocation invocation = new ActionInvocation(service.getAction("GetAudio"));
        new ActionCallback.Default(invocation, upnpService.getControlPoint()).run();
        Integer volume = (Integer) invocation.getOutput("CurrentVolume").getValue();
        return volume.intValue();
    }
    
    @Override
    public void remote(boolean b) {
        if(!getStatus()) return;
        if(b)
        {
            setVolume(currentVolume);
            isMute = false;
        } else {
            setVolume(0);
            isMute = true;
        }
    }

    @Override
    public void setVolume(int value) {
        if(!isMute) currentVolume = getVolume();
        ActionInvocation invocation = new ActionInvocation(service.getAction("SetVolume"));
        invocation.setInput("NewVolume", value);
        new ActionCallback.Default(invocation, upnpService.getControlPoint()).run();
    }
}
