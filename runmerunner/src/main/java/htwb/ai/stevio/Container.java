package htwb.ai.stevio;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/*
 * @author Mario Teklic
 */public class Container { private String analysedClass;
    private List<String> methodsWithAnnotation;
    private List<String> methodsWithoutAnnotation ;
    private Map<String, String> methodsWithAnnotationAndError;

    public Container(String analysedClass){
        this.analysedClass = analysedClass;
        methodsWithAnnotation= new ArrayList<>();
        methodsWithoutAnnotation= new ArrayList<>();
        methodsWithAnnotationAndError= new HashMap<>();
    }

    public List<String> getMethodsWithAnnotation() {
        return methodsWithAnnotation;
    }

    public List<String> getMethodsWithoutAnnotation() {
        return methodsWithoutAnnotation;
    }

    public Map<String, String> getMethodsWithAnnotationAndError() {
        return methodsWithAnnotationAndError;
    }

    /**
     *  Sorts all lists alphabetically.
     */
    public void sort(){
        this.methodsWithAnnotation = this.methodsWithAnnotation
                .stream()
                .sorted()
                .collect(Collectors.toList());

        this.methodsWithoutAnnotation = this.methodsWithoutAnnotation
                .stream()
                .sorted()
                .collect(Collectors.toList());

        this.methodsWithAnnotationAndError = this.methodsWithAnnotationAndError
                .entrySet()
                .stream()
                .sorted()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldVal, newVal) -> oldVal, LinkedHashMap::new));
    }

    /**
     * Prints all lists/hashmaps which are hold in the container object
     */
    public void print() {
        PrintWriter pw = new PrintWriter(new PrintStream(System.out));

        pw.write("Analysed class '" + this.analysedClass + "':\n");

        pw.write("   Methods with @RunMe:\n");
        for(String s : this.methodsWithAnnotation){
            pw.write("     " + s + "\n");
        }

        pw.write("   Methods without @RunMe:\n");
        for(String s : this.methodsWithoutAnnotation){
            pw.write("     " + s + "\n");
        }

        pw.write("   Not invoceable:\n");
        for(Map.Entry<String, String> entry : this.methodsWithAnnotationAndError.entrySet()){
            pw.write("     " + entry.getKey() + ": " + entry.getValue() + "\n");
        }

        pw.flush();
    }
}
