package os;

import entity.Job;
import entity.PCB;
import entity.Zone;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 作业调度器
 */
public class JobDispatcher {
    private Queue<Job> poolQueue;//后备队列
    private int pcbId = 1;//每次创建一个进程，进程名使用该值，并自增1

    public JobDispatcher(Queue<Job> poolQueue) {
        this.poolQueue = poolQueue;
    }

    /**
     * 调度作业
     * 对当前后备队列最多遍历其当前长度次，尝试调度出number个作业，为它们分配内存、创建PCB
     * @param number 请求调度的作业数
     * @param memory 内存
     * @return 返回创建的PCB
     */
    public Queue<PCB> dispatch(int number, Memory memory){
        Queue<PCB> result = new LinkedList<>();
        int count = 0;
        int forCount = poolQueue.size();

        for (int i = 0; i < forCount; i++) {
            //调度的个数到达指定个数，跳出循环
            if(count>=number) {
                break;
            }

            Job job = poolQueue.poll();
            Zone zone = memory.requestZone(job.getNeedMemory());//尝试申请内存
            //若申请成功
            if (zone != null) {
                //对pcbId的处理的为了显示的时候比较整齐
                PCB pcb = new PCB("进程" + (pcbId>=10?pcbId:(pcbId+" ")), job, zone);
                result.offer(pcb);
                zone.setPcbName(pcb.getPcbName());//设置内存结点占用进程的进程名
                pcbId++;
                count++;
            }
            else{
                poolQueue.offer(job);
            }
        }

        return result;
    }

    /**
     * 是否还有后备作业
     * @return
     */
    public boolean hasPoolJob(){
        return poolQueue.size()>0;
    }

    /**
     * 打印后备队列
     */
    public void printPoolQueue(){
        System.out.println("后备队列：");
        if(poolQueue.size()==0){
            System.out.println("空");
        }
        else {
            for (Job job : poolQueue) {
                System.out.println(job);
            }
        }
        System.out.println();
    }


























}
