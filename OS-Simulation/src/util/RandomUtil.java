package util;

import java.util.Random;

public class Util {
    /**
     * 随机决定是否阻塞进程
     * 1/5概率
     */
    public static boolean doBlockOrNot(){
        Random random = new Random();
        int value = random.nextInt(100);
        return value<=19;
    }

    /**
     * 随机获取一个时间片中的第N（N>0 && N<=timeSlice）个时间点
     * @param timeSlice
     * @return
     */
    public static int getRandomTimePoint(int timeSlice){
        return getRandomInRange(timeSlice);
    }

    public static int getRandomBlockTime(){
        return getRandomInRange(16);
    }

    private static int getRandomInRange(int range){
        Random random = new Random();
        int value = random.nextInt(range);
        while (value==0){
            value = random.nextInt(range);
        }
        return value;
    }
}
