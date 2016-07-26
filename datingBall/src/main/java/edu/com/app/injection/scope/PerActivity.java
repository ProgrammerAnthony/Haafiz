package edu.com.app.injection.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * different signal with {@link javax.inject.Singleton}
 * Singleton Identifies a type that the injector only instantiates once. Not inherited.
 */
@Documented
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {
}
