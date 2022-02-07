package org.matsu.hateoas.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.stream.Stream;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import org.matsu.hateoas.http.HttpMethods;

public class HalLink {
  private String href;
  private String rel;
  private String type;

  public static HalLink from(String href, String rel, String type) {
    HalLink link = new HalLink();
    link.href = href;
    link.rel = rel;
    link.type = type;
    return link;
  }

  public static HalLink from(Method method, String basePath, Object id) {
    String rel = getLinkRelation(method);
    String href = getHrefFromPathAnnotation(method, basePath, id);
    String type = getTypeFromMethodAnnotation(method);

    return HalLink.from(href, rel, type);
  }

  private static String getLinkRelation(Method method) {
      boolean hasRelationAnnotation = method.isAnnotationPresent(HalRelation.class);
      if (!hasRelationAnnotation) { 
          return method.getName();
      }
      return method.<HalRelation>getAnnotation(HalRelation.class).value();
  }

  private static String getHrefFromPathAnnotation(Method method, String basePath, Object id) {
    String methodPath =
        Optional.ofNullable(method.<Path>getAnnotation(Path.class))
            .map(ann -> ann.value())
            .orElse("");
    return basePath + getSlashIfNeeded(basePath, methodPath) + ReflectionUtil.addIdToPath(methodPath, id);
  }

  private static String getSlashIfNeeded(String basePath, String methodPath) {
      if (basePath.endsWith("/")) return "";
      if (methodPath.isBlank()) return "";
      if (methodPath.startsWith("/")) return "";
      return "/";
  }

  private static String getTypeFromMethodAnnotation(Method method) {
    Annotation[] annotations = method.getAnnotations();
    return Stream.of(annotations)
        .filter(HttpMethods::annotationIsHttpMethodAnnotation)
        .findFirst()
        .map(ann -> ann.annotationType().getAnnotation(HttpMethod.class).value())
        .orElse("");
  }

  public String getHref() { return href; }
  public void setHref(String href) { this.href = href; }
  public String getRel() { return rel; }
  public void setRel(String rel) { this.rel = rel; }
  public String getType() { return type; }
  public void setType(String type) { this.type = type; }

  public String toString() {
    return "Href = " + href + ", Rel = " + rel + ", Type = " + type;
  }
}
