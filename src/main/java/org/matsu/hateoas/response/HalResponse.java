package org.matsu.hateoas.response;

import java.util.List;

import org.matsu.hateoas.links.HalLink;

public interface HalResponse {
    public static List<HalLink> generateLinks();
}
