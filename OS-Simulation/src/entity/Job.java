package entity;

/**
 * 作业
 */
public class Job {
    private String jobName;
    private int needTIme;
    private int needMemory;

    public Job(String jobName, int needTIme, int needMemory) {
        this.jobName = jobName;
        this.needTIme = needTIme;
        this.needMemory = needMemory;
    }

    public Job() {}

    @Override
    public String toString() {
        //填充空格
        String needTimeBlank = needTIme>=100?"":(needTIme>=10?" ":"  ");
        String needMemoryBlank =needMemory>=1000?"":(needMemory>=100?" ":(needMemory>=10?"  ":"   "));
        return jobName + "{" +
                "需要时间：" + (needTIme + needTimeBlank) +
                ", 需要内存：" + (needMemory + needMemoryBlank) +
                '}';
    }

    //getter setter
    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public int getNeedTIme() {
        return needTIme;
    }

    public void setNeedTIme(int needTIme) {
        this.needTIme = needTIme;
    }

    public int getNeedMemory() {
        return needMemory;
    }

    public void setNeedMemory(int needMemory) {
        this.needMemory = needMemory;
    }
}
