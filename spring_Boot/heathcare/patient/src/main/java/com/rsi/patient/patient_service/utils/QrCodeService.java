package com.rsi.patient.patient_service.utils;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.rsi.patient.patient_service.exception.QrCodeGenerationException;

@Service
public class QrCodeService {

    public String generateBase64(String content) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, 300, 300);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", stream);

            return Base64.getEncoder().encodeToString(stream.toByteArray());
        } catch (Exception e) {
            throw new QrCodeGenerationException("Failed to generate QR code");
        }
    }
}
