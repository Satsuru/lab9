package com.example.pharmacy;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class InsertFrame extends JFrame {
    private JTable table;
    private JButton insertBtn, deleteBtn, editBtn, logoutBtn, searchButton;
    private JTextField[] fields = new JTextField[5];
    private JComboBox<String> diseaseBox;
    private JComboBox<String> filterBox;
    private JTextField searchField;
    private DBConnection db;
    private Statement stmt;
    private EditDialog editDialog;
    private String userRole;

    public InsertFrame(String role) {
        this.userRole = role;
        setTitle("Аптека — " + (role.equals("ADMIN") ? "Администратор" : "Гость"));
        setSize(800, 430);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        String[] labels = {"ID", "Название", "Год выпуска", "Годен до (ГГГГ-ММ-ДД)", "Цена"};
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setBounds(20, 20 + i * 30, 150, 25);
            add(label);
            fields[i] = new JTextField();
            fields[i].setBounds(170, 20 + i * 30, 150, 25);
            add(fields[i]);
        }

        JLabel diseaseLabel = new JLabel("Заболевание");
        diseaseLabel.setBounds(20, 170, 150, 25);
        add(diseaseLabel);
        diseaseBox = new JComboBox<>(new String[]{"Грипп", "Аллергия", "Covid-19", "Головная боль"});
        diseaseBox.setBounds(170, 170, 150, 25);
        add(diseaseBox);

        insertBtn = new JButton("Добавить");
        deleteBtn = new JButton("Удалить");
        editBtn = new JButton("Изменить");
        insertBtn.setBounds(20, 210, 90, 30);
        deleteBtn.setBounds(120, 210, 90, 30);
        editBtn.setBounds(220, 210, 110, 30);
        add(insertBtn);
        add(deleteBtn);
        add(editBtn);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(350, 20, 420, 300);
        add(scrollPane);

        searchField = new JTextField();
        searchField.setBounds(350, 330, 200, 25);
        add(searchField);

        searchButton = new JButton("Поиск");
        searchButton.setBounds(560, 330, 90, 25);
        add(searchButton);

        filterBox = new JComboBox<>(new String[]{"Все", "Грипп", "Аллергия", "Covid-19", "Головная боль"});
        filterBox.setBounds(660, 330, 100, 25);
        add(filterBox);

        logoutBtn = new JButton("Выход");
        logoutBtn.setBounds(660, 360, 100, 25);
        add(logoutBtn);

        db = new DBConnection();
        db.init();
        try {
            stmt = db.getConnection().createStatement();
            reloadTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        insertBtn.addActionListener(e -> insertData());
        deleteBtn.addActionListener(e -> deleteData());
        editBtn.addActionListener(e -> editData());
        searchButton.addActionListener(e -> search());
        filterBox.addActionListener(e -> filter());
        logoutBtn.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });

        editDialog = new EditDialog(this, true, stmt);

        if (!userRole.equals("ADMIN")) {
            insertBtn.setEnabled(false);
            deleteBtn.setEnabled(false);
            editBtn.setEnabled(false);
        }
    }

    private void insertData() {
        try {
            Double.parseDouble(fields[4].getText());
            Date.valueOf(fields[3].getText());

            String sql = String.format("INSERT INTO medicines VALUES (%s, '%s', %s, '%s', %s, '%s')",
                    fields[0].getText(), fields[1].getText(), fields[2].getText(),
                    fields[3].getText(), fields[4].getText(), diseaseBox.getSelectedItem());
            stmt.executeUpdate(sql);
            reloadTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка: неверный формат числа или даты.");
        }
    }

    private void deleteData() {
        try {
            int row = table.getSelectedRow();
            if (row == -1) return;
            String id = table.getValueAt(row, 0).toString();
            stmt.executeUpdate("DELETE FROM medicines WHERE id=" + id);
            reloadTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void editData() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        editDialog.setData(table, row);
        editDialog.setVisible(true);
        reloadTable();
    }

    private void reloadTable() {
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM medicines");
            table.setModel(new MedicineTableModel(rs));
            db.close(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void search() {
        try {
            String keyword = searchField.getText();
            String sql = "SELECT * FROM medicines WHERE name LIKE ?";
            PreparedStatement ps = db.getConnection().prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            table.setModel(new MedicineTableModel(rs));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void filter() {
        try {
            String selected = (String) filterBox.getSelectedItem();
            String sql = selected.equals("Все") ? "SELECT * FROM medicines"
                    : "SELECT * FROM medicines WHERE disease=?";
            PreparedStatement ps = db.getConnection().prepareStatement(sql);
            if (!selected.equals("Все")) ps.setString(1, selected);
            ResultSet rs = ps.executeQuery();
            table.setModel(new MedicineTableModel(rs));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
