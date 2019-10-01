/*
 *     Copyright 2018 - 2019 Paul Hagedorn (Panzer1119)
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package de.codemakers.base.util;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeUtil {
    
    public static final DateTimeFormatter ISO_TIME_FIXED_LENGTH = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    public static final DateTimeFormatter ISO_LOCAL_DATE_TIME_FIXED_LENGTH = new DateTimeFormatterBuilder().parseCaseInsensitive().append(DateTimeFormatter.ISO_LOCAL_DATE).appendLiteral('T').append(ISO_TIME_FIXED_LENGTH).toFormatter(Locale.getDefault());
    public static final DateTimeFormatter ISO_OFFSET_DATE_TIME_FIXED_LENGTH = new DateTimeFormatterBuilder().parseCaseInsensitive().append(ISO_LOCAL_DATE_TIME_FIXED_LENGTH).appendOffset("+HH:MM:ss", "+00:00").toFormatter(Locale.getDefault());
    public static final DateTimeFormatter ISO_OFFSET_REGION_DATE_TIME_FIXED_LENGTH = new DateTimeFormatterBuilder().parseCaseInsensitive().append(ISO_OFFSET_DATE_TIME_FIXED_LENGTH).appendLiteral('[').appendZoneRegionId().appendLiteral(']').toFormatter(Locale.getDefault());
    
    public static final DateTimeFormatter ISO_TIME_FIXED_LENGTH_FOR_FILES = DateTimeFormatter.ofPattern("HH.mm.ss.SSS");
    public static final DateTimeFormatter ISO_LOCAL_DATE_TIME_FIXED_LENGTH_FOR_FILES = new DateTimeFormatterBuilder().parseCaseInsensitive().append(DateTimeFormatter.ISO_LOCAL_DATE).appendLiteral('T').append(ISO_TIME_FIXED_LENGTH_FOR_FILES).toFormatter(Locale.getDefault());
    public static final DateTimeFormatter ISO_OFFSET_DATE_TIME_FIXED_LENGTH_FOR_FILES = new DateTimeFormatterBuilder().parseCaseInsensitive().append(ISO_LOCAL_DATE_TIME_FIXED_LENGTH_FOR_FILES).appendOffset("+HHMMss", "+0000").toFormatter(Locale.getDefault());
    public static final DateTimeFormatter ISO_OFFSET_REGION_DATE_TIME_FIXED_LENGTH_FOR_FILES = new DateTimeFormatterBuilder().parseCaseInsensitive().append(ISO_OFFSET_DATE_TIME_FIXED_LENGTH_FOR_FILES).appendLiteral('[').appendZoneRegionId().appendLiteral(']').toFormatter(Locale.getDefault());
    
    public static long getTimeFloored(long timestamp, long every, TimeUnit unit) {
        return getTimeFloored(Instant.ofEpochMilli(timestamp), every, unit).toEpochMilli();
    }
    
    public static Instant getTimeFloored(Instant instant, long every, TimeUnit unit) {
        if (instant == null) {
            return null;
        }
        if (unit == null || every < 0) {
            return instant;
        }
        switch (unit) {
            case NANOSECONDS:
                return instant.minusNanos(instant.getNano() % every);
            case MICROSECONDS:
            case MILLISECONDS:
            case SECONDS:
            case MINUTES:
            case HOURS:
            case DAYS:
                return instant.minusMillis(instant.toEpochMilli() % unit.toMillis(every));
        }
        return null;
    }
    
    public static long getTimeCeiled(long timestamp, long every, TimeUnit unit) {
        return getTimeCeiled(Instant.ofEpochMilli(timestamp), every, unit).toEpochMilli();
    }
    
    public static Instant getTimeCeiled(Instant instant, long every, TimeUnit unit) {
        if (instant == null) {
            return null;
        }
        if (unit == null || every < 0) {
            return instant;
        }
        switch (unit) {
            case NANOSECONDS:
                return instant.minusNanos(instant.getNano() % every).plusNanos(unit.toNanos(every));
            case MICROSECONDS:
            case MILLISECONDS:
            case SECONDS:
            case MINUTES:
            case HOURS:
            case DAYS:
                return instant.minusMillis(instant.toEpochMilli() % unit.toMillis(every)).plusMillis(unit.toMillis(every));
        }
        return null;
    }
    
}
