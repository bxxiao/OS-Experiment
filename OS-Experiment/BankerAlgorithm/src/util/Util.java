package util;

import pcb.PCB;

import java.util.*;

public class Util {
    private static int reqA;
    private static int reqB;
    private static int reqC;

    /**
     * 随机创建5个进程
     */
    public static List<PCB> createPCBRandom(int[][] max, int[][] need){
        List<PCB> result = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            PCB pcb = new PCB();
            String pcbName = "进程" + (i+1);

            generateRandomReq(10,15,12);

            pcb.setPcbName(pcbName);
            max[i][0] = reqA;
            max[i][1] = reqB;
            max[i][2] = reqC;
            need[i][0] = reqA;
            need[i][1] = reqB;
            need[i][2] = reqC;
            result.add(pcb);
        }
        return result;
    }

    /**
     * 根据需求随机创建一个资源请求
     */
    public static int[] createReqRandom(int[] need){
        int[] req = new int[3];
        generateRandomReq(need[0], need[1], need[2]);
        req[0] = reqA;
        req[1] = reqB;
        req[2] = reqC;
        return req;
    }

    /**
     * 各种队列中存的都是进程在pcbList中的索引，其编号应是索引+1
     * 通过该方法将索引+1返回
     * 如[0,1,2]返回[1,2,3]
     */
    public static String toString(Collection<Integer> queue){
        String numbers = "";
        if(queue.size()==0){
            return "[]";
        }
        for (Integer integer : queue) {
            numbers += ((integer+1) + ",");
        }
        numbers = numbers.substring(0, numbers.length()-1);
        return "[" + numbers + "]";
    }

    /**
     * int数组中小于10的数右补一个空格
     */
    public static String toString(int[] array){
        String numStr = "";
        for (int i = 0; i < array.length; i++) {
            String temp = array[i]>=10 ? (""+array[i]) : (array[i]+" ");
            numStr += (temp + ", ");
        }
        numStr = numStr.substring(0, numStr.length()-2);
        return "[" + numStr + "]";
    }

    public static String mapToString(Map<Integer, int[]> map){
        String result = "";
        if (map.size()==0){
            return "{}";
        }

        Set<Integer> keySet = map.keySet();
        for (Integer key : keySet) {
            String temp = (key+1) + "-" + Arrays.toString(map.get(key));
            result += (temp + ",");
        }
        result = result.substring(0, result.length()-1);
        return "{" + result + "}";
    }

    private static void generateRandomReq(int maxA, int maxB, int maxC){
        Random random = new Random();

        reqA = random.nextInt(maxA + 1);
        reqB = random.nextInt(maxB + 1);
        reqC = random.nextInt(maxC + 1);
        //避免3个都为0
        while (reqA==0 && reqB==0 && reqC==0){
            reqA = random.nextInt(maxA + 1);
            reqB = random.nextInt(maxB + 1);
            reqC = random.nextInt(maxC + 1);
        }
    }


}
