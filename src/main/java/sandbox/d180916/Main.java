package sandbox.d180916;

import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {
        Hoge hoge = new Hoge();
        System.out.printf("hoge.example -> %s%n", hoge.example() );

        System.out.println("---check load : normal---");
        hoge.checkLoad();

        TestClassLoader.addTestResourceMap(
                "sandbox/d180916/hoge.properties",
                Paths.get("testdata/hoge_test01.properties").toUri().toURL());
        try ( TestClassLoader cl = new TestClassLoader() ) {
            Class<?> c = cl.loadClass("sandbox.d180916.Hoge");
            Object hoge2 = c.newInstance();

            System.out.println("---check load : TestClassLoader---");
            c.getMethod("checkLoad").invoke(hoge2);
        }
    }
}
