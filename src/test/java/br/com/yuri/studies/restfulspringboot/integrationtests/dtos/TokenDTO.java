package br.com.yuri.studies.restfulspringboot.integrationtests.dtos;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@XmlRootElement(name = "TokenDTO")
public class TokenDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 7193095246006170374L;

	private String username;
	private Boolean authenticated;
	private Date created;
	private Date expiration;
	private String accessToken;
	private String refreshToken;

	public TokenDTO() {
		// empty constructor.
	}

	public TokenDTO(String username, Boolean authenticated, Date created, Date expiration, String accessToken, String refreshToken) {
		this.username = username;
		this.authenticated = authenticated;
		this.created = created;
		this.expiration = expiration;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(Boolean authenticated) {
		this.authenticated = authenticated;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TokenDTO that = (TokenDTO) o;
		return Objects.equals(username, that.username) && Objects.equals(authenticated, that.authenticated) && Objects.equals(created, that.created) && Objects.equals(expiration, that.expiration) && Objects.equals(accessToken, that.accessToken) && Objects.equals(refreshToken, that.refreshToken);
	}

	@Override
	public int hashCode() {
		return Objects.hash(username, authenticated, created, expiration, accessToken, refreshToken);
	}
}
