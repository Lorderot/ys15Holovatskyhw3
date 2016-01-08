package ua.yandex.shad.stream;

import ua.yandex.shad.collections.DynamicList;
import ua.yandex.shad.function.IntUnaryOperator;
import ua.yandex.shad.function.IntToIntStreamFunction;
import ua.yandex.shad.function.IntPredicate;
import ua.yandex.shad.function.IntConsumer;
import ua.yandex.shad.function.IntBinaryOperator;

import java.util.Iterator;
import java.util.NoSuchElementException;

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
        ExecuteCalledMethods elementsInStream = new ExecuteCalledMethods();
        Double generalAverage = null;
        long count = 0;
        while (elementsInStream.hasNext()) {
            DynamicList<Integer> processedElement = elementsInStream.next();
            if (processedElement == null) {
                continue;
            }
            count++;
            Integer accumulator = 0;
            for (Integer i : processedElement) {
                accumulator += i;
            }
            double average = (double) accumulator / processedElement.size();
            if (generalAverage != null) {
                generalAverage += average;
            } else {
                generalAverage = average;
            }
        }
        if (generalAverage == null) {
            throw new IllegalArgumentException();
        }
        calledMethods = new DynamicList<>();
        return generalAverage / count;
    }

    @Override
    public Integer max() throws IllegalArgumentException {
        ExecuteCalledMethods elementsInStream = new ExecuteCalledMethods();
        Integer generalMax = null;
        while (elementsInStream.hasNext()) {
            DynamicList<Integer> processedElement = elementsInStream.next();
            if (processedElement == null) {
                continue;
            }
            Integer max = processedElement.get(0);
            for (Integer i : processedElement) {
                if (i > max) {
                    max = i;
                }
            }
            if (generalMax != null) {
                generalMax = (generalMax < max) ? max : generalMax;
            } else {
                generalMax = max;
            }
        }
        calledMethods = new DynamicList<>();
        if (generalMax == null) {
            throw new IllegalArgumentException();
        }
        return generalMax;
    }

    @Override
    public Integer min() throws IllegalArgumentException {
        ExecuteCalledMethods elementsInStream = new ExecuteCalledMethods();
        Integer generalMin = null;
        while (elementsInStream.hasNext()) {
            DynamicList<Integer> processedElement = elementsInStream.next();
            if (processedElement == null) {
                continue;
            }
            Integer min = processedElement.get(0);
            for (Integer i : processedElement) {
                if (i < min) {
                    min = i;
                }
            }
            if (generalMin != null) {
                generalMin = (generalMin > min) ? min : generalMin;
            } else {
                generalMin = min;
            }
        }
        calledMethods = new DynamicList<>();
        if (generalMin == null) {
            throw new IllegalArgumentException();
        }
        return generalMin;
    }

    @Override
    public long count() {
        ExecuteCalledMethods elementsInStream = new ExecuteCalledMethods();
        Long count = null;
        while (elementsInStream.hasNext()) {
            DynamicList<Integer> processedElement = elementsInStream.next();
            if (processedElement == null) {
                continue;
            }
            if (count != null) {
                count += processedElement.size();
            } else {
                count = (long) processedElement.size();
            }
        }
        if (count == null) {
            throw new IllegalArgumentException();
        }
        calledMethods = new DynamicList<>();
        return count;
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
        ExecuteCalledMethods elementsInStream = new ExecuteCalledMethods();
        while (elementsInStream.hasNext()) {
            DynamicList<Integer> processedElement = elementsInStream.next();
            if (processedElement == null) {
                continue;
            }
            for (Integer i : processedElement) {
                action.accept(i);
            }
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
        ExecuteCalledMethods elementsInStream = new ExecuteCalledMethods();
        Integer generalAccumulator = null;
        while (elementsInStream.hasNext()) {
            DynamicList<Integer> processedElement = elementsInStream.next();
            if (processedElement == null) {
                continue;
            }
            Integer accumulator = op.apply(identity, processedElement.get(0));
            for (int i = 1; i < processedElement.size(); i++) {
                accumulator = op.apply(accumulator, processedElement.get(i));
            }

            if (generalAccumulator != null) {
                generalAccumulator += accumulator;
            } else {
                generalAccumulator = accumulator;
            }
        }
        if (generalAccumulator == null) {
            throw new IllegalArgumentException();
        }
        calledMethods = new DynamicList<>();
        return generalAccumulator;
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

    private class ExecuteCalledMethods
            implements Iterator<DynamicList<Integer>> {
        private Iterator<Integer> numbersInStream = intStream.iterator();
        public boolean hasNext() {
            return numbersInStream.hasNext();
        }

        @Override
        public DynamicList<Integer> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            DynamicList<Integer> temporary = new DynamicList<>();
            temporary.add(numbersInStream.next());
            for (Method m : calledMethods) {
                if (temporary.isEmpty()) {
                    return null;
                } else {
                    temporary = m.execute(temporary);
                }
            }
            if (temporary.isEmpty()) {
                return null;
            } else {
                return temporary;
            }
        }
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

        public abstract  DynamicList<Integer> execute(
                DynamicList<Integer> result, Object function);
    }
}
