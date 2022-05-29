package com.suhj.C01_CopyOnWriteArrayList;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 自定义快照
 * @author Haojie
 * @date 2022/5/27
 */
public class T01_try_snapshot {

    private static String[] getSnapshot(String[] data){
        final String[] snapshot;
        snapshot = data;
        return snapshot;
    }

    public static void main(String[] args) {

        String[]  data  = new String[]{"11111","22222"};
        String[] snapshot = getSnapshot(data);
        String[] newdata = new String[]{"11111","22222","33333"};
        data = newdata;
        Arrays.stream(snapshot).forEach(System.out::println);
        Arrays.stream(data).forEach(System.out::println);

        System.out.println(1<<2);

    }

}
