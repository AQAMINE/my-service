package com.myservice.infrastructure.adapters.out.grpc;

import com.myservice.domain.ports.out.EncryptionServicePort;
// Imports issus du code généré par Protobuf
import com.myservice.infrastructure.adapters.out.grpc.proto.CryptoServiceGrpc;
import com.myservice.infrastructure.adapters.out.grpc.proto.DecryptRequest;
import com.myservice.infrastructure.adapters.out.grpc.proto.EncryptRequest;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class EncryptionGrpcAdapter implements EncryptionServicePort {

    @GrpcClient("crypto-service")
    private CryptoServiceGrpc.CryptoServiceBlockingStub cryptoStub;

    @Override
    public EncryptionResult encrypt(String plainText) {
        EncryptRequest request = EncryptRequest.newBuilder()
                .setPlainText(plainText)
                .build();

        var response = cryptoStub.encrypt(request);

        return new EncryptionResult(
                response.getCipherTextBase64(),
                response.getIvBase64()
        );
    }

    @Override
    public String decrypt(String cipherTextBase64, String ivBase64) {
        DecryptRequest request = DecryptRequest.newBuilder()
                .setCipherTextBase64(cipherTextBase64)
                .setIvBase64(ivBase64)
                .build();

        var response = cryptoStub.decrypt(request);

        return response.getPlainText();
    }
}