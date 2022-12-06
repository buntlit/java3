import com.gb.Massive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestCheckFoursAndOnes {

    private final int[] massiveForCheck;
    private final boolean resultOfCheck;
    Massive massive;

    public TestCheckFoursAndOnes(int[] massiveForCheck, boolean resultOfCheck) {
        this.massiveForCheck = massiveForCheck;
        this.resultOfCheck = resultOfCheck;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new int[]{1, 1, 1, 4, 4, 1, 4, 4,}, true},
                {new int[]{1, 1, 1, 1, 1, 1}, false},
                {new int[]{4, 4, 4, 4}, false},
                {new int[]{1, 4, 4, 1, 1, 4, 3}, false},
                {new int[]{4, 4, 4, 4, 4, 1, 4, 4, 4}, true}
        });
    }


    @Before
    public void beforeMethod() {
        massive = new Massive();
    }

    @Test
    public void testCheckFoursAndOnes() {
        Assert.assertEquals(resultOfCheck, massive.checkFoursAndOnes(massiveForCheck));
    }
}
