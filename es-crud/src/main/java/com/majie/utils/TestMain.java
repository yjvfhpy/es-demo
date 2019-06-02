package com.majie.utils;

import static java.lang.Character.UnicodeBlock.*;
import com.google.common.base.Strings;
import org.apache.http.client.utils.DateUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liujian@webull.com
 * @date 2019-05-27 09:56
 */
public class TestMain {

    public static void main2(String[] args) {
        String containChinese = "test,我有中文";
        String containNoChiese = "test, i don't contain chinese";
        String containBianDian = "test， i don't contain chinese";
        String containEmoji = "test, i don't contain chinese😁😁👳🏻‍♂️🍍🍈🍅💗📹🇦🇴🚐";
        String japan = "fafdsa は払うようなやり";
        String russian = "iammmljl,okполезешь впооолд";
        String kor = "fafasㅑㅐㅐㅗ 하ㅓㅍㄹ ㅓㅗㅍ춫 ㅏㅏㄹ러ㅏ ";

        System.out.println("containChinese 是否包含中文 :" + checkStringContainChinese(containChinese));
        System.out.println("containNoChiese 是否包含中文 :" + checkStringContainChinese(containNoChiese));
        System.out.println("containBianDian 是否包含中文 :" + checkStringContainChinese(containBianDian));
        System.out.println("containEmoji 是否包含中文 :" + checkStringContainChinese(containEmoji));


        System.out.println("japan 是否包含中文 :" + checkStringContainChinese(japan));
        System.out.println("russian 是否包含中文 :" + checkStringContainChinese(russian));
        System.out.println("kor 是否包含中文 :" + checkStringContainChinese(kor));


    }

    private static boolean checkStringContainChinese(String checkStr){
        if(!Strings.isNullOrEmpty(checkStr)){
            char[] checkChars = checkStr.toCharArray();
            for(int i = 0; i < checkChars.length; i++){
                char checkChar = checkChars[i];
                if(checkCharContainChinese(checkChar)){
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkCharContainChinese(char checkChar){
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(checkChar);
        if(CJK_UNIFIED_IDEOGRAPHS == ub || CJK_COMPATIBILITY_IDEOGRAPHS == ub || CJK_COMPATIBILITY_FORMS == ub ||
                CJK_RADICALS_SUPPLEMENT == ub || CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A == ub || CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B == ub){
            return true;
        }
        return false;
    }


    public static void main3(String[] args) {
        Long lastId = null;
        Date lastUpdateTime = Optional.ofNullable(lastId).map(t ->  new Date(t)).orElse( null);
        System.out.println(lastUpdateTime);

    }

    public static void main4(String[] args) {
        Long lastId = 2l;
        lastId = lastId  != null && lastId.intValue() != 0 ? lastId : null;
        Date lastUpdateTime = Optional.ofNullable(lastId).map(t ->  new Date(t)).orElse( null);
        System.out.println(lastUpdateTime);

        System.out.println(DateUtils.formatDate(lastUpdateTime, "dd-MMM-yy HH:mm:ss"));


    }


    public static void main(String[] args) {
        List<String> ids = null;
            if(ids == null)
                System.out.println(Collections.EMPTY_LIST);
        List<Integer> sourceIds = ids.stream().map(id -> {
            Integer sourceId = Integer.valueOf(id);
            return sourceId;
        }).collect(Collectors.toList());
        System.out.println(sourceIds);
    }

}