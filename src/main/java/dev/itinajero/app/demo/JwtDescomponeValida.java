package dev.itinajero.app.demo;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class JwtDescomponeValida {

    public static void main(String[] args) throws Exception {
        // Tiempo de espera entre pasos (en segundos)
        int sleepSeconds = 10;

        // Clave secreta utilizada para la firma HMAC-SHA256
        String secret = "mi_clave_secreta_para_jwt_2025_12345678901234567890123456789012";

        String jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpdGluYWplcm8iLCJpYXQiOjE3NTcyNzM4MzksImV4cCI6MTc1NzI3NzQzOSwiaWRTdWN1cnNhbCI6MSwic3VjdXJzYWwiOiJTdWN1cnNhbCBDZW50cm8iLCJwZXJmaWxlcyI6WyJBRE1JTiJdLCJpZCI6MSwibm9tYnJlQ29tcGxldG8iOiJJdsOhbiBFbGlzZW8gVGluYWplcm8gRMOtYXoiLCJlbWFpbCI6Iml2YW5ldGluYWplcm9AZ21haWwuY29tIn0.ujQCVj9m-J4Q3vCKpVnaK8TUNkN92_WB7ihNBXxXSMk";
        log("[Paso 1] JWT recibido: " + jwt);
        Thread.sleep(sleepSeconds * 1000);

        String[] partes = jwt.split("\\.");
        String headerB64 = partes[0];
        String payloadB64 = partes[1];
        String signatureB64 = partes[2];
        log("[Paso 2] Partes separadas: header, payload, signature");
        log("Header Base64URL: " + headerB64);
        log("Payload Base64URL: " + payloadB64);
        log("Signature Base64URL: " + signatureB64);
        Thread.sleep(sleepSeconds * 1000);

        String headerJson = base64UrlDecode(headerB64);
        log("[Paso 3] Header decodificado: " + headerJson);
        Thread.sleep(sleepSeconds * 1000);

        String payloadJson = base64UrlDecode(payloadB64);
        log("[Paso 4] Payload decodificado: " + payloadJson);
        Thread.sleep(sleepSeconds * 1000);

        String mensaje = headerB64 + "." + payloadB64;
        log("[Paso 5] Mensaje a validar (header.payload): " + mensaje);
        Thread.sleep(sleepSeconds * 1000);

        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        mac.init(keySpec);
        log("[Paso 6] Inicializando HMAC-SHA256 con la clave secreta...");
        Thread.sleep(sleepSeconds * 1000);

        byte[] hmac = mac.doFinal(mensaje.getBytes());
        String firmaBase64Url = Base64.getUrlEncoder().withoutPadding().encodeToString(hmac);
        log("[Paso 7] Firma calculada Base64URL: " + firmaBase64Url);
        Thread.sleep(sleepSeconds * 1000);

        boolean valida = firmaBase64Url.equals(signatureB64);
        log("[Paso 8] ¿La firma es válida?: " + (valida ? "SÍ" : "NO"));
    }

    // Método auxiliar para imprimir logs    // Método auxiliar para decodificar Base64URL
    public static String base64UrlDecode(String input) {
        return new String(Base64.getUrlDecoder().decode(input));
    }

    // Método para imprimir logs con fecha y hora (formato yyyy-MM-dd HH:mm)
    public static void log(String resultado) {
        String fecha = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.printf("[%s] %s\n", fecha, resultado);
    }

}
