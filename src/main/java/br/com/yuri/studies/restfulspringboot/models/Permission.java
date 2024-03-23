package br.com.yuri.studies.restfulspringboot.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Table(name = "permission")
@Entity
public class Permission implements Serializable, GrantedAuthority {

	@Serial
	private static final long serialVersionUID = -2491883028738884274L;

	@Id
	private Long id;

	@Column
	private String description;

	public Permission() {
	}

	public Permission(Long id, String description) {
		this.id = id;
		this.description = description;
	}

	@Override
	public String getAuthority() {
		return "ROLE_" + description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Permission that = (Permission) o;
		return Objects.equals(id, that.id) && Objects.equals(description, that.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, description);
	}
}
