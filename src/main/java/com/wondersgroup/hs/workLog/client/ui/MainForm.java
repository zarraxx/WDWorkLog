package com.wondersgroup.hs.workLog.client.ui;

import com.wondersgroup.hs.workLog.client.core.LogClientWSImpl;
import com.wondersgroup.hs.workLog.client.core.WorkLog;
import com.wondersgroup.hs.workLog.client.core.WorkLogEditSheet;
import org.apache.http.cookie.Cookie;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zarra on 15/3/9.
 */
public class MainForm {
    private JPanel panel1;
    private JComboBox cbWeek;
    private JButton bPrev;
    private JButton bThisWeek;
    private JButton bNext;
    private JButton addButton;
    private JButton removeButton;
    private JTable tbLogs;

    private Cookie cookie;

    private LogClientWSImpl client;

    private String logsURL;
    private String addLogURL;
    private String removeLogURL;
    private String thisWeek;

    private List<WorkLog> logCache = new ArrayList<>();

    private boolean isLoading;

    final static private String[] columns = {"Select","确认","ID","项目名称","模块/子系统名","任务类型",
            "已经投入","实际","弹性工作","计划","尚需投入","工作描述","实际投入日期","修改时间"};


    class LogTableModel extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return logCache.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            String v = "";
            WorkLog log = logCache.get(rowIndex);
            switch (columnIndex){
                case 0:
                    break;
                case 1:
                    v=log.getStatus();
                    break;
                case 2:
                    v=log.getId();
                    break;
                case 3:
                    v=log.getProjectName();
                    break;
                case 4:
                    v=log.getModuleName();
                    break;
                case 5:
                    v=log.getTaskType();
                    break;
                case 6:
                    v=log.getWorkedDay();
                    break;
                case 7:
                    v=log.getUseDay();
                    break;
                case 8:
                    v=log.getFlexDay();
                    break;
                case 9:
                    v=log.getPlanDay();
                    break;
                case 10:
                    v=log.getWillDay();
                    break;
                case 11:
                    v=log.getDesc();
                    break;
                case 12:
                    v=log.getDate();
                    break;
                case 13:
                    v=log.getModifyTime();
                    break;
            }
            return v;
        }

        @Override
        public String getColumnName(int col) {
            return columns[col];
        }
    }

    public MainForm() {
        final MainForm self = this;
        bPrev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = cbWeek.getSelectedIndex();
                index = index-1 >0 ?index-1:0;
                cbWeek.setSelectedIndex(index);
                String week = (String) cbWeek.getSelectedItem();
                self.setupLogs(week);
            }
        });


        bThisWeek.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cbWeek.setSelectedItem(thisWeek);
                self.setupLogs(thisWeek);
            }
        });
        bNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = cbWeek.getSelectedIndex();
                int size  = cbWeek.getItemCount();
                index = index  >= size-1  ? size - 1 :index+1;
                cbWeek.setSelectedIndex(index);
                String week = (String) cbWeek.getSelectedItem();
                self.setupLogs(week);
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String html = client.getEditLogPage(addLogURL,cookie);
                    WorkLogEditSheet editSheet = client.getEditSheet(html);

                    //System.out.println(editSheet);
                    EditLogDialog editLogDialog = new EditLogDialog();
                    editLogDialog.setCookie(cookie);
                    editLogDialog.setEditSheet(editSheet);

                    editLogDialog.setSize(800,400);
                    editLogDialog.setVisible(true);


                    setupLogs((String) cbWeek.getSelectedItem());

                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error when getEditLogPage");
                }
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTable target = tbLogs;
                int row = target.getSelectedRow();
                WorkLog log = logCache.get(row);
                try {
                    boolean ok = client.removeWorkLog(log.getId(),cookie);
                    if (ok){
                        JOptionPane.showMessageDialog(null, "Remove Log Success");
                        setupLogs((String) cbWeek.getSelectedItem());
                    }else{
                        JOptionPane.showMessageDialog(null, "Remove Log Fail");
                    }
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, "Error when remove Log");
                    e1.printStackTrace();
                }
            }
        });
        cbWeek.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isLoading)
                    setupLogs((String) cbWeek.getSelectedItem());
            }
        });
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public static void openMainFrame(Cookie cookie){
        JFrame frame = new JFrame("WorkLog");
        MainForm mainForm = new MainForm();
        mainForm.client = new LogClientWSImpl();
        mainForm.setCookie(cookie);



        frame.setContentPane(mainForm.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);

        mainForm.setupData();
    }



    public void setupData(){
        String html = null;
        try {
            isLoading = true;
            html = client.getPersonWorkLogPage(cookie);

            String[] urls = client.getFuncURLS(html);
            logsURL = urls[2];


            String[] weeks = client.getWeeks(html);

            thisWeek = weeks[0];


            for (int i=1;i<weeks.length;i++){
                cbWeek.addItem(weeks[i]);
            }

            cbWeek.setSelectedItem(thisWeek);

            tbLogs.setModel(new LogTableModel());

            tbLogs.addMouseListener(new MouseInputAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (e.getClickCount() == 2) {
                        JTable target = (JTable)e.getSource();
                        int row = target.getSelectedRow();
                        WorkLog log = logCache.get(row);

                        try {
                            String html = client.getEditLogPage(log.getUrl(),cookie);
                            WorkLogEditSheet editSheet = client.getEditSheet(html);

                            //System.out.println(editSheet);
                            EditLogDialog editLogDialog = new EditLogDialog();
                            editLogDialog.setCookie(cookie);
                            editLogDialog.setEditSheet(editSheet);

                            editLogDialog.setSize(800,400);
                            editLogDialog.setVisible(true);

                        } catch (IOException e1) {
                            e1.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error when getEditLogPage");
                        }

                        //System.out.println(row);
                    }
                }
            });



            isLoading = false;
            this.setupLogs(thisWeek);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error when setupData");
        }
    }

    public void setupLogs(String week){
        try {
            String html = client.getLogsPage(logsURL,week,cookie);

            addLogURL = client.getLogFuncURLs(html)[0];
            removeLogURL = client.getLogFuncURLs(html)[1];

            //System.out.println(html);

            WorkLog[] logs = client.getWorkLogs(html);
            logCache.clear();

            for (WorkLog log:logs){
                logCache.add(log);
                //System.out.println(log);
            }

            AbstractTableModel tableModel = (AbstractTableModel) tbLogs.getModel();
            tableModel.fireTableStructureChanged();

            TableColumnModel columnModel = tbLogs.getColumnModel();

            columnModel.getColumn(0).setMaxWidth(40);

            columnModel.getColumn(1).setMaxWidth(60);
            columnModel.getColumn(2).setMaxWidth(70);

            columnModel.getColumn(3).setMaxWidth(150);
            columnModel.getColumn(4).setMaxWidth(150);

            columnModel.getColumn(5).setMaxWidth(120);

            columnModel.getColumn(6).setMaxWidth(40);
            columnModel.getColumn(7).setMaxWidth(40);
            columnModel.getColumn(8).setMaxWidth(40);
            columnModel.getColumn(9).setMaxWidth(40);
            columnModel.getColumn(10).setMaxWidth(40);

            columnModel.getColumn(11).setMinWidth(200);

            columnModel.getColumn(12).setMaxWidth(140);
            columnModel.getColumn(13).setMaxWidth(180);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error when setupLogs");
        }
    }
}
