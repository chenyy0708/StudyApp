package com.example.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by chenyy on 2021/6/22.
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface LikeLoader {
}
