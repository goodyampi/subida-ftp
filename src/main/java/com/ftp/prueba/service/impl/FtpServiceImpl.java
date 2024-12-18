package com.ftp.prueba.service.impl;

import java.io.InputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ftp.prueba.config.FtpProperties;
import com.ftp.prueba.service.FtpService;

@Service
public class FtpServiceImpl implements FtpService {
    private static final Logger logger = LoggerFactory.getLogger(FtpServiceImpl.class);

    @Autowired
    private FtpProperties ftpProperties;

    @Override
    public boolean uploadFile(String filename, InputStream fileStream) {
        FTPClient ftpClient = new FTPClient();
        try {
            logger.info("Conectando al servidor FTP: {}:{}", ftpProperties.getServer(),
                    ftpProperties.getPort());
            ftpClient.connect(ftpProperties.getServer(), ftpProperties.getPort());
            ftpClient.login(ftpProperties.getUser(), ftpProperties.getPassword());
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            String remotePath = ftpProperties.getBaseDirectory() + "/" + filename;
            logger.info("Subiendo archivo a: {}", remotePath);

            boolean done = ftpClient.storeFile(remotePath, fileStream);
            fileStream.close();

            if (done) {
                logger.info("Archivo subido exitosamente: {}", filename);
            } else {
                logger.warn("No se pudo subir el archivo: {}", filename);
            }

            ftpClient.logout();
            return done;

        } catch (Exception ex) {
            logger.error("Error al subir archivo al servidor FTP", ex);
            return false;
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                    logger.info("Desconectado del servidor FTP.");
                }
            } catch (Exception e) {
                logger.error("Error al desconectar del servidor FTP", e);
            }
        }
    }

}
