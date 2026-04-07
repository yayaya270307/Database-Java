package javatutorial;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FormKaryawan extends JFrame {

    private JTextField txtNip, txtNama, txtTempLhr, txtTgl, txtBln, txtThn, txtJabatan;
    private JButton btnInsert, btnUpdate, btnDelete, btnClose;
    private JTable TblKar;
    private DefaultTableModel defTab;

    public FormKaryawan() {
        setTitle("Form Karyawan - CRUD Java Swing");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        fillTable(false);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel background = new JPanel(new BorderLayout());
        background.setBackground(new Color(210, 206, 188));
        background.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(background, BorderLayout.CENTER);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(245, 242, 233));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(178, 172, 147), 2),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));
        background.add(card, BorderLayout.CENTER);

        // Panel Input
        JPanel panelInput = new JPanel(new GridBagLayout());
        panelInput.setBackground(new Color(245, 242, 233));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panelInput.add(new JLabel("NIP"), gbc);
        gbc.gridx = 1; txtNip = new JTextField(18); panelInput.add(txtNip, gbc);
        txtNip.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) searchByNip();
            }
        });

        gbc.gridx = 0; gbc.gridy = 1; panelInput.add(new JLabel("Nama"), gbc);
        gbc.gridx = 1; txtNama = new JTextField(22); panelInput.add(txtNama, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelInput.add(new JLabel("Temp Lhr"), gbc);
        gbc.gridx = 1; txtTempLhr = new JTextField(22); panelInput.add(txtTempLhr, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelInput.add(new JLabel("Tgl Lhr"), gbc);
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        datePanel.setBackground(new Color(245, 242, 233));
        txtTgl = new JTextField(2); txtBln = new JTextField(2); txtThn = new JTextField(4);
        datePanel.add(txtTgl); datePanel.add(new JLabel("-")); datePanel.add(txtBln);
        datePanel.add(new JLabel("-")); datePanel.add(txtThn);
        gbc.gridx = 1; panelInput.add(datePanel, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panelInput.add(new JLabel("Jabatan"), gbc);
        gbc.gridx = 1; txtJabatan = new JTextField(22); panelInput.add(txtJabatan, gbc);

        // Panel Button
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        panelBtn.setBackground(new Color(245, 242, 233));
        btnInsert = new JButton("Insert");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClose = new JButton("Close");

        panelBtn.add(btnInsert);
        panelBtn.add(btnUpdate);
        panelBtn.add(btnDelete);
        panelBtn.add(btnClose);
        panelBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Table
        TblKar = new JTable();
        TblKar.setFillsViewportHeight(true);
        TblKar.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = TblKar.getSelectedRow();
                if (row >= 0) {
                    txtNip.setText(TblKar.getValueAt(row, 0).toString());
                    txtNama.setText(TblKar.getValueAt(row, 1).toString());
                    txtTempLhr.setText(TblKar.getValueAt(row, 2).toString());
                    String tgllhrDisplay = TblKar.getValueAt(row, 3).toString();
                    String tgllhr = parseTanggalDariTabel(tgllhrDisplay);
                    if (tgllhr != null && tgllhr.length() >= 10) {
                        txtThn.setText(tgllhr.substring(0, 4));
                        txtBln.setText(tgllhr.substring(5, 7));
                        txtTgl.setText(tgllhr.substring(8, 10));
                    }
                    txtJabatan.setText(TblKar.getValueAt(row, 4).toString());
                }
            }
        });
        JScrollPane scroll = new JScrollPane(TblKar);
        scroll.setPreferredSize(new Dimension(0, 220));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(245, 242, 233));
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(178, 172, 147)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        tablePanel.add(scroll, BorderLayout.CENTER);

        card.add(panelInput);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(panelBtn);
        card.add(Box.createRigidArea(new Dimension(0, 14)));
        card.add(tablePanel);

        // Event
        btnInsert.addActionListener(e -> insertData());
        btnUpdate.addActionListener(e -> updateData());
        btnDelete.addActionListener(e -> deleteData());
        btnClose.addActionListener(e -> dispose());
    }

    private void fillTable(boolean filter) {
        Object[] colHeader = {"NIP", "Nama", "Temp Lahir", "Tgl Lahir", "Jabatan"};
        defTab = new DefaultTableModel(null, colHeader);
        ResultSet rs = null;
        CKaryawan ck = new CKaryawan();

        if (filter) {
            ck.setNip(txtNip.getText());
            rs = ck.getRecordByNip();
        } else {
            rs = ck.getRecords();
        }

        try {
            while (rs.next()) {
                String[] rows = {
                    rs.getString(1), rs.getString(2), rs.getString(3),
                    formatTanggalUntukTabel(rs.getString(4)), rs.getString(5)
                };
                defTab.addRow(rows);
            }
            TblKar.setModel(defTab);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void insertData() {
        CKaryawan ck = new CKaryawan();
        ck.setNip(txtNip.getText());
        ck.setNama(txtNama.getText());
        ck.setTempatLahir(txtTempLhr.getText());
        String tglFormatted = formatTanggalLahir();
        if (tglFormatted == null) {
            JOptionPane.showMessageDialog(this, "Tanggal lahir tidak valid (YYYY-MM-DD).");
            return;
        }
        ck.setTglLahir(tglFormatted);
        ck.setJabatan(txtJabatan.getText());

        if (ck.insert()) {
            JOptionPane.showMessageDialog(this, "Data Tersimpan");
            fillTable(false);
            clearForm();
        }
    }

    private void updateData() {
        CKaryawan ck = new CKaryawan();
        ck.setNip(txtNip.getText());
        ck.setNama(txtNama.getText());
        ck.setTempatLahir(txtTempLhr.getText());
        String tglFormatted = formatTanggalLahir();
        if (tglFormatted == null) {
            JOptionPane.showMessageDialog(this, "Tanggal lahir tidak valid (YYYY-MM-DD).");
            return;
        }
        ck.setTglLahir(tglFormatted);
        ck.setJabatan(txtJabatan.getText());

        if (ck.update()) {
            JOptionPane.showMessageDialog(this, "Data Diupdate");
            fillTable(false);
            clearForm();
        }
    }

    private void deleteData() {
        int answer = JOptionPane.showConfirmDialog(this, "Yakin dihapus?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            CKaryawan ck = new CKaryawan();
            ck.setNip(txtNip.getText());
            if (ck.delete()) {
                JOptionPane.showMessageDialog(this, "Data Terhapus");
                fillTable(false);
                clearForm();
            }
        }
    }

    private void searchByNip() {
        CKaryawan ck = new CKaryawan();
        ck.setNip(txtNip.getText());
        ResultSet rs = ck.getRecordByNip();

        try {
            if (rs.next()) {
                txtNama.setText(rs.getString(2));
                txtTempLhr.setText(rs.getString(3));
                String tgllhr = rs.getString(4);
                txtThn.setText(tgllhr.substring(0, 4));
                txtBln.setText(tgllhr.substring(5, 7));
                txtTgl.setText(tgllhr.substring(8, 10));
                txtJabatan.setText(rs.getString(5));
                fillTable(true);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void clearForm() {
        txtNip.setText("");
        txtNama.setText("");
        txtTempLhr.setText("");
        txtTgl.setText("");
        txtBln.setText("");
        txtThn.setText("");
        txtJabatan.setText("");
    }

    private String formatTanggalLahir() {
        String thn = txtThn.getText().trim();
        String bln = txtBln.getText().trim();
        String tgl = txtTgl.getText().trim();

        if (thn.isEmpty() || bln.isEmpty() || tgl.isEmpty()) {
            return null;
        }

        try {
            int tahun = Integer.parseInt(thn);
            int bulan = Integer.parseInt(bln);
            int hari = Integer.parseInt(tgl);

            // Validasi range
            if (tahun < 1900 || tahun > 2100) {
                JOptionPane.showMessageDialog(this, "Tahun harus antara 1900-2100");
                return null;
            }
            if (bulan < 1 || bulan > 12) {
                JOptionPane.showMessageDialog(this, "Bulan harus antara 1-12");
                return null;
            }
            if (hari < 1 || hari > 31) {
                JOptionPane.showMessageDialog(this, "Hari harus antara 1-31");
                return null;
            }

            return String.format("%04d-%02d-%02d", tahun, bulan, hari);
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Format tanggal tidak valid. Gunakan angka.");
            return null;
        }
    }

    private String formatTanggalUntukTabel(String tanggal) {
        if (tanggal == null || tanggal.length() < 10) return tanggal == null ? "" : tanggal;
        try {
            return tanggal.substring(8, 10) + "-" +
                    tanggal.substring(5, 7) + "-" +
                    tanggal.substring(0, 4);
        } catch (IndexOutOfBoundsException e) {
            return tanggal;
        }
    }

    private String parseTanggalDariTabel(String tanggal) {
        if (tanggal == null || tanggal.length() < 10) return tanggal;
        try {
            String hari = tanggal.substring(0, 2);
            String bulan = tanggal.substring(3, 5);
            String tahun = tanggal.substring(6, 10);
            return tahun + "-" + bulan + "-" + hari;
        } catch (IndexOutOfBoundsException e) {
            return tanggal;
        }
    }
}
