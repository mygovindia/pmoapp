package com.sanskrit.pmo.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    @Deprecated
    public static final char QUOTE = '\'';
    @Deprecated
    public static final char SECONDS = 's';

    public static final class ISO8601 {
        public static String fromCalendar(Calendar calendar) {
            String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(calendar.getTime());
            return formatted.substring(0, 22) + ":" + formatted.substring(22);
        }

        public static String now() {
            return fromCalendar(GregorianCalendar.getInstance());
        }

        public static Calendar toCalendar(String iso8601string) throws ParseException {
            Calendar calendar = GregorianCalendar.getInstance();
            String s = iso8601string.replace("Z", "+00:00");
            try {
                calendar.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s.substring(0, 22) + s.substring(23)));
                return calendar;
            } catch (IndexOutOfBoundsException e) {
                throw new ParseException("Invalid length", 0);
            }
        }
    }

    public static String dateToString(Date date) {
        if (date != null) {
            return new SimpleDateFormat("dd MMM yyyy").format(date);
        }
        return "";
    }

    public static String dateToPastTenseString(Date date) {
        if (date == null) return "";
        return DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), 1000).toString();
    }

    public static String dateStringToFormattedString(String dateString) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            return new SimpleDateFormat("dd MMM yyyy").format(df.parse(dateString));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String dateStringToFormattedString1(String dateString) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return new SimpleDateFormat("dd MMM yyyy").format(df.parse(dateString));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date stringToDate(String string) {
        Date date = null;
        if (!(string == null || string.length() == 0)) {
            try {
                date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(string);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    public static Date isoStringToDate(String string) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean hasSeconds(CharSequence inFormat) {
        return hasDesignator(inFormat, SECONDS);
    }

    public static boolean hasDesignator(CharSequence inFormat, char designator) {
        if (inFormat == null) {
            return false;
        }
        int length = inFormat.length();
        int i = 0;
        while (i < length) {
            int count = 1;
            char c = inFormat.charAt(i);
            if (c == '\'') {
                count = skipQuotedText(inFormat, i, length);
            } else if (c == designator) {
                return true;
            }
            i += count;
        }
        return false;
    }

    private static int skipQuotedText(CharSequence s, int i, int len) {
        if (i + 1 < len && s.charAt(i + 1) == QUOTE) {
            return 2;
        }
        int count = 1;
        i++;
        while (i < len) {
            if (s.charAt(i) == QUOTE) {
                count++;
                if (i + 1 >= len || s.charAt(i + 1) != QUOTE) {
                    return count;
                }
                i++;
            } else {
                i++;
                count++;
            }
        }
        return count;
    }
}
