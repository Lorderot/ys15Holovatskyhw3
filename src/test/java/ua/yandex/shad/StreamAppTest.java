/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.yandex.shad;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import ua.yandex.shad.function.IntPredicate;
import ua.yandex.shad.stream.*;

/**
 *
 * @author andrii
 */
public class StreamAppTest {
    
    private IntStream intStream;

    @Before
    public void init() {
        int[] intArr = {-1, 0, 1, 2, 3};
        intStream = AsIntStream.of(intArr);
    }
    
    @Test
    public void testStreamOperations() {
        System.out.println("streamOperations");
        int expResult = 42;
        int result = StreamApp.streamOperations(intStream);
        assertEquals(expResult, result);        
    }

    @Test
    public void testStreamToArray() {
        System.out.println("streamToArray");
        int[] expResult = {-1, 0, 1, 2, 3};
        int[] result = StreamApp.streamToArray(intStream);
        assertArrayEquals(expResult, result);        
    }

    @Test
    public void testStreamForEach() {
        System.out.println("streamForEach");
        String expResult = "-10123";
        String result = StreamApp.streamForEach(intStream);
        assertEquals(expResult, result);        
    }

    @Test
    public void testForEach_CheckIfAllIntermediateOperationsWereDeletedFromStack() {
        String expResult = "-101";
        String result = StreamApp.streamForEach(intStream.filter(x -> x < 2));
        assertEquals(expResult, result);

        expResult = "-10123";
        result = StreamApp.streamForEach(intStream);
        assertEquals(expResult, result);
    }


    @Test (expected = NullPointerException.class)
    public void testForEach_NullFunction() {
        intStream.forEach(null);
    }

    @Test (expected = NullPointerException.class)
    public void testReduce_NullFunction() {
        intStream.reduce(0, null);
    }

    @Test
    public void testReduce_CheckIfIntermediateOperationsChangeOriginalStream() {
        IntStream original = intStream;
        int result = intStream.filter(x -> x > 1).reduce(0, (x,y) -> (x += y));
        int expResult = 5;
        assertEquals(expResult, result);
        assertArrayEquals(original.toArray(), intStream.toArray());
    }

    @Test
    public void testReduce_CheckIfAllIntermediateOperationsWereDeletedFromStack() {
        int expResult = 1;
        long result = intStream.filter(x -> x < 0).reduce(0,(x, y) -> x - y);
        assertEquals(expResult, result);

        expResult = -5;
        result = intStream.reduce(0,(x, y) -> x - y);
        assertEquals(expResult, result);
    }

    @Test (expected = NullPointerException.class)
    public void testOf_NullFunction() {
        AsIntStream.of(null);
    }

    @Test (expected = NullPointerException.class)
    public void testFilter_NullFunction() {
        intStream.filter(null);
    }

    @Test (expected = NullPointerException.class)
    public void testMap_NullFunction() {
        intStream.map(null);
    }

    @Test (expected = NullPointerException.class)
    public void testFlat_MapNullFunction() {
        intStream.flatMap(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSum_EmptyStream() {
        System.out.println(intStream.filter(x -> x < -1).sum());
    }

    @Test
    public void testSum_AfterIntermediateOperations() {
        int result = intStream.filter(x -> x > 0).sum();
        int expResult = 6;
        assertEquals(expResult, result);
    }

    @Test
    public void testSum_WithOnlyPositiveNumbers() {
        int result = AsIntStream.of(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}).sum();
        int expResult = 55;
        assertEquals(expResult, result);
    }

    @Test
    public void testSum_WithNegativeNumbers() {
        int result = AsIntStream.of(new int[]{-10, -9, -8, -7, -6, -5, -4, -3, -2,
                -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10}).sum();
        int expResult = 0;
        assertEquals(expResult, result);
    }

    @Test
    public void testSum_CheckIfIntermediateOperationsChangeOriginalStream() {
        int[] original = intStream.toArray();
        int result = intStream.filter(x -> x > 1).sum();
        int expResult = 5;
        assertEquals(expResult, result);
        assertArrayEquals(original, intStream.toArray());
    }

    @Test
    public void testSum_CheckIfAllIntermediateOperationsWereDeletedFromStack() {
        int expResult = -1;
        long result = intStream.filter(x -> x <= 0).sum();
        assertEquals(expResult, result);

        expResult = 5;
        result = intStream.sum();
        assertEquals(expResult, result);
    }


    @Test (expected = IllegalArgumentException.class)
    public void testMin_EmptyStream() {
        intStream.filter(x -> x < -1).min();
    }

    @Test
    public void testMin_CheckIfIntermediateOperationsChangeOriginalStream() {
        int[] original = intStream.toArray();
        int result = intStream.filter(x -> x < 1).min();
        int expResult = -1;
        assertEquals(expResult, result);

        assertArrayEquals(original, intStream.toArray());
    }

    @Test
    public void testMin_CheckIfAllIntermediateOperationsWereDeletedFromStack() {
        int expResult = 1;
        int result = intStream.filter(x -> x >= 1).min();
        assertEquals(expResult, result);

        expResult = -1;
        result = intStream.min();
        assertEquals(expResult, result);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testMax_EmptyStream() {
        intStream.filter(x -> x < -1).max();
    }

    @Test
    public void testMax_CheckIfIntermediateOperationsChangeOriginalStream() {
        int[] original = intStream.toArray();
        int result = intStream.filter(x -> x > 1).max();
        int expResult = 3;
        assertEquals(expResult, result);

        assertArrayEquals(original, intStream.toArray());
    }

    @Test
    public void testMax_CheckIfAllIntermediateOperationsWereDeletedFromStack() {
        int expResult = 0;
        int result = intStream.filter(x -> x <= 0).max();
        assertEquals(expResult, result);

        expResult = 3;
        result = intStream.max();
        assertEquals(expResult, result);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testAverage_EmptyStream() {
        AsIntStream.of(new int[0]).average();
    }

    @Test
    public void testAverage_CheckIfIntermediateOperationsChangeOriginalStream() {
        int[] original = intStream.toArray();
        double result = intStream.filter(x -> x > 1).average();
        double expResult = 2.5;
        assertEquals(expResult, result, 0.00001);

        assertArrayEquals(original, intStream.toArray());
    }

    @Test
    public void testAverage_CheckIfAllIntermediateOperationsWereDeletedFromStack() {
        double expResult = -0.5;
        double result = intStream.filter(x -> x <= 0).average();
        assertEquals(expResult, result, 0.00001);

        expResult = 1;
        result = intStream.average();
        assertEquals(expResult, result, 0.00001);
    }


    @Test
    public void testCount_CheckIfAllIntermediateOperationsWereDeletedFromStack() {
        int expResult = 2;
        long result = intStream.filter(x -> x <= 0).count();
        assertEquals(expResult, result);

        expResult = 5;
        result = intStream.count();
        assertEquals(expResult, result);
    }

    @Test
    public void testCount_CheckIfIntermediateOperationsChangeOriginalStream() {
        int[] original = intStream.toArray();
        long result = intStream.filter(x -> x > 1).count();
        int expResult = 2;
        assertEquals(expResult, result);

        assertArrayEquals(original, intStream.toArray());
    }


}
