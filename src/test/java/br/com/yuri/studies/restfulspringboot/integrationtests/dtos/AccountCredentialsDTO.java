package br.com.yuri.studies.restfulspringboot.integrationtests.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@XmlRootElement(name = "AccountCredentialsDTO")
public class AccountCredentialsDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 5932443650697672834L;

	@NotEmpty
	private String username;
	@NotEmpty
	private String password;

	public AccountCredentialsDTO() {
		// empty constructor.
	}

	public AccountCredentialsDTO(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AccountCredentialsDTO that = (AccountCredentialsDTO) o;
		return Objects.equals(username, that.username) && Objects.equals(password, that.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(username, password);
	}
}
