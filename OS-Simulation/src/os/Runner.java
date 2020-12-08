package os;

import entity.Job;
import entity.PCB;
import util.RandomUtil;

import java.util.Queue;

/**
 * 模拟类
 */
public class Runner {
    public static final int MAX_READY_PCB = 5;//就绪队列的最大长度

    private int timeSlice;//时间片大小
    private JobDispatcher jobDispatcher;
    private PCBDispatcher pcbDispatcher;
    private Memory memory;
    private int currentTime;//当前时间点
    private PCB currentPCB;//当前进程，即被调度的进程

    public Runner(Queue<Job> poolQueue, int timeSlice){
        this.timeSlice = timeSlice>0?timeSlice:10;
        jobDispatcher = new JobDispatcher(poolQueue);
        pcbDispatcher = new PCBDispatcher();
        memory = new Memory();
        currentTime = 0;
    }

    public void run(){
        {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>模拟开始, 时间片大小为：" + timeSlice);
            System.out.println("每一块Start-End为一轮循环；最小可用内存大小为" + Memory.MIN_SIZE);
            System.out.println("初始后备队列：");
            jobDispatcher.printPoolQueue();
        }

        dispatchJob();//作业调度
        while (jobDispatcher.hasPoolJob() || pcbDispatcher.getReadyPCBCount()>0 || pcbDispatcher.hasBlockPCB()){
            System.out.println("\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++Start");
            currentPCB = pcbDispatcher.dispatch();
            if(currentPCB!=null) {
                printAfterDispatchPCB();//打印信息
                runTimeSlice();
            }
            else{
                System.out.println("当前就绪队列为空(时间点：" + currentTime + ")");
                if(currentTime%10==0) {
                    printGlobalSituation();
                }
                currentTime++;
                pcbDispatcher.addATime(currentTime);
            }
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++End");
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>模拟结束");
        pcbDispatcher.printFinishPCBsUtterly();
    }

    /**
     * 运行一个时间片
     */
    private void runTimeSlice(){
        int i;
        boolean doBlock = RandomUtil.doBlockOrNot();//随机决定是否阻塞
        Integer blockPoint = doBlock ? RandomUtil.getRandomTimePoint(timeSlice+1) : null;//若决定阻塞，随机决定在时间片的第几个时间点阻塞
        System.out.println("\n==========================================时间片开始(时间点：" + currentTime + ")=======================================");
        for (i = 0; i < timeSlice; i++) {
            //若决定阻塞且到达阻塞时间点，将进程阻塞
            if(blockPoint!=null && blockPoint-1==i){
                blockCurrentPCB();
                currentTime++;
                break;
            }

            currentTime++;
            pcbDispatcher.addATime(currentTime);//就绪队列、阻塞队列中的进程的等待时间，剩余阻塞时间加1或减1
            currentPCB.addRunTime();

            //进程运行时间+1后，判断是否完成
            if(currentPCB.isFinished()){
                doFinishWork();
                dispatchJob();//有进程完成时，尝试调度新的作业
                break;
            }
        }
        if (i==timeSlice) {
            System.out.println(currentPCB.getPcbName() + " 运行了一个时间片后未完成，加入就绪队列");
            currentPCB.setStatus(PCB.READY);
            pcbDispatcher.offerPCB(currentPCB);
        }
        System.out.println("==========================================时间片结束(时间点：" + currentTime + ")=======================================");
    }

    /**
     * 通过作业调度器尝试调度作业，调度的作业数是【5-当前就绪队列长度】
     */
    private void dispatchJob(){
        int count = MAX_READY_PCB - pcbDispatcher.getReadyPCBCount() - pcbDispatcher.getBlockPCBCount();
        //若后备队列不为空
        if(jobDispatcher.hasPoolJob()){
            Queue<PCB> dispatchPCBs = jobDispatcher.dispatch(count, memory);
            //print
            printInDispatchJobs(dispatchPCBs);

            pcbDispatcher.offerReadyPCBs(dispatchPCBs);
        }
    }

    /**
     * 阻塞当前进程
     */
    private void blockCurrentPCB(){
        int blockTime = RandomUtil.getRandomBlockTime();
        currentPCB.setStatus(PCB.BLOCK);
        currentPCB.setBlockTime(blockTime);
        pcbDispatcher.addBlockPCB(currentPCB);

        System.out.println("在时间点：" + currentTime + " " + currentPCB.getPcbName() + " 被阻塞，进入阻塞队列，阻塞时间为" + blockTime);
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

        System.out.println("在时间点：" + currentTime + " " + currentPCB.getPcbName() + " 运行完成，回收内存，进程进入完成队列");
    }


    //以下是一堆信息打印
    private void printInDispatchJobs(Queue<PCB> dispatchPCBs){
        System.out.println("\n在时间点：" + currentTime + " 进行了作业调度");
        if(dispatchPCBs.size()==0){
            System.out.println("没有符合要求的作业可调度");
        }
        else {
            System.out.println("本次作业调度创建的PCB（将加入就绪队列）：");
            for (PCB pcb : dispatchPCBs) {
                System.out.println(pcb);
            }
        }
    }

    /**
     * 调度进程后打印信息
     */
    private void printAfterDispatchPCB(){
        System.out.println("在时间点" + currentTime + " 调度了进程：");
        System.out.println(currentPCB);
        printGlobalSituation();
    }

    /**
     * 打印全局概况，即后被队列、就绪队列等
     */
    private void printGlobalSituation(){
        System.out.println("------------当前全局资源概况：-------------------");
        jobDispatcher.printPoolQueue();
        pcbDispatcher.printReadyQueue();
        pcbDispatcher.printBlockQueue();
        pcbDispatcher.printFinishPCBsOutline();
        memory.printZoneList();
        System.out.println("-------------------------------------------");
    }
}
