package com.example.launcher.rocket4j.util;

import java.util.ArrayList;
import java.util.List;

public class ThreadUtil {
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    public static List<String> getThreadStackTrace(Thread thread) {
        ArrayList<String> stackList = new ArrayList<>();
        try {
            if (thread == null) {
                return stackList;
            }
            StackTraceElement[] stackTraceElements = thread.getStackTrace();
            if (stackTraceElements.length == 0) {
                return stackList;
            }
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                stackList.add(String.valueOf(stackTraceElement));
            }
            return stackList;
        } catch (Throwable ignored) {
            return stackList;
        }
    }

}