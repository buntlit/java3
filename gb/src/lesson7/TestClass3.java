package lesson7;

public class TestClass3 {
    @BeforeSuite
    public static void before(){
        System.out.println("Before anno3");
    }
    @Test(priority = 3)
    public static void test1(){
        System.out.println("test3 1");
    }
    @Test(priority = 3)
    public static void test4(){
        System.out.println("test3 4");
    }
}
