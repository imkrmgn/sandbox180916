package sandbox.d180916;

import java.io.File;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class TestClassLoader extends URLClassLoader {

    private static Map<String, URL> testResourceMap = new HashMap<>();

    /**
     * テストリソースのマッピングをクリアする
     */
    public static void clearTestResourceMap() {
        testResourceMap.clear();
    }

    /**
     * リソース名とテストリソースのマッピングを追加する
     * @param resourceName テストリソース名
     * @param testResourceURL テストリソースを読み込むためのURL
     */
    public static void addTestResourceMap(String resourceName, URL testResourceURL) {
        testResourceMap.put(resourceName, testResourceURL);
    }

    //--------------------------------------------------

    public TestClassLoader() {
        super(new URL[0]);
        this.addURLFromClasspath();
    }

    /**
     * CLASSPATHに設定されている全てのパスを、
     * 自インスタンスによる検索対象として登録(addURL)
     */
    private void addURLFromClasspath() {
        String classPath = System.getProperty("java.class.path");
        String[] paths = classPath.split(File.pathSeparator);
        for (String path : paths) {
            URI uri = Paths.get(path).toUri();
            URL url;
            try {
                url = uri.toURL();
            } catch (MalformedURLException e) {
                throw new UncheckedIOException(e);
            }
            this.addURL(url);
        }
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            /*
             * クラスローダーの実装の約束事として、
             * 先にparentのloadClassを呼び出べきだが、
             * テスト対象クラスをこのクラスローダーで読み込む必要があるため、
             * 先に自信でロードを試みる。
             */
            Class<?> c = null;
            if (isTestTarget(name)) {
                c = this.findLoadedClass(name);
                if (c == null) {
                    c = this.findClass(name);
                }
            }
            if (c == null) {
                c = this.getParent().loadClass(name);
            }
            if (resolve) {
                this.resolveClass(c);
            }
            return c;
        }
    }

    /**
     * 自インスタンスでのロード対象か判断。
     *
     * 【注意点】
     * java.*は、SecurityManagerによりブートストラップクラスローダ以外からはロードできない。
     * TestClassLoader自身はシステムクラスローダでロードされているのでロード対象から除外。
     * JUnit関連のクラスもシステムクラスローダでロードされているのでロード対象から除外。
     */
    private boolean isTestTarget(String name) {
        return name.startsWith("sandbox.")
                && !name.equals(this.getClass().getName());
    }

    /**
     * @param name リソース名
     * @return リソース名にテストリソースがマッピングされていたら、そのリソースを読み込むためのURL。
     *         マッピングされていない場合は、デフォルトの動作で得られたURL。
     */
    @Override
    public URL getResource(String name) {
        if (testResourceMap.containsKey(name)) {
            return testResourceMap.get(name);
        } else {
            return super.getResource(name);
        }
    }
}
