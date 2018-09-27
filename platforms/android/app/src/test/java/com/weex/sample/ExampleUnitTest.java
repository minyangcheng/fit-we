package com.weex.sample;

import com.fit.we.library.util.FitUtil;

import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testLocalIp(){
        System.out.println(compareVersion("20.0.5.1","20.0.50.1"));
    }

    public static int compareVersion(String version1, String version2) {
        if (version1 == null || version2 == null) {
            return 0;
        }
        version1 = version1.toLowerCase().replaceAll("v", "");
        version2 = version2.toLowerCase().replaceAll("v", "");
        String[] versionArray1 = version1.split("\\.");//注意此处为正则匹配，不能用"."；
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
        int diff = 0;
        while (idx < minLength
            && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
            && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
            ++idx;
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }

}
