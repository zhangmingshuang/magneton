// package cn.nascent.framework.test.jsr303.judger;
//
// import cn.nascent.framework.test.annotation.TestComponent;
// import cn.nascent.framework.test.judger.Affirmed;
// import cn.nascent.framework.test.judger.Judger;
// import cn.nascent.framework.test.util.AnnotationUtils;
// import cn.nascent.framework.test.util.NumberUtil;
// import java.lang.annotation.Annotation;
// import java.math.BigDecimal;
// import java.util.Map;
// import java.util.Optional;
// import javax.validation.constraints.DecimalMax;
// import javax.validation.constraints.Max;
//
/// **
// * .
// *
// * @author zhangmsh 2021/8/4
// * @since 2.0.0
// */
// @TestComponent
// public class Jsr303MaxJudger implements Judger {
//
// @Override
// public Class[] annotationTypes() {
// return new Class[] {Max.class, DecimalMax.class};
// }
//
// @Override
// public Affirmed isAffirmed(Annotation annotation, Object value) {
// if (value == null) {
// return Affirmed.fail("注解@Max但是值为null");
// }
// if (!NumberUtil.isNumberType(value.getClass())) {
// return Affirmed.fail("值%s不是数字(Number)类型", value);
// }
// Map<String, Object> metadata = AnnotationUtils.getMetadata(annotation);
// if (annotation.annotationType() == Max.class) {
// return this.doMaxAffirmed(value, metadata);
// } else {
// return this.doDecimalMaxAffirmed(metadata, value);
// }
// }
//
// private Affirmed doDecimalMaxAffirmed(Map<String, Object> metadata, Object value) {
// String decimalMaxValue = (String) metadata.get("value");
// boolean inclusive = (boolean) metadata.get("inclusive");
// BigDecimal max = new BigDecimal(Optional.ofNullable(decimalMaxValue).orElse("0"));
// Number currentValue = NumberUtil.cast(value.getClass(), value.toString());
// boolean success;
// if (inclusive) {
// // 包括
// success = max.doubleValue() >= currentValue.doubleValue();
// if (success) {
// return Affirmed.success("值%s小于等于@DecimalMin(%s)", currentValue, max);
// }
// return Affirmed.fail("值%s大于@DecimalMin(%s)", currentValue, max);
// } else {
// success = max.doubleValue() < currentValue.doubleValue();
// if (success) {
// return Affirmed.success("值%s小于@DecimalMin(%s)", currentValue, max);
// }
// return Affirmed.fail("值%s大于等于@DecimalMin(%s)", currentValue, max);
// }
// }
//
// private Affirmed doMaxAffirmed(Object value, Map<String, Object> metadata) {
// long max = (long) metadata.get("value");
// Number currentValue = NumberUtil.cast(value.getClass(), value.toString());
// boolean success = max >= currentValue.doubleValue();
// if (success) {
// return Affirmed.success("值%s小于@Max(%s)", currentValue, max);
// }
// return Affirmed.fail("值%s大于@Max(%s)", value, max);
// }
// }
