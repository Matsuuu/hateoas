package org.matsu.demo.filters;

import javax.ws.rs.ext.Provider;

import org.matsu.hateoas.interceptors.HalResponseFilter;

@Provider
public class HateoasFilter extends HalResponseFilter {
}
