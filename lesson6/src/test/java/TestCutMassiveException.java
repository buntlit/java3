import com.gb.Massive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestCutMassiveException {

    private final int[] oldMassive;
    private final int[] newMassive;
    Massive massive;

    public TestCutMassiveException(int[] oldMassive, int[] newMassive) {
        this.oldMassive = oldMassive;
        this.newMassive = newMassive;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new int[]{5, 6, 1, 7, 6}, new int[]{6}},
                {new int[]{1, 2, 2, 3, 1, 7}, new int[]{1, 7}},
                {new int[]{1, 2, 2, 3, 1, 7}, new int[]{}},
                {new int[]{5, 6, 1, 7, 6}, new int[]{1, 7, 6}},
                {new int[]{5, 6, 1, 7, 6}, new int[]{}}
        });
    }


    @Before
    public void beforeMethod() {
        massive = new Massive();
    }


    @Test(expected = RuntimeException.class)
    public void testCutMassiveException() {
        Assert.assertArrayEquals(newMassive, massive.cutMassiveAfterLastFourth(oldMassive));
    }
}
