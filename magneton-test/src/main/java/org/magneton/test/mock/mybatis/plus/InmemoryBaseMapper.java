package org.magneton.test.mock.mybatis.plus;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * .
 *
 * @author zhangmsh 2022/5/16
 * @since 2.0.8
 */
public class InmemoryBaseMapper implements BaseMapper<Object> {

	private final Map<Serializable, ObjectNode> datas = new ConcurrentSkipListMap<>();

	private final AtomicInteger idGenerator = new AtomicInteger();

	@Override
	public int insert(Object entity) {
		return 0;
	}

	@Override
	public int deleteById(Serializable id) {
		return 0;
	}

	@Override
	public int deleteByMap(Map<String, Object> columnMap) {
		return 0;
	}

	@Override
	public int delete(Wrapper<Object> queryWrapper) {
		return 0;
	}

	@Override
	public int deleteBatchIds(Collection<?> idList) {
		return 0;
	}

	@Override
	public int deleteById(Object entity) {
		return 0;
	}

	@Override
	public int updateById(Object entity) {
		return 0;
	}

	@Override
	public int update(Object entity, Wrapper<Object> updateWrapper) {
		return 0;
	}

	@Override
	public Object selectById(Serializable id) {
		return null;
	}

	@Override
	public List<Object> selectBatchIds(Collection<? extends Serializable> idList) {
		return null;
	}

	@Override
	public List<Object> selectByMap(Map<String, Object> columnMap) {
		return null;
	}

	@Override
	public Object selectOne(Wrapper<Object> queryWrapper) {
		return null;
	}

	@Override
	public Long selectCount(Wrapper<Object> queryWrapper) {
		return null;
	}

	@Override
	public List<Object> selectList(Wrapper<Object> queryWrapper) {
		return null;
	}

	@Override
	public List<Map<String, Object>> selectMaps(Wrapper<Object> queryWrapper) {
		return null;
	}

	@Override
	public List<Object> selectObjs(Wrapper<Object> queryWrapper) {
		return null;
	}

	@Override
	public <E extends IPage<Object>> E selectPage(E page, Wrapper<Object> queryWrapper) {
		return null;
	}

	@Override
	public <E extends IPage<Map<String, Object>>> E selectMapsPage(E page, Wrapper<Object> queryWrapper) {
		return null;
	}

}
