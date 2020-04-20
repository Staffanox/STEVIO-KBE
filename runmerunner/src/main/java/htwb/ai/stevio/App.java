package htwb.ai.stevio;

/*
 *
 * @author Mario Teklic
 */public class App {

    public static void main(String[] args) throws ClassNotFoundException {

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
        System.out.println(container.getMethodsWithAnnotation());
        container.sort();
        container.print();
        //Check userinput. Exit program if input was wrong.

    }
}
