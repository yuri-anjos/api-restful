package br.com.yuri.studies.restfulspringboot.services;

import br.com.yuri.studies.restfulspringboot.configs.FileStorageConfig;
import br.com.yuri.studies.restfulspringboot.exceptions.FileStorageException;
import br.com.yuri.studies.restfulspringboot.exceptions.MyFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

	private final Path fileStorageLocation;

	@Autowired
	public FileStorageService(FileStorageConfig fileStorageConfig) {
		fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create the directory where the uploaded files should be stored!", ex);
		}
	}

	public String storeFile(MultipartFile file) {
		var filename = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			if (filename.contains("..")) {
				throw new FileStorageException("Sorry, Filename has invalid path sequence: " + filename);
			}

			Path targetLocation = fileStorageLocation.resolve(filename);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			return filename;
		} catch (Exception ex) {
			throw new FileStorageException("Could not store file " + filename + ". Please try again!", ex);
		}
	}

	public Resource loadFileAsResource(String filename){
		try {
			Path filePath = fileStorageLocation.resolve(filename).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if(resource.exists()) {
				return resource;
			}else {
				throw new MyFileNotFoundException("File not found");
			}
		} catch (Exception ex){
			throw new MyFileNotFoundException("File not found: " + filename, ex);
		}
	}
}
