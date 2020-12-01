package run;

import zone.Memory;
import zone.Zone;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Runner {
    private Map<Integer, Zone> allocatedZone;//存放已分配结点与作业id的对应关系
    private Memory memory;//内存
    private List<Reqest> reqests;//请求序列

    public Runner(List<Reqest> reqests){
        this.reqests = reqests;
        memory = new Memory();
        allocatedZone = new HashMap<>();
    }

    public void run(int model){
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>动态分区分配模拟开始，本次模拟使用 " + (model==Memory.FF?"首次适应算法":"最佳适应算法"));
        System.out.println("内存分区链表初始状态：");
        memory.printZoneList();
        System.out.println();

        for (int i = 0; i < reqests.size(); i++) {
            Reqest reqest = reqests.get(i);
            System.out.println("===========================================================================START");
            System.out.println("作业" + reqest.getJobId() + " 发出请求：" + (reqest.getType()==Reqest.APPLY?"申请":"释放")
                    + "，分区大小：" + reqest.getReqSize());
            //根据请求类型作对应操作
            switch (reqest.getType()){
                //请求空闲分区
                case Reqest.APPLY:
                    Zone reqZone = memory.requestInModel(reqest.getReqSize(), model);
                    if(reqZone==null){
                        System.out.println("请求空闲内存分区失败，无适合的空闲分区");
                        break;
                    }
                    allocatedZone.put(reqest.getJobId(), reqZone);//请求成功的分区结点放进map，与作业id对应
                    System.out.println("请求空闲分区成功，分配后的内存分区链表：");
                    memory.printZoneList();
                    break;
                //释放请求
                case Reqest.RELEASE:
                    if(allocatedZone.get(reqest.getJobId())==null){
                        System.out.println("错误，该作业无请求过分区");
                        break;
                    }
                    memory.recycle(allocatedZone.remove(reqest.getJobId()));//回收内存
                    System.out.println("回收内存成功，回收后的内存分区链表：");
                    memory.printZoneList();
                    break;
                default:
                    System.out.println("请选择正确的模式");
                    return;
            }
            System.out.println("===========================================================================END");
        }

        System.out.println("模拟结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }
}
