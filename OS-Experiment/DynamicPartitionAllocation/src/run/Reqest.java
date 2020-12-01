package run;

/**
 * 对作业发出请求的封装
 */
public class Reqest {
    //请求的类型，申请或释放内存
    public static final int APPLY = 0;
    public static final int RELEASE = 1;

    private int jobId;//作业id
    private int type;//类型
    private int reqSize;//请求内存大小

    public Reqest(int jobId, int type, int reqSize){
        this.jobId = jobId;
        this.type = type;
        this.reqSize = reqSize;
    }

    //getter、setter
    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getReqSize() {
        return reqSize;
    }

    public void setReqSize(int reqSize) {
        this.reqSize = reqSize;
    }
}
