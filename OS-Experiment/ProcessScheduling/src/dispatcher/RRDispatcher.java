package dispatcher;

import pcb.PCB;

import java.util.List;

public class RRDispatcher extends Dispatcher {
    private int timeSlice;//一个时间片大小

    public RRDispatcher(List<PCB> pcbs, int timeSlice) {
        super(pcbs);
        this.timeSlice = timeSlice;
    }

    @Override
    public void run() {
        System.out.println("》》》》》》》》》》》》》》》》》》》时间片轮转调度模拟开始，时间片大小为：" + timeSlice);
        while (true){
            currentTime++;
            notReachQueue.listenTime(currentTime, readyQueue);

            if(isLeisure){
                if(readyQueue.isEmpty() && notReachQueue.isEmpty()){
                    calculateAvgTurnaroundTime();
                    System.out.println("时间片轮转调度模拟结束《《《《《《《《《《《《《《《《《《《《《《《《《《《《《《《《《《《《《《《《《《《");
                    printFinishStatus();
                    break;
                }
                //就绪队列不为空，进行调度
                if(!readyQueue.isEmpty()) {
                    currentPCB = readyQueue.poll();//获取队列头进程
                    currentPCB.setStatus(PCB.RUN);
                    isLeisure = false;
                    //运行一个时间片的时间
                    System.out.println("\n");
                    System.out.println("==================时间片开始(时间点：" + currentTime + ")==========================BEGIN");
                    System.out.println("///////////////////////>>>>>刚开始时（刚调度完）的进程状态：");
                    printPCBStatus();
                    runTimeSlice();
                    System.out.println("\n\n///////////////////////>>>>>时间片结束时的进程状态：");
                    printPCBStatus();
                    System.out.println("==================时间片结束(时间点：" + currentTime + ")==========================END");
                }else {
                    if (currentTime % 20 == 0) {
                        System.out.println("\n\n------------------当前时间：" + currentTime + "---------------");
                        printPCBStatus();
                    }
                }
            }
        }
    }

    /**
     * 对进程执行一个时间片的时间
     */
    private void runTimeSlice(){
        int i = 0;
        while (i<timeSlice){
            runATimeUnit();

            //进程执行完成时，currentPCB会被置null
            //此时应跳出循环，不可增加currentTime。否则在run方法中又会+1，造成多加了1次
            if(currentPCB==null){
                return;
            }
            currentTime++;
            notReachQueue.listenTime(currentTime, readyQueue);
            i++;
        }
        //执行一个时间片后，若进程未结束，需要改变相关数据，并将进程转移到就绪队列
        currentPCB.setStatus(PCB.READY);
        readyQueue.offer(currentPCB);
        currentPCB = null;
        isLeisure = true;
    }
}
