package com.ftp.prueba.service;

import java.io.InputStream;

public interface FtpService {
    /**
     * Sube un archivo al servidor FTP.
     * 
     * @param filename Nombre del archivo en el servidor FTP.
     * @param fileStream InputStream del archivo que se subirá.
     * @return true si el archivo se sube correctamente, false en caso contrario.
     */
    boolean uploadFile(String filename, InputStream fileStream);
}
