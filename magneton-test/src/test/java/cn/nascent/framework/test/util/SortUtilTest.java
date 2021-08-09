package cn.nascent.framework.test.util;

import cn.nascent.framework.test.annotation.TestSort;
import com.google.common.collect.Lists;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh 2021/8/3
 * @since
 */
class SortUtilTest {

  @Test
  void sort() {
    List<Object> sort = SortUtil.sort(Lists.newArrayList(new S1(), new S2()));
    Assertions.assertEquals(sort.get(0).getClass(), S2.class);
    Assertions.assertEquals(sort.get(1).getClass(), S1.class);
  }

  @TestSort
  public static class S1 {}

  @TestSort(1)
  public static class S2 {}
}
