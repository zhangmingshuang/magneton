package org.magneton.spring.cache;

import org.magneton.spring.properties.MagnetonProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 提供缓存的能力，内部动态切换缓存实现.
 *
 * @author zhangmsh
 * @since 2024
 */
@Component
public class StargateMCache implements MCache {

	@Resource
	private MagnetonProperties magnetonProperties;

}
