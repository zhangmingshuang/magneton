// package cn.nascent.framework.test.judger;
//
// import com.google.common.base.Strings;
// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.Setter;
// import lombok.ToString;
//
/// **
// * .
// *
// * @author zhangmsh 2021/8/4
// * @since 2.0.0
// */
// @Setter
// @Getter
// @ToString
// @AllArgsConstructor
// public class Affirmed {
//
// private boolean affirmed;
//
// private String msg;
//
// public static Affirmed fail(String msg, Object... args) {
// return new Affirmed(false, Strings.lenientFormat(msg, args));
// }
//
// public static Affirmed success(String msg, Object... args) {
// return new Affirmed(true, Strings.lenientFormat(msg, args));
// }
// }
