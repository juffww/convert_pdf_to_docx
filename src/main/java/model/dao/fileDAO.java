package model.dao;

import model.bean.file;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class fileDAO {
    private Connection conn;

    public fileDAO(Connection conn) {
        this.conn = conn;
    }

    private file mapResultSetToFile(ResultSet rs) throws SQLException {
        return new file(
                rs.getInt("user_id"),
                rs.getString("file_path"),
                rs.getLong("file_size"),
                rs.getTimestamp("upload_time"),
                rs.getString("original_filename"),
                rs.getInt("file_id")
        );
    }

    // Thêm file mới
    public boolean insert(file file) {
        String sql = "INSERT INTO files (user_id, original_filename, file_path, file_size) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, file.getUserId());
            ps.setString(2, file.getOriginalFilename());
            ps.setString(3, file.getFilePath());
            ps.setLong(4, file.getFileSize());

            int result = ps.executeUpdate();
            if (result > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    file.setFileId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy file theo ID
    public file getById(int fileId) {
        String sql = "SELECT * FROM files WHERE file_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, fileId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToFile(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy tất cả file của một user
    public List<file> getByUserId(int userId) {
        List<file> files = new ArrayList<>();
        String sql = "SELECT * FROM files WHERE user_id=? ORDER BY upload_time DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                files.add(mapResultSetToFile(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return files;
    }

    // Lấy tất cả file
    public List<file> getAll() {
        List<file> files = new ArrayList<>();
        String sql = "SELECT * FROM files ORDER BY upload_time DESC";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                files.add(mapResultSetToFile(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return files;
    }
}