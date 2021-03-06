package com.afl.aflcv.services.files;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class UploadFileServiceImpl implements IUploadFileService {
	
	public final static String DIRECTORIO_UPLOAD = "uploads";
	private Logger log = LoggerFactory.getLogger(UploadFileServiceImpl.class);
	
	@Override
	public Resource cargar(String nombreImagen) throws MalformedURLException {
		Path path = getPath(DIRECTORIO_UPLOAD, nombreImagen);
		log.info("upload: " + path.toString());
		
		Resource resource = null;
		resource = new UrlResource(path.toUri());
	    if(!resource.exists() || !resource.isReadable()) {
	    	path = getPath("src/main/resources/static/images", "no-user.png");
			resource = new UrlResource(path.toUri());
			log.error("Error, no se pudo cargar la imagen " + nombreImagen + " se sustituye por imagen anónima");
	    };	
		return resource;
	}
	
	@Override
	public Resource salidaFichero(Path path) throws MalformedURLException {
		log.info("salida de fichero: " + path.toString());
		
		Resource resource = null;
		resource = new UrlResource(path.toUri());
	    if(!resource.exists() || !resource.isReadable()) {
	    	Path path2 = getPath("src/main/resources/static/images", "no-user.png");
			resource = new UrlResource(path2.toUri());
			log.error("Error, no se pudo cargar la imagen " + path.toString() + " se sustituye por imagen anónima");
	    };	
		return resource;
	}
	

	@Override
	public String copia(MultipartFile archivo) throws IOException {

		String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replace(" ", "");
		Path rutaArchivo = getPath(DIRECTORIO_UPLOAD, nombreArchivo);
		log.info("upload: " + rutaArchivo.toString());
		Files.copy(archivo.getInputStream(), rutaArchivo);
		return nombreArchivo;
	}

	@Override
	public boolean eliminar(String archivo) {
		if (archivo != null && archivo.length() > 0) {
			Path rutaFotoAnterior = getPath (DIRECTORIO_UPLOAD, archivo);
			File archivoFotoAnterior = rutaFotoAnterior.toFile();
			if (archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
				archivoFotoAnterior.delete();
				return true;
			}
		}
		return false;
	}

	@Override
	public Path getPath(String path, String archivo) {
		Path rutaArchivo = Paths.get(path).resolve(archivo).toAbsolutePath();
		return rutaArchivo;
	}
	

}
