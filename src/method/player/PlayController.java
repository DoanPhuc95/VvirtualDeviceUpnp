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
public class PlayController extends CDPlayer implements Services.PlayService, Services{
    
    org.fourthline.cling.model.meta.Service service;
    
    public PlayController(Device device, UpnpService upnpService) {
        super(device, upnpService);
        service = device.findService(new UDAServiceId("PlayCD"));
    }
    
    @Override
    public void play() {
        ActionInvocation invocation = new ActionInvocation(service.getAction("Play"));
        new ActionCallback.Default(invocation, upnpService.getControlPoint()).run();
    }

    @Override
    public void pause() {
        ActionInvocation invocation = new ActionInvocation(service.getAction("Pause"));
        new ActionCallback.Default(invocation, upnpService.getControlPoint()).run();
    }

    @Override
    public void stop() {
        ActionInvocation invocation = new ActionInvocation(service.getAction("Stop"));
        new ActionCallback.Default(invocation, upnpService.getControlPoint()).run();
    }

    @Override
    public String getMode() {
        ActionInvocation invocation = new ActionInvocation(service.getAction("GetPlayMode"));
        new ActionCallback.Default(invocation, upnpService.getControlPoint()).run();
        String mode = (String) invocation.getOutput("Mode").getValue();
        return mode;
    }

    @Override
    public void remote(boolean b) {
        if(!isOn) return;
        String mode = getMode();
        switch(mode) 
        {
            case "STOP": return;
            default:
                if(b) play();
                else pause();
        }
    }
}
