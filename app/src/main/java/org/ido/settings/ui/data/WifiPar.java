package org.ido.settings.ui.data;

import java.io.Serializable;

public class WifiPar implements Serializable {
    String name;
    String psw;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }
}
