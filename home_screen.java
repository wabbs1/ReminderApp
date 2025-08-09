/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.sjii.sg.reminderapp;
import java.awt.Color;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ButtonGroup;
/**
 *
 * @author abhi
 */
public class home_screen extends javax.swing.JFrame implements Serializable {
    private Reminder reminder;
    private ArrayList<Reminder> reminderList;
    private Storage storage;
    private TrayIcon trayIcon;
    /**
     * Creates new form home_screen
     */
    public home_screen() {
        initComponents();
        this.setTitle("Abhi's reminder app");
        Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/icon.png"));
        this.setIconImage(image);
        storage= new Storage();
        Storage.deserialize();
        this.reminderList = storage.getReminderList();
        System.out.println(reminderList.size());
        setLocationRelativeTo(null);
        LocalDateTime rn = LocalDateTime.now();
        fillReminderTables();
        notificationSender();
        initialiseTabChangeListener();
        initializeTray();
        initialiseTableClickListeners();
        initializeButtonGroup();
        acknowledgeButton.setVisible(false);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
    }
    
    public void fillReminderTables(){
        System.out.println("refreshing reminder tables");
        clearReminderTables();
        sortReminderList();
        DefaultTableModel reminderTableModel = (DefaultTableModel)reminderTable.getModel();
        DefaultTableModel dueTodayTableModel = (DefaultTableModel)dueTodayTable.getModel();
        DefaultTableModel pastDueTableModel = (DefaultTableModel)pastDueTable.getModel();
        DefaultTableModel pastRemindersTableModel = (DefaultTableModel)pastRemindersTable.getModel();
        // Hide the column that stores the Reminder object
        reminderTable.getColumnModel().getColumn(4).setMinWidth(0);
        reminderTable.getColumnModel().getColumn(4).setMaxWidth(0);
        reminderTable.getColumnModel().getColumn(4).setWidth(0);
        dueTodayTable.getColumnModel().getColumn(3).setMinWidth(0);
        dueTodayTable.getColumnModel().getColumn(3).setMaxWidth(0);
        dueTodayTable.getColumnModel().getColumn(3).setWidth(0);
        pastDueTable.getColumnModel().getColumn(4).setMinWidth(0);
        pastDueTable.getColumnModel().getColumn(4).setMaxWidth(0);
        pastDueTable.getColumnModel().getColumn(4).setWidth(0);
        pastRemindersTable.getColumnModel().getColumn(4).setMinWidth(0);
        pastRemindersTable.getColumnModel().getColumn(4).setMaxWidth(0);
        pastRemindersTable.getColumnModel().getColumn(4).setWidth(0);
        
        reminderTable.setRowSelectionAllowed(true);
        reminderTable.setColumnSelectionAllowed(false); // optional: only allow row selection
        reminderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reminderTable.setSelectionBackground(Color.LIGHT_GRAY);
        reminderTable.setSelectionForeground(Color.BLACK);
        
        dueTodayTable.setRowSelectionAllowed(true);
        dueTodayTable.setColumnSelectionAllowed(false); // optional: only allow row selection
        dueTodayTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dueTodayTable.setSelectionBackground(Color.LIGHT_GRAY);
        dueTodayTable.setSelectionForeground(Color.BLACK);
        
        pastDueTable.setRowSelectionAllowed(true);
        pastDueTable.setColumnSelectionAllowed(false); // optional: only allow row selection
        pastDueTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pastDueTable.setSelectionBackground(Color.LIGHT_GRAY);
        pastDueTable.setSelectionForeground(Color.BLACK);
        
        pastRemindersTable.setRowSelectionAllowed(true);
        pastRemindersTable.setColumnSelectionAllowed(false); // optional: only allow row selection
        pastRemindersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pastRemindersTable.setSelectionBackground(Color.LIGHT_GRAY);
        pastRemindersTable.setSelectionForeground(Color.BLACK);
        
        Object[] reminderRowData= new Object [5];
        Object[] dueTodayRowData= new Object [4];
        if(!reminderList.isEmpty()){for (int i=0;i<reminderList.size();i++){
            if (reminderList.get(i).getDue().isAfter(LocalDateTime.now())){
            reminderRowData[0]=reminderList.get(i).getTitle();
            reminderRowData[1]=reminderList.get(i).getDescription();
            LocalDate chosenDueDate = reminderList.get(i).getDueDate();
            reminderRowData[2]=(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(chosenDueDate));
            reminderRowData[3]=reminderList.get(i).gettime_due();
            reminderRowData[4]=reminderList.get(i);
            System.out.println(reminderList.get(i).getDueDate().toString());
            System.out.println(LocalDate.now().toString());
            reminderTableModel.addRow(reminderRowData);
            System.out.println(LocalDate.now().toString().equals(reminderList.get(i).getDueDate().toString()));
            if(LocalDate.now().toString().equals(reminderList.get(i).getDueDate().toString())){
                dueTodayRowData[0]=reminderList.get(i).getTitle();
                dueTodayRowData[1]=reminderList.get(i).getDescription();
                dueTodayRowData[2]=reminderList.get(i).gettime_due();
                dueTodayRowData[3]=reminderList.get(i);
                System.out.println(dueTodayRowData[3]);
                dueTodayTableModel.addRow(dueTodayRowData);
            }
            }else if (reminderList.get(i).getDue().isBefore(LocalDateTime.now())){
                if(reminderList.get(i).isAcknowledged()==false){
                Object[] pastDueRowData=new Object [5];
                pastDueRowData[0]=reminderList.get(i).getTitle();
                pastDueRowData[1]=reminderList.get(i).getDescription();
                LocalDate chosenDueDate = reminderList.get(i).getDueDate();
                pastDueRowData[2]=(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(chosenDueDate));
                pastDueRowData[3]=reminderList.get(i).gettime_due();
                pastDueRowData[4]=reminderList.get(i);
                pastDueTableModel.addRow(pastDueRowData);
                }else{
                Object[] pastRemindersRowData = new Object [5];
                pastRemindersRowData[0]=reminderList.get(i).getTitle();
                pastRemindersRowData[1]=reminderList.get(i).getDescription();
                LocalDate chosenDueDate = reminderList.get(i).getDueDate();
                pastRemindersRowData[2]=(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(chosenDueDate));
                pastRemindersRowData[3]=reminderList.get(i).gettime_due();
                pastRemindersRowData[4]=reminderList.get(i);
                pastRemindersTableModel.addRow(pastRemindersRowData);
            }
            
        }}
        
        //System.out.println(reminderTable.getModel().getValueAt(0, 4));
        
    }}    
    
    public void initialiseTableClickListeners(){
    initialiseReminderTableClickListner();
        initialiseDueTodayTableClickListner();
        initialisePastDueTableClickListener();
        initialisePastTremindersTableClickListener();
    }
    
    public void clearReminderTables()
    {
        DefaultTableModel reminderTableModel = (DefaultTableModel)reminderTable.getModel();
        while(reminderTableModel.getRowCount() > 0)
        {
            reminderTableModel.removeRow(0);
            System.out.println("reminder table row removed");
        }
        DefaultTableModel dueTodayTableModel = (DefaultTableModel)dueTodayTable.getModel();
        while(dueTodayTableModel.getRowCount() > 0)
        {
            dueTodayTableModel.removeRow(0);
            System.out.println("due today table row removed");
        }
        DefaultTableModel pastDueTableModel = (DefaultTableModel)pastDueTable.getModel();
        while(pastDueTableModel.getRowCount()>0)
        {
            pastDueTableModel.removeRow(0);
            System.out.println("past due table row removed");
        }
        DefaultTableModel pastRemindersTableModel = (DefaultTableModel)pastRemindersTable.getModel();
        while(pastRemindersTableModel.getRowCount()>0)
        {
            pastRemindersTableModel.removeRow(0);
            System.out.println("past reminders table row removed");
        }
    }
    public void sortReminderList(){
        reminderList.sort(Comparator.comparing(Reminder::getDue));
        
    }
    public void initialiseReminderTableClickListner(){
    
        reminderTable.getSelectionModel().addListSelectionListener(e->{
        if (!e.getValueIsAdjusting()) {
        int selectedRow = reminderTable.getSelectedRow();

            
        if (selectedRow != -1) {
            // If using sorting, convert to reminderTableModel 
            System.out.println("selected");
            int modelRow = reminderTable.convertRowIndexToModel(selectedRow);

            String title = (String) reminderTable.getModel().getValueAt(modelRow, 0);
            String date = reminderTable.getModel().getValueAt(modelRow, 2).toString();
            String time = reminderTable.getModel().getValueAt(modelRow,3).toString();    
            detailsLabel.setText("Reminder: " + title + " on " + date+" at "+time);
        }else{
            detailsLabel.setText(" ");
        }
    }
        });
    }
    public void initialiseDueTodayTableClickListner(){
    
        dueTodayTable.getSelectionModel().addListSelectionListener(e->{
        if (!e.getValueIsAdjusting()) {
            

        int selectedRow = dueTodayTable.getSelectedRow();
        if (selectedRow != -1) {
            // If using sorting, convert to reminderTableModel index
            System.out.println("selected");
            int modelRow = dueTodayTable.convertRowIndexToModel(selectedRow);

            String title = (String) dueTodayTable.getModel().getValueAt(modelRow, 0);
            String time = dueTodayTable.getModel().getValueAt(modelRow, 2).toString();

            detailsLabel.setText("Reminder: " + title + " at " + time);
            
        }else{
            detailsLabel.setText(" ");
        }
    }
        }
        );
        
    }
    public void initialisePastDueTableClickListener(){
        pastDueTable.getSelectionModel().addListSelectionListener(e->{
        if (!e.getValueIsAdjusting()) {
        int selectedRow = pastDueTable.getSelectedRow();
        if (selectedRow != -1) {
            // If using sorting, convert to reminderTableModel index
            System.out.println("selected");
            int modelRow = pastDueTable.convertRowIndexToModel(selectedRow);

            String title = (String) pastDueTable.getModel().getValueAt(modelRow, 0);
            String date = pastDueTable.getModel().getValueAt(modelRow, 2).toString();
            String time = pastDueTable.getModel().getValueAt(modelRow,3).toString();    
            detailsLabel.setText("Reminder: " + title + " on " + date+" at "+time);
            
        }else{
            detailsLabel.setText(" ");
        }
    }
        }
        );
    }
    public void initialisePastTremindersTableClickListener(){
        pastRemindersTable.getSelectionModel().addListSelectionListener(e->{
        if (!e.getValueIsAdjusting()) {
        int selectedRow = pastRemindersTable.getSelectedRow();
        if (selectedRow != -1) {
            System.out.println("selected");
            int modelRow = pastRemindersTable.convertRowIndexToModel(selectedRow);

            String title = (String) pastRemindersTable.getModel().getValueAt(modelRow, 0);
            String date = pastRemindersTable.getModel().getValueAt(modelRow, 2).toString();
            String time = pastRemindersTable.getModel().getValueAt(modelRow,3).toString();
            detailsLabel.setText("Reminder: " + title + " on " + date+ " at "+time);
            
        }else{
            detailsLabel.setText(" ");
        }
    }
        });
    }
    public void initialiseTabChangeListener(){
        reminderTabs.addChangeListener(e->{
        int selectedIndex = reminderTabs.getSelectedIndex();
        if(selectedIndex==2){
            acknowledgeButton.setVisible(true);
        }else{
            acknowledgeButton.setVisible(false);
        }
        });
    }
    public void initializeTray(){
        SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/icon.png"));
            
            PopupMenu popup = new PopupMenu();
            MenuItem openItem = new MenuItem("Open");
            MenuItem exitItem = new MenuItem("Exit");

            openItem.addActionListener(e -> this.setVisible(true));
            exitItem.addActionListener(e -> {
                tray.remove(trayIcon);
                System.exit(0);
            });

            popup.add(openItem);
            popup.add(exitItem);
            
            trayIcon = new TrayIcon(image, "Reminder App",popup);
            trayIcon.setImageAutoSize(true);
        try {
        // Initialize the tray icon once
            tray.add(trayIcon); // Only add ONCE
        }catch (Exception e) {
        e.printStackTrace();
    }
    }
    
    public void notificationSender(){
    Timer timer = new Timer();
    TimerTask sendFirstTimeReminders = new TimerTask() {
        @Override
        public void run() {
            System.out.println("Checking reminders...");
            fillReminderTables();
            LocalDateTime now = LocalDateTime.now();

            for (int i = 0; i < reminderList.size(); i++) {
                Reminder reminder = reminderList.get(i);
                if (now.isAfter(reminder.getDue()) && !reminder.isNotified()) {
                    reminder.setNotified();
                    Storage.serialize();

                    trayIcon.displayMessage(
                        "Reminder: " + reminder.getTitle() + " at " + reminder.gettime_due(),
                        reminder.getDescription(),
                        MessageType.INFO
                    );
                }
            }
        }
    };
    
    timer.scheduleAtFixedRate(sendFirstTimeReminders, 0, 60_000);
    sendUnacknowledgedReminders();
  }
    private Timer unacknowledgedTimer;
    public void sendUnacknowledgedReminders(){
    if (unacknowledgedTimer!=null){
    unacknowledgedTimer.cancel();
    }
    
    unacknowledgedTimer = new Timer();
    TimerTask sendUnAck = new TimerTask(){
    @Override
    public void run(){
    System.out.println("checking for unacknowledged reminders every " + Storage.reminderRepeatInterval/1000 + "seconds");
        fillReminderTables();
        int count =0;
        String message = "";
        for (int i=0;i<reminderList.size();i++){
            Reminder reminder = reminderList.get(i);
            if(reminder.isNotified()&&!reminder.isAcknowledged()){
            count+=1;
            message = message + reminder.getTitle()+", ";
            }
        }
        if(count>0){trayIcon.displayMessage(count+" unacknowledged reminders",message,MessageType.INFO);}
    }
    };
    unacknowledgedTimer.scheduleAtFixedRate(sendUnAck,0,Storage.reminderRepeatInterval);
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        App_title_label = new javax.swing.JLabel();
        add_reminder_button = new javax.swing.JButton();
        TitleTextField = new javax.swing.JTextField();
        date_picker = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        HourComboBox = new javax.swing.JComboBox<>();
        MinuteComboBox = new javax.swing.JComboBox<>();
        hourLabel = new javax.swing.JLabel();
        minuteLabel = new javax.swing.JLabel();
        detailsLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        DescriptionTextArea = new javax.swing.JTextArea();
        clearReminderSelectionButton = new javax.swing.JButton();
        deleteReminderButton = new javax.swing.JButton();
        reminderTabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        reminderTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        dueTodayTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        pastDueTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        pastRemindersTable = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        oneHourRadioButton = new javax.swing.JRadioButton();
        halfHourRadioButton = new javax.swing.JRadioButton();
        quarterHourRadioButton = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        acknowledgeButton = new javax.swing.JButton();
        viewReminderButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        App_title_label.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        App_title_label.setText("Abhi's reminder App");

        add_reminder_button.setText("add reminder");
        add_reminder_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_reminder_buttonActionPerformed(evt);
            }
        });

        date_picker.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                date_pickerPropertyChange(evt);
            }
        });

        jLabel1.setText("title");

        jLabel2.setText("description");

        jLabel3.setText("date");

        HourComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23" }));
        HourComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HourComboBoxActionPerformed(evt);
            }
        });

        MinuteComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
            "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
            "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
            "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
            "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
            "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"
        }));

        hourLabel.setText("Hour");

        minuteLabel.setText("Minute");

        detailsLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        DescriptionTextArea.setColumns(20);
        DescriptionTextArea.setRows(5);
        DescriptionTextArea.setAutoscrolls(false);
        jScrollPane1.setViewportView(DescriptionTextArea);
        DescriptionTextArea.setLineWrap(true);
        DescriptionTextArea.setWrapStyleWord(true);

        clearReminderSelectionButton.setText("clear reminder selection");
        clearReminderSelectionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearReminderSelectionButtonActionPerformed(evt);
            }
        });

        deleteReminderButton.setText("delete reminder");
        deleteReminderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteReminderButtonActionPerformed(evt);
            }
        });

        reminderTabs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reminderTabsMouseClicked(evt);
            }
        });

        reminderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title", "Description", "Date", "Time", "Title 5"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        reminderTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                reminderTableFocusGained(evt);
            }
        });
        reminderTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reminderTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(reminderTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 690, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(9, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        reminderTabs.addTab("all reminders", jPanel1);

        dueTodayTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title", "Description", "Time", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dueTodayTable.setEnabled(false);
        dueTodayTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dueTodayTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(dueTodayTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 693, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        reminderTabs.addTab("due today", jPanel2);

        jPanel3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPanel3FocusGained(evt);
            }
        });
        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel3MouseClicked(evt);
            }
        });

        pastDueTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "title", "description", "date", "time", "null"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        pastDueTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pastDueTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(pastDueTable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 681, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                .addContainerGap())
        );

        reminderTabs.addTab("past due", jPanel3);

        pastRemindersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title", "Description", "Date", "Time", "Title 5"
            }
        ));
        jScrollPane5.setViewportView(pastRemindersTable);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 683, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                .addContainerGap())
        );

        reminderTabs.addTab("past reminders", jPanel4);

        oneHourRadioButton.setText("1 hour");
        oneHourRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oneHourRadioButtonActionPerformed(evt);
            }
        });

        halfHourRadioButton.setText("30 minutes");
        halfHourRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                halfHourRadioButtonActionPerformed(evt);
            }
        });

        quarterHourRadioButton.setText("15 minutes");
        quarterHourRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quarterHourRadioButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("prompt unacknowledged reminders every:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(quarterHourRadioButton)
                    .addComponent(halfHourRadioButton)
                    .addComponent(oneHourRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(430, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(oneHourRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(halfHourRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(quarterHourRadioButton)
                .addContainerGap(320, Short.MAX_VALUE))
        );

        reminderTabs.addTab("preferences", jPanel5);

        acknowledgeButton.setText("acknowledge");
        acknowledgeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acknowledgeButtonActionPerformed(evt);
            }
        });

        viewReminderButton.setText("view reminder");
        viewReminderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewReminderButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(App_title_label, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(hourLabel)
                                                    .addComponent(minuteLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(MinuteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(HourComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(add_reminder_button, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                        .addComponent(jLabel3)
                                                        .addGap(65, 65, 65)
                                                        .addComponent(date_picker, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(83, 83, 83)))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel2)
                                            .addGap(38, 38, 38)
                                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(TitleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(3, 3, 3))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(detailsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(clearReminderSelectionButton, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                            .addComponent(deleteReminderButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(acknowledgeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(viewReminderButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(reminderTabs, javax.swing.GroupLayout.PREFERRED_SIZE, 705, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(App_title_label, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TitleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(date_picker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(62, 62, 62)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(HourComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(hourLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(MinuteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(minuteLabel))
                        .addGap(28, 28, 28)
                        .addComponent(add_reminder_button)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(detailsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(clearReminderSelectionButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deleteReminderButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(viewReminderButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                                .addComponent(acknowledgeButton))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(reminderTabs, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void add_reminder_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_reminder_buttonActionPerformed
        // TODO add your handling code here:
        LocalDate chosen_date = null;
        try{chosen_date = date_picker.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        System.out.println(chosen_date);
        String chosenHour= HourComboBox.getSelectedItem().toString();
        String chosenMinute= MinuteComboBox.getSelectedItem().toString();
        Reminder x = new Reminder(TitleTextField.getText(),DescriptionTextArea.getText(),chosen_date,chosenHour,chosenMinute);
        if (LocalDateTime.now().isAfter(x.getDue())||x.getTitle().isEmpty()){
            System.out.println("invalid");
            if(LocalDateTime.now().isAfter(x.getDue())&&x.getTitle().isEmpty()){   
                new invalidReminder("you have not set a title and your date and time is past the current, try again").setVisible(true);
            }else if(LocalDateTime.now().isAfter(x.getDue())&&!x.getTitle().isEmpty()){
                new invalidReminder("the date and time you have sent is past the current, try again").setVisible(true);
            }else if(!LocalDateTime.now().isAfter(x.getDue())&&x.getTitle().isEmpty()){
                new invalidReminder("the title you have set is not valid try again").setVisible(true);
            }
        }else{this.reminderList.add(x);
        Storage.serialize();}
        fillReminderTables();}
        catch(NullPointerException e){
            new invalidReminder("you have not set a date, set a valid date in your reminder.").setVisible(true);
        }
    }//GEN-LAST:event_add_reminder_buttonActionPerformed

    private void date_pickerPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_date_pickerPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_date_pickerPropertyChange

    private void HourComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HourComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_HourComboBoxActionPerformed

    private void reminderTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reminderTableMouseClicked
        // TODO add your handling code here:
        System.out.println("reminder table clicked");
    }//GEN-LAST:event_reminderTableMouseClicked

    private void clearReminderSelectionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearReminderSelectionButtonActionPerformed
        // TODO add your handling code here:
        reminderTable.clearSelection();
        dueTodayTable.clearSelection();
    }//GEN-LAST:event_clearReminderSelectionButtonActionPerformed

    private void deleteReminderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteReminderButtonActionPerformed
        // TODO add your handling code here:
        int reminderTableSelectedRow = reminderTable.getSelectedRow();
        int dueTodayTableSelectedRow = dueTodayTable.getSelectedRow();
        int pastDueTableSelectedRow = pastDueTable.getSelectedRow();
        int pastRemindersTableSelectedRow = pastRemindersTable.getSelectedRow();
        System.out.println(reminderTableSelectedRow);
        System.out.println(dueTodayTableSelectedRow);
        System.out.println(pastDueTableSelectedRow);
        System.out.println(pastRemindersTableSelectedRow);
         if(reminderTableSelectedRow !=-1){
            int modelRow = reminderTable.convertRowIndexToModel(reminderTableSelectedRow);
            Reminder x = (Reminder) reminderTable.getModel().getValueAt(modelRow,4);
            reminderList.remove(x);
            Storage.serialize();
            fillReminderTables();
        }else if (dueTodayTableSelectedRow!=-1){
            int modelRow = dueTodayTable.convertRowIndexToModel(dueTodayTableSelectedRow);
            Reminder x = (Reminder) dueTodayTable.getModel().getValueAt(modelRow,3);
            reminderList.remove(x);
            Storage.serialize();
            fillReminderTables();
        }else if (pastDueTableSelectedRow!=-1){
            int modelRow = pastDueTable.convertRowIndexToModel(pastDueTableSelectedRow);
            Reminder x = (Reminder) pastDueTable.getModel().getValueAt(modelRow,4);
            reminderList.remove(x);
            Storage.serialize();
            fillReminderTables();
        }else if (pastRemindersTableSelectedRow!=-1){
            int modelRow = pastRemindersTable.convertRowIndexToModel(pastRemindersTableSelectedRow);
            Reminder x = (Reminder) pastRemindersTable.getModel().getValueAt(modelRow,4);
            reminderList.remove(x);
            Storage.serialize();
            fillReminderTables();
        }
    }//GEN-LAST:event_deleteReminderButtonActionPerformed

    private void reminderTabsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reminderTabsMouseClicked
        // TODO add your handling code here:
        
        reminderTable.clearSelection();
        dueTodayTable.clearSelection();
        pastDueTable.clearSelection();
        pastRemindersTable.clearSelection();
    }//GEN-LAST:event_reminderTabsMouseClicked

    private void dueTodayTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dueTodayTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_dueTodayTableMouseClicked

    private void pastDueTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pastDueTableMouseClicked

    }//GEN-LAST:event_pastDueTableMouseClicked

    private void reminderTableFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_reminderTableFocusGained

    }//GEN-LAST:event_reminderTableFocusGained

    private void jPanel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jPanel3MouseClicked

    private void jPanel3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel3FocusGained

    }//GEN-LAST:event_jPanel3FocusGained

    private void acknowledgeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acknowledgeButtonActionPerformed
        // TODO add your handling code here:
        int pastDueTableSelectedRow = pastDueTable.getSelectedRow();
        System.out.println(pastDueTableSelectedRow);
        if (pastDueTableSelectedRow!=1){
            int modelRow = pastDueTable.convertRowIndexToModel(pastDueTableSelectedRow);
            Reminder x = (Reminder) pastDueTable.getModel().getValueAt(modelRow,4);
            x.setAcknowledged();
            Storage.serialize();
            fillReminderTables();
        }
    }//GEN-LAST:event_acknowledgeButtonActionPerformed

    private void oneHourRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oneHourRadioButtonActionPerformed
        // TODO add your handling code here:
        Storage.reminderRepeatInterval=3_600_000;
        Storage.serializeInterval();
        sendUnacknowledgedReminders();
    }//GEN-LAST:event_oneHourRadioButtonActionPerformed

    private void halfHourRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_halfHourRadioButtonActionPerformed
        // TODO add your handling code here:
        Storage.reminderRepeatInterval=1_800_000;
        Storage.serializeInterval();
        sendUnacknowledgedReminders();
    }//GEN-LAST:event_halfHourRadioButtonActionPerformed

    private void quarterHourRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quarterHourRadioButtonActionPerformed
        // TODO add your handling code here:
        Storage.reminderRepeatInterval=900_000;
        Storage.serializeInterval();
        sendUnacknowledgedReminders();
    }//GEN-LAST:event_quarterHourRadioButtonActionPerformed

    private void viewReminderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewReminderButtonActionPerformed
        // TODO add your handling code here:
        int reminderTableSelectedRow = reminderTable.getSelectedRow();
        int dueTodayTableSelectedRow = dueTodayTable.getSelectedRow();
        int pastDueTableSelectedRow = pastDueTable.getSelectedRow();
        int pastRemindersTableSelectedRow = pastRemindersTable.getSelectedRow();
        String title=null;
        String date_and_time = null;
        String description = null;
        System.out.println(reminderTableSelectedRow);
        System.out.println(dueTodayTableSelectedRow);
        System.out.println(pastDueTableSelectedRow);
        System.out.println(pastRemindersTableSelectedRow);
         if(reminderTableSelectedRow !=-1){
            int modelRow = reminderTable.convertRowIndexToModel(reminderTableSelectedRow);
            Reminder x = (Reminder) reminderTable.getModel().getValueAt(modelRow,4);
            title = x.getTitle();
            date_and_time = x.getDueDate().toString()+" at "+x.gettime_due().toString();
            description=x.getDescription();
        }else if (dueTodayTableSelectedRow!=-1){
            int modelRow = dueTodayTable.convertRowIndexToModel(dueTodayTableSelectedRow);
            Reminder x = (Reminder) dueTodayTable.getModel().getValueAt(modelRow,3);
            title = x.getTitle();
            date_and_time = x.getDueDate().toString()+" at "+x.gettime_due().toString();
            description=x.getDescription();
        }else if (pastDueTableSelectedRow!=-1){
            int modelRow = pastDueTable.convertRowIndexToModel(pastDueTableSelectedRow);
            Reminder x = (Reminder) pastDueTable.getModel().getValueAt(modelRow,4);
            title = x.getTitle();
            date_and_time = x.getDueDate().toString()+" at "+x.gettime_due().toString();
            description=x.getDescription();
        }else if (pastRemindersTableSelectedRow!=-1){
            int modelRow = pastRemindersTable.convertRowIndexToModel(pastRemindersTableSelectedRow);
            Reminder x = (Reminder) pastRemindersTable.getModel().getValueAt(modelRow,4);
            title = x.getTitle();
            date_and_time = x.getDueDate().toString()+" at "+x.gettime_due().toString();
            description=x.getDescription();
        }
        ReminderViewer viewer = new ReminderViewer(title,date_and_time,description);
        if(title!=null){viewer.setVisible(true);}
        
    }//GEN-LAST:event_viewReminderButtonActionPerformed

    public void initializeButtonGroup(){
        ButtonGroup x = new ButtonGroup();
        x.add(oneHourRadioButton);
        x.add(halfHourRadioButton);
        x.add(quarterHourRadioButton);
        
        Storage.deserializeInterval();
        
        if (Storage.reminderRepeatInterval == 3_600_000) {
    oneHourRadioButton.setSelected(true);
} else if (Storage.reminderRepeatInterval == 1_800_000) {
    halfHourRadioButton.setSelected(true);
} else if (Storage.reminderRepeatInterval == 900_000) {
    quarterHourRadioButton.setSelected(true);
} else {
    Storage.reminderRepeatInterval = 3_600_000;
    oneHourRadioButton.setSelected(true);
}
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(home_screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(home_screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(home_screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(home_screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        if (!SingleInstanceManager.lockInstance(".myapp.lock")) {
        System.out.println("Another instance is already running.");
        System.exit(0);
    }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new home_screen().setVisible(true);
            }
        });
        
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel App_title_label;
    private javax.swing.JTextArea DescriptionTextArea;
    private javax.swing.JComboBox<String> HourComboBox;
    private javax.swing.JComboBox<String> MinuteComboBox;
    private javax.swing.JTextField TitleTextField;
    private javax.swing.JButton acknowledgeButton;
    private javax.swing.JButton add_reminder_button;
    private javax.swing.JButton clearReminderSelectionButton;
    private com.toedter.calendar.JDateChooser date_picker;
    private javax.swing.JButton deleteReminderButton;
    private javax.swing.JLabel detailsLabel;
    private javax.swing.JTable dueTodayTable;
    private javax.swing.JRadioButton halfHourRadioButton;
    private javax.swing.JLabel hourLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel minuteLabel;
    private javax.swing.JRadioButton oneHourRadioButton;
    private javax.swing.JTable pastDueTable;
    private javax.swing.JTable pastRemindersTable;
    private javax.swing.JRadioButton quarterHourRadioButton;
    private javax.swing.JTable reminderTable;
    private javax.swing.JTabbedPane reminderTabs;
    private javax.swing.JButton viewReminderButton;
    // End of variables declaration//GEN-END:variables
}
