package test;

import dispatcher.HRRNDispatcher;
import dispatcher.RRDispatcher;
import dispatcher.SFJDispatcher;
import pcb.PCB;
import util.PCBUtil;

import java.util.*;

public class Test {
    public static void main(String[] args){
        //打印分隔
        String separator = "\n\n\n\n***************************************************************************************************************************************\n" +
                "***************************************************************************************************************************************\n" +
                "***************************************************************************************************************************************\n\n\n\n";
        List<PCB> pcbs = PCBUtil.createPCBRandom();//随机创建测试进程
        // List<PCB> pcbs = getPCBSelf();//自定义测试进程
        SFJDispatcher sfj = new SFJDispatcher(pcbs);
        HRRNDispatcher hrrn = new HRRNDispatcher(pcbs);
        RRDispatcher rr = new RRDispatcher(pcbs, 10);//时间片长度指定为10

        System.out.println("测试进程如下：");
        for (PCB pcb : pcbs) {
            System.out.println(pcb.printOutlineInfo());
        }
        System.out.println("\n\n");

        sfj.run();//短进程优先调度
        System.out.println(separator);
        hrrn.run();//高响应比优先调度
        System.out.println(separator);
        rr.run();//时间片轮转调度

        System.out.println();
        System.out.println("===============模拟结束===============");
        System.out.println("短进程优先调度的平均周转时间：" + sfj.getAvgTurnaroundTime());
        System.out.println("高响应比优先调度的平均周转时间：" + hrrn.getAvgTurnaroundTime());
        System.out.println("时间片轮转调度的平均周转时间：" + rr.getAvgTurnaroundTime());
        System.out.println("======================================");

    }

    /**
     * 自定义进程
     */
    private static List<PCB> getPCBSelf(){
        List<PCB> result = new ArrayList<>();
        PCB pcb1 = new PCB();
        PCB pcb2 = new PCB();
        PCB pcb3 = new PCB();
        PCB pcb4 = new PCB();
        PCB pcb5 = new PCB();
        //设置进程名
        pcb1.setPCBName("进程A");
        pcb2.setPCBName("进程B");
        pcb3.setPCBName("进程C");
        pcb4.setPCBName("进程D");
        pcb5.setPCBName("进程E");
        //设置到达时间
        pcb1.setArriveTime(0);
        pcb2.setArriveTime(1);
        pcb3.setArriveTime(5);
        pcb4.setArriveTime(30);
        pcb5.setArriveTime(6);
        //设置所需运行时间
        pcb1.setNeedTime(30);
        pcb2.setNeedTime(36);
        pcb3.setNeedTime(25);
        pcb4.setNeedTime(48);
        pcb5.setNeedTime(50);

        result.add(pcb1);
        result.add(pcb2);
        result.add(pcb3);
        result.add(pcb4);
        result.add(pcb5);
        return result;
    }
}
