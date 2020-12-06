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


    public PCB dispatch(){
        return readyQueue.size()>0 ? readyQueue.poll() : null;
    }

    public void offerReadyPCBs(Queue<PCB> queue){
        while (queue.size()>0){
            readyQueue.offer(queue.poll());
        }
    }

    public void offerPCB(PCB pcb){
        readyQueue.offer(pcb);
    }

    public void addFinishPCB(PCB pcb){
        finishQueue.add(pcb);
    }

    /**
     * 减少阻塞队列中进程的剩余阻塞时间
     */
    public void reduceBlockTime(){
        int count = blockQueue.size();
        for (int i = 0; i < count; i++) {
            blockQueue.get(i).reduceBlockTIme();
            //若进程阻塞时间结束，则加入就绪队列
            if(blockQueue.get(i).getBlockTime()==0){
                readyQueue.offer(blockQueue.remove(i));
                i--;//让下一次循环还是当前位置
                count--;
            }
        }
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

    public void printFinishPCBs(){
        System.out.println("\n------------------------------------------------");
        System.out.println("完成队列：");
        for (PCB pcb : finishQueue) {
            System.out.println(pcb);
        }
        System.out.println("------------------------------------------------");
    }

    public void printBlockQueue(){
        System.out.println("\n------------------------------------------------");
        System.out.println("阻塞队列：");
        for (PCB pcb : blockQueue) {
            System.out.println(pcb);
        }
        System.out.println("------------------------------------------------");
    }

}
