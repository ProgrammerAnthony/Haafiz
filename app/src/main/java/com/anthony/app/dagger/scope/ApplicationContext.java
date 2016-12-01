package com.anthony.app.dagger.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Identifies for application context
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationContext {

}
