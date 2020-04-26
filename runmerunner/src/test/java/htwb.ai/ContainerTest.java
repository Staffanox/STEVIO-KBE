package htwb.ai;

import htwb.ai.stevio.Container;
import htwb.ai.stevio.Inspector;
import htwb.ai.stevio.RunMe;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/*
 *
 * @author Mario Teklic
 */public class ContainerTest {
    private final String CLASSNAME = "htwb.ai.stevio.ClassWithAnnons";
    private Inspector inspector;
    private Container container;

    @BeforeEach
    public void beforeEachTest() throws ClassNotFoundException, IllegalAccessException {
        inspector = new Inspector();
        container = new Container(CLASSNAME);
        inspector.analyse(container, CLASSNAME, RunMe.class);

    }

    @Test
    public void ListWithAnnonShouldBeSortedInAlphabeticalOrder() {
        //todo Tests sind hardcoded, eine Methode mehr oder weniger macht sie kaputt

        List<String> tmp = new ArrayList<>(container.getMethodsWithAnnotation());

        container.sort();
        Assert.assertNotEquals(tmp, container.getMethodsWithAnnotation());

        List<String> expected = new ArrayList<>();
        expected.add("method1");
        expected.add("method10");
        expected.add("method2");
        expected.add("method3");
        expected.add("method4");
        expected.add("method9");
        for (int i = 0; i < container.getMethodsWithAnnotation().size(); i++) {
            Assert.assertEquals(expected.get(i), container.getMethodsWithAnnotation().get(i));
        }

        container.print();
    }

    @Test
    public void ListWithoutAnnonShouldBeSortedInAlphabeticalOrder() {
        //todo Tests sind hardcoded, eine Methode mehr oder weniger macht sie kaputt

        List<String> tmp = new ArrayList<>(container.getMethodsWithoutAnnotation());
        container.sort();

        //Methoden sind in diesem Fall schon sortiert
        //Assert.assertNotEquals(tmp, container.getMethodsWithoutAnnotation());

        List<String> expected = new ArrayList<>();
        expected.add("method5");
        expected.add("method6");
        expected.add("method7");
        expected.add("method8");

        for (int i = 0; i < container.getMethodsWithoutAnnotation().size(); i++) {
            Assert.assertEquals(expected.get(i), container.getMethodsWithoutAnnotation().get(i));
        }
    }

    @Test
    public void notInvocableMethods() {
        //todo Tests sind hardcoded, eine Methode mehr oder weniger macht sie kaputt

        List<String> expected = new ArrayList<>();
        expected.add("method10");
        expected.add("method9");

        for(String key : container.getMethodsWithAnnotationAndError().keySet()){
            Assert.assertTrue(expected.contains(key));
        }
        Assert.assertEquals(expected.size(), container.getMethodsWithAnnotationAndError().keySet().size());
    }


}
