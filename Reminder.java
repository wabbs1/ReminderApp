/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sjii.sg.reminderapp;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
/**
 *
 * @author abhi
 */
public class Reminder implements Serializable {
    private String title;
    private String description;
    private LocalDate dueDate;
    private LocalTime time_due;
    private LocalDateTime due;
    private boolean notified;
    private boolean acknowledged;

    public Reminder(String title, String description, LocalDate dueDate, String hour, String minute) {
        this.title = title;
        if(description.isEmpty()){
            this.description="-";
        }else{
        this.description = description;}
        this.dueDate = dueDate;
        String timeInput= hour+":"+minute;
        this.time_due=LocalTime.parse(timeInput);
        this.due=LocalDateTime.of(dueDate,time_due);
        this.notified=false;
        this.acknowledged=false;
    }
    
    

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public LocalTime gettime_due(){
        return time_due;
    }

    public LocalDateTime getDue() {
        return due;
    }
    
    public void setNotified(){
        this.notified=true;
    }

    public boolean isNotified() {
        return notified;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged() {
        this.acknowledged = true;
    }
        
    
}
