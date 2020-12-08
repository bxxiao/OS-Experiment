package os;

import entity.PCB;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 进程调度器
 */
public class PCBDispatcher {
    private Queue<PCB> readyQueue = new LinkedList<>();
    private List<PCB> finishQueue = new ArrayList<>();
    private List<PCB> blockQueue = new ArrayList<>();

    /**
     * 调度进程
     * 调度算法是RR，所以每次调度就绪队列的队首进程（若有）
     */
    public PCB dispatch(){
        PCB pcb = null;
        if(readyQueue.size()>0){
            pcb = readyQueue.poll();
            pcb.setStatus(PCB.RUNNING);
        }
        return pcb;
    }

    /**
     * 将批量进程加入就绪队列
     */
    public void offerReadyPCBs(Queue<PCB> queue){
        while (queue.size()>0){
            readyQueue.offer(queue.poll());
        }
    }

    /**
     * 进程加入就绪队列队尾
     * @param pcb
     */
    public void offerPCB(PCB pcb){
        readyQueue.offer(pcb);
    }

    public void addFinishPCB(PCB pcb){
        finishQueue.add(pcb);
    }

    /**
     * 经过一个时间点后，需要增加就绪队列、阻塞队列中进程的等待时间以及减少阻塞队列中队列的剩余阻塞时间
     */
    public void addATime(int time){
        reduceBlockTime(time);
        addWaitTIme();
    }

    public void addBlockPCB(PCB pcb){
        blockQueue.add(pcb);
    }

    /**
     * @return 就绪队列进程个数
     */
    public int getReadyPCBCount(){
        return readyQueue.size();
    }

    public boolean hasBlockPCB(){
        return blockQueue.size()>0;
    }

    //print

    /**
     * 完整输出完成队列的所有信息
     */
    public void printFinishPCBsUtterly(){
        System.out.println("完成队列：");
        if(finishQueue.size()==0){
            System.out.println("空");
        }
        else {
            for (PCB pcb : finishQueue) {
                System.out.println(pcb);
            }
        }
        System.out.println();
    }

    /**
     * 输出完成队列的概要信息
     */
    public void printFinishPCBsOutline(){
        System.out.println("完成队列：");
        if(finishQueue.size()==0){
            System.out.println("空");
        }
        else {
            String queue = "";
            int i = 1;
            for (PCB pcb : finishQueue) {
                queue += ("[" + pcb.getPcbName() + ",完成时间：" + pcb.getFinishTime() + "]-" + (i%6==0?"\n":""));
                i++;
            }
            queue = queue.substring(0, (i-1)%6==0?(queue.length()-2):(queue.length()-1));
            System.out.println(queue);
        }
        System.out.println();
    }

    public void printBlockQueue(){
        System.out.println("阻塞队列：");
        if(blockQueue.size()==0){
            System.out.println("空");
        }
        else {
            for (PCB pcb : blockQueue) {
                System.out.println(pcb);
            }
        }
        System.out.println();
    }

    public void printReadyQueue(){
        System.out.println("就绪队列：");
        if(readyQueue.size()==0){
            System.out.println("空");
        }
        else {
            for (PCB pcb : readyQueue) {
                System.out.println(pcb);
            }
        }
        System.out.println();
    }

    //private
    /**
     * 减少阻塞队列中进程的剩余阻塞时间，
     * 且若进程阻塞时间结束，则加入就绪队列
     */
    private void reduceBlockTime(int time){
        int count = blockQueue.size();
        for (int i = 0; i < count; i++) {
            blockQueue.get(i).reduceBlockTIme();
            //若进程阻塞时间结束，则加入就绪队列
            if(blockQueue.get(i).getBlockTime()==0){
                PCB pcb = blockQueue.remove(i);
                pcb.setStatus(PCB.READY);
                readyQueue.offer(pcb);
                System.out.println(">>>>>在时间点 " + time + "，" + pcb.getPcbName() + " 阻塞结束，进入就绪队列");
                i--;//让下一次循环还是当前位置
                count--;
            }
        }
    }

    private void addWaitTIme(){
        for (PCB pcb : readyQueue) {
            pcb.addWaitTime();
        }
        for (PCB pcb : blockQueue) {
            pcb.addWaitTime();
        }
    }
}
