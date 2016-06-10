package ru.example.makaroff.wheathervideo.Utilits;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public final static String TIME_FORMAT = "HH:mm:ss";

    public static String convertDateToStringUsingDateFormat(Date date, String stringDateFormat) {
        return new SimpleDateFormat(stringDateFormat, Locale.getDefault())
                .format(date, new StringBuffer(), new FieldPosition(0))
                .toString();
    }
}
