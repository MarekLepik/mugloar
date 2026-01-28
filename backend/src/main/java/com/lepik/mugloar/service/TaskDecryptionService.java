package com.lepik.mugloar.service;

import com.lepik.mugloar.client.dto.TaskResponse;
import com.lepik.mugloar.crypto.StringDecoder;
import com.lepik.mugloar.model.EncryptionType;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class TaskDecryptionService {

    public TaskResponse decrypt(TaskResponse task) {
        EncryptionType type = EncryptionType.fromCode(task.getEncrypted() == null ? 0 : task.getEncrypted());

        return switch (type) {
            case BASE64 -> decrypt(task, this::decodeBase64);
            case ROT13 -> decrypt(task, this::decodeRot13);
            default -> task;
        };
    }

    private TaskResponse decrypt(TaskResponse task, StringDecoder decoder) {
        TaskResponse decrypted = new TaskResponse();
        decrypted.setAdId(decoder.decode(task.getAdId()));
        decrypted.setMessage(decoder.decode(task.getMessage()));
        decrypted.setReward(decoder.decode(task.getReward()));
        decrypted.setProbability(decoder.decode(task.getProbability()));
        decrypted.setExpiresIn(task.getExpiresIn());
        decrypted.setEncrypted(EncryptionType.NONE.ordinal());
        return decrypted;
    }

    private String decodeBase64(String encoded) {
        if (encoded == null || encoded.isBlank()) {
            return encoded;
        }
        try {
            byte[] decoded = Base64.getDecoder().decode(encoded);
            return new String(decoded, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return encoded; // fail-safe
        }
    }

    private String decodeRot13(String encoded) {
        if (encoded == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(encoded.length());
        for (char c : encoded.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                sb.append((char) ((c - 'a' + 13) % 26 + 'a'));
            } else if (c >= 'A' && c <= 'Z') {
                sb.append((char) ((c - 'A' + 13) % 26 + 'A'));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}