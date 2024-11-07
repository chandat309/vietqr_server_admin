package com.vietqradminbe.infrastructure.configuration.timehelper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeHelperUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static String getCurrentTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZonedDateTime vietnamTime = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        return vietnamTime.format(formatter);
    }

    public static String getEpochStartTime() {
        // Mốc thời gian bắt đầu (Epoch)
        return "1970-01-01 00:00:00";
    }

    // Chuyển đổi thời gian từ chuỗi định dạng "yyyy-MM-dd HH:mm:ss" (giờ Việt Nam) sang long theo UTC+7
    public static long convertVietnamTimeStringToEpochLong(String vietnamTimeString) {
        LocalDateTime localDateTime = LocalDateTime.parse(vietnamTimeString, FORMATTER);
        ZonedDateTime vietnamZonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        return vietnamZonedDateTime.toInstant().toEpochMilli();
    }
}
