package pcb;

import java.util.*;

/**
 * 就绪队列
 */
public class ReadyQueue {
    private List<PCB> queue = new ArrayList<>();


    /**
     * 第一个进程出队
     */
    public PCB poll(){
        return queue.remove(0);
    }

    /**
     * 进程入队
     */
    public void offer(PCB pcb){
        queue.add(pcb);
    }

    public void addWaitTime(){
        for (PCB pcb : queue) {
            pcb.addWaitTime();
        }
    }

    public boolean isEmpty(){
        return queue.size()==0;
    }

    public void print(){
        System.out.println(">>>>>>>>>就绪队列：");
        for (PCB pcb : queue) {
            System.out.println(pcb);
        }
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<");
    }

    /**
     * 根据短进程优先对就绪队列排序
     */
    public void sortInSJF(){
        Collections.sort(queue, (o1, o2) -> {
            //按所需运行时间从小到大排序；若所需运行时间相同，按等待时间从大到小排序
            if(o1.getNeedTime()<o2.getNeedTime()){
                return -1;
            }
            else if(o1.getNeedTime()>o2.getNeedTime()){
                return 1;
            }
            else{
                if(o1.getHadWaitTime()>o2.getHadWaitTime()){
                    return -1;
                }
                else if(o1.getHadWaitTime()<o2.getHadWaitTime()){
                    return 1;
                }
                else{
                    return 0;
                }
            }
        });
    }

    /**
     * 根据高响应比优先调度算法对就绪队列排序
     */
    public void sortInHRRN(){
        Collections.sort(queue, (o1, o2) -> {
            //按所需运行时间从小到大排序；若所需运行时间相同，按等待时间从大到小排序
            if(o1.getRespRatio()>o2.getRespRatio()){
                return -1;
            }
            else if(o1.getRespRatio()<o2.getRespRatio()){
                return 1;
            }
            else{
                if(o1.getHadWaitTime()>o2.getHadWaitTime()){
                    return -1;
                }
                else if(o1.getHadWaitTime()<o2.getHadWaitTime()){
                    return 1;
                }
                else{
                    return 0;
                }
            }
        });
    }
}
