import com.gb.Massive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestCutMassiveResult {

    private final int[] oldMassive;
    private final int[] newMassive;
    Massive massive;

    public TestCutMassiveResult(int[] oldMassive, int[] newMassive) {
        this.oldMassive = oldMassive;
        this.newMassive = newMassive;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new int[]{5, 6, 4, 1, 7, 4, 6}, new int[]{6}},
                {new int[]{1, 2, 4, 4, 2, 3, 4, 1, 7}, new int[]{1, 7}},
                {new int[]{1, 2,4,4, 2, 3, 1, 7}, new int[]{2,3,1,7}},
                {new int[]{5, 6, 4, 1, 7, 6}, new int[]{1, 7, 6}},
                {new int[]{5, 6, 4, 1, 7, 6, 4}, new int[]{}}
        });
    }


    @Before
    public void beforeMethod() {
        massive = new Massive();
    }

    @Test
    public void testCutMassiveResult() {
        Assert.assertArrayEquals(newMassive, massive.cutMassiveAfterLastFourth(oldMassive));
    }
}
