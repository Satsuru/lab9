package com.example.pharmacy;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginBtn;
    private DBConnection db;

    public LoginFrame() {
        setTitle("Вход в систему");
        setSize(300, 180);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel userLabel = new JLabel("Логин:");
        userLabel.setBounds(20, 20, 80, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(100, 20, 150, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Пароль:");
        passLabel.setBounds(20, 60, 80, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 60, 150, 25);
        add(passwordField);

        loginBtn = new JButton("Войти");
        loginBtn.setBounds(90, 100, 100, 30);
        add(loginBtn);

        db = new DBConnection();
        db.init();

        loginBtn.addActionListener(e -> login());
    }

    private void login() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        try {
            Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT role FROM users WHERE username=? AND password=?");
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                InsertFrame app = new InsertFrame(role);
                app.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Неверные имя пользователя или пароль");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
