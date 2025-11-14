package model.dao;

import model.bean.convertedFile;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class convertedFileDAO {
    private Connection conn;

    public convertedFileDAO(Connection conn) {
        this.conn = conn;
    }

    private convertedFile mapResultSetToConvertedFile(ResultSet rs) throws SQLException {
        return new convertedFile(
                rs.getInt("converted_id"),
                rs.getInt("queue_id"),
                rs.getString("output_filename"),
                rs.getString("output_path"),
                rs.getLong("output_size"),
                rs.getInt("conversion_time"),
                rs.getTimestamp("created_at")
        );
    }

    public boolean insert(convertedFile file) {
        String sql = "INSERT INTO converted_files (queue_id, output_filename, output_path, output_size, conversion_time) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, file.getQueueId());
            ps.setString(2, file.getOutputFilename());
            ps.setString(3, file.getOutputPath());
            ps.setLong(4, file.getOutputSize());
            ps.setInt(5, file.getConversionTime());

            int result = ps.executeUpdate();
            if (result > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    file.setConvertedId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy converted file theo ID
    public convertedFile getById(int convertedId) {
        String sql = "SELECT * FROM converted_files WHERE converted_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, convertedId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToConvertedFile(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy converted file theo queue_id
    public convertedFile getByQueueId(int queueId) {
        String sql = "SELECT * FROM converted_files WHERE queue_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, queueId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToConvertedFile(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy tất cả converted files
    public List<convertedFile> getAll() {
        List<convertedFile> files = new ArrayList<>();
        String sql = "SELECT * FROM converted_files ORDER BY created_at DESC";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                files.add(mapResultSetToConvertedFile(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return files;
    }

    // Lấy converted files theo user
    public List<convertedFile> getByUserId(int userId) {
        List<convertedFile> files = new ArrayList<>();
        String sql = "SELECT cf.* FROM converted_files cf " +
                "JOIN conversion_queue_item cq ON cf.queue_id = cq.queue_id " +
                "JOIN files f ON cq.file_id = f.file_id " +
                "WHERE f.user_id = ? " +
                "ORDER BY cf.created_at DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                files.add(mapResultSetToConvertedFile(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return files;
    }

    // Tính trung bình thời gian conversion
    public double getAverageConversionTime() {
        String sql = "SELECT AVG(conversion_time) FROM converted_files";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}