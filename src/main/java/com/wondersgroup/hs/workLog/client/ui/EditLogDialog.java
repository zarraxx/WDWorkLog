package com.wondersgroup.hs.workLog.client.ui;

import com.wondersgroup.hs.workLog.client.core.LogClientWSImpl;
import com.wondersgroup.hs.workLog.client.core.WorkLogEditSheet;
import org.apache.http.cookie.Cookie;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditLogDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel pTop;
    private JPanel pRoot;
    private JPanel pLine1;
    private JComboBox cbWeek;
    private JComboBox cbName;
    private JComboBox cbProject;
    private JComboBox cbModule;
    private JPanel pLine2;
    private JPanel pLine3;
    private JComboBox cbTaskType;
    private JComboBox cbUsed;
    private JPanel pLine4;
    private JComboBox cbWorkDay;
    private JPanel pButtom;
    private JPanel pCenter;
    private JTextArea txtDesc;
    private JTextField txtFlex;


    private WorkLogEditSheet editSheet;

    private List<WorkLogEditSheet.Module> availableMods = new ArrayList<>();

    private boolean isInSetupData;

    private LogClientWSImpl client = new LogClientWSImpl();

    private Cookie cookie;

    private WorkLogEditSheet.Project currentProject;
    private WorkLogEditSheet.Module  currentModule;
    private WorkLogEditSheet.DayCost currentDayCost;

    public EditLogDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        cbProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editSheet != null && !isInSetupData){
                    int index = cbProject.getSelectedIndex();
                    WorkLogEditSheet.Project project = editSheet.getProjects()[index];
                    editSheet.setNmProjectSn(project.id);
                    currentProject = project;
                    updateModuleCB(project);
                }

            }
        });

        cbTaskType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editSheet != null && !isInSetupData)
                    editSheet.setStTaskType((String) cbTaskType.getSelectedItem());
            }
        });
        cbModule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editSheet != null && !isInSetupData) {
                    int index = cbModule.getSelectedIndex();
                    WorkLogEditSheet.Module module = availableMods.get(index);
                    editSheet.setNmTaskSn(module.id);
                    currentModule = module;
                }
            }
        });
        cbUsed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editSheet != null && !isInSetupData){
                    int index  = cbUsed.getSelectedIndex();
                    WorkLogEditSheet.DayCost cost = editSheet.getDayCosts()[index];
                    editSheet.setNmWeekCost(cost.value);
                    currentDayCost = cost;
                }
            }
        });
        cbWorkDay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editSheet != null && !isInSetupData) {
                    String value = (String) cbWorkDay.getSelectedItem();
                    editSheet.setStWeekDay(value);
                }
            }
        });
    }

    private void onOK() {
        Map<String,String> postBody = createPostData();
        try {
            client.submitWorkLog(postBody,cookie);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error when submit log");
        }
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public void setCookie(Cookie cookie){
        this.cookie = cookie;
    }


    public void setEditSheet(WorkLogEditSheet editSheet) {
        this.editSheet = editSheet;

        isInSetupData = true;

        cbWeek.removeAllItems();
        cbWeek.addItem(editSheet.getStWeekStart());
        cbWeek.setSelectedIndex(0);

        cbName.removeAllItems();
        cbName.addItem(editSheet.getStUserName());
        cbName.setSelectedIndex(0);

        cbProject.removeAllItems();
        WorkLogEditSheet.Project[] projects = editSheet.getProjects();
        int selectIndex = 0;
        for (int i =0;i<projects.length;i++){
            WorkLogEditSheet.Project project = projects[i];
            cbProject.addItem(project.name.trim());
            if (editSheet.getNmProjectSn().equals(project.id)){
                selectIndex = i;
            }
        }

        cbProject.setSelectedIndex(selectIndex);
        currentProject = projects[selectIndex];

        updateModuleCB(currentProject);



        String[] taskTypes = editSheet.getTaskTypes();
        selectIndex = 0;
        for (int i = 0 ;i<taskTypes.length;i++){
            String taskType = taskTypes[i];
            cbTaskType.addItem(taskType);
            //System.out.println(taskType+" vs "+editSheet.getStTaskType());
            if (taskType.equals(editSheet.getStTaskType()))
                selectIndex = i;
        }

        cbTaskType.setSelectedIndex(selectIndex);

        WorkLogEditSheet.DayCost[] dayCosts = editSheet.getDayCosts();
        selectIndex = 0;
        for (int i = 0 ;i<dayCosts.length;i++){
            WorkLogEditSheet.DayCost dayCost = dayCosts[i];
            cbUsed.addItem(dayCost.displayValue);
            //System.out.println(taskType+" vs "+editSheet.getStTaskType());
            if (dayCost.value.equals(editSheet.getNmWeekCost()))
                selectIndex = i;
        }
        cbUsed.setSelectedIndex(selectIndex);
        currentDayCost = dayCosts[cbUsed.getSelectedIndex()];

        try {
            String[] weekDays = client.getThisWeekData(editSheet.getStWeekStart(),editSheet.getId(),cookie);
            selectIndex = 0;
            for (int i = 0 ;i<weekDays.length;i++){
                String weekDay = weekDays[i];
                cbWorkDay.addItem(weekDay);
                //System.out.println(weekDay+" vs "+editSheet.getStWeekDay());
                if (weekDay.equals(editSheet.getStWeekDay()))
                    selectIndex = i;
            }
            cbWorkDay.setSelectedIndex(selectIndex);

        } catch (IOException e) {
            e.printStackTrace();
        }


        txtFlex.setText(editSheet.getNmOvertimeWork());
        txtDesc.setText(editSheet.getStWorkDesc());
        isInSetupData = false;


    }

    public void updateModuleCB(WorkLogEditSheet.Project project){
        cbModule.removeAllItems();
        availableMods.clear();
        int selectIndex = 0;
        for(WorkLogEditSheet.Module module:editSheet.getModules()){
            if (module.name.contains(project.id)){
                availableMods.add(module);
                if (module.id.equals(editSheet.getNmTaskSn())){
                     selectIndex = availableMods.size() -1;
                }
            }
        }

        for (WorkLogEditSheet.Module module : availableMods){
            cbModule.addItem(module.name);
        }

        cbModule.setSelectedIndex(selectIndex);
        currentModule = availableMods.get(selectIndex);
    }

    public Map<String,String> createPostData(){

        String taskType = (String) cbTaskType.getSelectedItem();
        String weekDay  = (String) cbWorkDay.getSelectedItem();

        System.out.println(taskType);
        System.out.println(weekDay);

        Map<String,String> postData = new HashMap<>();

        postData.put("wadCurrentPage","1");
        postData.put("stWeekStart",editSheet.getStWeekStart());
        postData.put("nmUserSn",editSheet.getUserID());
        postData.put("stUserName",editSheet.getStUserName());
        postData.put("nmProjectSn",currentProject.id);
        postData.put("nmTaskSn",currentModule.id);
        postData.put("stTaskType",taskType);
        postData.put("nmWeekCost",currentDayCost.value);
        postData.put("nmOvertimeWork",txtFlex.getText());
        postData.put("stWeekDay",weekDay);
        postData.put("stWorkDesc",txtDesc.getText());
        postData.put("nmWeekPlanCost","0");
        postData.put("nmAlreadyCost","1");


        if (editSheet.getId() != null){
            postData.put("wadFormName","modifyOneWorkLogByWeek");

            postData.put("stFullName",editSheet.getStUserName());
            postData.put("nmSn",editSheet.getId());
        }
        else {
            postData.put("wadFormName","addOneWorkLogByWeek");

            postData.put("stUserName",editSheet.getStUserName());
        }



        return postData;
    }

    public static void main(String[] args) {
        EditLogDialog dialog = new EditLogDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
