package com.example.newsclient.Model.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016-05-04.
 */
public class TimeUtil {
    private static TimeUtil timeUtil;
    private SimpleDateFormat formatter;

    public static TimeUtil getInstance() {
        if (timeUtil == null)
            synchronized (AppUtil.class) {
                if (timeUtil == null) {
                    timeUtil = new TimeUtil();
                }
            }
        return timeUtil;
    }

    private TimeUtil() {
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    /**
     * @return 与现在的时间差（如：2小时前）
     */
    public String getTimeForNow(String date) {
        Date msgDate = null;
        Date nowDate = new Date(System.currentTimeMillis());
        try {
            msgDate = formatter.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cEnd = Calendar.getInstance();
        Calendar cNow = Calendar.getInstance();

        cEnd.setTime(msgDate);
        cNow.setTime(nowDate);

        int year_cEnd = cEnd.get(Calendar.YEAR);
        int month_cEnd = cEnd.get(Calendar.MONTH);
        int day_cEnd = cEnd.get(Calendar.DAY_OF_MONTH);
        int hour_cEnd = cEnd.get(Calendar.HOUR_OF_DAY);

        int year_cNow = cNow.get(Calendar.YEAR);
        int month_cNow = cNow.get(Calendar.MONTH);
        int day_cNow = cNow.get(Calendar.DAY_OF_MONTH);
        int hour_cNow = cNow.get(Calendar.HOUR_OF_DAY);

        if (year_cEnd == year_cNow) {
            if (month_cEnd == month_cNow) {
                if (day_cEnd == day_cNow) {
                    if (hour_cEnd == hour_cNow) {
                        return cNow.get(Calendar.MINUTE) - cEnd.get(Calendar.MINUTE) + "分钟前";
                    } else {
                        return hour_cNow - hour_cEnd + "小时前";
                    }
                } else {
                    return day_cNow - day_cEnd + "天前";
                }
            }
            return month_cNow - month_cEnd + "个月前";
        }
        return date;
    }
}
