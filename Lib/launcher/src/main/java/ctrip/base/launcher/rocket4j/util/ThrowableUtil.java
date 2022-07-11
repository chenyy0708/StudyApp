package ctrip.base.launcher.rocket4j.util;

import java.util.ArrayList;
import java.util.List;

public class ThrowableUtil {
    public static List<String> getStack(Throwable e) {
        List<String> stackList = new ArrayList<>();
        if (e == null) {
            return stackList;
        } else {
            StackTraceElement[] stackTraceElements = e.getStackTrace();
            if (stackTraceElements != null && stackTraceElements.length != 0) {
                int var4 = stackTraceElements.length;

                for (StackTraceElement stackTraceElement : stackTraceElements) {
                    stackList.add(String.valueOf(stackTraceElement));
                }

                return stackList;
            } else {
                return stackList;
            }
        }
    }

    public static String getThrowableString(Throwable e) {
        return String.format("ErrorMessage: %s\nStackTrace: %s", String.valueOf(e), getStack(e));
    }
}