package org.matsu.hateoas.filters;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
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

    if (!ReflectionUtil.hasHalResponseAnnotation(responseContext)) {
      return;
    }

    Object entity = responseContext.getEntity();
    Class<?> clazz = ReflectionUtil.getClassFromEntity(entity);
    HalResponse annotation = ReflectionUtil.getHalResponseAnnotation(clazz);
    Class<?> controller = annotation.value();
    String basePath = ReflectionUtil.getBasePathFromController(controller);
    Method[] methods = controller.getMethods();

    if (entity instanceof List<?> entityList) {
      entityList.forEach(ent -> applyLinksToEntity(methods, basePath, ent));
    } else {
      applyLinksToEntity(methods, basePath, entity);
    }
  }

  private void applyLinksToEntity(Method[] methods, String basePath,
                                  Object entity) {

    Object id = ReflectionUtil.getEntityId(entity);
    List<HalLink> links = getLinks(methods, basePath, id);
    Map<String, HalLink> entityLinks = ReflectionUtil.getLinksMap(entity);
    Map<String, HalLink> entityLinksAsMap = ReflectionUtil.linksListToMap(links);
    entityLinks.putAll(entityLinksAsMap);
  }

  private List<HalLink> getLinks(Method[] methods, String basePath, Object id) {
    return Stream.of(methods)
        .filter(ReflectionUtil::methodIsAnEndpoint)
        .map(method -> HalLink.from(method, basePath, id))
        .toList();
  }
}
