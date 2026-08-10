package com.myservice.domain.ports.out;

public interface EncryptionServicePort {
    EncryptionResult encrypt(String rawPassword);
    String decrypt(String encryptedPassword, String iv);

    record EncryptionResult(String encryptedPassword, String iv) {}
}
