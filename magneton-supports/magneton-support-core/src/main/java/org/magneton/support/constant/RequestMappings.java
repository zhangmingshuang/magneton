package org.magneton.support.constant;

import java.util.Set;
import org.magneton.core.collect.Sets;

/**
 * @author zhangmsh 2022/3/19
 * @since 1.0.0
 */
public interface RequestMappings {

	Set<String> SUPPORT_IGNORE_MAPPERS = Sets.newHashSet("/api-doc.html");

}
