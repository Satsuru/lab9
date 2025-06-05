package com.example.pharmacy;

import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.ArrayList;

public class MedicineTableModel extends AbstractTableModel {
    private final String[] colNames = {"ID", "Name", "Release Year", "Expiration", "Price", "Disease"};
    private final ArrayList<String[]> data = new ArrayList<>();

    public MedicineTableModel(ResultSet rs) throws SQLException {
        while (rs.next()) {
            data.add(new String[]{
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("release_year"),
                    rs.getString("expiration_date"),
                    rs.getString("price"),
                    rs.getString("disease")
            });
        }
    }

    public int getRowCount() { return data.size(); }

    public int getColumnCount() { return colNames.length; }

    public Object getValueAt(int r, int c) { return data.get(r)[c]; }

    public String getColumnName(int c) { return colNames[c]; }
}
