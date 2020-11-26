package dispatcher;

import pcb.NotReachQueue;
import pcb.PCB;
import pcb.ReadyQueue;
import util.PCBUtil;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 进程调度器的抽象类
 */
public abstract class Dispatcher {
    public static final int SFJ_MODEL = 1;
    public static final int HRRN_MODEL = 2;
    protected ReadyQueue readyQueue = new ReadyQueue();//就绪队列
    protected NotReachQueue notReachQueue;//未到达进程
    protected List<PCB> finishedPCB = new ArrayList<>();//已执行完成的进程
    protected int currentTime = -1;//当前时间
    protected PCB currentPCB;//当前正在运行的进程
    protected boolean isLeisure = true;//模拟cpu是否空闲
    protected double avgTurnaroundTime;//平均周转时间

    public Dispatcher(List<PCB> pcbs){
        //深拷贝一个新的进程集合，目的是不影响原进程集合的数据
        List<PCB> temp = null;
        try {
            temp = PCBUtil.deepCopy(pcbs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        notReachQueue = new NotReachQueue(temp);
    }

    /**
     * 运行
     */
    public abstract void run();

    /**
     * SFJ和HRRN的运行只有对就绪队列的排序方法不同，这里把它们的运行方式抽取出来
     */
    protected void runInModel(int model){
        if(model==SFJ_MODEL) {
            System.out.println("》》》》》》》》》》》》》》》》》》》》》》》》》》短进程优先调度模拟开始");
        }
        if(model==HRRN_MODEL){
            System.out.println("》》》》》》》》》》》》》》》》》》》》》》》》》》高响应比优先调度模拟开始");
        }

        while (true){
            currentTime++;
            //监听时间，若是有到达时间对应的进程，将其加入就绪队列
            notReachQueue.listenTime(currentTime, readyQueue);
            //为空闲状态
            if(isLeisure){
                if(readyQueue.isEmpty() && notReachQueue.isEmpty()){
                    calculateAvgTurnaroundTime();
                    if(model==SFJ_MODEL) {
                        System.out.println("短进程优先调度模拟结束《《《《《《《《《《《《《《《《《《《");
                    }
                    if(model==HRRN_MODEL){
                        System.out.println("高响应比优先调度模拟结束《《《《《《《《《《《《《《《《《《《");
                    }
                    printFinishStatus();
                    break;
                }
                //就绪队列不为空，进行调度
                if(!readyQueue.isEmpty()) {
                    if(model==SFJ_MODEL){
                        readyQueue.sortInSJF();
                    }
                    else{
                        readyQueue.sortInHRRN();
                    }
                    currentPCB = readyQueue.poll();//获取排序后队列的第一个进程
                    isLeisure = false;
                    currentPCB.setStatus(PCB.RUN);
                    //调度之前输出当前进程状态
                    System.out.println("\n----------------在时间点 " +  currentTime +" 执行进程调度----------------");
                    System.out.println(">>>>>>>>>>>>>>>>>>调度后的进程状态：");
                    printPCBStatus();
                    runATimeUnit();
                }else {
                    if (currentTime % 20 == 0) {
                        System.out.println("\n\n------------------当前时间：" + currentTime + "---------------");
                        printPCBStatus();
                    }
                }
            }
            //不是空闲状态，表示有进程正在运行
            else{
                if(currentTime%20==0){
                    System.out.println("\n\n------------------当前时间：" + currentTime + "---------------");
                    printPCBStatus();
                }
                runATimeUnit();
            }
        }
    }

    /**
     * 计算平均周转时间
     */
    protected void calculateAvgTurnaroundTime(){
        double sum = 0;
        for (int i = 0; i < finishedPCB.size(); i++) {
            sum += finishedPCB.get(i).getTurnaroundTime();
        }
        avgTurnaroundTime = sum/finishedPCB.size();
    }

    /**
     * 当前进程运行一个时间单位
     */
    protected void runATimeUnit(){
        currentPCB.addRunTime();//运行 一个时间单位
        readyQueue.addWaitTime();//就绪队列中的进程等待时间+1
        //若是已运行完成，设置完成时间，改变状态、计算周转时间，并放到完成队列
        if(currentPCB.getNeedTime()==currentPCB.getHadRunTime()){
            currentPCB.setFinishedTime(currentTime+1);//在currentTime这个点运行了一个时间单位后完成，所以完成时间是currentTime+1
            currentPCB.setStatus(PCB.FINISH);
            currentPCB.calculateTurnaroundTime();
            finishedPCB.add(currentPCB);
            currentPCB = null;
            isLeisure = true;
        }
    }

    /**
     * 输出当前各进程概况
     */
    protected void printPCBStatus(){
        System.out.println("==========================================================");
        if(readyQueue.isEmpty() && currentPCB==null){
            System.out.println("当前就绪队列为空，cpu空闲");
        }
        else {
            readyQueue.print();
            System.out.println();
            notReachQueue.print();
            System.out.println();
            System.out.println(">>>>>>>>>正在运行的进程：");
            System.out.println(currentPCB);
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<");
            System.out.println();
            System.out.println(">>>>>>>>>已运行完成进程：");
            for (PCB pcb : finishedPCB) {
                System.out.println(pcb);
            }
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<");
        }
        System.out.println("==========================================================");
    }

    public double getAvgTurnaroundTime(){
        return avgTurnaroundTime;
    }

    /**
     * 进程完成顺序
     */
    protected void printFinishStatus(){
        System.out.println("进程完成顺序：");
        for (PCB pcb : finishedPCB) {
            System.out.println(pcb);
        }
        System.out.println("====OVER====");
    }
}
