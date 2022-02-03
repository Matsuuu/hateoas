package org.matsu.hateoas.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.matsu.hateoas.http.HttpMethods;
import org.matsu.hateoas.links.HalLink;
import org.matsu.hateoas.links.HalLinks;

public class ReflectionUtil {

  public static void getAnnotation() {}

  public static HalResponse getHalResponseAnnotation(Class<?> clazz) {
    return clazz.<HalResponse>getAnnotation(HalResponse.class);
  }

  public static List<HalLink> getLinksList(Object entity) {
    Class<?> clazz = entity.getClass();
    for (Field f : clazz.getDeclaredFields()) {
      System.out.println("Field " + f.getName());
    }
    try {
      return Stream.of(clazz.getDeclaredFields())
          .filter(f -> f.isAnnotationPresent(HalLinks.class))
          .map(f
               -> ReflectionUtil.<List<HalLink>>getFieldFromClass(entity, clazz,
                                                                  f))
          .filter(Objects::nonNull)
          .findFirst()
          .orElseThrow();

    } catch (Exception ex) {
      System.out.println("Error in class " + clazz);
      ex.printStackTrace();
      System.err.println(
          "Class marked as HalResponse, but no @HalLinks annotation is present on a field of class " +
          clazz.getName());
      return new ArrayList<>();
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T getFieldFromClass(Object entity, Class<?> clazz,
                                        Field field) {
    System.out.println("Checking field " + field.getName());
    try {
      // Try to directly access the field
      return (T)field.get(entity);
    } catch (IllegalAccessException illegalAccessException) {
      try {
        // Try to get the value via a getter function
        Method getterMethod =
            clazz.getMethod("get" + capitalize(field.getName()));
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
