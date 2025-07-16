package com.example.audio_api.controller;

import com.example.audio_api.service.AudioService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController @RequestMapping
public class AudioController {

    private final AudioService audioService;

    public AudioController(AudioService audioService) {
        this.audioService = audioService;
    }

    @PostMapping("/api/download")
    public ResponseEntity<String> downloadAudio(@RequestBody Map<String, String> request) {

        String audioURL = request.get("url");

        if (audioURL == null || audioURL.isBlank()) {
            return ResponseEntity.badRequest().body("URL is required!");
        }

        try {

            String outputFile = audioService.audioConversion(audioURL);
            return ResponseEntity.ok("api/audio/" + outputFile);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Conversion failed!");
        }

    }

    @GetMapping("/audio/{filename}")
    public ResponseEntity<Resource> streamAudio(@PathVariable String filename) throws IOException {
        return audioService.streamAudioFile(filename);
    }

}