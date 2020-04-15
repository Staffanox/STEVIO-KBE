package htwb.ai;

import htwb.ai.stevio.Container;
import htwb.ai.stevio.Inspector;
import htwb.ai.stevio.RunMe;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

/*
 *
 * @author Mario Teklic
 */public class InspectorTest {

    private final String CLASSNAME = "htwb.ai.stevio.ClassWithAnnons";
    private Inspector inspector;
    private Container container;

    @BeforeEach
    public void doBefore(){
         inspector = new Inspector();
         container = new Container(CLASSNAME);

         inspector.analyse(container, CLASSNAME, RunMe.class);
         container.sort();
    }

    @Test
    public void expectedMethodsWithAnnonList() {

        List<String> expected = new ArrayList<>();
        expected.add("method1");
        expected.add("method2");
        expected.add("method3");
        expected.add("method4");

        for (int i = 0; i < container.getMethodsWithAnnotation().size(); i++) {
            Assert.assertEquals(expected.get(i), container.getMethodsWithAnnotation().get(i));
        }
    }

     @Test
     public void expectedMethodsWithoutAnnonList() {

          List<String> expected = new ArrayList<>();
          expected.add("method5");
          expected.add("method6");
          expected.add("method7");
          expected.add("method8");

          for (int i = 0; i < container.getMethodsWithoutAnnotation().size(); i++) {
               Assert.assertEquals(expected.get(i), container.getMethodsWithoutAnnotation().get(i));
          }

          container.print();
     }

    @Test
    public void expectedMethodsWithAnnonAndErrorList() {

        List<String> expected = new ArrayList<>();
        expected.add("method9");

        Assert.assertEquals(1, container.getMethodsWithAnnotationAndError().size());
        Assert.assertEquals("IllegalArgumentException", container.getMethodsWithAnnotationAndError().get("method9"));

    }

    @Test
    public void analyseShouldThrowClassNotFound() {
        try {
            inspector.analyse(container, "blub", RunMe.class);
            fail();
        } catch (Exception e){
            // expection was thrown as expected.
        }
    }
}
