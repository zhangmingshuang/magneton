package org.magneton.adaptive.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis自动装配器
 *
 * @author zhangmsh 2022/9/16
 * @since
 */
@Configuration(proxyBeanMethods = false)
public class AptMybatisAutoConfiguration {

	@Value("${db.type:mysql}")
	private String dbType;

	/**
	 * Mybatis Plus 分布插件
	 * @return the bean
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.getDbType(dbType));
		interceptor.addInnerInterceptor(paginationInnerInterceptor);
		return interceptor;
	}

}
