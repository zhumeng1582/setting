package org.ido.settings.ui.data;

import java.io.Serializable;

public class WeekdayBean implements Serializable {
    private SelectBean select1 = new SelectBean();
    private SelectBean select2 = new SelectBean();
    private SelectBean select3 = new SelectBean();

    public SelectBean getSelect1() {
        return select1;
    }

    public void setSelect1(SelectBean select1) {
        this.select1 = select1;
    }

    public SelectBean getSelect2() {
        return select2;
    }

    public void setSelect2(SelectBean select2) {
        this.select2 = select2;
    }

    public SelectBean getSelect3() {
        return select3;
    }

    public void setSelect3(SelectBean select3) {
        this.select3 = select3;
    }

}
