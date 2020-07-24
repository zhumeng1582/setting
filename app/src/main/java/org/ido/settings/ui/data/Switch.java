package org.ido.settings.ui.data;

import java.io.Serializable;

public class Switch implements Serializable {
    private boolean switchGoable = false;
    //星期日。。。星期六
    private boolean[] weekday = new boolean[7];
    private boolean select1 = true;
    private boolean select2 = true;

    public boolean isSwitchGoable() {
        return switchGoable;
    }

    public void setSwitchGoable(boolean switchGoable) {
        this.switchGoable = switchGoable;
    }

    public boolean isWeekEmpty(){
        for (boolean b : weekday) {
            if (b) {
                return false;
            }
        }
        return true;
    }
    public boolean[] getWeekday(){
        return weekday;
    }
    public boolean isWeekday1() {
        return weekday[0];
    }


    public void setWeekday1(boolean weekday1) {
        this.weekday[0] = weekday1;
    }

    public boolean isWeekday2() {
        return weekday[1];
    }

    public void setWeekday2(boolean weekday2) {
        this.weekday[1] = weekday2;
    }

    public boolean isWeekday3() {
        return weekday[2];
    }

    public void setWeekday3(boolean weekday3) {
        this.weekday[2] = weekday3;
    }

    public boolean isWeekday4() {
        return weekday[3];
    }

    public void setWeekday4(boolean weekday4) {
        this.weekday[3] = weekday4;
    }

    public boolean isWeekday5() {
        return weekday[4];
    }

    public void setWeekday5(boolean weekday5) {
        this.weekday[4] = weekday5;
    }

    public boolean isWeekday6() {
        return weekday[5];
    }

    public void setWeekday6(boolean weekday6) {
        this.weekday[5] = weekday6;
    }

    public boolean isWeekday7() {
        return weekday[6];
    }

    public void setWeekday7(boolean weekday7) {
        this.weekday[6] = weekday7;
    }

    public boolean isSelect1() {
        return select1;
    }

    public void setSelect1(boolean select1) {
        this.select1 = select1;
    }

    public boolean isSelect2() {
        return select2;
    }

    public void setSelect2(boolean select2) {
        this.select2 = select2;
    }
}
