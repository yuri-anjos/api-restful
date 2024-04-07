package br.com.yuri.studies.restfulspringboot.integrationtests.dtos.wrappers;

import br.com.yuri.studies.restfulspringboot.integrationtests.dtos.PersonDTO;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@XmlRootElement(name = "PagedModelPerson")
public class PagedModelPerson implements Serializable {
	@Serial
	private static final long serialVersionUID = -1839823123073475315L;

	private List<PersonDTO> content;

	public PagedModelPerson() {
	}

	public List<PersonDTO> getContent() {
		return content;
	}

	public void setContent(List<PersonDTO> content) {
		this.content = content;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PagedModelPerson that = (PagedModelPerson) o;
		return Objects.equals(content, that.content);
	}

	@Override
	public int hashCode() {
		return Objects.hash(content);
	}
}
