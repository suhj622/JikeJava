package com.suhj.C01_CopyOnWriteArrayList;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 验证 Iterator 方法的快照原理
 * @author Haojie
 * @date 2022/5/27
 */
public class T00_CopyOnWriteArrayList_Iterator {

    public static void main(String[] args) {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("11111");
        list.add("22222");
        list.add("33333");
        Iterator<String> it = list.iterator();
        list.add("44444");

        while (it.hasNext()){
            System.out.println(it.next());
        }

        System.out.println(list);

    }



}
