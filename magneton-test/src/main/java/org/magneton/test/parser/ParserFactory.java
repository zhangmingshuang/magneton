package org.magneton.test.parser;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.google.common.collect.Lists;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.core.ChaosContext;
import org.magneton.test.parser.Definition.DefinitionBuilder;
import org.magneton.test.util.AnnotationUtil;
import org.magneton.test.util.FieldUtil;
import org.magneton.test.util.GenericUtil;
import org.magneton.test.util.PrimitiveUtil;

/**
 * .
 *
 * @author zhangmsh 2021/8/17
 * @since 2.0.0
 */
@TestComponent
public class ParserFactory {

  public static ParserFactory getInstance() {
    ParserFactory parserFactory = ChaosContext.getComponent(ParserFactory.class);
    Verify.verifyNotNull(parserFactory, "not parser factory found");
    return parserFactory;
  }

  public <T> Definition parse(Class<T> clazz) {
    Preconditions.checkNotNull(clazz, "clazz must be not null");
    return this.parseClass(clazz);
  }

  private <T> Definition parseClass(Class<T> clazz) {
    if (Object.class == clazz || PrimitiveUtil.isPrimitive(clazz)) {
      return this.buildPrimitiveDefinition(clazz);
    }
    DefinitionBuilder classDefinitionBuilder =
        Definition.builder().clazz(clazz).annotations(AnnotationUtil.findAnnotations(clazz));
    if (!this.isIgnore(clazz)) {
      Set<Field> fields = FieldUtil.getFields(clazz);
      if (fields.isEmpty()) {
        return classDefinitionBuilder.build();
      }
      List<Definition> fieldDefinitions = Lists.newArrayListWithCapacity(fields.size());
      for (Field field : fields) {
        fieldDefinitions.add(this.parseField(field));
      }
      classDefinitionBuilder.childDefinitions(fieldDefinitions);
    }
    return classDefinitionBuilder.build();
  }

  private Definition parseField(Field field) {
    Class<?> fieldType = field.getType();
    if (PrimitiveUtil.isPrimitive(fieldType)
        || String.class == fieldType
        || Object.class == fieldType) {
      return this.buildFieldDefinition(field);
    }
    Definition fieldDefinitiion = this.buildFieldDefinition(field);
    if (!this.isIgnore(field.getType())) {
      Set<Field> nextFields = FieldUtil.getFields(fieldType);
      List<Definition> nextFieldDefinitions = Lists.newArrayListWithCapacity(nextFields.size());
      if (!nextFields.isEmpty()) {
        for (Field nextField : nextFields) {
          nextFieldDefinitions.add(this.buildFieldDefinition(nextField));
        }
      }
      fieldDefinitiion.setChildDefinitions(nextFieldDefinitions);
    }
    return fieldDefinitiion;
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private boolean isIgnore(Class clazz) {
    return clazz.getName().startsWith("java.");
  }

  private <T> Definition buildPrimitiveDefinition(Class<T> clazz) {
    return Definition.builder()
        .clazz(clazz)
        .annotations(AnnotationUtil.findAnnotations(clazz))
        .build();
  }

  private Definition buildFieldDefinition(Field field) {
    return Definition.builder()
        .clazz(field.getType())
        .field(field)
        .annotations(AnnotationUtil.findAnnotations(field))
        .generics(GenericUtil.getGenerics(field))
        .build();
  }
}
