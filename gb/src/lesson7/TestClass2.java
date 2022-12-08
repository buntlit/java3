package lesson7;

public class TestClass2 {
    public static void before(){
        System.out.println("Before anno2");
    }
    @Test(priority = 3)
    public static void test1(){
        System.out.println("test2 1");
    }
    @Test
    public static void test2(){
        System.out.println("test2 2");
    }
    @AfterSuite
    public static void after(){
        System.out.println("After anno2");
    }
}
