package test;

import entity.Job;
import os.Runner;

import java.util.LinkedList;
import java.util.Queue;

public class Test {
    public static void main(String[] args) {
        Queue<Job> jobs = generateJobs();
        Runner runner = new Runner(jobs, 10);
        runner.run();
    }

    private static Queue<Job> generateJobs(){
        Queue<Job> jobs = new LinkedList<>();
        //为了使命令行输出整齐，指定作业名时若id小于10，补充一个空格
        Job job1 = new Job("作业1 ", 25, 16);
        Job job2 = new Job("作业2 ", 40, 256);
        Job job3 = new Job("作业3 ", 37, 1000);
        Job job4 = new Job("作业4 ", 100, 512);
        Job job5 = new Job("作业5 ", 45, 24);
        Job job6 = new Job("作业6 ", 15, 64);
        Job job7 = new Job("作业7 ", 26, 312);
        Job job8 = new Job("作业8 ", 60, 30);
        Job job9 = new Job("作业9 ", 53, 55);
        Job job10 = new Job("作业10", 31, 256);
        Job job11 = new Job("作业11", 94, 999);
        Job job12 = new Job("作业12", 50, 80);
        Job job13 = new Job("作业13", 12, 100);
        jobs.offer(job1);
        jobs.offer(job2);
        jobs.offer(job3);
        jobs.offer(job4);
        jobs.offer(job5);
        jobs.offer(job6);
        jobs.offer(job7);
        jobs.offer(job8);
        jobs.offer(job9);
        jobs.offer(job10);
        jobs.offer(job11);
        jobs.offer(job12);
        jobs.offer(job13);
        return jobs;
    }
}
