package ctrip.base.launcher.rocket4j.util;

public class Log4Rocket {
    public interface Logger {
        public static final String TAG = "rocket4j";

        public void log(String message);
    }

    private Logger mLogger;
    private String mPrefix;

    public Log4Rocket(String prefix, Logger logger) {
        mLogger = logger;
        mPrefix = prefix;
    }

    public void d(String message) {
        if (mLogger != null) {
            mLogger.log(mPrefix.concat(message));
        }
    }

    public void d(String format, Object o) {
        if (mLogger != null) {
            mLogger.log(mPrefix.concat(String.format(format, object2String(o))));
        }
    }

    public void d(String format, Object o1, Object o2) {
        if (mLogger != null) {
            mLogger.log(mPrefix.concat(String.format(format, object2String(o1), object2String(o2))));
        }
    }

    public void d(String format, Object o1, Object o2, Object o3) {
        if (mLogger != null) {
            mLogger.log(mPrefix.concat(String.format(format, object2String(o1), object2String(o2), object2String(o3))));
        }
    }

    public void d(String format, Object o1, Object o2, Object o3, Object o4) {
        if (mLogger != null) {
            mLogger.log(mPrefix.concat(String.format(format, object2String(o1), object2String(o2), object2String(o3), object2String(o4))));
        }
    }

    /**
     * 对象转字符串
     *
     * @param o
     * @return
     */
    private static String object2String(Object o) {
        return String.valueOf(o);
    }
}
