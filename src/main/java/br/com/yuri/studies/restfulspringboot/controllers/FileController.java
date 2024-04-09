package br.com.yuri.studies.restfulspringboot.controllers;

import br.com.yuri.studies.restfulspringboot.dtos.UploadFileResponseDTO;
import br.com.yuri.studies.restfulspringboot.services.FileStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/files/v1")
@Tag(name = "File Endpoint", description = "Endpoints that manages Files")
public class FileController {
	private static final Logger LOGGER = Logger.getLogger(FileController.class.getName());

	private FileStorageService service;

	@Autowired
	public FileController(FileStorageService fileStorageService) {
		this.service = fileStorageService;
	}

	@PostMapping("/uploadFile")
	public UploadFileResponseDTO uploadFile(@RequestParam(name = "file") MultipartFile file) {
		LOGGER.info("<uploadFile> Storing file on disk...");

		var filename = service.storeFile(file);
		String fileDownloadUri = ServletUriComponentsBuilder
				.fromCurrentContextPath()
				.path("/file/v1/downloadFile/")
				.path(filename)
				.toUriString();
		return new UploadFileResponseDTO(filename, fileDownloadUri, file.getContentType(), file.getSize());
	}

	@PostMapping("/uploadMultipleFiles")
	public List<UploadFileResponseDTO> uploadMultipleFiles(@RequestParam(name = "files") MultipartFile[] files) {
		LOGGER.info("<uploadMultipleFiles> Storing files on disk...");

		return Arrays.asList(files).stream().map(this::uploadFile).toList();
	}

	@GetMapping("/downloadFile/{filename:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable("filename") String filename, HttpServletRequest httpServletRequest) {
		LOGGER.info("<downloadFile> Reading a file from disk...");

		Resource resource = service.loadFileAsResource(filename);
		String contentType = "";

		try {
			contentType = httpServletRequest.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (Exception ex) {
			LOGGER.info("could not determine file type!");
		}

		if (contentType.isBlank()) {
			contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
		}


		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(
						HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
}
