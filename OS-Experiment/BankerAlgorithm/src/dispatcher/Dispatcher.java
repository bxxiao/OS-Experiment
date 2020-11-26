package dispatcher;

import pcb.PCB;
import util.Util;

import java.util.*;

public class Dispatcher {
    private int[] available = new int[]{10,15,12};//A B C三种资源的个数，初始值分别为10 15 12
    private int[][] max = new int[5][3];//最大需求矩阵
    private int[][] allocation = new int[5][3];//已分配矩阵
    private int[][] need = new int[5][3];//需求矩阵
    private Integer currentPCB;//当前进程编号

    private List<PCB> pcbList;//所有进程，以下各队列存放的是进程在pcbList中的索引
    private Queue<Integer> finishQueue = new LinkedList<>();//完成队列
    private Queue<Integer> readyQueue;//就绪队列（存放进程编号）
    private Queue<Integer> blockQueue = new LinkedList<>();
    private Map<Integer, int[]> blockReq = new HashMap<>();//存放阻塞队列中进程被阻塞时的请求，以 进程id-请求 的形式存储
    private List<String> safeInfo = new ArrayList<>();//安全判断时产生的序列信息

    public Dispatcher(){
        readyQueue = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            readyQueue.offer(i);
        }
        pcbList = Util.createPCBRandom(max, need);
    }

    public void run(){
        int runBlock = 0;//运行阻塞队列的进程的次数，当有进程运行完毕时，会增加该变量的值，增加的量是阻塞队列的长度（即有进程完成时把阻塞的队列都调用一次）
        while (readyQueue.size()>0 || blockQueue.size() > 0) {
            int[] req;//进程发出的请求（随机产生）

            //调度进程
            //若指定了调用阻塞队列的进程
            if(runBlock>0){
                //队头进程编号出队
                currentPCB = blockQueue.poll();
                runBlock--;
                //获取进程被阻塞时的请求
                req = blockReq.remove(currentPCB);
            }
            else {
                currentPCB = readyQueue.poll();
                req = Util.createReqRandom(need[currentPCB]);//随机创建请求
            }
            //修改进程状态
            pcbList.get(currentPCB).setStatus(PCB.RUN);

            System.out.println("=======================================================================================================START");
            System.out.println("进程" + (currentPCB+1) + "发出请求：" + Arrays.toString(req));
            printSituation();//输出当前进程概况

            //判断请求是否超过当前资源数
            if(req[0]<=available[0] && req[1]<=available[1] && req[2]<=available[2]){
                //分配资源
                allocate(req);
                //执行安全性算法，若不安全，还原资源分配
                if(!isSafe()){
                    System.out.println("\n!!!!!本次请求不安全(具体如下)，可能引起死锁，拒绝分配资源，进程进入阻塞队列");
                    printSafeInfo();

                    restore(req);//还原资源分配
                    pcbList.get(currentPCB).setStatus(PCB.BLOCK);
                    blockQueue.offer(currentPCB);
                    blockReq.put(currentPCB, req);
                    System.out.println("=======================================================================================================END\n\n");
                    continue;
                }

                //若安全，打印相关信息，并判断进程是否已完成
                System.out.println("\n本次请求安全，存在的安全序列：");
                printSafeInfo();
                if(need[currentPCB][0]==0 && need[currentPCB][1]==0 && need[currentPCB][2]==0){
                    //回收资源
                    available[0] += allocation[currentPCB][0];
                    available[1] += allocation[currentPCB][1];
                    available[2] += allocation[currentPCB][2];
                    //设置进程完成时的状态
                    pcbList.get(currentPCB).setStatus(PCB.FINISH);
                    //加入完成队列
                    finishQueue.add(currentPCB);
                    if(blockQueue.size()>0) {
                        runBlock = blockQueue.size();
                    }
                }
                //未完成，将进程加入就绪队列队尾
                else{
                    pcbList.get(currentPCB).setStatus(PCB.READY);
                    readyQueue.offer(currentPCB);
                }
                currentPCB = null;
                System.out.println("资源分配给进程并运行后：");
                printSituation();
            }
            //当前没有足够资源，加入阻塞队列
            else{
                System.out.println("本次请求资源数超过系统当前资源，进程进入阻塞队列");
                pcbList.get(currentPCB).setStatus(PCB.BLOCK);
                blockQueue.offer(currentPCB);
                blockReq.put(currentPCB, req);
            }
            System.out.println("=======================================================================================================END\n\n");
        }
    }


    /**
     * 安全性算法
     */
    private boolean isSafe(){
        List<Integer> pcbIds = getNotFinishPCBIndex();//获取所有未完成进程在pcbList中的索引
        int pcbCount = pcbIds.size();
        boolean[] finish = new boolean[pcbCount];//初始值为false，finish[i]对应的进程编号是pcbIds[i]
        int[] work = new int[]{available[0], available[1], available[2]};//可分配资源，初始值与available同

        boolean haveFinish = true;//表示当前循环是否有完成的进程
        int finishCount = 0;//已完成的进程数

        safeInfo.clear();

        /**
         * 对pcbIds进行循环，循环条件是haveFinish为true且finishCount小于5：
         *     每次循环开始，haveFinish设为false
         *     再对finish进行遍历：
         *         若对应进程need小于work，则增加work，进程设为完成，且haveFinish设为true，finishCount+1。并记录信息
         *              记录信息按  进程\资源         Work               Need              Allocation         Work+Allocation      Finish 的顺序
         *         若对应进程need大于，遍历下一个
         *     若一次遍历下来haveFinish为false，将会跳出循环，此时count小于5
         * 循环结束时，判断count是否等于5，若否，则此次资源分配不安全
         */
        while(haveFinish && finishCount<pcbCount){
            haveFinish = false;
            for (int i = 0; i < pcbCount; i++) {
                if(finish[i]==false){
                    //Need<=Word
                    if(need[pcbIds.get(i)][0]<=work[0] && need[pcbIds.get(i)][1]<=work[1] && need[pcbIds.get(i)][2]<=work[2]){
                        finish[i] = true;
                        //增加work前记录信息
                        String info = "进程" + (pcbIds.get(i)+1) + "        " + Util.toString(work) + "        " +
                                Util.toString(need[pcbIds.get(i)]) + "         " + Util.toString(allocation[pcbIds.get(i)]);
                        //增加work
                        work[0] += allocation[pcbIds.get(i)][0];
                        work[1] += allocation[pcbIds.get(i)][1];
                        work[2] += allocation[pcbIds.get(i)][2];
                        haveFinish = true;
                        finishCount++;
                        //记录增加work后的信息
                        info += ("        " + Util.toString(work) + "          " + true);
                        safeInfo.add(info);
                    }
                }
            }
        }
        //若不通过安全算法，记录第一个Need>Work的进程相关信息
        if(finishCount<pcbCount){
            for (int i = 0; i < pcbCount; i++) {
                if(finish[i]==false){
                    String info = "进程" + (pcbIds.get(i)+1) + "        " + Util.toString(work) + "        " +
                            Util.toString(need[pcbIds.get(i)]) + "         " + Util.toString(allocation[pcbIds.get(i)]) +
                            "        " + "[x , x ,x ]" + "          " + false;
                    safeInfo.add(info);
                    break;
                }
            }
        }

        return finishCount==pcbCount;
    }

    /**
     * 分配资源
     */
    private void allocate(int[] req){
        available[0] -= req[0];
        available[1] -= req[1];
        available[2] -= req[2];
        allocation[currentPCB][0] += req[0];
        allocation[currentPCB][1] += req[1];
        allocation[currentPCB][2] += req[2];
        need[currentPCB][0] -= req[0];
        need[currentPCB][1] -= req[1];
        need[currentPCB][2] -= req[2];
    }

    /**
     * 还原资源分配
     */
    private void restore(int[] req){
        available[0] += req[0];
        available[1] += req[1];
        available[2] += req[2];
        allocation[currentPCB][0] -= req[0];
        allocation[currentPCB][1] -= req[1];
        allocation[currentPCB][2] -= req[2];
        need[currentPCB][0] += req[0];
        need[currentPCB][1] += req[1];
        need[currentPCB][2] += req[2];
    }


    /**
     * 输出进程概况情况
     */
    private void printSituation(){
        System.out.println("--------------------当前进程概况-------------------");
        System.out.println("当前可用资源(Available)：" + Arrays.toString(available));
        System.out.println("进程\\资源      最大需求(Max)            已分配(Allocation)     尚需(Need)        状态");
        for (int i = 0; i < 5; i++) {
            System.out.println("  进程" + (i+1) + "        " + Util.toString(max[i]) + "             " +
                    Util.toString(allocation[i]) + "          " + Util.toString(need[i]) + "     " + pcbList.get(i).getStatus());
        }
        System.out.println("运行中的进程：" + (currentPCB==null?"":(currentPCB+1)));
        System.out.println("就绪队列的进程：" + Util.toString(readyQueue));
        System.out.println("阻塞队列的进程：" + Util.toString(blockQueue));
        System.out.println("阻塞请求：" + Util.mapToString(blockReq));
        System.out.println("运行完成的进程：" + Util.toString(finishQueue));
        System.out.println("----------------------------------------------------\n");
    }

    /**
     * 输出安全判断时产生的信息
     */
    private void printSafeInfo(){
        System.out.println("进程\\资源         Work               Need              Allocation         Work+Allocation      Finish");
        safeInfo.forEach((info)-> System.out.println(info));
    }

    /**
     * 获取未完成进程在pcbList中的索引
     */
    private List<Integer> getNotFinishPCBIndex(){
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < pcbList.size(); i++) {
            if(pcbList.get(i).getStatus()!=PCB.FINISH){
                result.add(i);
            }
        }
        return result;
    }

}
