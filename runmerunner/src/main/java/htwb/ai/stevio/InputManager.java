package htwb.ai.stevio;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/*
 *
 * @author Mario Teklic
 */public class InputManager {


    public boolean checkInput(String[] args) throws ClassNotFoundException {
        checkValidInput(args[0]);
        checkClassName(args[0]);

        return true;
    }

    public static Class checkClassName(String className) throws ClassNotFoundException {

        Class clazz;
        try {
          clazz = Class.forName(className);
        } catch (ClassNotFoundException classNotFoundException) {
            throw new ClassNotFoundException("Class is invalid");
        }

        return clazz;
    }


    public boolean checkValidInput(String input) {

        //could be in one line since null is empty, however I'd like to be specific and not get a IllegalArgument when having a Nullpointer
        if (input == null)
            throw new NullPointerException("Classname has to be specified");
        else if (input.isEmpty())
            throw new IllegalArgumentException("Classname can't be empty, nor null.\n Please provide a valid Classname");
        else return true;


    }
}
