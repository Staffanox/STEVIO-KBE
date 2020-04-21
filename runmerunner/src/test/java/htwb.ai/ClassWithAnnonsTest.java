package htwb.ai;

import htwb.ai.stevio.ClassWithAnnons;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class ClassWithAnnonsTest {

    private ClassWithAnnons cwa = new ClassWithAnnons();


    @Test
    public void method1() {
        Assert.assertEquals(cwa.method1(), "method-1 with annon");

    }

    @Test
    public void method2() {
        Assert.assertEquals(cwa.method2(), "method-2 with annon");

    }

    @Test
    public void method3() {
        Assert.assertEquals(cwa.method3(), "method-3 with annon");

    }

    @Test
    public void method4() {
        Assert.assertEquals(cwa.method4(), "method-4 with annon");

    }

    @Test
    public void method5() {
        Assert.assertEquals(cwa.method5(), "method-5 without annon");

    }

    @Test
    public void method6() {
        Assert.assertEquals(cwa.method6(), "method-6 without annon");

    }

    @Test
    public void method7() {
        Assert.assertEquals(cwa.method7(), "method-7 without annon");

    }

    @Test
    public void method8() {
        Assert.assertEquals(cwa.method8(), "method-8 without annon");

    }


    @Test
    public void method9() {
        Assert.assertEquals(cwa.method9(""), "method-9, not invocable");

    }

}
