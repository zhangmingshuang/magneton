package org.magneton.support.basic.service.impl;

import java.util.Optional;

import javax.annotation.Nullable;
import javax.annotations.VisibleForTesting;

import org.magneton.core.base.Strings;
import org.magneton.support.basic.dao.intf.BasicSysConfigDao;
import org.magneton.support.basic.entity.BasicSysConfigDO;
import org.magneton.support.basic.service.BasicSysConfigService;
import org.magneton.support.basic.wrapper.BasicSysConfigQuery;
import org.magneton.support.constant.CacheConstant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * .
 *
 * @author zhangmsh 2022/2/16
 * @since 1.2.0
 */
@Service
public class BasicSysConfigServiceImpl implements BasicSysConfigService {

	@Autowired
	private BasicSysConfigDao basicSysConfigDao;

	@Nullable
	@Override
	@Cacheable(cacheNames = CacheConstant.BASIC_SYS_CONFIG, key = "#key", condition = "#key != null")
	public String getValue(String key) {
		return this.getValueFromDb(key);
	}

	@VisibleForTesting
	@Nullable
	public String getValueFromDb(String key) {
		if (Strings.isNullOrEmpty(key)) {
			return null;
		}
		BasicSysConfigQuery query = new BasicSysConfigQuery().select.configValue().end().where.configKey().eq(key)
				.end();
		Optional<BasicSysConfigDO> optional = this.basicSysConfigDao.mapper().findOne(BasicSysConfigDO.class, query);
		return optional.map(BasicSysConfigDO::getConfigValue).orElse(null);
	}

}
