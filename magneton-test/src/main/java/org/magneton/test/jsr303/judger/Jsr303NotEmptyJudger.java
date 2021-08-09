// package cn.nascent.framework.test.jsr303.judger;
//
// import cn.nascent.framework.test.annotation.TestComponent;
// import cn.nascent.framework.test.judger.Affirmed;
// import cn.nascent.framework.test.judger.Judger;
// import java.lang.annotation.Annotation;
// import java.util.Collection;
// import java.util.Map;
// import javax.validation.constraints.NotEmpty;
//
/// **
// * .
// *
// * @author zhangmsh 2021/8/4
// * @since 2.0.0
// */
// @TestComponent
// public class Jsr303NotEmptyJudger implements Judger {
//
// @Override
// public Class[] annotationTypes() {
// return new Class[] {NotEmpty.class};
// }
//
// @Override
// public Affirmed isAffirmed(Annotation annotation, Object value) {
// if (value == null) {
// return Affirmed.fail("注解@NotEmpty但是值为null");
// }
// Class<?> clazz = value.getClass();
// if (Collection.class.isAssignableFrom(clazz)) {
// if (((Collection) value).isEmpty()) {
// return Affirmed.fail("集合(%s)注解@NotEmpty但是size=0", value.getClass());
// }
// }
//
// if (Map.class.isAssignableFrom(clazz)) {
// if (((Map) value).isEmpty()) {
// return Affirmed.fail("Map(%s)注解@NotEmpty但是size=0", value.getClass());
// }
// }
// return Affirmed.success("@NotEmpty符合预期");
// }
// }
