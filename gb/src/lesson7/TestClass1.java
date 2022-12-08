package lesson7;

public class TestClass1 {
    @BeforeSuite
    public static void before(){
        System.out.println("Before anno1");
    }
    @Test(priority = 3)
    public static void test1(){
        System.out.println("test1 1");
    }
    @Test
    public static void test2(){
        System.out.println("test1 2");
    }
    @Test(priority = 7)
    public static void test3(){
        System.out.println("test1 3");
    }
    @Test(priority = 3)
    public static void test4(){
        System.out.println("test1 4");
    }
    @AfterSuite
    public static void after(){
        System.out.println("After anno1");
    }
}
