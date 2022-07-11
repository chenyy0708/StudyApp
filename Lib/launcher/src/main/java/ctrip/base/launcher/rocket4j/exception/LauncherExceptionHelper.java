package ctrip.base.launcher.rocket4j.exception;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class LauncherExceptionHelper {

    /**
     * debug崩溃，release报告
     */
    @SuppressLint("LongLogTag")
    public static void boom(ExceptionData data) {
        if (data == null) {
            return;
        }
        Log.e("Ctrip Launcher Exception:" + data.getGroup() + "@" + data.getTag(), getExceptionSummary(data));
    }

    private static String getExceptionSummary(ExceptionData data) {
        StringBuilder stringBuilder = new StringBuilder();
        if (data.getExcetion() == null) {
            data.setException(new RuntimeException(data.getErrorMessage()));
        }
        String msg = String.valueOf(data.getExcetion());
        stringBuilder.append(msg).append("\n\n");
        StackTraceElement[] stackTraceElements = data.getExcetion().getStackTrace();
        for (StackTraceElement traceElement : stackTraceElements) {
            stringBuilder.append(traceElement).append("\n");
        }
        return stringBuilder.toString();
    }

    private static String getStackTrace(StackTraceElement[] stackTraceElements) {
        List<String> stackList = new ArrayList<>();
        for (StackTraceElement traceElement : stackTraceElements) {
            stackList.add(String.valueOf(traceElement));
        }
//        return JSON.toJSONString(stackList);
        return "";
    }

}
