package users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UserTest {
    @Test
    void checkPasswordAcceptsOnlyCorrectPassword() {
        User user = new User("jdoe", "John", "Doe", "secret");

        assertTrue(user.checkPassword("secret"));
        assertFalse(user.checkPassword("wrong"));
    }

    @Test
    void baseUserHasNoRole() {
        User user = new User("jdoe", "John", "Doe", "secret");

        assertEquals("User", user.getRole());
    }
}
