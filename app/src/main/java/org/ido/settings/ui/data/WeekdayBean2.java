package org.ido.settings.ui.data;

import java.io.Serializable;

public class WeekdayBean2 implements Serializable {
    private SelectBean select1 = new SelectBean();
    private SelectBean select2 = new SelectBean();

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


}
