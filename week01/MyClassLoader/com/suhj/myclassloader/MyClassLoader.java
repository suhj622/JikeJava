package com.suhj.myclassloader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyClassLoader extends ClassLoader {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        System.out.println(new MyClassLoader().findClass("Hello").newInstance().getClass());
    }

    @Override
    protected Class< ? > findClass(String name) throws ClassNotFoundException {

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        List<Integer> list = new ArrayList();
        try {
            fis = new FileInputStream("D:\\data\\Hello.xlass");
            bis = new BufferedInputStream(fis);
            while(bis.available()>0){
                list.add(255 - bis.read());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] bytes = new byte[list.size()];
        for(int i = 0; i < list.size(); i++){
            int item = list.get(i);
            bytes[i] = (byte)item;
        }
        return defineClass(name, bytes, 0, bytes.length);
    }
}
