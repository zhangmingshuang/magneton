// package cn.nascent.framework.test.jsr303.judger;
//
// import cn.nascent.framework.test.annotation.TestComponent;
// import cn.nascent.framework.test.judger.Affirmed;
// import cn.nascent.framework.test.judger.Judger;
// import java.lang.annotation.Annotation;
// import javax.validation.constraints.AssertFalse;
// import javax.validation.constraints.AssertTrue;
//
/// **
// * .
// *
// * @author zhangmsh 2021/8/4
// * @since 2.0.0
// */
// @TestComponent
// public class Jsr303AssertTrueAndAssertFalseJudger implements Judger {
//
// @Override
// public Class[] annotationTypes() {
// return new Class[] {AssertTrue.class, AssertFalse.class};
// }
//
// @Override
// public Affirmed isAffirmed(Annotation annotation, Object value) {
// String annotationName = annotation.annotationType().getSimpleName();
// if (value == null) {
// return Affirmed.fail("注解@%s但是值为null", annotationName);
// }
// if (annotation.annotationType() == AssertTrue.class) {
// boolean success = Boolean.TRUE.equals(value);
// if (!success) {
// return Affirmed.fail("注解@%s但值为%s", annotationName, value);
// }
// }
// return Affirmed.success("@%s符合预期", annotationName);
// }
// }
