package util;

import pcb.PCB;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PCBUtil {
    /**
     * 随机创建5个进程对象
     */
    public static List<PCB> createPCBRandom(){
        List<PCB> pcbs = new ArrayList<>();
        Random random = new Random();
        int a = 'A';
        for (int i = 0; i < 5; i++, a++) {
            PCB pcb = new PCB();
            pcb.setPCBName("进程" + ((char)a));
            //获取不为0的随机数（小于51）
            int randomTime = random.nextInt(51);
            while (randomTime==0){
                randomTime = random.nextInt(51);
            }
            pcb.setArriveTime(i==0 ? 0 : randomTime);

            randomTime = random.nextInt(51);
            while (randomTime==0){
                randomTime = random.nextInt(51);
            }
            pcb.setNeedTime(randomTime);
            pcbs.add(pcb);
        }
        return pcbs;
    }

    /**
     * 深拷贝集合
     */
    public static <T> List<T> deepCopy(List<T> src)throws Exception{
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }
}
