package shop.ozip.dev.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Common {
    public static final String formatTimeStamp(Timestamp ts){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dateFormat.format(ts);
    }
}
