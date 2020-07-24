package org.ido.settings.utils;

import com.blankj.utilcode.util.StringUtils;

public class Strings {
    public static int compareTo(String value1, String value2) {
        if (StringUtils.isEmpty(value1) && StringUtils.isEmpty(value2)) {
            return 0;
        } else if (StringUtils.isEmpty(value1)) {
            return -1;
        } else if (StringUtils.isEmpty(value2)) {
            return 1;
        } else {
            return value1.compareTo(value2);
        }
    }
}
