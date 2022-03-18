package org.magneton.module.distributed.cache;

import java.io.Serializable;

import lombok.Data;

@Data
public class TestA implements Serializable {

	private int i;

	private String s;

	private TestA testA;

}