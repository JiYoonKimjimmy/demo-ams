package study.singleton;

import java.io.Serial;
import java.io.Serializable;

public class Singleton implements Serializable {

    /**
     * LazyHolder Inner Class 를 활용한 Singleton 패턴 구현
     */
//    public static Singleton getInstance() {
//        return LazyHolder.INSTANCE;
//    }
//
//    private static class LazyHolder {
//        private static final Singleton INSTANCE = new Singleton();
//    }
//
//    public void print(int i) {
//        System.out.println(i + " : " + this);
//    }

    /**
     * `synchronized` & `volatile` 키워드를 활용한 Singleton 패턴 구현
     * - `volatile` 주의 사항
     *   - `volatile` 키워드는 변수 선언함과 동시에 다른 스레드에 즉시 공개된다.
     *   - `volatile` 키워드는 메인 메모리와 CPU 캐시 간의 동기화를 강제하여 모든 스레드가 항상 최신 정보를 읽도록 한다.
     *   - 하지만, 동기화 작업때문에 추가적인 메모리 접근을 필요로 하면서 성능 저하가 발생할 수 있다.
     *   - `volatile` 키워드 성능 이슈를 막기 위해서는 적절한 변수에 사용하는 것과
     *   - `synchronized` 블록으로 변수에 대한 스레드 접근을 동기화한다.
     */
    private static volatile Singleton INSTANCE = null;
    private static int VALUE = 0;

    public static Singleton getInstance(int value) {
        if (INSTANCE == null) {
            synchronized (Singleton.class) {
                INSTANCE = new Singleton();
                VALUE = value * 10;
            }
        }
        return INSTANCE;
    }

    /**
     * `readResolve()`: 역직렬화 과정의 객체 반환 함수
     * `@Serial`: 직렬화 과정에서 사용되는 함수와 필드를 명시적 표시를 위한 애노테이션
     */
    @Serial
    protected Object readResolve() {
        return INSTANCE;
    }

    public void print(int i) {
        System.out.println(i + " : " + this + " = " + VALUE);
    }

    public void setValue(int value) {
        VALUE = value;
    }

    public int getValue() {
        return VALUE;
    }
}
