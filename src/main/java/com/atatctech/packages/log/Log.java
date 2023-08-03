package com.atatctech.packages.log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Log {
    public static class Time {
        public final static Time FUTURE = new Time(SpecialCase.FUTURE);
        public final static Time PAST = new Time(SpecialCase.PAST);
        protected final long baseTime;

        public Time() {
            baseTime = System.currentTimeMillis();
        }

        public Time(long baseTime) {
            if (baseTime < 0) throw new IllegalArgumentException("`baseTime` must be a positive integer.");
            this.baseTime = baseTime;
        }

        public Time(long baseTime, @NotNull Unit unit) {
            if (baseTime < 0) throw new IllegalArgumentException("`baseTime` must be a positive integer.");
            this.baseTime = convert(unit, Unit.Millisecond, baseTime);
        }

        public Time(@NotNull SpecialCase specialCase) {
            baseTime = (long) switch (specialCase) {
                case FUTURE -> Float.POSITIVE_INFINITY;
                case PAST -> Float.NEGATIVE_INFINITY;
            };
        }

        public static @NotNull Time parseString(@NotNull String baseTime, @NotNull String format) {
            SimpleDateFormat form = new SimpleDateFormat(format);
            try {
                return new Time(form.parse(baseTime).getTime());
            } catch (ParseException e) {
                return new Time();
            }
        }

        public static @NotNull Time parseString(@NotNull String baseTime) {
            return parseString(baseTime, "yyyy-MM-dd HH:mm:ss");
        }

        protected static int getUnitCoefficients(@NotNull Unit unit) {
            return switch (unit) {
                case Millisecond -> 1;
                case Second -> 1000;
                case Minute -> 60000;
                case Hour -> 3600000;
                case Day -> 86400000;
                case Week -> 604800000;
            };
        }

        public static long convert(@NotNull Unit origin, @NotNull Unit result, long value) {
            return value * getUnitCoefficients(origin) / getUnitCoefficients(result);
        }

        public static long calculateDuration(@NotNull Runnable action, @NotNull Unit unit) {
            long baseTime = System.currentTimeMillis();
            action.run();
            long endTime = System.currentTimeMillis();
            return (endTime - baseTime) / getUnitCoefficients(unit);
        }

        public static long calculateDuration(@NotNull Time time1, @NotNull Time time2, @NotNull Unit unit) {
            return convert(Unit.Millisecond, unit, Math.abs(time1.getBaseTime() - time2.getBaseTime()));
        }

        public static boolean theSame(@NotNull Time time1, @NotNull Time time2, @NotNull Unit unit) {
            return convert(Unit.Millisecond, unit, time1.getBaseTime()) == convert(Unit.Millisecond, unit, time2.getBaseTime());
        }

        public boolean isAfter(@NotNull Time other) {
            return getBaseTime() > other.getBaseTime();
        }

        public boolean isFuture() {
            return isAfter(new Time());
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (!(o instanceof Time time)) return false;
            return getBaseTime() == time.getBaseTime();
        }

        @Override
        public int hashCode() {
            return (int) (getBaseTime() ^ (getBaseTime() >>> 32));
        }

        public boolean isCurrent() {
            return equals(new Time());
        }

        public boolean isBefore(@NotNull Time other) {
            return getBaseTime() < other.getBaseTime();
        }

        public boolean isPast() {
            return isBefore(new Time());
        }

        public boolean isBetween(@NotNull Time time1, @NotNull Time time2) {
            return isAfter(time1) && isBefore(time2);
        }

        public @NotNull Time forward(@NotNull TimePeriod gap) {
            return new Time(getBaseTime() + gap.getMilliseconds());
        }

        public @NotNull Time backward(@NotNull TimePeriod gap) {
            return new Time(getBaseTime() - gap.getMilliseconds());
        }

        public @NotNull String getStamp() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return formatter.format(new Date(getBaseTime()));
        }

        public @NotNull String getStamp(@NotNull String format) {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(new Date(getBaseTime()));
        }

        public long getDuration(@NotNull Unit unit) {
            long duration = Math.abs(System.currentTimeMillis() - getBaseTime());
            return duration / getUnitCoefficients(unit);
        }

        public long getDuration() {
            return Math.abs(System.currentTimeMillis() - getBaseTime());
        }

        public @NotNull Milliseconds getDurationAsGap() {
            return new Milliseconds(getDuration());
        }

        public long getDuration(@NotNull Time time, @NotNull Unit unit) {
            long duration = Math.abs(time.getBaseTime() - getBaseTime());
            return duration / getUnitCoefficients(unit);
        }

        public @NotNull TimePeriod getDurationAsGap(@NotNull Time time, @NotNull Unit unit) {
            return TimePeriod.fromUnit(getDuration(time, unit), unit);
        }

        public long getBaseTime(@NotNull Unit unit) {
            return baseTime / getUnitCoefficients(unit);
        }

        public long getBaseTime() {
            return getBaseTime(Unit.Millisecond);
        }

        public enum Unit {
            Week, Day, Hour, Minute, Second, Millisecond
        }

        public enum SpecialCase {
            FUTURE, PAST
        }

        public static class TimePeriod {
            public static @NotNull TimePeriod fromUnit(long n, @NotNull Unit unit) {
                return switch (unit) {
                    case Week -> new Weeks(n);
                    case Day -> new Days(n);
                    case Hour -> new Hours(n);
                    case Minute -> new Minutes(n);
                    case Second -> new Seconds(n);
                    case Millisecond -> new Milliseconds(n);
                };
            }

            protected final long milliseconds;

            public TimePeriod(long milliseconds) {
                this.milliseconds = milliseconds;
            }

            public long getMilliseconds() {
                return milliseconds;
            }
        }

        public static class Milliseconds extends TimePeriod {
            public Milliseconds(long milliseconds) {
                super(milliseconds);
            }
        }

        public static class Seconds extends Milliseconds {
            public Seconds(long seconds) {
                super(seconds * 1000);
            }
        }

        public static class Minutes extends Seconds {
            public Minutes(long minutes) {
                super(minutes * 60);
            }
        }

        public static class Hours extends Minutes {
            public Hours(long hours) {
                super(hours * 60);
            }
        }

        public static class Days extends Hours {
            public Days(long days) {
                super(days * 24);
            }
        }

        public static class Weeks extends Days {
            public Weeks(long weeks) {
                super(weeks * 7);
            }
        }
    }
}
