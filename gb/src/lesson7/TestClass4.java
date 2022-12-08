package lesson7;

public class TestClass4 {
    @BeforeSuite
    public static void before(){
        System.out.println("Before anno1");
    }
    @Test(priority = 3)
    public static void test1(){
        System.out.println("test4 1");
    }
    @Test
    public static void test2(){
        System.out.println("test4 2");
    }
    @Test(priority = 7)
    public static void test3(){
        System.out.println("test4 3");
    }
    @Test(priority = 3)
    public static void test4(){
        System.out.println("test4 4");
    }
    @Test(priority = 4)
    public static void test5(){
        System.out.println("test4 5");
    }
    @Test
    public static void test6(){
        System.out.println("test4 6");
    }
    @Test(priority = 8)
    public static void test7(){
        System.out.println("test4 7");
    }
    @Test(priority = 10)
    public static void test8(){
        System.out.println("test4 8");
    }
    @Test(priority = 7)
    public static void test9(){
        System.out.println("test4 9");
    }
    @Test
    public static void test10(){
        System.out.println("test4 10");
    }
    @Test(priority = 2)
    public static void test11(){
        System.out.println("test4 11");
    }
    @Test(priority = 5)
    public static void test12(){
        System.out.println("test4 12");
    }
    @AfterSuite
    public static void after(){
        System.out.println("After anno1");
    }
}
