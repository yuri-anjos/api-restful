package br.com.yuri.studies.restfulspringboot.dtos;

import jakarta.validation.constraints.NotEmpty;

import java.util.Objects;

public class UserDTO {
	@NotEmpty
	private String username;
	@NotEmpty
	private String fullname;
	@NotEmpty
	private String password;
	@NotEmpty
	private String confirmPassword;

	public UserDTO() {
		// empty constructor.
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserDTO userDTO = (UserDTO) o;
		return Objects.equals(username, userDTO.username) && Objects.equals(fullname, userDTO.fullname) && Objects.equals(password, userDTO.password) && Objects.equals(confirmPassword, userDTO.confirmPassword);
	}

	@Override
	public int hashCode() {
		return Objects.hash(username, fullname, password, confirmPassword);
	}
}
