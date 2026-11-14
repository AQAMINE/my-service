package com.myservice.application.service;

import com.myservice.domain.model.ExternalAccount;
import com.myservice.domain.ports.out.EncryptionServicePort;
import com.myservice.domain.ports.out.ExternalAccountRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalAccountServiceTest {

    @Mock
    private ExternalAccountRepositoryPort externalAccountRepositoryPort;

    @Mock
    private EncryptionServicePort encryptionServicePort;

    private ExternalAccountService service;

    @BeforeEach
    void setUp() {
        service = new ExternalAccountService(externalAccountRepositoryPort, encryptionServicePort);
    }

    @Test
    void createAccount_encryptsPasswordBeforeSave() {
        UUID userId = UUID.randomUUID();
        ExternalAccount account = ExternalAccount.builder()
                .userId(userId)
                .username("alice")
                .build();

        when(encryptionServicePort.encrypt("s3cret"))
                .thenReturn(new EncryptionServicePort.EncryptionResult("cipher-b64", "iv-b64"));
        when(externalAccountRepositoryPort.save(any(ExternalAccount.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ExternalAccount saved = service.createAccount(account, "s3cret");

        assertEquals("cipher-b64", saved.getEncryptedPassword());
        assertEquals("iv-b64", saved.getEncryptionIv());

        ArgumentCaptor<ExternalAccount> captor = ArgumentCaptor.forClass(ExternalAccount.class);
        verify(externalAccountRepositoryPort).save(captor.capture());
        assertEquals("cipher-b64", captor.getValue().getEncryptedPassword());
    }

    @Test
    void revealPassword_returnsDecryptedValueForOwner() {
        UUID accountId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        ExternalAccount account = ExternalAccount.builder()
                .id(accountId)
                .userId(ownerId)
                .encryptedPassword("cipher")
                .encryptionIv("iv")
                .build();

        when(externalAccountRepositoryPort.findById(accountId)).thenReturn(Optional.of(account));
        when(encryptionServicePort.decrypt("cipher", "iv")).thenReturn("plain-password");

        assertEquals("plain-password", service.revealPassword(accountId, ownerId));
    }

    @Test
    void revealPassword_rejectsNonOwner() {
        UUID accountId = UUID.randomUUID();
        ExternalAccount account = ExternalAccount.builder()
                .id(accountId)
                .userId(UUID.randomUUID())
                .encryptedPassword("cipher")
                .encryptionIv("iv")
                .build();

        when(externalAccountRepositoryPort.findById(accountId)).thenReturn(Optional.of(account));

        assertThrows(SecurityException.class,
                () -> service.revealPassword(accountId, UUID.randomUUID()));
    }

    @Test
    void getAccountByIdAndUserId_rejectsNonOwner() {
        UUID accountId = UUID.randomUUID();
        ExternalAccount account = ExternalAccount.builder()
                .id(accountId)
                .userId(UUID.randomUUID())
                .build();

        when(externalAccountRepositoryPort.findById(accountId)).thenReturn(Optional.of(account));

        assertThrows(SecurityException.class,
                () -> service.getAccountByIdAndUserId(accountId, UUID.randomUUID()));
    }
}
