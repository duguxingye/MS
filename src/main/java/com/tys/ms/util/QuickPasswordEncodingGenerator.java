package com.tys.ms.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class QuickPasswordEncodingGenerator {

    void init(List<Integer> list) {
        list.clear();
        for (int i = 0; i < 10; i++) {
            list.add(i + 1);
        }
    }

    public static BigDecimal add(String num1, String num2, String num3) {
        BigDecimal bd1 = new BigDecimal(num1);
        BigDecimal bd2 = new BigDecimal(num2);
        BigDecimal bd3 = new BigDecimal(num3);
        return bd1.add(bd2).add(bd3);
    }



    /**
     * @param args
     */
    public static void main(String[] args) {
        String leaderId = "area01";
        String leaderIdInSql = "\'" + leaderId +"\'";
        String querySql = "select distinct c.job_id from ms.app_user a inner join ms.app_user b on a.job_id=b.leader_id or a.job_id=b.job_id inner join ms.app_user c on b.job_id=c.leader_id or b.job_id=c.job_id where a.job_id=" + leaderIdInSql;
        System.out.println(querySql);

////        BigDecimal a = new BigDecimal("2.00");
////        BigDecimal b = new BigDecimal("2.00");
////        BigDecimal c = new BigDecimal("2.00");
////        BigDecimal d = new BigDecimal("5.00");
//
//        String a = "2.00";
//        String b = "2.00";
//        String c = "2.00";
//
//        BigDecimal d = new BigDecimal("44");
////        String d = "5.00";
//
//        //是否不相等
//        System.out.println(add(a, b, c).compareTo(d) != 0);



//        String password = "123456";
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        System.out.println(passwordEncoder.encode(password));

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
