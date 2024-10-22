package com.frimac.lectoqr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.frimac.lectoqr.services.QRCodeService;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/qr")
public class QRCodeController {

    @Autowired
    private QRCodeService qrCodeService;

    @PostMapping("/decode")
    public ResponseEntity<String> decodeQRCode(@RequestParam("file") MultipartFile file) throws ChecksumException, FormatException {
        try {
            // Guardar el archivo temporalmente
            File tempFile = File.createTempFile("temp", file.getOriginalFilename());
            file.transferTo(tempFile);
            
            // Decodificar el código QR
            String decodedText = qrCodeService.decodeQRCode(tempFile);
            
            // Eliminar el archivo temporal
            tempFile.delete();
            
            return new ResponseEntity<>(decodedText, HttpStatus.OK);
        } catch (IOException | NotFoundException e) {
            return new ResponseEntity<>("Error decoding QR Code: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
