package users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ManagerTest {
    @Test
    void managerLogin() {
        Manager manager = new Manager("manager1", "Claire", "Boss", "admin");

        assertEquals("Manager", manager.getRole());
        assertTrue(manager.checkPassword("admin"));
    }
}
