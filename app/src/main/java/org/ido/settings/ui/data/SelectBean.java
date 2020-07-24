package org.ido.settings.ui.data;

import java.io.Serializable;

public class SelectBean implements Serializable {

    private String open = "00:00";
    private String close = "00:00";

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

}
