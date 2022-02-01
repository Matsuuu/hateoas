package org.matsu.hateoas.interceptors;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import org.matsu.hateoas.http.HttpMethods;
import org.matsu.hateoas.links.HalLink;
import org.matsu.hateoas.response.HalResponse;

@Provider
public class HalResponseIntercepter implements ContainerResponseFilter {

  @Override
  public void filter(ContainerRequestContext requestContext,
                     ContainerResponseContext responseContext)
      throws IOException {

    if (!hasHalResponseAnnotation(responseContext)) {
      return;
    }

    Object entity = responseContext.getEntity();
    Class<?> clazz = getClassFromEntity(entity);
    HalResponse annotation = getHalResponseAnnotation(clazz);
    Class<?> controller = annotation.value();
    String basePath = getBasePathFromController(controller);
    Method[] methods = controller.getMethods();
    //

    if (entity instanceof List entityList) {
      entityList.forEach(ent -> applyLinksToEntity(methods, basePath, ent));
    } else {
      applyLinksToEntity(methods, basePath, entity);
    }
  }

  private <T> T getValueFromEntity(Object entity, String valueName) {
    T value = null;
    try {
      Class<?> clazz = entity.getClass();
      Method method = clazz.getMethod(valueName);
      value = (T)method.invoke(entity);
    } catch (NoSuchMethodException | SecurityException |
             IllegalAccessException | IllegalArgumentException |
             InvocationTargetException e) {
      e.printStackTrace();
      System.err.println(
          "The annotated HalResponse response object must have a getter method called " +
          valueName + " or in the case of a record, a property called " +
          valueName + ".");
    }
    return value;
  }

  private String getBasePathFromController(Class<?> controller) {
    Path pathAnnotation = controller.getAnnotation(Path.class);
    return pathAnnotation != null ? pathAnnotation.value() : "";
  }

  private void applyLinksToEntity(Method[] methods, String basePath,
                                  Object entity) {

    Long id = getValueFromEntity(entity, "id");
    List<HalLink> links = getLinks(methods, basePath, id);
    List<HalLink> entityLinks = getValueFromEntity(entity, "links");
    entityLinks.addAll(links);
  }

  private List<HalLink> getLinks(Method[] methods, String basePath, long id) {
    return Stream.of(methods)
        .filter(this::methodIsAnEndpoint)
        .map(method -> HalLink.from(method, basePath, id))
        .toList();
  }

  private boolean methodIsAnEndpoint(Method method) {
    return Stream.of(method.getAnnotations())
        .filter(HttpMethods::annotationIsHttpMethodAnnotation)
        .findFirst()
        .isPresent();
  }

  private HalResponse getHalResponseAnnotation(Class<?> clazz) {
    return clazz.<HalResponse>getAnnotation(HalResponse.class);
  }

  private boolean entityIsList(Object entity) {
    return entity instanceof List<?>;
  }

  private Class<?> getClassFromEntity(Object entity) {
    if (entity instanceof List<?> list) {
      if (list.isEmpty())
        return null;
      return list.get(0).getClass();
    }
    return entity.getClass();
  }

  private boolean hasHalResponseAnnotation(ContainerResponseContext responseContext) {
    Class<?> classFromEntity = getClassFromEntity(responseContext.getEntity());
    if (classFromEntity == null)
      return false;
    return classFromEntity.isAnnotationPresent(HalResponse.class);
  }
}
