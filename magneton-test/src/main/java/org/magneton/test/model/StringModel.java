package org.magneton.test.model;

import javax.annotation.Nullable;
import org.magneton.test.model.generate.EmailAddressGenerator;
import org.magneton.test.model.generate.base.GenericGenerator;

public enum StringModel {
  NORMAL(null),

  EMAIL(EmailAddressGenerator.getInstance());

  private final GenericGenerator genericGenerator;

  StringModel(GenericGenerator genericGenerator) {
    this.genericGenerator = genericGenerator;
  }

  @Nullable
  public GenericGenerator getGenericGenerator() {
    return this.genericGenerator;
  }
}
