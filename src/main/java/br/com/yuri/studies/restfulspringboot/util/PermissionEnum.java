package br.com.yuri.studies.restfulspringboot.util;

public enum PermissionEnum {

	ADMIN(1L, "ADMIN"),
	MANAGER(2L, "MANAGER"),
	COMMON_USER(3L, "COMMON_USER");

	private final Long id;
	private final String description;

	PermissionEnum(Long id, String description) {
		this.id = id;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}
}
