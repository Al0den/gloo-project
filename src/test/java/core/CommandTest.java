package core;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import cli.Command;

class CommandTest {
    @Test
    void lambdaCommand() {
        String[][] capturedArgs = new String[1][];
        Command command = args -> capturedArgs[0] = args;

        command.execute(new String[] {"one", "two"});

        assertArrayEquals(new String[] {"one", "two"}, capturedArgs[0]);
    }
}
