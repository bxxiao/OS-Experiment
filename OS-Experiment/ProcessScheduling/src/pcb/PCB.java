package pcb;

import java.io.Serializable;

public class PCB implements Serializable {
    //进程的4种状态
    public static final String NOT_REACH = "未到达";
    public static final String READY = "就绪";
    public static final String RUN = "运行中";
    public static final String FINISH = "已完成";

    private String PCBName;//进程名
    private double respRatio = 1;//响应比，响应比=（等待时间+要求服务时间）/要求服务时间，即（hadWaitTime+needTime）/needTime，初始值为1
    private int arriveTime;//到达时间
    private int hadWaitTime = 0;//已等待时间
    private int needTime;//所需运行时间
    private int hadRunTime = 0;//已运行时间
    private int finishedTime = -1;//完成运行时的时间，为-1表示未完成
    private int turnaroundTime = -1;//周转时间
    private String status = PCB.NOT_REACH;//进程状态

    /**
     * 等待时间+1，并计算响应比
     */
    public void addWaitTime(){
        this.hadWaitTime++;
        respRatio = ((double)hadWaitTime)/((double)needTime) + 1;
    }

    public void addRunTime(){
        this.hadRunTime++;
    }

    /**
     * 计算周转时间
     */
    public void calculateTurnaroundTime(){
        turnaroundTime = finishedTime - arriveTime;
    }

    /**
     * 打印进程的部分信息
     * @return
     */
    public String printOutlineInfo(){
        return PCBName + "{" +
                "到达时间：" + (arriveTime>=10?arriveTime:(" "+arriveTime)) +
                ", 需要时间：" + (needTime>=10?needTime:(" "+needTime)) +
                ", 已等待时间：" + (hadWaitTime>=10?hadWaitTime:(" "+hadWaitTime)) +
                ", 完成时间：" + (finishedTime>=10?finishedTime:(" "+finishedTime)) +
                ", 周转时间：" + (turnaroundTime>=10?turnaroundTime:(" "+turnaroundTime)) +
                ", 响应比：" + respRatio +
                '}';
    }

    @Override
    public String toString() {
        return  PCBName + "{" +
                "当前状态：" + status +
                ", 到达时间：" + (arriveTime>=10?arriveTime:(" "+arriveTime)) +
                ", 已等待时间：" + (hadWaitTime>=10?hadWaitTime:(" "+hadWaitTime)) +
                ", 需要时间：" + (needTime>=10?needTime:(" "+needTime)) +
                ", 已运行时间：" + (hadRunTime>=10?hadRunTime:(" "+hadRunTime)) +
                ", 完成时间：" + (finishedTime>=10?finishedTime:(" "+finishedTime)) +
                ", 周转时间：" + (turnaroundTime>=10?turnaroundTime:(" "+turnaroundTime)) +
                ", 响应比：" + respRatio +
                '}';
    }

    //getter  setter
    public int getHadWaitTime() {
        return hadWaitTime;
    }

    public void setHadWaitTime(int hadWaitTime) {
        this.hadWaitTime = hadWaitTime;
    }

    public String getPCBName() {
        return PCBName;
    }

    public void setPCBName(String PCBName) {
        this.PCBName = PCBName;
    }

    public double getRespRatio() {
        return respRatio;
    }

    public void setRespRatio(double respRatio) {
        this.respRatio = respRatio;
    }

    public int getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(int arriveTime) {
        this.arriveTime = arriveTime;
    }

    public int getNeedTime() {
        return needTime;
    }

    public void setNeedTime(int needTime) {
        this.needTime = needTime;
    }

    public int getHadRunTime() {
        return hadRunTime;
    }

    public void setHasRunTime(int hasRunTime) {
        this.hadRunTime = hasRunTime;
    }

    public int getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(int finishedTime) {
        this.finishedTime = finishedTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }
}
