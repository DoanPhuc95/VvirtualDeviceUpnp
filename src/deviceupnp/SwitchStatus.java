/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deviceupnp;

import java.beans.PropertyChangeSupport;
import org.fourthline.cling.binding.annotations.UpnpAction;
import org.fourthline.cling.binding.annotations.UpnpInputArgument;
import org.fourthline.cling.binding.annotations.UpnpOutputArgument;
import org.fourthline.cling.binding.annotations.UpnpService;
import org.fourthline.cling.binding.annotations.UpnpServiceId;
import org.fourthline.cling.binding.annotations.UpnpServiceType;
import org.fourthline.cling.binding.annotations.UpnpStateVariable;

/**
 *
 * @author Admins
 */
@UpnpService(
        serviceId = @UpnpServiceId("SwitchStatus"),
        serviceType = @UpnpServiceType(value = "SwitchStatus", version = 1)
)

public class SwitchStatus {
    private final PropertyChangeSupport propertyChangeSupport;
    
    public SwitchStatus() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }
    
    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }
    
    @UpnpStateVariable(defaultValue = "0", sendEvents = false)
    private boolean target = false;
    
    @UpnpStateVariable(defaultValue = "0")
    private boolean status = false;
    
    @UpnpAction
    public void setTarget(@UpnpInputArgument(name = "NewTargetValue") boolean newTargetValue) {
        boolean targetOldValue = target;
        target = newTargetValue;
        boolean statusOldValue = status;
        status = newTargetValue;

        // These have no effect on the UPnP monitoring but it's JavaBean compliant
        getPropertyChangeSupport().firePropertyChange("target", targetOldValue, target);
        getPropertyChangeSupport().firePropertyChange("status", statusOldValue, status);

        // This will send a UPnP event, it's the name of a state variable that sends events
        getPropertyChangeSupport().firePropertyChange("Status", statusOldValue, status);
    }
    
    @UpnpAction(out = @UpnpOutputArgument(name = "RetTargetValue"))
    public boolean getTarget() {
        return target;
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "ResultStatus"))
    public boolean getStatus() {
        return status;
    }
}
