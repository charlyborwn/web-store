package pti.test.server;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Syrotyuk R.
 */
public class MyPropertiesTest {

    @Test
    public void getInstance() {
        assertEquals("test_complete", MyProperties.getInstance().getProperty("test"));
    }

}