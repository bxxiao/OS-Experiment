package test;

import entity.Job;
import os.Runner;

import java.util.LinkedList;
import java.util.Queue;

public class Test {
    public static void main(String[] args) {
        Queue<Job> jobs = new LinkedList<>();
        Job job1 = new Job("作业1", 5, 30);
        Job job2 = new Job("作业2", 20, 50);
        Job job3 = new Job("作业3", 5, 1024);
        Job job4 = new Job("作业4", 14, 1000);
        jobs.offer(job1);
        jobs.offer(job2);
        jobs.offer(job3);
        jobs.offer(job4);

        Runner runner = new Runner(jobs, 5);
        runner.run();
    }

}
