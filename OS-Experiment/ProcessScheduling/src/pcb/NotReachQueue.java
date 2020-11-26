package pcb;

import java.util.List;

/**
 * 未到达的进程队列
 */
public class NotReachQueue {

    private List<PCB> queue;

    public NotReachQueue(List<PCB> queue) {
        this.queue = queue;
    }

    /**
     * 监听当前时间，将到达时间的进程加入就绪队列
     */
    public void listenTime(int currentTime, ReadyQueue readyQueue){
        for (int i = 0; i < queue.size(); i++) {
            if(queue.get(i).getArriveTime()==currentTime){
                PCB pcb = queue.remove(i);
                pcb.setStatus(PCB.READY);
                readyQueue.offer(pcb);
            }
        }
    }

    /**
     * 输出进程概况
     */
    public void print(){
        System.out.println(">>>>>>>>>未到达进程：");
        for (PCB pcb : queue) {
            System.out.println(pcb);
        }
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<");
    }

    public boolean isEmpty(){
        return queue.size()==0;
    }
}
