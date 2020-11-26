package dispatcher;

import pcb.PCB;
import java.util.List;

/**
 * 基于短进程优先的进程调度器
 */
public class SFJDispatcher extends Dispatcher{

    public SFJDispatcher(List<PCB> pcbs){
        super(pcbs);
    }

    @Override
    public void run(){
       runInModel(Dispatcher.SFJ_MODEL);
    }
}
