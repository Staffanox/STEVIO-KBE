package htwb.ai.stevio;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AppTest {
    
    @Test
    public void returnsHelloWorldShouldReturnHelloWorld() {
        assertEquals( App.returnsHelloWorld(), "Hello World!" );
    }
}
