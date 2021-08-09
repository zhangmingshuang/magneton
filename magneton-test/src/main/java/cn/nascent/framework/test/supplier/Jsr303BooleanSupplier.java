package cn.nascent.framework.test.supplier;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 标准的{@code jsr303}标准注解进行校验.
 *
 * @author zhangmsh 2021/8/4
 * @since 2.0.0
 */
@Slf4j
public class Jsr303BooleanSupplier extends AbstractJsr303Supplier<Jsr303BooleanSupplier> {

	public Jsr303BooleanSupplier(Object obj) {
		super(obj);
	}

	@SneakyThrows
	@Override
	protected boolean doBooleanJudge() {
		if (this.getObj() == null) {
			return true;
		}
		// 实例化一个 validator工厂
		try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
			// 获取validator实例
			Validator validator = validatorFactory.getValidator();
			// 调用调用，得到校验结果信息 Set
			Set<ConstraintViolation<Object>> constraintViolations = validator.validate(super.getObj());
			super.addError(constraintViolations);
			return constraintViolations.isEmpty();
		}
		// Class clazz = this.obj.getClass();
		// Field[] fields = clazz.getDeclaredFields();
		// boolean response = true;
		// for (Field field : fields) {
		// field.setAccessible(true);
		// Object value = field.get(this.obj);
		// List<Annotation> annotations = AnnotationUtils.findAnnotations(field);
		// for (Annotation annotation : annotations) {
		// Judger judger =
		// Objects.requireNonNull(ChaosContext.get(JudegerFactory.class))
		// .getJudeger(annotation.annotationType());
		// if (judger == null) {
		// continue;
		// }
		// Affirmed affirmed = judger.isAffirmed(annotation, value);
		// if (!affirmed.isAffirmed()) {
		// log.error(
		// "{}#{}的@{}不是期望的:{}",
		// clazz.getName(),
		// field.getName(),
		// annotation.annotationType().getName(),
		// affirmed.getMsg());
		// }
		// response &= affirmed.isAffirmed();
		// }
		// }
		// return response;
	}

}
