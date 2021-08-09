// package cn.nascent.framework.test.judger;
//
// import cn.nascent.framework.test.annotation.TestAutowired;
// import cn.nascent.framework.test.annotation.TestComponent;
// import cn.nascent.framework.test.core.AfterAutowrited;
// import com.google.common.base.Preconditions;
// import com.google.common.base.Strings;
// import com.google.common.collect.Maps;
// import java.lang.annotation.Annotation;
// import java.util.List;
// import java.util.Map;
// import javax.annotation.Nullable;
//
/// **
// * .
// *
// * @author zhangmsh 2021/8/4
// * @since 2.0.0
// */
// @TestComponent
// public class JudegerFactory implements AfterAutowrited {
//
// @TestAutowired private List<Judger> judgers;
// private Map<Class, Judger> annotationJudger = Maps.newHashMap();
//
// @Nullable
// public Judger getJudeger(Class<? extends Annotation> annotationType) {
// Preconditions.checkNotNull(annotationType);
// Judger judger = this.annotationJudger.get(annotationType);
// if (judger == null) {
// return null;
// }
// return judger;
// }
//
// @Override
// public void afterAutowrited() {
// for (Judger judger : this.judgers) {
// Class[] annotationTypes = judger.annotationTypes();
// if (annotationTypes == null) {
// continue;
// }
// for (Class annotationType : annotationTypes) {
// Judger exist = this.annotationJudger.put(annotationType, judger);
// if (exist != null) {
// throw new RuntimeException(
// Strings.lenientFormat(
// "存在多个重复的注解判断器%s与%s", judger.getClass(), exist.getClass()));
// }
// }
// }
// }
// }
