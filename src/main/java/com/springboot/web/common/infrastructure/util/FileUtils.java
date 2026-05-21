package com.springboot.web.common.infrastructure.util;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

/**
 * Clase Utility para manejar operaciones relacionadas con archivos, como guardar y eliminar imágenes de productos.
 */
@Service
public class FileUtils {

    public String saveProductImage(MultipartFile file) {

        String uniqueFileName;

        try (InputStream inputStream = file.getInputStream()) {

            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

            uniqueFileName = UUID.randomUUID().toString().concat("-").concat(fileName);

            Path path = Path.of("uploads/products/");

            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            Files.copy(inputStream, path.resolve(uniqueFileName), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
        }
        return uniqueFileName;
    }

    public void deleteProductImage(String fileName) {
        try {
            Path base = Path.of("uploads/products/").toAbsolutePath();
            Path path = base.resolve(fileName);

            Files.deleteIfExists(path);

        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file " + fileName, e);
        }
    }

}
