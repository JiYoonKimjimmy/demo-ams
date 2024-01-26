package study.trywithresources;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.Cleaner;

public class TryWithResourcesTest {

    @Test
    void tryWithResourcesTest() {
        /**
         * try-with-resources 블록은 Java 7 에 도입되었고,
         * 선언된 자원은 코드 블록 종료되는 시점에 자동으로 `close()` 함수 호출된다.
         */
        try (BufferedReader br = new BufferedReader(new FileReader("test.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void cleanerTest() {
        Cleaner cleaner = Cleaner.create();
        String str = "Hello World";
        cleaner.register(str, () -> System.out.println("Object is cleaned up"));
        System.out.println("str = " + str);
    }

}
