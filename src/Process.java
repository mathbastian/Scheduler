/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabriel
 */
public class Process {
    private Integer pid;
    private Integer priority;
    private Integer totalTime;
    private Integer remainingTime;
    private Integer insertionTime;
    private Boolean finished;

    public Process(Integer pid, Integer priority, Integer totalTime, Integer currentTime) {
        this.pid = pid;
        this.priority = priority;
        this.totalTime = totalTime;
        this.remainingTime = totalTime;
        this.finished = false;
        this.insertionTime = currentTime;
    }

    /**
     * @return the pid
     */
    public Integer getPid() {
        return pid;
    }
    
    /**
     * @return the priority
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * @return the totalTime
     */
    public Integer getTotalTime() {
        return totalTime;
    }

    /**
     * @return the remainingTime
     */
    public Integer getRemainingTime() {
        return remainingTime;
    }
    
    public Integer getInsertionTime(){
        return insertionTime;
    }
    
    public void runProcess(){
        if(remainingTime > 0){
            remainingTime--;
        }
        
        finished = remainingTime == 0;
    }
        
    public Boolean isFinished(){
        return finished;
    }
}
