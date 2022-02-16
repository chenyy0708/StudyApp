package com.example.common.utils;

import com.example.common.BaseAppLike;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenyy on 2022/2/16.
 */

public class ModuleProvider {

    private static HashMap<String, BaseAppLike> moduleLikes = new HashMap<>();

    public static void register(Class clazz) {
        moduleLikes.put(clazz.getName(), loadModuleLike(clazz));
    }

    public static void init() {
        try {
            Class clazz = Class.forName("com.example.modulelike.generated.ModuleLoaderInit");
            ReflectUtils.invokeStaticMethod(clazz, "init");
            for (Map.Entry<String, BaseAppLike> entry : moduleLikes.entrySet()) {
                if (entry != null && entry.getValue() != null) entry.getValue().onCreate();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static BaseAppLike loadModuleLike(Class clazz) {
        try {
            BaseAppLike appLike = (BaseAppLike) clazz.newInstance();
            return appLike;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
