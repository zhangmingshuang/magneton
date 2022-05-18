package org.magneton.module.pay.core;

import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.module.pay.core.Amounts.Fen;
import org.magneton.module.pay.core.Amounts.Jiao;
import org.magneton.module.pay.core.Amounts.Yuan;

/**
 * @author zhangmsh 2022/4/7
 * @since 1.0.0
 */
class AmountsTest {

	@Test
	void fenYuan() {
		int v = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
		Yuan yuan = Amounts.fromFen(v).toYuan(3);
		System.out.println(yuan);
		Fen fen = yuan.toFen();
		Assertions.assertEquals(v, fen.getValue());
	}

	@Test
	void fenJiao() {
		int v = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
		Jiao jiao = Amounts.fromFen(v).toJiao();
		System.out.println(jiao);
		Fen fen = jiao.toFen();
		Assertions.assertEquals(v, fen.getValue());
	}

	@Test
	void jiaoYuan() {
		int v = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
		Yuan yuan = Amounts.fromJiao(v).toYuan();
		System.out.println(yuan);
		Jiao jiao = yuan.toJiao();
		Assertions.assertEquals(v, jiao.getValue().intValue());
	}

}