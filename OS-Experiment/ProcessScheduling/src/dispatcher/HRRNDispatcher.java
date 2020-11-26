package dispatcher;

import pcb.PCB;

import java.util.List;

/**
 * 基于高响应比优先的进程调度器
 */
public class HRRNDispatcher extends Dispatcher {
    public HRRNDispatcher(List<PCB> pcbs) {
        super(pcbs);
    }

    @Override
    public void run() {
        runInModel(Dispatcher.HRRN_MODEL);
    }
}
