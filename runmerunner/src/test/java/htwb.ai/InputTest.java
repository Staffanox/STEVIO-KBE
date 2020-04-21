package htwb.ai;

import htwb.ai.stevio.InputManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;



public class InputTest {

    private final InputManager inputManager = new InputManager();

    @Test
    public void nullInput() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            inputManager.checkValidInput(null);

        });


    }

    @Test
    public void illegalInputZero() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            inputManager.checkValidInput("");

        });
    }


    @Test
    public void validInput() {
        inputManager.checkValidInput("htwb.ai.stevio.Inspector");
    }


    @Test
    public void checkInvalidClassname() {
        Assertions.assertThrows(ClassNotFoundException.class, () -> {
            InputManager.checkClassName("lel");

        });
    }

    @Test
    public void checkValidClassname() throws ClassNotFoundException {
        InputManager.checkClassName("htwb.ai.stevio.Inspector");
    }

    @Test
    public void checkValidClassnameContainer() throws ClassNotFoundException {
        InputManager.checkClassName("htwb.ai.stevio.Container");
    }

    @Test
    public void checkWholeNullInput() {
        String testString[] = {null};
        Assertions.assertThrows(NullPointerException.class, () -> {
            inputManager.checkInput(testString);

        });
    }


    @Test
    public void checkWholeInvalidInput() {
        String testString[] = {"htwb.ai.stevio.Inspecto"};
        Assertions.assertThrows(ClassNotFoundException.class, () -> {
            inputManager.checkInput(testString);

        });
    }

    @Test
    public void checkWholeValidInput() throws ClassNotFoundException {
        String testString[] = {"htwb.ai.stevio.Inspector"};
        inputManager.checkInput(testString);
    }

    @Test
    public void checkWholeValidInputTwo() throws ClassNotFoundException {
        String testString[] = {"htwb.ai.stevio.Container"};
        inputManager.checkInput(testString);


    }
}
