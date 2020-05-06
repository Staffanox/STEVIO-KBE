package htwb.ai.stevio;

import htwb.ai.RunMe;

/*
 *
 * @author Mario Teklic
 */public class ClassWithAnnons {

    @RunMe
    public String method1() {
        return "method-1 with annon";
    }

    @RunMe
    public String method2() {
        return "method-2 with annon";
    }

    @RunMe
    public String method3() {
        return ("method-3 with annon");
    }

    @RunMe
    public String method4() {
        return ("method-4 with annon");
    }

    public String method6() {
        return ("method-6 without annon");
    }

    public String method5() {
        return ("method-5 without annon");
    }

    public String method7() {
        return ("method-7 without annon");
    }

    public String method8() {
        return ("method-8 without annon");
    }

    @RunMe
    public String method9(String string) {
        return ("method-9, not invocable");
    }

    @RunMe
    private void method10(String string) {
        System.out.println("not invoceable method");
    }
}
