package com.wei.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static String getDate(Integer mi){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        System.out.println("当前时间：" + sdf.format(now));

        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, mi);
        System.out.println(sdf.format(nowTime.getTime()));
        return sdf.format(nowTime.getTime());
    }

    public static void main(String[] args) {
        getDate(-40);
    }
}
