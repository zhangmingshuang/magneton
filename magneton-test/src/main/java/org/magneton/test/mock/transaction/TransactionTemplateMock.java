package org.magneton.test.mock.transaction;

import com.alibaba.testable.core.annotation.MockInvoke;

import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 统一的事务Mock.
 *
 * @author zhangmsh 2021/8/30
 * @since 2.0.0
 */
public class TransactionTemplateMock {

	@MockInvoke(targetClass = TransactionTemplate.class)
	public <T> T execute(TransactionCallback<T> action) {
		return action.doInTransaction(new SimpleTransactionStatus());
	}

}
