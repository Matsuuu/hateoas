package org.matsu.hateoas.response;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface HalResponse {
    Class<?> value();
}
