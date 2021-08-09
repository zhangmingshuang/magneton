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
// import javax.validation.constraints.DecimalMin;
// import javax.validation.constraints.Min;
//
/// **
// * .
// *
// * @author zhangmsh 2021/8/4
// * @since 2.0.0
// */
// @TestComponent
// public class Jsr303MinJudger implements Judger {
//
// @Override
// public Class[] annotationTypes() {
// return new Class[] {Min.class, DecimalMin.class};
// }
//
// @Override
// public Affirmed isAffirmed(Annotation annotation, Object value) {
// if (value == null) {
// return Affirmed.fail("注解@Min但是值为null");
// }
// if (!NumberUtil.isNumberType(value.getClass())) {
// return Affirmed.fail("值%s不是数字(Number)类型", value);
// }
// Map<String, Object> metadata = AnnotationUtils.getMetadata(annotation);
// if (annotation.annotationType() == Min.class) {
// return this.doMinAffirmed(metadata, value);
// } else {
// return this.doDecimalMinAffirmed(metadata, value);
// }
// }
//
// private Affirmed doDecimalMinAffirmed(Map<String, Object> metadata, Object value) {
// String decimalMinValue = (String) metadata.get("value");
// boolean inclusive = (boolean) metadata.get("inclusive");
// BigDecimal min = new BigDecimal(Optional.ofNullable(decimalMinValue).orElse("0"));
// Number currentValue = NumberUtil.cast(value.getClass(), value.toString());
// boolean success;
// if (inclusive) {
// // 包括
// success = min.doubleValue() <= currentValue.doubleValue();
// if (success) {
// return Affirmed.success("值%s大于@DecimalMin(%s)", currentValue, min);
// }
// return Affirmed.fail("值%s小于@DecimalMin(%s)", currentValue, min);
// } else {
// success = min.doubleValue() < currentValue.doubleValue();
// if (success) {
// return Affirmed.success("值%s大于等于@DecimalMin(%s)", currentValue, min);
// }
// return Affirmed.fail("值%s小于@DecimalMin(%s)", currentValue, min);
// }
// }
//
// private Affirmed doMinAffirmed(Map<String, Object> metadata, Object value) {
// long min = (long) metadata.get("value");
// Number currentValue = NumberUtil.cast(value.getClass(), value.toString());
// boolean success = min <= currentValue.doubleValue();
// if (success) {
// return Affirmed.success("值%s大于@Min(%s)", currentValue, min);
// }
// return Affirmed.fail("值%s小于@Min(%s)", currentValue, min);
// }
// }
