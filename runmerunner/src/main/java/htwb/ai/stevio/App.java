package htwb.ai.stevio;

import htwb.ai.RunMe;

/*
 *
 * @author Mario Teklic
 */public class App {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException {

        InputManager inputManager = new InputManager();

        //Best case no exception, but better than sys.exit(1)
        try {
            inputManager.checkInput(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String classToInspect = args[0];
        Class runMeClass = RunMe.class;

        Inspector inspector = new Inspector();
        Container container = new Container(classToInspect);

        inspector.analyse(container, classToInspect, runMeClass);
        container.sort();
        container.print();
        //Check userinput. Exit program if input was wrong.

    }
}
