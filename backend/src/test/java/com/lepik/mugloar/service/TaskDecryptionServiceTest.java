package com.lepik.mugloar.service;

import com.lepik.mugloar.client.dto.TaskResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskDecryptionServiceTest {

    private TaskDecryptionService decryptionService;

    @BeforeEach
    void setUp() {
        decryptionService = new TaskDecryptionService();
    }

    @Test
    void decrypt_encrypted_one_encoding() {
        TaskResponse task = new TaskResponse();
        task.setAdId("TGE5OVR5eGc=");
        task.setMessage("SW52ZXN0aWdhdGUgRGV0bGVmIENocmlzdG9waGVycyBhbmQgZmluZCBvdXQgdGhlaXIgcmVsYXRpb24gdG8gdGhlIG1hZ2ljIGRvZy4=");
        task.setReward("133");
        task.setExpiresIn(3);
        task.setProbability("Umlza3k=");
        task.setEncrypted(1);

        TaskResponse decryptedTask = decryptionService.decrypt(task);
        assertEquals("La99Tyxg", decryptedTask.getAdId());
        assertEquals("Investigate Detlef Christophers and find out their relation to the magic dog.", decryptedTask.getMessage());
        assertEquals("Risky", decryptedTask.getProbability());
    }

    @Test
    void decrypt_encrypted_two_encoding() {
        TaskResponse task = new TaskResponse();
        task.setAdId("Yn99Glkt");
        task.setMessage("Vairfgvtngr Qrgyrs Puevfgbcuref naq svaq bhg gurve eryngvba gb gur zntvp qbt.");
        task.setReward("200");
        task.setExpiresIn(5);
        task.setProbability("Evfxl");
        task.setEncrypted(2);

        TaskResponse decryptedTask = decryptionService.decrypt(task);
        assertEquals("La99Tyxg", decryptedTask.getAdId());
        assertEquals("Investigate Detlef Christophers and find out their relation to the magic dog.", decryptedTask.getMessage());
        assertEquals("Risky", decryptedTask.getProbability());
    }
}