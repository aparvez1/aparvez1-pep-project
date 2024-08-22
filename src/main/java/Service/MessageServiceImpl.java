package Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageServiceImpl implements MessageService {

    @Override
    public Message createMessage(Message message) {

        String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?,?,?)";

        try (Connection conn = ConnectionUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            int rowsEffected = ps.executeUpdate();
            if (rowsEffected == 1) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int message_id = ps.getGeneratedKeys().getInt(1);

                        return getByMessageId(message_id);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Message> getAllMessages() {
        try (Connection conn = ConnectionUtil.getConnection()) {

            String sql = "SELECT * FROM message";

            try (PreparedStatement ps = conn.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery()) {
                List<Message> messages = new ArrayList<>();
                while (rs.next()) {
                    Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                    rs.getString("message_text"), rs.getLong("time_posted_epoch"));

                    messages.add(message);
                }

                return messages;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message getByMessageId(int message_id) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM message WHERE message_id = ?";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, message_id);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        Message foundMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));

                        return foundMessage;
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteMessage(int message_id) {
        String sql = "DELETE FROM message WHERE message_id=?";
        try (Connection conn = ConnectionUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            int rowsEffected = ps.executeUpdate();
            if (rowsEffected > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Message updateMessage(int message_id, Message message) {
        String sql = "UPDATE message SET message_text=?  where message_id=?";

        try (Connection conn = ConnectionUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
                    
                    
            ps.setString(1, message.getMessage_text());
            ps.setInt(2, message_id);

            int update = ps.executeUpdate();
            if (update == 1) {
                return getByMessageId(message_id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Message> getAllMessagesByAccountId(int account_id) {
        String sql = "SELECT * FROM message WHERE posted_by=?";
        try (Connection conn = ConnectionUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();
            List<Message> messages = new ArrayList<>();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
            return messages;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
