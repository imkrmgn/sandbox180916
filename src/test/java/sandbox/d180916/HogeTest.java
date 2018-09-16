package sandbox.d180916;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(TestRunner.class)
public class HogeTest {

    @Before
    public void setUp() {
        TestClassLoader.clearTestResourceMap();
    }

    @Test
    public void testHoge00() throws IOException {
        Hoge hoge = new Hoge();
        String actual = hoge.example();

        assertEquals("test_resource", actual);
    }

    @Test
    public void testHoge01() throws IOException {
        TestClassLoader.addTestResourceMap(
                "sandbox/d180916/hoge.properties",
                Paths.get("testdata/hoge_test01.properties").toUri().toURL());

        Hoge hoge = new Hoge();
        String actual = hoge.example();

        assertEquals("test01", actual);
    }

    @Test
    public void testHoge02() throws IOException {
        TestClassLoader.addTestResourceMap(
                "sandbox/d180916/hoge.properties",
                Paths.get("testdata/hoge_test02.properties").toUri().toURL());

        Hoge hoge = new Hoge();
        String actual = hoge.example();

        assertEquals("test02", actual);
    }
}
