package com.gf.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author JusChui
 * @ClassName FileUitl.java
 * @Date 2021年04月14日 14:09:00
 * @Description
 */
public class FileUtil {

    public static String getFileId() {
        Random random = new Random(new Date().getTime());
        String r = String.valueOf(Math.abs(random.nextLong()));
        r = r.substring(0,5);
        //System.out.println(r);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = simpleDateFormat.format(new Date());
        time = time.concat(r);
        return time;
    }
}
