package org.matsu.hateoas.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerResponseContext;

import org.matsu.hateoas.http.HttpMethods;

public class ReflectionUtil {

    private static final String NO_ID = "<NO_ID>";

  public static void getAnnotation() {}

  public static HalResponse getHalResponseAnnotation(Class<?> clazz) {
    return clazz.getAnnotation(HalResponse.class);
  }

  public static String getBasePathFromController(Class<?> controller) {
    Path pathAnnotation = controller.getAnnotation(Path.class);
    String path = pathAnnotation != null ? pathAnnotation.value() : "";
    return path.substring(0,1) == "/" ? path : "/" + path;
  }

  public static boolean hasHalResponseAnnotation(ContainerResponseContext responseContext) {
    Class<?> classFromEntity = getClassFromEntity(responseContext.getEntity());
    if (classFromEntity == null)
      return false;
    return classFromEntity.isAnnotationPresent(HalResponse.class);
  }

  public static String getSelfPath(Class<?> clazz, Object entity) {
    HalSelf selfAnno = clazz.getAnnotation(HalSelf.class);
    if (selfAnno == null) return "";
    String selfAnnotationPath = selfAnno.value();
    Object id = getEntityId(entity);
    return addIdToPath(selfAnnotationPath, id);
  }

  public static String addIdToPath(String path, Object id) {
  // TODO: Make this mapping not overwrite everything
    return path.replaceAll("\\{\\w+\\}", id.toString());
  }

  public static Map<String, HalLink> getLinksMap(Object entity) {
    Class<?> clazz = entity.getClass();
    try {
      return Stream.of(clazz.getDeclaredFields())
          .filter(f -> f.isAnnotationPresent(HalLinks.class))
          .map(f -> ReflectionUtil.<Map<String, HalLink>>getFieldFromClass(entity, clazz, f))
          .filter(Objects::nonNull)
          .findFirst()
          .orElseThrow();

    } catch (Exception ex) {
      ex.printStackTrace();
      System.err.println("Class marked as HalResponse, but no @HalLinks annotation is present on a field of class " + clazz.getName());
      System.err.println("Make sure that you have a field of type Map<String, HalLink> annotated with the @HalLinks annotation.");
      return new HashMap<>();
    }
  }

  public static Map<String, HalLink> linksListToMap(List<HalLink> linksList) { 
    return linksList.stream().collect(Collectors.toMap(HalLink::getRel, Function.identity()));
  }

  public static Object getEntityId(Object entity) {
    Class<?> clazz = entity.getClass();
    return Stream.of(clazz.getDeclaredFields())
        .filter(f -> f.isAnnotationPresent(HalId.class))
        .map(f -> getFieldFromClass(entity, clazz, f))
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(NO_ID);
  }

  public static boolean isEntityId(Object id) {
      return !id.equals(NO_ID);
  }

  @SuppressWarnings("unchecked")
  public static <T> T getFieldFromClass(Object entity, Class<?> clazz,
                                        Field field) {
    try {
      // Try to directly access the field
      return (T)field.get(entity);
    } catch (IllegalAccessException illegalAccessException) {
      try {
        // Try to get the value via a getter function
        Method getterMethod = clazz.getMethod("get" + capitalize(field.getName()));
        return (T)getterMethod.invoke(entity);
      } catch (Exception methodException) {
        try {
          // Try to access a record field getter method
          Method recordGetterMethod = clazz.getMethod(field.getName());
          return (T)recordGetterMethod.invoke(entity);
        } catch (Exception recordGetterException) {
          String fieldName = field.getName();
          System.err.println("Could not find a accessor for field " +
                             fieldName + "\nTried " + fieldName + ", get" +
                             capitalize(fieldName) + "(), and " + fieldName +
                             "().");
        }
      }
    }
    return null;
  }

  public static String capitalize(String word) {
    return word.substring(0, 1).toUpperCase() + word.substring(1);
  }

  public static Class<?> getClassFromEntity(Object entity) {
    if (entity instanceof List<?> list) {
      if (list.isEmpty())
        return null;
      return list.get(0).getClass();
    }
    return entity.getClass();
  }

  public static boolean methodIsAnEndpoint(Method method) {
    return Stream.of(method.getAnnotations())
        .filter(HttpMethods::annotationIsHttpMethodAnnotation)
        .findFirst()
        .isPresent();
  }

  @SuppressWarnings("unchecked")
  public static <T> T getValueFromEntity(Object entity, String valueName) {
    try {
      Class<?> clazz = entity.getClass();
      Method method = clazz.getMethod(valueName);
      return (T)method.invoke(entity);
    } catch (NoSuchMethodException | SecurityException |
             IllegalAccessException | IllegalArgumentException |
             InvocationTargetException e) {
      e.printStackTrace();
      System.err.println(
          "The annotated HalResponse response object must have a getter method called " +
          valueName + " or in the case of a record, a property called " +
          valueName + ".");
      return null;
    }
  }
}
