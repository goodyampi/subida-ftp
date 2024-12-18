package com.ftp.prueba.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ftp.prueba.service.FtpService;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private FtpService ftpService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            logger.info("Recibida solicitud para subir archivo: {}", file.getOriginalFilename());



            // Verificar el tipo MIME del archivo
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("Solo se permiten imágenes.");
            }
            boolean success =
                    ftpService.uploadFile(file.getOriginalFilename(), file.getInputStream());

            if (success) {
                logger.info("Archivo subido exitosamente: {}", file.getOriginalFilename());
                return ResponseEntity
                        .ok("Imagen subida exitosamente: " + file.getOriginalFilename());
            } else {
                logger.warn("Falló la subida de archivo: {}", file.getOriginalFilename());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al subir la imagen.");
            }
        } catch (Exception e) {
            logger.error("Error procesando el archivo: {}", file.getOriginalFilename(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
