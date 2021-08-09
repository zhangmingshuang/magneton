package cn.nascent.framework.test.jsr303.processor;

import cn.nascent.framework.test.annotation.TestComponent;
import cn.nascent.framework.test.annotation.TestSort;
import cn.nascent.framework.test.core.Config;
import cn.nascent.framework.test.injector.Inject;
import cn.nascent.framework.test.injector.InjectType;
import cn.nascent.framework.test.injector.processor.statement.DataStatement;
import cn.nascent.framework.test.util.AnnotationUtils;
import cn.nascent.framework.test.util.NumberUtil;
import com.google.common.base.Strings;
import java.lang.annotation.Annotation;
import java.util.Map;
import javax.annotation.Nullable;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Max;

/**
 * 验证注解的元素值小于等于 {@code @DecimalMax} 指定的 value 值.
 *
 * <p>
 * 验证注解的元素值小于等于 {@code @Max} 指定的 value 值
 *
 * @author zhangmsh 2021/8/3
 * @since 2.0.0
 */
@TestComponent
@TestSort
public class Jsr303MaxProcessor extends AbstractJsr303NumberProcessor {

	public static final String METADATA_KEY = "jsr303__Max_temp";

	@Override
	public boolean processable(Class annotationType) {
		return Max.class == annotationType || DecimalMax.class == annotationType;
	}

	@Nullable
	@Override
	public void process(Config config, InjectType injectType, Inject inject, Annotation annotation,
			DataStatement dataStatement) {
		Map<String, Object> metadata = AnnotationUtils.getMetadata(annotation);
		if (annotation.annotationType() == Max.class) {
			this.doMaxProcess(config, injectType, inject, metadata, dataStatement);
		}
		else {
			this.doMaxDecimalProcess(config, injectType, inject, metadata, dataStatement);
		}
	}

	private void doMaxDecimalProcess(Config config, InjectType injectType, Inject inject, Map<String, Object> metadata,
			DataStatement dataStatement) {
		String value = (String) metadata.get("value");
		boolean inclusive = (boolean) metadata.get("inclusive");
		if (Strings.isNullOrEmpty(value)) {
			value = "0";
		}
		Number number = NumberUtil.cast(inject.getInectType(), value);
		this.setValue(config, injectType, inject, dataStatement, number.doubleValue());
	}

	private void doMaxProcess(Config config, InjectType injectType, Inject inject, Map<String, Object> metadata,
			DataStatement dataStatement) {
		long max = (long) metadata.get("value");
		this.setValue(config, injectType, inject, dataStatement, max);
	}

	private void setValue(Config config, InjectType injectType, Inject inject, DataStatement dataStatement,
			double max) {
		if (injectType.isDemon()) {
			dataStatement.breakNext(max + 1);
		}
		else {
			dataStatement.setValue(max - 1);
		}
	}

}
