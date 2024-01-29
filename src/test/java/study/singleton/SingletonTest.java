package study.singleton;

import org.junit.jupiter.api.Test;

public class SingletonTest {

    @Test
    void nonSingletonInstanceTest() {

        /**
         * 일반 객체 생성은 객체 참조값은 계속 변경된다.
         */
        for (int i = 0; i < 10; i++) {
            NonSingleton nonSingleton = new NonSingleton(i);
            nonSingleton.print(i);
        }

    }

    @Test
    void singletonInstanceTest() {

        /**
         * Singleton 패턴의 객체 생성은 객체 참조값은 변하지 않는다.
         */
        for (int i = 0; i < 10; i++) {
            Singleton instance = Singleton.getInstance(i);
            instance.print(i);
        }

    }

}
