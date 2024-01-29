package study.singleton;

public class NonSingleton {

    private static int VALUE = 0;

    public NonSingleton(int value) {
        VALUE = value * 10;
    }

    public void print(int i) {
        System.out.println(i + " : " + this + " = " + VALUE);
    }

}
