package javatutorial;

import java.sql.*;

public class CKaryawan {
    private String nip;
    private String nama;
    private String temp_lahir;
    private String tgl_lahir;
    private String jabatan;

    private CConnection c = new CConnection();

    public CKaryawan() {
        c.openConnection();
    }

    // SETTER & GETTER
    public void setNip(String value) { this.nip = value; }
    public String getNip() { return this.nip; }

    public void setNama(String value) { this.nama = value; }
    public String getNama() { return this.nama; }

    public void setTempatLahir(String value) { this.temp_lahir = value; }
    public String getTempatLahir() { return this.temp_lahir; }

    public void setTglLahir(String value) { this.tgl_lahir = value; }
    public String getTglLahir() { return this.tgl_lahir; }

    public void setJabatan(String value) { this.jabatan = value; }
    public String getJabatan() { return this.jabatan; }

    // INSERT
    public boolean insert() {
        boolean r = false;
        String sql = "INSERT INTO karyawan VALUES('" + nip + "','" + nama + "','" +
                     temp_lahir + "','" + tgl_lahir + "','" + jabatan + "')";
        try {
            Statement stmt = c.cn.createStatement();
            stmt.executeUpdate(sql);
            c.closeConnection();
            r = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            r = false;
        }
        return r;
    }

    // UPDATE
    public boolean update() {
        boolean r = false;
        String sql = "UPDATE karyawan SET nm_kar='" + nama + "',tem_lhr='" + temp_lahir +
                     "',tgl_lhr='" + tgl_lahir + "',jabatan='" + jabatan +
                     "' WHERE nip='" + nip + "'";
        try {
            Statement stmt = c.cn.createStatement();
            stmt.executeUpdate(sql);
            c.closeConnection();
            r = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            r = false;
        }
        return r;
    }

    // DELETE
    public boolean delete() {
        boolean r = false;
        String sql = "DELETE FROM karyawan WHERE nip='" + nip + "'";
        try {
            Statement stmt = c.cn.createStatement();
            stmt.executeUpdate(sql);
            c.closeConnection();
            r = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            r = false;
        }
        return r;
    }

    // GET ALL
    public ResultSet getRecords() {
        ResultSet rs = null;
        String sql = "SELECT * FROM karyawan";
        try {
            Statement stmt = c.cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }

    // GET BY NIP
    public ResultSet getRecordByNip() {
        ResultSet rs = null;
        String sql = "SELECT * FROM karyawan WHERE nip='" + nip + "'";
        try {
            Statement stmt = c.cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }
}