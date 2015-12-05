package ua.yandex.shad.stream;

import ua.yandex.shad.collections.DynamicList;
import ua.yandex.shad.function.IntUnaryOperator;
import ua.yandex.shad.function.IntToIntStreamFunction;
import ua.yandex.shad.function.IntPredicate;
import ua.yandex.shad.function.IntConsumer;
import ua.yandex.shad.function.IntBinaryOperator;

public class AsIntStream implements IntStream {
    private DynamicList<Integer> intStream;
    private DynamicList<Method> calledMethods;
    private AsIntStream(DynamicList<Integer> stream) {
        intStream = stream;
        calledMethods = new DynamicList<>();
    }

    public static IntStream of(int... values) throws NullPointerException {
        if (values == null) {
            throw new NullPointerException();
        }
        DynamicList<Integer> stream = new DynamicList<>();
        for (int i : values) {
            stream.add(i);
        }
        IntStream intStream = new AsIntStream(stream);
        return intStream;
    }

    @Override
    public Double average() throws IllegalArgumentException {
        DynamicList<Integer> processedStream = executeCalledMethods();
        if (processedStream.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Integer accumulator = 0;
        for (Integer i : processedStream) {
            accumulator += i;
        }
        double average = (double) accumulator / processedStream.size();
        calledMethods = new DynamicList<>();
        return average;
    }

    @Override
    public Integer max() throws IllegalArgumentException {
        DynamicList<Integer> processedStream = executeCalledMethods();
        if (processedStream.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Integer max = processedStream.get(0);
        for (Integer i : processedStream) {
            if (i > max) {
                max = i;
            }
        }
        calledMethods = new DynamicList<>();
        return max;
    }

    @Override
    public Integer min() throws IllegalArgumentException {
        DynamicList<Integer> processedStream = executeCalledMethods();
        if (processedStream.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Integer min = processedStream.get(0);
        for (Integer i : processedStream) {
            if (i < min) {
                min = i;
            }
        }
        calledMethods = new DynamicList<>();
        return min;
    }

    @Override
    public long count() {
        DynamicList<Integer> processedStream = executeCalledMethods();
        calledMethods = new DynamicList<>();
        return processedStream.size();
    }

    @Override
    public IntStream filter(IntPredicate predicate)
            throws NullPointerException {

        if (predicate == null) {
            throw new NullPointerException();
        }
        calledMethods.add(new Method(Methods.FILTER, predicate));
        return this;
    }

    @Override
    public void forEach(IntConsumer action) throws NullPointerException {
        if (action == null) {
            throw new NullPointerException();
        }
        DynamicList<Integer> processedStream = executeCalledMethods();
        for (Integer i : processedStream) {
            action.accept(i);
        }
        calledMethods = new DynamicList<>();
    }

    @Override
    public IntStream map(IntUnaryOperator mapper)
            throws NullPointerException {

        if (mapper == null) {
            throw new NullPointerException();
        }
        calledMethods.add(new Method(Methods.MAP, mapper));
        return this;
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op)
            throws NullPointerException, IllegalArgumentException {
        DynamicList<Integer> processedStream = executeCalledMethods();
        if (processedStream.isEmpty()) {
            throw new IllegalArgumentException();
        }
        int accumulator = op.apply(identity, processedStream.get(0));
        for (int i = 1; i < processedStream.size(); i++) {
            accumulator = op.apply(accumulator, processedStream.get(i));
        }
        calledMethods = new DynamicList<>();
        return accumulator;
    }

    @Override
    public Integer sum() throws IllegalArgumentException {
        return this.reduce(0, (sum, x) -> sum + x);
    }

    @Override
    public int[] toArray() {
        /*DynamicList<Integer> processedStream = executeCalledMethods();*/
        int[] result = new int[intStream.size()];
        int i = 0;
        for (Integer j : intStream) {
            result[i++] = j;
        }
        return result;
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func)
            throws NullPointerException {

        if (func == null) {
            throw new NullPointerException();
        }
        calledMethods.add(new Method(Methods.FLATMAP, func));
        return this;
    }


    private static DynamicList<Integer> filter(
            DynamicList<Integer> streamGeneratedFromElement,
            IntPredicate predicate) {

        DynamicList<Integer> result = new DynamicList<>();
        for (Integer i : streamGeneratedFromElement) {
            if (predicate.test(i)) {
                result.add(i);
            }
        }
        return result;
    }
    
    private static DynamicList<Integer> map(
            DynamicList<Integer> streamGeneratedFromElement,
            IntUnaryOperator mapper) {
        for (int i = 0; i < streamGeneratedFromElement.size(); i++) {
            streamGeneratedFromElement.set(i, mapper.apply(
                    streamGeneratedFromElement.get(i)));
        }
        return streamGeneratedFromElement;
    }

    private static DynamicList<Integer> flatMap(
            DynamicList<Integer> streamGeneratedFromElement,
            IntToIntStreamFunction func) {

        DynamicList<Integer> result = new DynamicList<>();
        for (Integer i : streamGeneratedFromElement) {
            int[] newElements = func.applyAsIntStream(i).toArray();
            for (int j : newElements) {
                result.add(j);
            }
        }
        return result;
    }

    private DynamicList<Integer> executeCalledMethods() {
        DynamicList<Integer> result = new DynamicList<>();
        for (Integer i : intStream) {
            DynamicList<Integer> temporary = new DynamicList<>();
            temporary.add(i);
            for (Method m : calledMethods) {
                if (temporary.isEmpty()) {
                    break;
                } else {
                    temporary = m.execute(temporary);
                }
            }
            for (Integer j : temporary) {
                result.add(j);
            }
        }
        return result;
    }

    private static class Method {
        private final Methods method;
        private final Object function;
        private Method(Methods method, Object function) {
            this.method = method;
            this.function = function;
        }
        private DynamicList<Integer> execute(DynamicList<Integer> result) {
            return method.execute(result, function);
        }
    }

    private enum Methods {
        FILTER {
            @Override
            public DynamicList<Integer> execute(
                    DynamicList<Integer> result, Object function)
                    throws IllegalArgumentException {

                if (!(function instanceof IntPredicate)) {
                    throw new IllegalArgumentException();
                }
                return AsIntStream.filter(result, (IntPredicate) function);
            }
        }, MAP {
            @Override
            public DynamicList<Integer> execute(DynamicList<Integer> result,
                                                Object function)
                    throws IllegalArgumentException {
                if (!(function instanceof IntUnaryOperator)) {
                    throw new IllegalArgumentException();
                }
                return AsIntStream.map(result, (IntUnaryOperator) function);
            }
        }, FLATMAP {
            @Override
            public DynamicList<Integer> execute(
                    DynamicList<Integer> result, Object function)
                    throws IllegalArgumentException {

                if (!(function instanceof IntToIntStreamFunction)) {
                    throw new IllegalArgumentException();
                }
                return AsIntStream.flatMap(result,
                        (IntToIntStreamFunction) function);
            }
        };

        abstract public DynamicList<Integer> execute(
                DynamicList<Integer> result, Object function);
    }
}
