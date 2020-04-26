package htwb.ai.stevio;



import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/*
 *
 * @author Mario Teklic
 */public class Inspector {

     private final InputManager inputManager = new InputManager();

    /**
     * Analyzes all methods of the given class und searches for the given annotation
     * @param container all method names (and exception if any occur) will be stored in this object
     * @param classToAnalyse class which will be analysed
     * @param annotation annotation which will be analysed for
     */
    public void analyse(Container container, String classToAnalyse, Class annotation) throws ClassNotFoundException, IllegalAccessException {

        Class<?> clazz;
        clazz = InputManager.checkClassName(classToAnalyse);

        for (Method m : clazz.getDeclaredMethods()) {

            //Workaround: "mvn clean package" always adds a class called "$jacocoInit" to the lists when the tests are executed..
            if (!m.getName().equals("$jacocoInit")) {

                m.setAccessible(true);
                Annotation[] annotations = m.getAnnotations();

                if (annotations.length > 0) {
                    for (Annotation a : annotations) {
                        if (a.annotationType().equals(annotation)) {
                            try {
                                //Execute method with @runMe
                                Object obj = clazz.getDeclaredConstructor().newInstance();
                                m.invoke(obj);
                                container.getMethodsWithAnnotation().add(m.getName());
                            } catch (Exception e) {
                                //Execution failed
                                container.getMethodsWithAnnotation().add(m.getName());
                                container.getMethodsWithAnnotationAndError().put(m.getName(), e.getClass().getSimpleName());
                            }
                        }
                    }
                } else {
                    //No @runMe found
                    container.getMethodsWithoutAnnotation().add(m.getName());
                }
            }
        }
    }
}
