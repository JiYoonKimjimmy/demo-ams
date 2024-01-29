package study.singleton;

import org.junit.jupiter.api.Test;

import java.io.*;

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

    @Test
    void singletonWithSerializableTest() {
        Singleton instance = Singleton.getInstance(1);

        // Serialize
        try {
            FileOutputStream fos = new FileOutputStream("out.ser");
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(instance);
            out.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        instance.setValue(2);

        // Deserialize
        Singleton instance2 = null;
        try {
            FileInputStream fis = new FileInputStream("out.ser");
            ObjectInputStream in = new ObjectInputStream(fis);
            instance2 = (Singleton) in.readObject();
            in.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (instance == instance2) {
            System.out.println("Two objects are same");
        } else {
            System.out.println("Two objects are not same");
        }

        System.out.println(instance + " : " + instance.getValue());
        System.out.println(instance2 + " : " + instance2.getValue());

    }

}
