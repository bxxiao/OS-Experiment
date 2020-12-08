package entity;

/**
 * 进程
 */
public class PCB {
    //进程的4种状态
    public static final String READY = "就绪";
    public static final String RUNNING = "运行中";
    public static final String BLOCK = "阻塞";
    public static final String FINISHED = "完成";

    private String pcbName;
    private Job job;
    private Zone zone;//进程被分配的内存结点
    private String status = READY;
    private int hadRunTime = 0;
    private int finishTime = -1;//进程运行完成的时间点
    private int blockTime = 0;//进程被阻塞时的剩余阻塞时间
    private int waitTime = 0;

    public PCB(String pcbName, Job job, Zone zone) {
        this.pcbName = pcbName;
        this.job = job;
        this.zone = zone;
    }

    /**
     * 是否已完成
     * @return
     */
    public boolean isFinished(){
        return hadRunTime==job.getNeedTIme();
    }

    public void addWaitTime(){
        this.waitTime++;
    }

    public void reduceBlockTIme(){
        blockTime--;
    }

    public void addRunTime(){
        this.hadRunTime++;
    }

    @Override
    public String toString() {
        return pcbName + "{ " +
                job + (zone==null?"":(", 内存结点：" + zone)) +
                ", 状态：" + status +
                ", 已运行时间：" + (hadRunTime + (hadRunTime>=100?"":(hadRunTime>=10?" ":"  "))) +
                ", 等待时间：" + (waitTime + (waitTime>=100?"":(waitTime>=10?" ":"  "))) +
                ((status==PCB.BLOCK)?(", 剩余阻塞时间：" + (blockTime>=10?blockTime:(blockTime+" "))):"") +
                (finishTime==-1?"":(", 进程完成时间：" + finishTime)) +
                '}';
    }

    //getter setter
    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public int getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(int blockTime) {
        this.blockTime = blockTime;
    }

    public String getPcbName() {
        return pcbName;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getHadRunTime() {
        return hadRunTime;
    }

    public void setHadRunTime(int hadRunTime) {
        this.hadRunTime = hadRunTime;
    }
}
