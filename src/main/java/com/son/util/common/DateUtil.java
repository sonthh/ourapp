package com.son.util.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

    public static String getDateForTitle(Date date) {
        int dayOfWeek = getDayOfWeek(date);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM");
        return (dayOfWeek == Calendar.SUNDAY ? "CN" : ("T" + dayOfWeek)) + " " +
                df.format(date);
    }

    public static int getDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public static String getFormatForExcelTitle(Date date, String type) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        if (type.equals("week")) {
            int weekOfMonth = c.get(Calendar.WEEK_OF_YEAR);
            return "Tuần " + weekOfMonth + "-" + year;
        }

        if (type.equals("month")) {
            int month = c.get(Calendar.MONTH) + 1;
            return "Tháng " + month + "-" + year;
        }
        return null;
    }

    public static List<Date> getDateList(Date startDate, Date endDate) {
        List<Date> dates = new ArrayList<Date>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (!calendar.getTime().after(endDate)) {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }
}
