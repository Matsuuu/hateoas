package org.matsu.hateoas.http;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;

public class HttpMethods {

  public static final List<String> JAX_RS_HTTP_METHODS =
      List.of(GET.class.getName(), POST.class.getName(), PUT.class.getName(),
              DELETE.class.getName(), PATCH.class.getName(),
              HEAD.class.getName(), OPTIONS.class.getName());

  public static boolean annotationIsHttpMethodAnnotation(Annotation ann) {
    return JAX_RS_HTTP_METHODS.contains(ann.annotationType().getName());
  }
}
