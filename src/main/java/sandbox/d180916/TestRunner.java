package sandbox.d180916;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class TestRunner extends BlockJUnit4ClassRunner {

    private static TestClassLoader testClassLoader = new TestClassLoader();

    public TestRunner(Class<?> testClass) throws InitializationError, ClassNotFoundException {
        /*
         * ここでテストケースのクラス(HogeTest)を、
         * TestClassLoaderでロードしたクラスに差し替える。
         */
        super(testClassLoader.loadClass(testClass.getName()));
    }
}
