package com.github.kgrech.statcollectior.client.api;

import com.github.kgrech.statcollectior.client.monitor.CpuChecker;
import com.github.kgrech.statcollectior.client.monitor.MemoryChecker;
import com.github.kgrech.statcollectior.client.monitor.ProcessesChecker;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests all system parameters checkers
 * @author Konstantin G. (kgrech@mail.ru)
 */
public class CheckerTests {

    @Before
    public void loadLibrary() {
        System.load(new File("./libstat_collector_native.so").getAbsolutePath());
    }

    @Test
    public void testCpuChecker() {
        CpuChecker checker = new CpuChecker();
        float value = Float.valueOf(checker.getValue());
        assertEquals("Parameter name should be correct", "cpu", checker.getType());
        assertTrue("Returned value should be positive", value > 0);
    }

    @Test
    public void testMemoryChecker() {
        MemoryChecker checker = new MemoryChecker();
        float value = Float.valueOf(checker.getValue());
        assertEquals("Parameter name should be correct", "memory", checker.getType());
        assertTrue("Returned value should be positive", value > 0);
    }

    @Test
    public void testProcessesChecker() {
        ProcessesChecker checker = new ProcessesChecker();
        int value = Integer.valueOf(checker.getValue());
        assertEquals("Parameter name should be correct", "processes", checker.getType());
        assertTrue("Returned value should be positive", value > 0);
    }
}
