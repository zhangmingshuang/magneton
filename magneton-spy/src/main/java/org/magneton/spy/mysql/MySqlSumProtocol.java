package org.magneton.spy.mysql;

import com.google.auto.service.AutoService;
import org.magneton.spy.protocol.Protocol;
import org.magneton.spy.protocol.SumProtocol;

/**
 * @author zhangmsh 2021/7/28
 * @since 1.0.0
 */
@AutoService(Protocol.class)
public class MySqlSumProtocol implements SumProtocol {}
