package test;

import run.Reqest;
import run.Runner;
import zone.Memory;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args){
        List<Reqest> reqests = generateReqests();
        Runner runner = new Runner(reqests);

        // runner.run(Memory.FF);
        runner.run(Memory.BF);

    }

    public static List<Reqest> generateReqests(){
        List<Reqest> reqestList = new ArrayList<>();
        Reqest r1 = new Reqest(1,Reqest.APPLY, 130);
        Reqest r2 = new Reqest(2,Reqest.APPLY, 60);
        Reqest r3 = new Reqest(3,Reqest.APPLY, 100);
        Reqest r4 = new Reqest(2,Reqest.RELEASE, 60);
        Reqest r5 = new Reqest(4,Reqest.APPLY, 200);
        Reqest r6 = new Reqest(3,Reqest.RELEASE, 100);
        Reqest r7 = new Reqest(1,Reqest.RELEASE, 130);
        Reqest r8 = new Reqest(5,Reqest.APPLY, 140);
        Reqest r9 = new Reqest(6,Reqest.APPLY, 60);
        Reqest r10 = new Reqest(7,Reqest.APPLY, 50);
        Reqest r11 = new Reqest(8,Reqest.APPLY, 60);
        reqestList.add(r1);
        reqestList.add(r2);
        reqestList.add(r3);
        reqestList.add(r4);
        reqestList.add(r5);
        reqestList.add(r6);
        reqestList.add(r7);
        reqestList.add(r8);
        reqestList.add(r9);
        reqestList.add(r10);
        reqestList.add(r11);
        return reqestList;
    }
}
