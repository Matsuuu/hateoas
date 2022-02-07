package org.matsu.hateoas.filters;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.ws.rs.HttpMethod;
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

    String currentPath = getCurrentPath(requestContext);
    Object entity = responseContext.getEntity();
    Class<?> clazz = ReflectionUtil.getClassFromEntity(entity);
    HalResponse annotation = ReflectionUtil.getHalResponseAnnotation(clazz);
    Class<?> controller = annotation.value();
    String basePath = ReflectionUtil.getBasePathFromController(controller);
    Method[] methods = controller.getMethods();

    if (entity instanceof List<?> entityList) {
      entityList.forEach(ent -> applyLinksToEntity(methods, basePath, clazz, ent, currentPath));
    } else {
      applyLinksToEntity(methods, basePath, clazz, entity, currentPath);
    }
  }

  private String getCurrentPath(ContainerRequestContext requestContext) {
    String baseUri = requestContext.getUriInfo().getBaseUri().getPath();
    String currentPath = requestContext.getUriInfo().getPath().replace(baseUri, "");
    if (currentPath.substring(0,1) != "/") {
        currentPath = "/" + currentPath;
    }
    return currentPath;
  }

  private void applyLinksToEntity(Method[] methods, String basePath,
                                  Class<?> clazz, Object entity, String currentPath) {

    Object id = ReflectionUtil.getEntityId(entity);
    List<HalLink> links = getLinks(methods, basePath, id);
    Map<String, HalLink> entityLinks = ReflectionUtil.getLinksMap(entity);
    Map<String, HalLink> entityLinksAsMap = ReflectionUtil.linksListToMap(links);
    entityLinksAsMap = determineSelf(entityLinksAsMap, currentPath, clazz, entity);
    entityLinks.putAll(entityLinksAsMap);
  }

  private Map<String, HalLink> determineSelf(Map<String, HalLink> entityLinksAsMap, String currentPath, Class<?> clazz, Object entity) {
      Map<String, HalLink> remappedMap = new HashMap<>(entityLinksAsMap);
      String selfPath = ReflectionUtil.getSelfPath(clazz, entity);
      if (selfPath == null) return remappedMap; 

      for (Map.Entry<String, HalLink> linkEntry : entityLinksAsMap.entrySet()) {
          HalLink link = linkEntry.getValue();
          if (!link.getType().equals(HttpMethod.GET)) continue; // Self links usually are just GET's. Prove me wrong.

          if (selfPath.equals(link.getHref())) {
            remappedMap.put("_self", linkEntry.getValue());
            break;
          }
      }
      HalLink mappedSelf = remappedMap.get("_self");
      if (mappedSelf != null) {
          remappedMap.remove(mappedSelf.getRel());
      }
      return remappedMap;
  }
  
  private List<HalLink> getLinks(Method[] methods, String basePath, Object id) {
    return Stream.of(methods)
        .filter(ReflectionUtil::methodIsAnEndpoint)
        .map(method -> HalLink.from(method, basePath, id))
        .toList();
  }
}
