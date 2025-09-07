package dev.itinajero.app.demo;

// Importa clases para HMAC y codificación Base64URL
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class JwtManual {

    public static void main(String[] args) throws Exception {
        // Tiempo de espera entre pasos (en segundos)
        int sleepSeconds = 10;

        // Clave secreta utilizada para la firma HMAC-SHA256
        String secret = "mi_clave_secreta_para_jwt_2025_12345678901234567890123456789012";
        log("[Paso 1] Clave secreta: " + secret);
        Thread.sleep(sleepSeconds * 1000);

        // Header JWT en formato JSON (tipo y algoritmo)
        String headerJson = "{\"typ\":\"JWT\",\"alg\":\"HS256\"}";
        log("[Paso 2] Header JSON: " + headerJson);
        Thread.sleep(sleepSeconds * 1000);

        // Payload JWT en formato JSON (claims personalizados)
        String payloadJson = "{\"sub\":\"itinajero\",\"iat\":1757273839,\"exp\":1757277439,\"idSucursal\":1,\"sucursal\":\"Sucursal Centro\",\"perfiles\":[\"ADMIN\"],\"id\":1,\"nombreCompleto\":\"Iván Eliseo Tinajero Díaz\",\"email\":\"ivanetinajero@gmail.com\"}";
        log("[Paso 3] Payload JSON: " + payloadJson);
        Thread.sleep(sleepSeconds * 1000);

        // Codifica el header y el payload en Base64URL (sin padding)
        String headerBase64Url = base64UrlEncode(headerJson.getBytes());
        log("[Paso 4] Header Base64URL: " + headerBase64Url);
        Thread.sleep(sleepSeconds * 1000);

        String payloadBase64Url = base64UrlEncode(payloadJson.getBytes());
        log("[Paso 5] Payload Base64URL: " + payloadBase64Url);
        Thread.sleep(sleepSeconds * 1000);

        // Une header y payload con un punto, formando el mensaje a firmar
        String mensaje = headerBase64Url + "." + payloadBase64Url;
        log("[Paso 6] Mensaje a firmar (header.payload): " + mensaje);
        Thread.sleep(sleepSeconds * 1000);

        // Inicializa el algoritmo HMAC-SHA256 con la clave secreta
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        mac.init(keySpec);
        log("[Paso 7] Inicializando HMAC-SHA256: Listo");
        Thread.sleep(sleepSeconds * 1000);

        // Calcula la firma HMAC del mensaje (header.payload)
        byte[] hmac = mac.doFinal(mensaje.getBytes());
        StringBuilder hmacHex = new StringBuilder();
        for (byte b : hmac) {
            hmacHex.append(String.format("%02x", b));
        }
        log("[Paso 8] Firma HMAC-SHA256 (hex): " + hmacHex.toString());
        Thread.sleep(sleepSeconds * 1000);

        // Codifica la firma en Base64URL (sin padding)
        String firmaBase64Url = base64UrlEncode(hmac);
        log("[Paso 9] Firma Base64URL: " + firmaBase64Url);
        Thread.sleep(sleepSeconds * 1000);

        // Construye el JWT completo: header.payload.signature
        String jwt = mensaje + "." + firmaBase64Url;
        log("[Paso 10] JWT final generado: " + jwt);
    }

    // Método auxiliar para codificar en Base64URL sin padding
    public static String base64UrlEncode(byte[] input) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(input);
    }

    // Método para imprimir logs con fecha y hora (formato yyyy-MM-dd HH:mm)
    public static void log(String resultado) {
        String fecha = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        System.out.printf("[%s] %s\n", fecha, resultado);
    }

}