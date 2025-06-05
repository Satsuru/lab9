package com.example.pharmacy;

import javax.swing.*;
import java.sql.*;

public class EditDialog extends JDialog {
    private JTextField[] fields = new JTextField[4]; // name, year, expiration, price
    private JComboBox<String> diseaseBox;
    private JButton okBtn;
    private Statement stmt;
    private String id;

    public EditDialog(JFrame parent, boolean modal, Statement stmt) {
        super(parent, modal);
        this.stmt = stmt;
        setTitle("Редактирование лекарства");
        setLayout(null);
        setSize(400, 300);
        setLocationRelativeTo(parent);

        String[] labels = {"Название", "Год выпуска", "Годен до", "Цена"};

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setBounds(20, 20 + i * 30, 150, 25);
            add(label);

            fields[i] = new JTextField();
            fields[i].setBounds(180, 20 + i * 30, 180, 25);
            add(fields[i]);
        }

        JLabel diseaseLabel = new JLabel("Заболевание");
        diseaseLabel.setBounds(20, 140, 150, 25);
        add(diseaseLabel);

        diseaseBox = new JComboBox<>(new String[]{"Грипп", "Аллергия", "Covid-19", "Головная боль"});
        diseaseBox.setBounds(180, 140, 180, 25);
        add(diseaseBox);

        okBtn = new JButton("Сохранить");
        okBtn.setBounds(140, 200, 120, 30);
        add(okBtn);

        okBtn.addActionListener(e -> {
            try {
                stmt.executeUpdate(String.format(
                        "UPDATE medicines SET name='%s', release_year=%s, expiration_date='%s', price=%s, disease='%s' WHERE id=%s",
                        fields[0].getText(), fields[1].getText(), fields[2].getText(), fields[3].getText(),
                        diseaseBox.getSelectedItem(), id
                ));
                setVisible(false);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Ошибка при обновлении: " + ex.getMessage());
            }
        });
    }

    public void setData(JTable table, int row) {
        id = table.getValueAt(row, 0).toString();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setText(table.getValueAt(row, i + 1).toString());
        }
        diseaseBox.setSelectedItem(table.getValueAt(row, 5).toString());
    }
}
