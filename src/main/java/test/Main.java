package test;

import com.github.cojac.deltadebugging.utils.Executor;
import framework.io.FileUtils;
import template.binary.Bits;
import template.math.Matrix;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        var obj = new Object();
        var threads = Executors.newFixedThreadPool(2);
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(1);


        threads.submit(() -> {
            synchronized (obj) {
                while(true) {
                    System.out.println(queue.poll(1000, TimeUnit.MINUTES));
                }
            }
        });
        threads.submit(() -> {
            synchronized (obj) {
                while(true) {
                    queue.add("Hello");
                }
            }
        });

        threads.shutdown();
    }


}
