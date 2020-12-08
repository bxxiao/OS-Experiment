package test;

import run.Request;
import run.Runner;
import zone.Memory;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args){
        List<Request> requests = generateReqests();
        Runner runner = new Runner(requests);

        // runner.run(Memory.FF);
        runner.run(Memory.BF);

    }

    public static List<Request> generateReqests(){
        List<Request> requestList = new ArrayList<>();
        Request r1 = new Request(1, Request.APPLY, 130);
        Request r2 = new Request(2, Request.APPLY, 60);
        Request r3 = new Request(3, Request.APPLY, 100);
        Request r4 = new Request(2, Request.RELEASE, 60);
        Request r5 = new Request(4, Request.APPLY, 200);
        Request r6 = new Request(3, Request.RELEASE, 100);
        Request r7 = new Request(1, Request.RELEASE, 130);
        Request r8 = new Request(5, Request.APPLY, 140);
        Request r9 = new Request(6, Request.APPLY, 60);
        Request r10 = new Request(7, Request.APPLY, 50);
        Request r11 = new Request(8, Request.APPLY, 60);
        requestList.add(r1);
        requestList.add(r2);
        requestList.add(r3);
        requestList.add(r4);
        requestList.add(r5);
        requestList.add(r6);
        requestList.add(r7);
        requestList.add(r8);
        requestList.add(r9);
        requestList.add(r10);
        requestList.add(r11);
        return requestList;
    }
}
