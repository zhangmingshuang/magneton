package cn.nascent.framework.test;

import cn.nascent.framework.test.core.Config;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Negative;
import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Setter;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since 2.0.0
 */
class ChaosTestTest {

  @Test
  void testNumberByDemo() {
    Config config = new Config();
    config.setDemonNullProbability(100);
    for (int i = 0; i < 10; i++) {
      Integer value = ChaosTest.createByDemon(Integer.class, config);
      Assertions.assertNull(value);
    }
  }

  @Test
  void testNumber() {
    Integer i = ChaosTest.createByAngel(int.class);
    System.out.println(i);
    Assertions.assertNotNull(i);
    Integer i2 = ChaosTest.createByAngel(Integer.class);
    System.out.println(i2);
    Assertions.assertNotNull(i2);
    int[] is = ChaosTest.createByAngel(int[].class);
    System.out.println(Arrays.toString(is));
    Assertions.assertNotNull(is);
    Integer[] is2 = ChaosTest.createByAngel(Integer[].class);
    System.out.println(Arrays.toString(is2));
    Assertions.assertNotNull(is2);

    // --
    Long l = ChaosTest.createByAngel(long.class);
    System.out.println(l);
    Assertions.assertNotNull(l);
    Long l2 = ChaosTest.createByAngel(Long.class);
    System.out.println(l2);
    Assertions.assertNotNull(l2);
    long[] ls = ChaosTest.createByAngel(long[].class);
    System.out.println(Arrays.toString(ls));
    Assertions.assertNotNull(ls);
    Long[] ls2 = ChaosTest.createByAngel(Long[].class);
    System.out.println(Arrays.toString(ls2));
    Assertions.assertNotNull(ls2);
  }

  @Test
  void testBase() {
    Base base = ChaosTest.createByAngel(Base.class);
    System.out.println(base);

    System.out.println("----");
    Base[] bases = ChaosTest.createByAngel(Base[].class);
    System.out.println(Arrays.deepToString(bases));

    System.out.println("----");
    ComposeBase composeBase = ChaosTest.createByAngel(ComposeBase.class);
    System.out.println(composeBase.toString());
  }

  @Test
  void testList() {
    // List list = ChaosTest.createByAngel(List.class);
    // System.out.println(list);

    System.out.println("---");
    ListObj listObj = ChaosTest.createByAngel(ListObj.class);
    System.out.println(listObj);
  }

  @Test
  void testMap() {
    // Map map = ChaosTest.createByAngel(Map.class);
    // System.out.println(map);

    System.out.println("---");
    MapObj mapObj = ChaosTest.createByAngel(MapObj.class);
    System.out.println(mapObj);
  }

  @RepeatedTest(10)
  void testJsr303MinAndMax() {
    Jsr303MinMaxDto jsr303Dto = ChaosTest.createByAngel(Jsr303MinMaxDto.class);
    System.out.println(jsr303Dto);

    Assertions.assertTrue(ChaosTest.booleanSupplier().jsr303(jsr303Dto));
    Assertions.assertFalse(ChaosTest.booleanSupplier().jsr303(new Jsr303MinMaxDto()));
  }

  @RepeatedTest(10)
  void testJsr303NotEmpty() {
    Jsr303NotEmptyDto dto = ChaosTest.createByAngel(Jsr303NotEmptyDto.class);
    System.out.println(dto);

    Assertions.assertTrue(ChaosTest.booleanSupplier().jsr303(dto));
    Assertions.assertFalse(ChaosTest.booleanSupplier().jsr303(new Jsr303NotEmptyDto()));
  }

  @Test
  void testJsr303AssertTrueFalse() {
    Jsr303AssertTrueFalseDto dto = ChaosTest.createByAngel(Jsr303AssertTrueFalseDto.class);
    System.out.println(dto);

    Assertions.assertTrue(ChaosTest.booleanSupplier().jsr303(dto));
    Assertions.assertFalse(ChaosTest.booleanSupplier().jsr303(new Jsr303AssertTrueFalseDto()));
  }

  @RepeatedTest(10)
  void testJsr303DecimalMinMax() {
    Jsr303DecimalMinMaxDto dto = ChaosTest.createByAngel(Jsr303DecimalMinMaxDto.class);
    System.out.println(dto);

    Assertions.assertTrue(ChaosTest.booleanSupplier().jsr303(dto));
    Assertions.assertFalse(ChaosTest.booleanSupplier().jsr303(new Jsr303DecimalMinMaxDto()));
  }

  @Test
  void testJsr303Digits() {
    Jsr303DigitsDto dto = ChaosTest.createByAngel(Jsr303DigitsDto.class);
    System.out.println(dto);

    Assertions.assertTrue(ChaosTest.booleanSupplier().jsr303(dto));
    Assertions.assertTrue(ChaosTest.booleanSupplier().jsr303(new Jsr303DigitsDto()));
  }

  @Test
  void testJsr303Email() {
    Jsr303EmailDto dto = ChaosTest.createByAngel(Jsr303EmailDto.class);
    System.out.println(dto);

    Assertions.assertTrue(ChaosTest.booleanSupplier().jsr303(dto));
    Assertions.assertFalse(ChaosTest.booleanSupplier().jsr303(new Jsr303EmailDto()));
  }

  @Test
  void testFuture() {
    Jsr303FutureDto dto = ChaosTest.createByAngel(Jsr303FutureDto.class);
    System.out.println(dto);

    Assertions.assertTrue(ChaosTest.booleanSupplier().jsr303(dto));
  }

  @Test
  void testDate() {
    DateDto dto = ChaosTest.createByAngel(DateDto.class);
    System.out.println(dto);

    Assertions.assertTrue(ChaosTest.booleanSupplier().notNull(dto));
  }

  @Test
  void testPast() {
    Jsr303PastDto dto = ChaosTest.createByAngel(Jsr303PastDto.class);
    System.out.println(dto);

    Assertions.assertTrue(ChaosTest.booleanSupplier().jsr303(dto));
  }

  @RepeatedTest(10)
  void testNegative() {
    Jsr303NegativeDto dto = ChaosTest.createByAngel(Jsr303NegativeDto.class);
    System.out.println(dto);

    Assertions.assertTrue(ChaosTest.booleanSupplier().jsr303(dto));
  }

  @Test
  void testPattern() {
    Jsr303PatternDto dto = ChaosTest.createByAngel(Jsr303PatternDto.class);
    System.out.println(dto);

    Assertions.assertFalse(ChaosTest.booleanSupplier().jsr303(dto));
  }

  @RepeatedTest(10)
  void testSize() {
    Jsr303SizeDto dto = ChaosTest.createByAngel(Jsr303SizeDto.class);
    System.out.println(dto);

    Assertions.assertTrue(ChaosTest.booleanSupplier().jsr303(dto));
  }

  @Setter
  @ToString
  public static class Jsr303SizeDto {

    @Size(min = 1, max = 3)
    private String str;

    @Size(min = 1, max = 3)
    private List<String> list;

    @Size(min = 1, max = 3)
    private Map<String, String> map;

    @Size(min = 1, max = 3)
    private int[] array;
  }

  @Setter
  @ToString
  public static class Jsr303PatternDto {

    @Pattern(regexp = "[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}")
    private String a;

    @Pattern(regexp = "[A-Za-z0-9]+")
    private String b;

    @Pattern(regexp = "/^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$/")
    private String email;
  }

  @Setter
  @ToString
  public static class Jsr303NegativeDto {

    @Negative private byte byteValue;

    @NegativeOrZero private Byte byteWrapperValue;

    @Negative private int intValue;

    @NegativeOrZero private Integer integerValue;

    @Negative private short shortValue;

    @NegativeOrZero private Short shortWrapperValue;

    @Negative private long longValue;

    @NegativeOrZero private Long longWrapperValue;

    @Negative private BigDecimal bigDecimalValue;

    @NegativeOrZero private BigInteger bigIntegerValue;
  }

  @Setter
  @ToString
  public static class Jsr303PastDto {

    @Past private Date date;

    @Past private Calendar calendar;

    @Past private Instant instant;

    @Past private LocalDate localDate;

    @Past private LocalDateTime localDateTime;

    @Past private LocalTime localTime;

    @Past private MonthDay monthDay;

    @Past private OffsetDateTime offsetDateTime;

    @Past private Year year;

    @Past private YearMonth yearMonth;

    @Past private ZonedDateTime zonedDateTime;

    @Past private HijrahDate hijrahDate;

    @Past private JapaneseDate japaneseDate;

    @Past private MinguoDate minguoDate;

    @Past private ThaiBuddhistDate thaiBuddhistDate;
  }

  @Setter
  @ToString
  public static class Jsr303FutureDto {

    @Future private Date date;

    @Future private Calendar calendar;

    @Future private Instant instant;

    @Future private LocalDate localDate;

    @Future private LocalDateTime localDateTime;

    @Future private LocalTime localTime;

    @Future private MonthDay monthDay;

    @Future private OffsetDateTime offsetDateTime;

    @Future private Year year;

    @Future private YearMonth yearMonth;

    @Future private ZonedDateTime zonedDateTime;

    @Future private HijrahDate hijrahDate;

    @Future private JapaneseDate japaneseDate;

    @Future private MinguoDate minguoDate;

    @Future private ThaiBuddhistDate thaiBuddhistDate;
  }

  @Setter
  @ToString
  public static class Jsr303EmailDto {

    @Email private String email = "asdfads";
  }

  @Setter
  @ToString
  public static class DateDto {

    private Date date;

    private Date[] dates;

    private java.sql.Date sqlDate;

    private java.sql.Date[] sqlDates;

    private Calendar calendar;

    private Calendar[] calendars;

    private Instant instant;

    private Instant[] instants;

    private LocalDate localDate;

    private LocalDate[] localDates;

    private LocalDateTime localDateTime;

    private LocalDateTime[] localDateTimes;

    private LocalTime localTime;

    private LocalTime[] localTimes;

    private MonthDay monthDay;

    private MonthDay[] monthDays;

    private OffsetDateTime offsetDateTime;

    private OffsetDateTime[] offsetDateTimes;

    private Year year;

    private Year[] years;

    private YearMonth yearMonth;

    private YearMonth[] yearMonths;

    private ZonedDateTime zonedDateTime;

    private ZonedDateTime[] zonedDateTimes;

    private HijrahDate hijrahDate;

    private HijrahDate[] hijrahDates;

    private JapaneseDate japaneseDate;

    private JapaneseDate[] japaneseDates;

    private MinguoDate minguoDate;

    private MinguoDate[] minguoDates;

    private ThaiBuddhistDate thaiBuddhistDate;

    private ThaiBuddhistDate[] thaiBuddhistDates;
  }

  @Setter
  @ToString
  public static class Jsr303DigitsDto {

    @Digits(integer = 1, fraction = 2)
    private byte byteValue;

    @Digits(integer = 1, fraction = 2)
    private Byte byteWrapperValue;

    @Digits(integer = 1, fraction = 2)
    private int intValue;

    @Digits(integer = 1, fraction = 2)
    private Integer integerValue;

    @Digits(integer = 1, fraction = 2)
    private short shortValue;

    @Digits(integer = 1, fraction = 2)
    private Short shortWrapperValue;

    @Digits(integer = 1, fraction = 2)
    private long longValue;

    @Digits(integer = 1, fraction = 2)
    private Long longWrapperValue;

    @Digits(integer = 1, fraction = 2)
    private BigDecimal bigDecimalValue;

    @Digits(integer = 1, fraction = 2)
    private BigInteger bigIntegerValue;
  }

  @Setter
  @ToString
  public static class Jsr303AssertTrueFalseDto {

    @AssertTrue private boolean trueValue;

    @AssertFalse private boolean falseValue;

    @AssertTrue private Boolean trueWrapperValue;

    @AssertFalse private Boolean falseWrapperValue;
  }

  @Setter
  @ToString
  public static class Jsr303NotEmptyDto {

    @NotEmpty public String str;

    @NotEmpty public CharSequence charSequence;

    @NotEmpty public Collection<String> collection;

    @NotEmpty public Map<String, Integer> map;

    @NotEmpty public int[] intArray;
  }

  @Setter
  @ToString
  public static class Jsr303DecimalMinMaxDto {

    @DecimalMin("1")
    @DecimalMax("10")
    private byte byteValue = 11;

    @DecimalMin("1")
    @DecimalMax("10")
    private Byte byteWrapperValue;

    @DecimalMin("1")
    @DecimalMax("10")
    private int intValue;

    @DecimalMin("1")
    @DecimalMax("10")
    private Integer integerValue;

    @DecimalMin("1")
    @DecimalMax("10")
    private short shortValue;

    @DecimalMin("1")
    @DecimalMax("10")
    private Short shortWrapperValue;

    @DecimalMin("1")
    @DecimalMax("10")
    private long longValue;

    @DecimalMin("1")
    @DecimalMax("10")
    private Long longWrapperValue;

    @DecimalMin("1.1")
    @DecimalMax("10.3")
    private BigDecimal bigDecimalValue;

    @DecimalMin("1.1")
    @DecimalMax("10.3")
    private BigInteger bigIntegerValue;
  }

  @Setter
  @ToString
  public static class Jsr303MinMaxDto {

    @Min(1)
    @Max(10)
    private byte byteValue;

    @Min(1)
    @Max(10)
    private Byte byteWrapperValue;

    @Min(1)
    @Max(10)
    private int intValue;

    @Min(1)
    @Max(10)
    private Integer integerValue;

    @Min(1)
    @Max(10)
    private short shortValue;

    @Min(1)
    @Max(10)
    private Short shortWrapperValue;

    @Min(1)
    @Max(10)
    private long longValue;

    @Min(1)
    @Max(10)
    private Long longWrapperValue;

    @Min(1)
    @Max(10)
    private BigDecimal bigDecimalValue;

    @Min(1)
    @Max(10)
    private BigInteger bigIntegerValue;
  }

  @Setter
  @ToString
  public static class MapObj {

    private Map<String, String> str;

    private Map<Integer, int[]> ints;

    private Map<Base, ListObj> s1;

    private Map<String, List<Integer>> s2;
  }

  @Setter
  @ToString
  public static class ListObj {

    private ArrayList<String> strs;

    private CopyOnWriteArrayList<String> strs2;

    private List<Integer> ids;

    private List<int[]> ids1;

    private Set<ComposeBase> composeBases;
  }

  @Setter
  @ToString
  public static class ComposeBase {

    private Base base;

    private Base[] bases;
  }

  @Setter
  @ToString
  public static class Base {

    private byte b;

    private Byte b1;

    private char c;

    private Character c1;

    private short s;

    private Short s1;

    private int i;

    private Integer i1;

    private long l;

    private Long l1;

    private float f;

    private Float f1;

    private double d;

    private Double d1;
  }
}
