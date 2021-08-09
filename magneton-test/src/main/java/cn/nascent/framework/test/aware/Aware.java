package cn.nascent.framework.test.aware;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since 2.0.0
 */
public interface Aware<T> {

	void setInterest(T newInstance);

}
