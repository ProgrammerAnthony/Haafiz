package com.anthony.app.dagger.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Identifies for activity context
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityContext {

}
