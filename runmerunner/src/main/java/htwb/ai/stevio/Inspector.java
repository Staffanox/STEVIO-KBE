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
            m.setAccessible(true);
            Annotation[] annotations = m.getAnnotations();

            if(annotations.length > 0){
                for (Annotation a : annotations) {
                    if (a.annotationType().equals(annotation)) {
                        try {
                            Object obj = clazz.getDeclaredConstructor().newInstance();
                            m.invoke(obj);
                            container.getMethodsWithAnnotation().add(m.getName());

                        }
                        catch(IllegalAccessException e){
                            container.getMethodsWithAnnotation().add(m.getName());
                            container.getMethodsWithAnnotationAndError().put(m.getName(), e.getClass().getSimpleName());
                            throw new IllegalAccessException("Error : Can't access this class");
                        }
                        catch (Exception e) {
                            container.getMethodsWithAnnotation().add(m.getName());
                            container.getMethodsWithAnnotationAndError().put(m.getName(), e.getClass().getSimpleName());
                        }
                    }
                }
            } else {
                container.getMethodsWithoutAnnotation().add(m.getName());
            }
        }
    }
}
