package com.example.audio_api.service;

import jakarta.annotation.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class AudioService {

    private static final String AUDIO_DIR = "audio/";

    public String audioConversion(String audioURL) throws IOException, InterruptedException {

        new File(AUDIO_DIR).mkdirs();
        String outputFileName = System.currentTimeMillis() + ".mp3";
        String outputFilePath = AUDIO_DIR + outputFileName;

        String pyScript = "scripts/audio_converter.py";

        ProcessBuilder pb = new ProcessBuilder("python3", pyScript, audioURL, outputFilePath);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("[PYTHON]" + line);
        }

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("Python process failed!");
        }

        return outputFileName;

    }

    public ResponseEntity<Resource> streamAudioFile(String fileName ) throws IOException {

        File file = new File(AUDIO_DIR + fileName);

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .contentLength(file.length())
                .body(resource);

    }

}