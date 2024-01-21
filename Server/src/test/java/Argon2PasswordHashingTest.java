import org.junit.jupiter.api.Test;
import org.warehouse.PasswordSecurity.Argon2PasswordHashing;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class Argon2PasswordHashingTest {

    @Test
    public void testPasswordHashingCorrect() {

        String password = "secretPassword12#";
        String hashedPassword = Argon2PasswordHashing.hashPassword(password);

        assertTrue(Argon2PasswordHashing.checkPassword(password, hashedPassword));
    }

    @Test
    public void testPasswordHashingIncorrect() {

        String password = "secretPassword12#";
        String hashedPassword = Argon2PasswordHashing.hashPassword(password);

        assertFalse(Argon2PasswordHashing.checkPassword("anotherPassword456", hashedPassword));
    }
}
