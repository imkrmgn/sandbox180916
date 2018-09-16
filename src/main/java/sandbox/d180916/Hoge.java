package sandbox.d180916;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class Hoge {

    public String example() throws IOException {
        Properties prop = new Properties();
        prop.load(this.getClass().getResourceAsStream("hoge.properties"));
        return prop.getProperty("sample");
    }

    public void checkLoad() throws IOException {
        URL url1 = this.getClass().getResource("hoge.properties");
        System.out.println(url1);

        URL url2 = this.getClass().getClassLoader().getResource("sandbox/d180916/hoge.properties");
        System.out.println(url2);

        /*
         * これは差し替えられない
         */
        URL url3 = ClassLoader.getSystemResource("sandbox/d180916/hoge.properties");
        System.out.println(url3);
    }
}
