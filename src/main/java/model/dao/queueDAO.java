package model.dao;

import model.bean.conversionQueueItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class queueDAO {
    private Connection conn;

    public queueDAO(Connection conn) {
        this.conn = conn;
    }

    private conversionQueueItem mapResultSetToQueue(ResultSet rs) throws SQLException {
        return new conversionQueueItem(
                rs.getInt("queue_id"),
                rs.getInt("file_id"),
                rs.getString("status"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("started_at"),
                rs.getTimestamp("completed_at"),
                rs.getString("error_message")
        );
    }

    // Thêm task vào queue
    public boolean insert(conversionQueueItem queue) {
        String sql = "INSERT INTO conversion_queue_item (file_id, status) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, queue.getFileId());
            ps.setString(2, queue.getStatus());

            int result = ps.executeUpdate();
            if (result > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    queue.setQueueId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật status
    public boolean updateStatus(int queueId, String status) {
        String sql = "UPDATE conversion_queue_item SET status=? WHERE queue_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, queueId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật started_at
    public boolean updateStartedAt(int queueId) {
        String sql = "UPDATE conversion_queue_item SET started_at=CURRENT_TIMESTAMP, status='PROCESSING' WHERE queue_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, queueId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật completed_at
    public boolean updateCompletedAt(int queueId, String status) {
        String sql = "UPDATE conversion_queue_item SET completed_at=CURRENT_TIMESTAMP, status=? WHERE queue_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, queueId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật error message
    public boolean updateError(int queueId, String errorMessage) {
        String sql = "UPDATE conversion_queue_item SET status='FAILED', error_message=?, completed_at=CURRENT_TIMESTAMP WHERE queue_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, errorMessage);
            ps.setInt(2, queueId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy queue item theo ID
    public conversionQueueItem getById(int queueId) {
        String sql = "SELECT * FROM conversion_queue_item WHERE queue_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, queueId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToQueue(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy queue item theo file_id
    public conversionQueueItem getByFileId(int fileId) {
        String sql = "SELECT * FROM conversion_queue_item WHERE file_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, fileId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToQueue(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy tất cả queue item theo status
    public List<conversionQueueItem> getByStatus(String status) {
        List<conversionQueueItem> queues = new ArrayList<>();
        String sql = "SELECT * FROM conversion_queue_item WHERE status=? ORDER BY created_at ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                queues.add(mapResultSetToQueue(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queues;
    }

    // Lấy tất cả queue item
    public List<conversionQueueItem> getAll() {
        List<conversionQueueItem> queues = new ArrayList<>();
        String sql = "SELECT * FROM conversion_queue_item ORDER BY created_at DESC";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                queues.add(mapResultSetToQueue(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queues;
    }

    // Đếm số queue theo status
    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM conversion_queue_item WHERE status=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}