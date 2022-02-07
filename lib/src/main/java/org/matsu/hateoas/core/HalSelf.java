package org.matsu.hateoas.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface HalSelf {
    public String value();
}
