
import java.util.ArrayList;
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
                    displayWaitingTime();
                }else{
                	
                	displayProcessInformation();            
                	
                	runningProcess.runProcess();   	
                    
                    if(runningProcess.isFinished()){
                        //processes.remove(runningProcess);
                    	updateWaitingProcesses(runningProcess);
                        runningProcess = null;
                        opQuantum = quantum;
                    } else { // get another process with higher priority considering quantum.
                    	updateWaitingProcesses(runningProcess);
                    	runningProcess = getNextProcess(runningProcess);
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
    
    private Process getNextProcess(Process runningProcess) {
    	Integer priority = runningProcess.getPriority();
    	Integer insertionTime = runningProcess.getInsertionTime();
    	Process currentProcess = runningProcess;
    	
    	for (Process process : processes) {
    		
    		if (process.isFinished() || process.equals(runningProcess))
    			continue;
    		
			if(process.getPriority() < priority) {
				priority = process.getPriority();
				currentProcess = process;
			}
			
			else if(process.getPriority() == priority) {
				
				if ( isQuantumRelevant() ) {
					if ( opQuantum > 0 ) {
						opQuantum--;
						return runningProcess;
					}
					else {
						currentProcess = process;
						opQuantum = quantum;
					}
				} else if (process.getInsertionTime() < insertionTime) {
					currentProcess = process;
				}
			}
			
		}
    	
    	return currentProcess;
    }
    
    private boolean isQuantumRelevant() {
    	
    	if( quantum == 0 )
    		return false;
    	
    	return true;
    	
    }
    
    private void updateWaitingProcesses( Process runningProcess ) {
    	for (Process process : processes) {
			if(process.equals(runningProcess))
				continue;
			
			if(process.isFinished() == false) {
				process.addWaitingTime();
			}
		}
    }
    
    private void displayWaitingTime() {
    	SISOPInterface.outputTextArea.setText("IDLE! \n");
    	
    	if (processes.size() == 0)
    		return;
    	
    	Integer amountOfProcesses = processes.size();
        Integer waitingTime = 0;
        
        for (Process process : processes) {
        	waitingTime += process.getWaitingTime();
		}
        float averageWaitingTime = waitingTime / amountOfProcesses;
        SISOPInterface.outputTextArea.append("AVERAGE WAITING TIME = " 
                + averageWaitingTime);
    }
    
    private void displayProcessInformation() {
    	SISOPInterface.outputTextArea.setText("RUNNING PROCESS PID = "
    										+ runningProcess.getPid());

    	SISOPInterface.outputTextArea.append("\n");

		SISOPInterface.outputTextArea.append("INSERTION TIME = " 
		        			+ runningProcess.getInsertionTime());
		
		SISOPInterface.outputTextArea.append("\n");
		
		SISOPInterface.outputTextArea.append("PRIORITY = " 
		        			+ runningProcess.getPriority());
		
		SISOPInterface.outputTextArea.append("\n");
		
		SISOPInterface.outputTextArea.append("REMAINING TIME = " 
		        			+ runningProcess.getRemainingTime());
    }
    
}
