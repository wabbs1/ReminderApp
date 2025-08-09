/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sjii.sg.reminderapp;
import java.io.*;
import java.nio.channels.*;
/**
 *
 * @author abhi
 */
public class SingleInstanceManager {
    private static FileLock lock;
    private static FileChannel channel;
    private static File lockFile;

    public static boolean lockInstance(String lockFileName) {
        try {
            lockFile = new File(System.getProperty("user.home"), lockFileName);
            channel = new RandomAccessFile(lockFile, "rw").getChannel();

            lock = channel.tryLock();
            if (lock == null) {
                // Already locked by another instance
                channel.close();
                return false;
            }

            // Add shutdown hook to release lock on exit
            Runtime.getRuntime().addShutdownHook(new Thread(() -> releaseLock()));

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void releaseLock() {
        try {
            if (lock != null) {
                lock.release();
                channel.close();
                lockFile.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}