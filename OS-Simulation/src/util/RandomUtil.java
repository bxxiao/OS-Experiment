package util;

import java.util.Random;

public class RandomUtil {
    /**
     * 随机决定是否阻塞进程
     * 3/10概率
     */
    public static boolean doBlockOrNot(){
        Random random = new Random();
        int value = random.nextInt(100);
        return value<=29;
    }

    /**
     * 随机获取一个时间片中的第N（N>0 && N<=timeSlice）个时间点
     * @param timeSlice
     * @return
     */
    public static int getRandomTimePoint(int timeSlice){
        return getRandomInRange(timeSlice);
    }

    /**
     * 获取随机阻塞时间
     * @return
     */
    public static int getRandomBlockTime(){
        Random random = new Random();
        int range = random.nextBoolean()?31:91;
        return getRandomInRange(range);
    }

    /**
     * 获取一个1- （range-1）的随机int值
     * @param range
     * @return
     */
    private static int getRandomInRange(int range){
        Random random = new Random();
        int value = random.nextInt(range);
        while (value==0){
            value = random.nextInt(range);
        }
        return value;
    }
}
