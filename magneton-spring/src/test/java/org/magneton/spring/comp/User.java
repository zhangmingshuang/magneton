package org.magneton.spring.comp;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class User {

	private int id;

	private String name;

	public User(int id, String name) {
		this.id = id;
		this.name = name;
	}

}