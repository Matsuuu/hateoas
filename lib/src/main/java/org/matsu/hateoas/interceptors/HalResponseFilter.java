package org.matsu.hateoas.interceptors;

import static org.matsu.hateoas.core.ReflectionUtil.getClassFromEntity;
import static org.matsu.hateoas.core.ReflectionUtil.getHalResponseAnnotation;
import static org.matsu.hateoas.core.ReflectionUtil.getLinksList;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.matsu.hateoas.core.HalLink;
import org.matsu.hateoas.core.HalResponse;
import org.matsu.hateoas.core.ReflectionUtil;

@Provider
public class HalResponseFilter implements ContainerResponseFilter {

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

    if (entity instanceof List<?> entityList) {
      entityList.forEach(ent -> applyLinksToEntity(methods, basePath, ent));
    } else {
      applyLinksToEntity(methods, basePath, entity);
    }
  }

  private String getBasePathFromController(Class<?> controller) {
    Path pathAnnotation = controller.getAnnotation(Path.class);
    return pathAnnotation != null ? pathAnnotation.value() : "";
  }

  private void applyLinksToEntity(Method[] methods, String basePath,
                                  Object entity) {

    Object id = ReflectionUtil.getEntityId(entity);
    List<HalLink> links = getLinks(methods, basePath, id);
    List<HalLink> entityLinks = getLinksList(entity);
    entityLinks.addAll(links);
  }

  private List<HalLink> getLinks(Method[] methods, String basePath, Object id) {
    return Stream.of(methods)
        .filter(ReflectionUtil::methodIsAnEndpoint)
        .map(method -> HalLink.from(method, basePath, id))
        .toList();
  }

  private boolean
  hasHalResponseAnnotation(ContainerResponseContext responseContext) {
    Class<?> classFromEntity = getClassFromEntity(responseContext.getEntity());
    if (classFromEntity == null)
      return false;
    return classFromEntity.isAnnotationPresent(HalResponse.class);
  }
}
