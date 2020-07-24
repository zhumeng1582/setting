package org.ido.settings.ui.data;

import java.io.Serializable;

public class NetworkPar implements Serializable {
    private String ipAddr;
    private String netmask;
    private String geteway;
    private String dns1;
    private String dns2;

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getNetmask() {
        return netmask;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    public String getGeteway() {
        return geteway;
    }

    public void setGeteway(String geteway) {
        this.geteway = geteway;
    }

    public String getDns1() {
        return dns1;
    }

    public void setDns1(String dns1) {
        this.dns1 = dns1;
    }

    public String getDns2() {
        return dns2;
    }

    public void setDns2(String dns2) {
        this.dns2 = dns2;
    }
}
