package com.sttis.controllers;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@Profile("dev") // <-- PENTING! Hanya aktif di profil development
public class SourceCodeController {

    @GetMapping("/download-source")
    public ResponseEntity<StreamingResponseBody> downloadSource() {

        // Path ke folder source code
        final Path sourceDir = Paths.get("src/main/java");

        StreamingResponseBody stream = outputStream -> {
            try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {
                Files.walk(sourceDir)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        try {
                            // Membuat struktur folder di dalam ZIP
                            ZipEntry zipEntry = new ZipEntry(sourceDir.relativize(path).toString());
                            zos.putNextEntry(zipEntry);
                            Files.copy(path, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            System.err.println("Gagal memproses file: " + path + " - " + e.getMessage());
                        }
                    });
            } catch (IOException e) {
                // Handle exception
                e.printStackTrace();
            }
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"java-source-code.zip\"")
                .body(stream);
    }
}