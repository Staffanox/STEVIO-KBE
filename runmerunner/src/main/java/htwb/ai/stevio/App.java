package htwb.ai.stevio;

/*
 *
 * @author Mario Teklic
 */public class App {

    public static void main(String[] args) {

        InputManager inputManager = new InputManager();

        //Check userinput. Exit program if input was wrong.
        if (!inputManager.checkInput(args)) {
            System.exit(1);
        }

        String classToInspect = args[0];
        Class runMeClass = RunMe.class;

        Inspector inspector = new Inspector();
        Container container = new Container(classToInspect);

        inspector.analyse(container, classToInspect, runMeClass);
        container.sort();
        container.print();
    }
}
