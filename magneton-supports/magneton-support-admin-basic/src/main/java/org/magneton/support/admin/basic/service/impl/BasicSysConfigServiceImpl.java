package org.magneton.support.admin.basic.service.impl;

import java.util.Optional;

import javax.annotation.Nullable;
import javax.annotations.VisibleForTesting;

import org.magneton.core.base.Strings;
import org.magneton.support.admin.basic.dao.intf.AdminSysConfigDao;
import org.magneton.support.admin.basic.entity.AdminSysConfigDO;
import org.magneton.support.admin.basic.service.BasicSysConfigService;
import org.magneton.support.admin.basic.wrapper.AdminSysConfigQuery;
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
@SuppressWarnings("unchecked")
@Service
public class BasicSysConfigServiceImpl implements BasicSysConfigService {

	@Autowired
	private AdminSysConfigDao adminSysConfigDao;

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
		AdminSysConfigQuery query = new AdminSysConfigQuery().select.configValue().end().where.configKey().eq(key)
				.end();
		Optional<AdminSysConfigDO> optional = this.adminSysConfigDao.mapper().findOne(AdminSysConfigDO.class, query);
		return optional.map(AdminSysConfigDO::getConfigValue).orElse(null);
	}

}
