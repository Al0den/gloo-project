package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class CommandInfoTest {
    @Test
    void commandInfoStoresCommandMetadata() {
        Command command = args -> { };

        CommandInfo info = new CommandInfo("description", command, "Manager", 2, "command <a> <b>");

        assertEquals("description", info.getDescription());
        assertSame(command, info.getAction());
        assertEquals("Manager", info.getRequiredRole());
        assertEquals(2, info.getMinArgs());
        assertEquals("command <a> <b>", info.getUsage());
    }
}
