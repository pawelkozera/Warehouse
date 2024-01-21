package org.warehouse.PasswordSecurity;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import java.security.SecureRandom;
import java.util.Arrays;

public class Argon2PasswordHashing {

    private static final int MEMORY = 15*1024;
    private static final int ITERATIONS = 2;
    private static final int PARALLELISM = 1;
    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 64;

    public static String hashPassword(String password) {
        Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(SALT_LENGTH,HASH_LENGTH,PARALLELISM,MEMORY,ITERATIONS);
        var encodedPassword = encoder.encode(password);

        return encodedPassword;
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(SALT_LENGTH,HASH_LENGTH,PARALLELISM,MEMORY,ITERATIONS);
        return encoder.matches(password, hashedPassword);
    }
}
