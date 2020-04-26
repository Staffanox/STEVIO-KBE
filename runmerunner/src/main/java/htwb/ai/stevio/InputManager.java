package htwb.ai.stevio;

/*
 *
 * @author Steven Schuette
 */public class InputManager {


    public void checkInput(String[] args) throws ClassNotFoundException {
        if (args.length != 1)
            throw new IllegalArgumentException("Error : Input has to be given");
        else {
            checkValidInput(args[0]);
            checkClassName(args[0]);
        }
    }

    public static Class checkClassName(String className) throws ClassNotFoundException {

        Class clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException classNotFoundException) {
            throw new ClassNotFoundException("Error : Class could not be found");
        }

        return clazz;
    }


    public void checkValidInput(String input) {

        //could be in one line since null is empty, however I'd like to be specific and not get a IllegalArgument when having a Nullpointer
        if (input == null)
            throw new NullPointerException("Error : Classname can't be null");
        else if (input.isEmpty())
            throw new IllegalArgumentException("Error : Please provide an input");


    }
}
