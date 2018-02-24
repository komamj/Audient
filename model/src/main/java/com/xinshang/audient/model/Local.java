package com.xinshang.audient.model;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by koma on 1/3/18.
 */
@Documented
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface Local {
}
