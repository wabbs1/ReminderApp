/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sjii.sg.reminderapp;
import java.io.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author abhi
 */
public class Storage implements Serializable {
    private static ArrayList<Reminder> reminderList = new ArrayList<>();
    public static long reminderRepeatInterval = 3_600_000;
    
    static{}
    
    public static void main(String[] args){
        serialize();
    }
    
    public static void serialize() {
        try {
            FileOutputStream fileOut = new FileOutputStream("storage.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(reminderList);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in storage.txt");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static void deserialize() {
        try {
            FileInputStream fileIn = new FileInputStream("storage.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            reminderList = (ArrayList<Reminder>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
        }
    }
    
    public static void serializeInterval(){
    try{
    FileOutputStream fileOut = new FileOutputStream("interval_storage.txt");
    ObjectOutputStream out = new ObjectOutputStream(fileOut);
    out.writeObject(reminderRepeatInterval);
    out.close();
    fileOut.close();
    System.out.println("interval timing saved in storage.txt");
    }catch(IOException e){
    e.printStackTrace();
    }
    }
    
    public static void deserializeInterval(){
    try{
    FileInputStream fileIn = new FileInputStream("interval_storage.txt");
    ObjectInputStream in = new ObjectInputStream(fileIn);
    reminderRepeatInterval = (Long) in.readObject();
    in.close();
    fileIn.close();
    }catch(IOException|ClassNotFoundException e){
    reminderRepeatInterval = 3_600_600;
    }
    }
    
    public Reminder getReminder(int i){
        return reminderList.get(i);
    }
    
    public void addReminder(Reminder r){
        reminderList.add(r);
    }
    
    public void removeReminder(int i){
        reminderList.remove(i);
    }
    
    public ArrayList<Reminder> getReminderList(){
        return reminderList;
    }
}
