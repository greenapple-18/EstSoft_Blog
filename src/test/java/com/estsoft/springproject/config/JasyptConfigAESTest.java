package com.estsoft.springproject.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.salt.RandomSaltGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JasyptConfigAESTest {
    @Test
    void stringEncryptor() {
        String password = "abcd";

        System.out.println(jasyptEncoding(password));
    }

    public String jasyptEncoding(String value) {
        String key = "jasypt_key";
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        pbeEnc.setPassword(key);
        pbeEnc.setIvGenerator(new RandomIvGenerator());
        pbeEnc.setSaltGenerator(new RandomSaltGenerator());
        return pbeEnc.encrypt(value);
    }
}