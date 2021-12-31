package org.magneton.test.test.model.generate;

import java.util.concurrent.ThreadLocalRandom;

import org.magneton.test.test.model.generate.base.AbstractGenericGenerator;
import org.magneton.test.test.model.generate.util.ChineseCharUtils;

public class ChineseAddressGeneratorAbstract extends AbstractGenericGenerator {

	private static AbstractGenericGenerator instance = new ChineseAddressGeneratorAbstract();

	private ChineseAddressGeneratorAbstract() {
	}

	public static AbstractGenericGenerator getInstance() {
		return instance;
	}

	private static String genProvinceAndCity() {
		return ChineseAreaList.provinceCityList
				.get(ThreadLocalRandom.current().nextInt(0, ChineseAreaList.provinceCityList.size()));
	}

	@Override
	public String generate() {
		StringBuilder result = new StringBuilder(genProvinceAndCity());
		result.append(ChineseCharUtils.genRandomLengthChineseChars(2, 3) + "路");
		result.append(ThreadLocalRandom.current().nextInt(1, 8000) + "号");
		result.append(ChineseCharUtils.genRandomLengthChineseChars(2, 3) + "小区");
		result.append(ThreadLocalRandom.current().nextInt(1, 20) + "单元");
		result.append(ThreadLocalRandom.current().nextInt(101, 2500) + "室");
		return result.toString();
	}

}
