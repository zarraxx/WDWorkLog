package com.wondersgroup.hs.workLog.client.ui;

import com.wondersgroup.hs.workLog.client.core.LogClientWSImpl;
import org.apache.http.cookie.Cookie;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void onOK() {
        String username = txtUsername.getText();
        String password = String.valueOf(txtPassword.getPassword());

        LogClientWSImpl logClientWS = new LogClientWSImpl();
        try {
            Cookie cookie = logClientWS.getSessionCookie();
            boolean ok = logClientWS.postLogin(username,password,cookie);
            if (ok){
                dispose();
                MainForm.openMainFrame(cookie);
                return;
            }
            else{
                JOptionPane.showMessageDialog(null, "Username or password Error!");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error when login");
        }

        dispose();
    }
}
