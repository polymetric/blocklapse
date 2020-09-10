package net.nodium.mcplugins.blocklapse;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Utils {
    public static long parseTime(String time) throws ParseException {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(time).getTime();
    }

    // takes ms since unix epoch and outputs how many minutes ago that was
    public static int secondsAgo(long time) {
        return (int) ((System.currentTimeMillis() - time) / 1000);
    }
}
