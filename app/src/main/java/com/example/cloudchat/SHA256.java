package com.example.cloudchat;

import java.math.BigInteger;
import java.security.MessageDigest;

/*
 * @param data to be encrypted
 * @param encrypt method, SH!-1, SHA-224, SHA-256, SHA-384, SHA-512
 * @return encryptSHA
 * @throws Exception
 * */

public class SHA256 {
    private static byte[] encryptSHA256(byte[] data) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        sha.update(data);
        return sha.digest();
    }

    public static String cryptPass(String pass) {
        byte[] inputData = pass.getBytes();
        byte[] outputData = new byte[0];
        try {
            outputData = encryptSHA256(inputData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        BigInteger shaData = new BigInteger(1, outputData);
        return shaData.toString();
    }
}
