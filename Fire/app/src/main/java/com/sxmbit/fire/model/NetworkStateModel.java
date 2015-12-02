package com.sxmbit.fire.model;

/**
 * Created by LinLin on 2015/11/20.监听网络连接变化
 */
public class NetworkStateModel
{
    private boolean hasNetwork;

    public NetworkStateModel(boolean hasNetwork)
    {
        this.hasNetwork = hasNetwork;
    }

    public boolean isHasNetwork()
    {
        return hasNetwork;
    }

    public void setHasNetwork(boolean hasNetwork)
    {
        this.hasNetwork = hasNetwork;
    }

    //    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
//
//    public void setProperty(boolean newValue)
//    {
//        boolean oldValue = hasNetwork;
//        hasNetwork = newValue;
//        changeSupport.firePropertyChange("property", oldValue, newValue);
//    }
//
//    public void addPropertyChangeListener(PropertyChangeListener l)
//    {
//        changeSupport.addPropertyChangeListener(l);
//    }
//
//    public void removePropertyChangeListener(PropertyChangeListener l)
//    {
//        changeSupport.removePropertyChangeListener(l);
//    }
}
