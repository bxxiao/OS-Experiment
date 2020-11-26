package pcb;

public class PCB {
    //进程4种状态
    public static final String READY = "已就绪";
    public static final String RUN = "运行中";
    public static final String BLOCK = "阻塞";
    public static final String FINISH = "完成";

    private String pcbName;
    private String status = READY;

    public PCB(){}

    public PCB(String pcbName){
        this.pcbName = pcbName;
    }


    public String getPcbName() {
        return pcbName;
    }

    public void setPcbName(String pcbName) {
        this.pcbName = pcbName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
