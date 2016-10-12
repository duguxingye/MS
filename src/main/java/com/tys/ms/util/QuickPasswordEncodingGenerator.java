package com.tys.ms.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

public class QuickPasswordEncodingGenerator {

    void init(List<Integer> list) {
        list.clear();
        for (int i = 0; i < 10; i++) {
            list.add(i + 1);
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String password = "123456";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode(password));

//        System.out.println(new Date());
//        List<Integer> list = new ArrayList<Integer>();
//        list.add(0);
//        for (int i = 0; i < 10; i++) {
//            list.add(i + 1);
//        }
//        for (Iterator<Integer> iter = list.iterator(); iter.hasNext();) {
//            int i = iter.next();
//            if (i < 6) {
//                iter.remove();
//            }
//        }
//        System.out.println(list);

    }
}
