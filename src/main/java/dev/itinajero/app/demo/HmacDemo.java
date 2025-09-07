package dev.itinajero.app.demo;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class HmacDemo {

    public static void main(String[] args) throws Exception {
        
        String SECRET_KEY_HS256 = "mi_clave_secreta_para_jwt_2025_12345678901234567890123456789012"; // 32+ chars para HS256
        
        String mensaje = "texto_a_firmar";

        // Algoritmo puede ser "HmacSHA256" o "HmacSHA512"
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY_HS256.getBytes(), "HmacSHA256");
        mac.init(keySpec);

        byte[] hmac = mac.doFinal(mensaje.getBytes());
        String firmaBase64 = Base64.getEncoder().encodeToString(hmac);

        System.out.println("Firma HMAC-SHA256 (Base64): " + firmaBase64);

    }

}