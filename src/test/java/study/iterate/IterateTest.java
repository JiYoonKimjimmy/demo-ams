package study.iterate;

import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class IterateTest {

    @Test
    void basicForFunctionAndStreamTest() {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 100000; i++) {
            list.add(i);
        }

        long result1 = checkTest(list, this::test1);
        long result2 = checkTest(list, this::test2);
        long result3 = checkTest(list, this::test3);
//        long result4 = checkTest(list, this::test4);

        System.out.println("result1 = " + result1);
        System.out.println("result2 = " + result2);
        System.out.println("result3 = " + result3);
//        System.out.println("result4 = " + result4);
    }

    private long checkTest(List<Integer> list, Function<List<Integer>, Integer> function) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        function.apply(list);
        stopWatch.stop();
        System.out.println("stopWatch.getTotalTimeMillis() = " + stopWatch.getTotalTimeMillis());
        System.out.println(stopWatch.prettyPrint());
        return stopWatch.getTotalTimeMillis();
    }

    private int test1(List<Integer> list) {
        int result = 0;
        for (int i = 0; i < list.size(); i++) {
            result += list.get(i);
            if (i == 79999) {
                break;
            }
        }
        System.out.println("test1.result = " + result);
        return result;
    }

    private int test2(List<Integer> list) {
        int count = 0, result = 0;
        for (int i : list) {
            result += i;
            if (++count == 79999) {
                break;
            }
        }
        System.out.println("test2.result = " + result);
        return result;
    }

//    private int test3(List<Integer> list) {
//        AtomicInteger result = new AtomicInteger();
//        list.forEach(result::addAndGet);
//        System.out.println("test3.result = " + result);
//        return result.get();
//    }
//
//    private int test4(List<Integer> list) {
//        AtomicInteger result = new AtomicInteger();
//        list.stream().forEach(result::addAndGet);
//        System.out.println("test4.result = " + result);
//        return result.get();
//    }

    private int test3(List<Integer> list) {
        AtomicInteger result = new AtomicInteger();
        List<Integer> collection = list.stream().filter(i -> {
            result.addAndGet(i);
            return i == 79999;
        }).toList();
        System.out.println("test3.result = " + result);
        return result.get();
    }

}
