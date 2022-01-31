package com.example.choiceitsamsungschool;


import androidx.annotation.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class SHAGenerator {

    public static String get_SHA_1_SecurePassword(
            String passwordToHash,
            String salt
    ) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16)
                        .substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return generatedPassword;
    }

    public static String get_SHA_256_SecurePassword(
            String passwordToHash,
            String salt
    ) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16)
                        .substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return generatedPassword;
    }

    public static String get_SHA_384_SecurePassword(
            String passwordToHash,
            String salt
    ) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-384");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16)
                        .substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return generatedPassword;
    }

    public static String get_SHA_512_SecurePassword(
            String passwordToHash,
            String salt
    ) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16)
                        .substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return generatedPassword;
    }

    // Add salt
    private static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return Arrays.toString(salt);
    }

    public static boolean check(
            @NonNull String originalPassword,
            @NonNull String hashPassword,
            @NonNull String salt,
            @NonNull String algorithm
    ) {
        String hashOriginalPassword;
        switch (algorithm) {
            case "SHA-1":
                hashOriginalPassword = get_SHA_1_SecurePassword(originalPassword, salt);
                return hashOriginalPassword.equals(hashPassword);
            case "SHA-256":
                hashOriginalPassword = get_SHA_256_SecurePassword(originalPassword, salt);
                return hashOriginalPassword.equals(hashPassword);
            case "SHA-384":
                hashOriginalPassword = get_SHA_384_SecurePassword(originalPassword, salt);
                return hashOriginalPassword.equals(hashPassword);
            case "SHA-512":
                hashOriginalPassword = get_SHA_512_SecurePassword(originalPassword, salt);
                return hashOriginalPassword.equals(hashPassword);
            default:
                return false;
        }
    }
}