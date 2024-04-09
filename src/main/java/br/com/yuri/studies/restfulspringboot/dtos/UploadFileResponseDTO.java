package br.com.yuri.studies.restfulspringboot.dtos;

import java.io.Serial;
import java.io.Serializable;

public class UploadFileResponseDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -5332971973427446417L;

	private String filename;
	private String fileDownloadUri;
	private String fileType;
	private long size;

	public UploadFileResponseDTO() {
	}

	public UploadFileResponseDTO(String filename, String fileDownloadUri, String fileType, long size) {
		this.filename = filename;
		this.fileDownloadUri = fileDownloadUri;
		this.fileType = fileType;
		this.size = size;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFileDownloadUri() {
		return fileDownloadUri;
	}

	public void setFileDownloadUri(String fileDownloadUri) {
		this.fileDownloadUri = fileDownloadUri;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
}
