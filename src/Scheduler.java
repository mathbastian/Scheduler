
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabriel
 */
public class Scheduler extends Thread{
    List<Process> processes = new ArrayList<Process>();
    private Boolean running = true;
    private Integer quantum = 0;
    private Integer opQuantum = 0;
    private Process runningProcess;
    private Integer nextPid = 0;
    private Integer currentTime = 0;
    
    public void addProcess(Process p){
        processes.add(p);
        updateCounter();
    }
    
    public Integer getCurrentTime(){
        return currentTime;
    }
    
    private void updateCounter(){
        SISOPInterface.labelProcessCount.setText("Processes Count: " + processes.size());
        SISOPInterface.labelCurrentTime.setText("Current Time: " + currentTime);
    }
    
    public void setQuantum(Integer quantum) {
        this.quantum = quantum;
        this.opQuantum = quantum;
    }
    
    public void stopScheduler(){
        running = false;
    }
    
    public Integer nextPid(){
        return nextPid++;
    }
    
    @Override
    public void run() {
        while(running){
            try {
                if(runningProcess == null){
                    for(Process p:processes){
                        if(!p.isFinished()){
                            runningProcess = p;
                            break;
                        }
                    }
                }
                
                if(runningProcess == null){
                    SISOPInterface.outputTextArea.setText("IDLE!");
                }else{
                    SISOPInterface.outputTextArea
                            .setText("RUNNING PROCESS PID = "
                            + runningProcess.getPid());
                    
                    SISOPInterface.outputTextArea.append("\n");
                    
                    SISOPInterface.outputTextArea.append("INSERTION TIME = " 
                            + runningProcess.getInsertionTime());
                    
                    SISOPInterface.outputTextArea.append("\n");
                    
                    SISOPInterface.outputTextArea.append("REMAINING TIME = " 
                            + runningProcess.getRemainingTime());
                    
                	runningProcess.runProcess();
                    
                    if(runningProcess.isFinished()){
                        processes.remove(runningProcess);
                        runningProcess = null;
                        opQuantum = quantum;
                    } else if ( opQuantum > 0 ) {
                    	
                    } else if ( opQuantum == 0 && opQuantum != quantum ) { //preemptivo com quantum
                    	
                    }
                    
                }
                
                currentTime++;
                updateCounter();
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private Integer getNextProcessIndex() {
    	Integer priority = 6;
    	Integer index = 0;
    	Process currentProcess = null;
    	for (Process process : processes) {
			if(process.getPriority() < priority) {
				priority = process.getPriority();
				index = processes.indexOf(process);
				currentProcess = process;
			}
			else if(process.getPriority() == priority) {
				if(process.getInsertionTime() < currentProcess.getInsertionTime()) {
					index = processes.indexOf(process);
					currentProcess = process;
				}
			}
		}
    	return processes.indexOf(currentProcess);
    }
    
}
