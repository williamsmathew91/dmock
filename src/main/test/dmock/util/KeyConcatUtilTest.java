package dmock.util;

import dmock.util.KeyConcatUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KeyConcatUtilTest {
    private KeyConcatUtil keyConcatUtil;

    @Before
    public void setUp() {
        keyConcatUtil = new KeyConcatUtil();
    }

    @Test
    public void shouldConcatPathAndMethodWithColon() {
        final String path = "somePath";
        final String method = "someMethod";

        String expectedString = path + KeyConcatUtil.DELIMITER + method;

        final String actualString = keyConcatUtil.concat(path, method);

        assertEquals(expectedString, actualString);
    }

}
