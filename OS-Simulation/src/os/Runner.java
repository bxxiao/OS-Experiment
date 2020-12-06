package os;

import entity.Job;
import entity.PCB;
import util.Util;

import java.util.Queue;

/**
 * 模拟类
 */
public class Runner {
    public static final int MAX_READY_PCB = 5;//就绪队列的最大长度

    private int timeSlice;
    private JobDispatcher jobDispatcher;
    private PCBDispatcher pcbDispatcher;
    private Memory memory;
    private int currentTime;
    private PCB currentPCB;//当前进程，即被调度的进程

    public Runner(Queue<Job> poolQueue, int timeSlice){
        this.timeSlice = timeSlice;
        jobDispatcher = new JobDispatcher(poolQueue);
        pcbDispatcher = new PCBDispatcher();
        memory = new Memory();
        currentTime = 0;
    }

    public void run(){
        {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>模拟开始\n\n");
            System.out.println("初始后被队列：");
            jobDispatcher.printPoolQueue();
        }

        dispatchJob();//作业调度
        while (jobDispatcher.hasPoolJob() || pcbDispatcher.getReadyPCBCount()>0 || pcbDispatcher.hasBlockPCB()) {
            System.out.println("============================================================================================START");
            currentPCB = pcbDispatcher.dispatch();
            if(currentPCB!=null) {
                printAfterDispatchPCB();//打印信息
                runTimeSlice();
            }
            else{
                pcbDispatcher.reduceBlockTime();
            }
            System.out.println("============================================================================================END");
        }
        pcbDispatcher.printFinishPCBs();
    }

    /**
     * 运行一个时间片
     */
    private void runTimeSlice(){
        boolean doBlock = Util.doBlockOrNot();//随机决定是否阻塞
        Integer blockPoint = doBlock ? Util.getRandomTimePoint(timeSlice) : null;//若决定阻塞，随机决定在时间片的第几个时间点阻塞
        for (int i = 0; i < timeSlice; i++) {
            currentTime++;
            //若决定阻塞且到达阻塞时间点，将进程阻塞
            //PS：若发送阻塞，则此时阻塞时间应该是currentTime-1
            if(blockPoint!=null && blockPoint-1==i){
                blockCurrentPCB();
                return;
            }

            pcbDispatcher.reduceBlockTime();//减少阻塞队列中进程的阻塞时间，且若阻塞时间变为0，则将进程添加到就绪队列
            currentPCB.addRunTime();

            //进程运行时间+1后，判断是否完成
            if(currentPCB.isFinished()){
                doFinishWork();
                dispatchJob();//有进程完成时，尝试调度新的作业
                return;
            }
        }
        pcbDispatcher.offerPCB(currentPCB);
    }

    /**
     * 通过作业调度器尝试调度作业，调度的作业数是【5-当前就绪队列长度】
     */
    private void dispatchJob(){
        int count = MAX_READY_PCB - pcbDispatcher.getReadyPCBCount();
        //若后备队列不为空
        if(jobDispatcher.hasPoolJob()){
            Queue<PCB> dispatchPCBs = jobDispatcher.dispatch(count, memory);
            //print
            printInDispatchPCBs(dispatchPCBs);

            pcbDispatcher.offerReadyPCBs(dispatchPCBs);
            memory.printZoneList();
        }
    }

    /**
     * 阻塞当前进程
     */
    private void blockCurrentPCB(){
        currentPCB.setStatus(PCB.BLOCK);
        currentPCB.setBlockTime(Util.getRandomBlockTime());
        pcbDispatcher.addBlockPCB(currentPCB);

        printInBlockPCB(currentPCB.getPcbName());
    }

    /**
     * 进程运行完成后的一些处理
     */
    private void doFinishWork(){
        currentPCB.getZone().setPcbName(null);
        memory.recycle(currentPCB.getZone());//回收内存
        currentPCB.setZone(null);
        currentPCB.setStatus(PCB.FINISHED);
        currentPCB.setFinishTime(currentTime);
        pcbDispatcher.addFinishPCB(currentPCB);

        printInPcbFinish(currentPCB.getPcbName());
    }


    //以下是一堆信息打印
    private void printInDispatchPCBs(Queue<PCB> dispatchPCBs){
        System.out.println("\n在时间点：" + currentTime + " 进行了作业调度");
        System.out.println("为调度的作业创建的PCB：\n----------------------------------------------------------\b");
        for (PCB pcb : dispatchPCBs) {
            System.out.println(pcb);
        }
        System.out.println("----------------------------------------------------------\n");
    }

    /**
     * 调度进程后打印信息
     */
    private void printAfterDispatchPCB(){
        System.out.println("-----------------------------------------------------");
        System.out.println("在时间点：" + currentTime + " 进行了进程调度");
        System.out.println("调度的进程：" + currentPCB);
        System.out.println("-----------------------------------------------------");
    }

    /**
     * 在发生进程阻塞时打印信息
     */
    private void printInBlockPCB(String pcbName){
        System.out.println("\n------------------------------------------");
        System.out.println("在时间点：" + (currentTime-1) + " " + pcbName + "被阻塞，进入阻塞队列");
        pcbDispatcher.printBlockQueue();
        System.out.println("------------------------------------------");
    }

    private void printInPcbFinish(String pcbName){
        System.out.println("-----------------------------------------------------");
        System.out.println("在时间点：" + currentTime + " " + pcbName + "运行完成，进入完成队列");
        pcbDispatcher.printFinishPCBs();
        memory.printZoneList();
        System.out.println("-----------------------------------------------------");
    }
}
