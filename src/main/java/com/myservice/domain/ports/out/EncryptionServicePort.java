package com.myservice.domain.ports.out;

public interface EncryptionServicePort {

    EncryptionResult encrypt(String plainText);

    String decrypt(String cipherTextBase64, String ivBase64);

    // Utilisation d'un record Java 21
    record EncryptionResult(String cipherTextBase64, String ivBase64) {}
}