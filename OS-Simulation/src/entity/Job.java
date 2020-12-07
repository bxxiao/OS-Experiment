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
        return jobName + "{" +
                "需要时间：" + needTIme +
                ", 需要内存：" + needMemory +
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
