package os;

import entity.Zone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 代表内存的类
 * 封装了一个分区链表，以及分区链表的分配内存、回收内存，搜索内存等操作
 */
public class Memory {
    public static final int MIN_SIZE = 15;

    //分区链表
    private Zone zoneList;
    //以升序的顺序存放空闲分区结点，用于最佳适应算法
    private List<Zone> bfZones;

    public Memory(){
        //初始时只有一个空闲结点，大小为640KB
        zoneList = new Zone(0, 1024, true);
        bfZones = new ArrayList<>();
        bfZones.add(zoneList);
    }

    /**
     * 根据请求内存大小分区请求
     * 若请求成功，返回分配的分区结点；否则返回null
     */
    public Zone requestZone(int reqSize){
        //使用ff算法找出适合的空闲分区
        Zone zone = null;

        zone = search(reqSize);

        //若找不到返回null；否则根据请求分区大小对结点进行分配操作，返回分配的分区结点
        return zone==null? null : allocate(zone, reqSize);
    }

    /**
     * 回收指定分区
     * 设置两个boolean变量标志当前分区的前分区、后分区情况：为非空闲或为null对应false；为空闲对应true
     * 再根据两个变量的值确定使用哪种回收方案
     */
    public void recycle(Zone zone){
        boolean preStatus = (zone.getPre()==null || !(zone.getPre().isLeisure())) ? false : true;
        boolean nextStatus = (zone.getNext()==null || !(zone.getNext().isLeisure())) ? false : true;;

        //前分区空闲、后分区非空闲，与前分区合并
        if(preStatus && !nextStatus){
            zone.getPre().addSize(zone.getSize());
            //删除当前分区（zone）
            zone.getPre().setNext(zone.getNext());
            if(zone.getNext()!=null){
                zone.getNext().setPre(zone.getPre());
            }
        }
        //前结点非空闲、后结点空闲,与后结点合并
        else if(!preStatus && nextStatus){
            //zone的zise加上后结点的size，并改变状态为空闲
            zone.addSize(zone.getNext().getSize());
            zone.setLeisure(true);

            //后结点从链表中移除
            zone.setNext(zone.getNext().getNext());
            //注意上面已修改了zone的next指针
            if(zone.getNext()!=null){
                zone.getNext().setPre(zone);
            }
        }
        //前结点、后结点皆空闲,3个结点合并
        else if(preStatus && nextStatus) {
            //前结点的size加上zone和后结点的size
            zone.getPre().addSize(zone.getSize() + zone.getNext().getSize());
            //把zone跟后结点移除
            zone.getPre().setNext(zone.getNext().getNext());
            if (zone.getNext().getNext() != null) {
                zone.getNext().getNext().setPre(zone.getPre());
            }
        }
        //前后结点都非空闲，创建新的空闲分区，即改变分区状态即可
        else{
            zone.setLeisure(true);
        }
        //回收分区后重置bfZones
        sortBfZones();
    }

    /**
     * 对指定的空闲分区结点执行分配操作，返回分配的分区结点
     * 该方法默认提供的分区合适
     */
    private Zone allocate(Zone zone, int reqSize){
        //若分区大小-请求大小不大于最小分区大小
        if(zone.getSize()-reqSize<= MIN_SIZE){
            zone.setLeisure(false);
            sortBfZones();
            return zone;
        }
        //否则切割当前结点（zone）
        else{
            //创建新结点，始址为zone的始址（优先使用空闲区低端地址）
            Zone newZone = new Zone(zone.getStart(), reqSize, false);

            //zone始址跟大小作对应改变
            zone.reduceSize(reqSize);
            zone.forwardStart(reqSize);

            //插入结点
            zone.insertBefore(newZone);
            //若是插入首结点之前，则需要改变首结点指向
            if(zoneList==zone){
                zoneList = newZone;
            }

            //创建新结点后重置bfZones
            sortBfZones();
            return newZone;
        }
    }

    /**
     * 使用最佳适应算法（BF）查找符合要求的空闲分区
     * （其实只是直接在bfZones中按顺序找，有适合的直接返回）
     * 返回找到的空闲分区结点；若找不到，返回null
     */
    private Zone search(int reqSize){
        for (Zone zone : bfZones) {
            if(zone.getSize() >= reqSize){
                return zone;
            }
        }
        return null;
    }

    /**
     * 以表格形式打印分区链表
     */
    public void printZoneList(){
        Zone current = zoneList;
        int index = 1;
        System.out.println("内存情况");
        System.out.println("分区结点号    始址     分区大小    状态     占用进程");
        while(current!=null){
            String number = index + (index>=10?"":" ");//结点号占两个位
            String start = current.getStart() + (current.getStart()>=10?(current.getStart()>=100?"":" "):"  ");//始址占3个位
            String size = current.getSize() + (current.getSize()>=10?(current.getSize()>=100?"":" "):"  ");//大小占3个位
            System.out.println("   " + number + "         " + start + "       " + size + "     " + (current.isLeisure()?"空闲  ":"非空闲")
                    + "     " + (current.getPcbName()==null?"无":current.getPcbName()));
            current = current.getNext();
            index++;
        }
    }

    /**
     * 清空bfZones，重新添加所有空闲分区进去，并排序
     * 该方法在分配内存存在切割的情况，或回收内存后都会被调用
     */
    private void sortBfZones(){
        bfZones.clear();
        Zone current = zoneList;
        while (current!=null){
            if(current.isLeisure()) {
                bfZones.add(current);
            }
            current = current.getNext();
        }

        Collections.sort(bfZones, Comparator.comparingInt(Zone::getSize));//按结点大小升序排序
    }
}
