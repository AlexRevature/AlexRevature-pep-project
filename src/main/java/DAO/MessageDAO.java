package DAO;

import java.util.List;
import java.util.ArrayList;
import java.sql.*;

import Util.ConnectionUtil;
import Model.*;

public class MessageDAO {

    Connection conn;

    public MessageDAO() {
        conn = ConnectionUtil.getConnection();
    }

    public Account createUser(String username, String password) {

        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement prep = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            prep.setString(1, username);
            prep.setString(2, password);

            if (prep.executeUpdate() == 0) return null;

            ResultSet keys = prep.getGeneratedKeys();
            if (keys.next()) {
                int account_id = keys.getInt(1);
                return new Account(account_id, username, password);
            }
            return null;


        } catch (SQLException e) {
            return null;
        }
    }

    public Account verifyUser(String username, String password) {
        
        try {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";
            PreparedStatement prep = conn.prepareStatement(sql);
            prep.setString(1, username);
            prep.setString(2, password);

            ResultSet result = prep.executeQuery();

            if (result.next()) {
                String tUser = result.getString("username");
                String tPassword = result.getString("password");
                int account_id = result.getInt("account_id");
                // System.out.println("Test");
                return new Account(account_id, tUser, tPassword);
            }

            return null;

        } catch (SQLException e) {
            return null;
        }

    }

    public Message createMessage(int account_id, String message_text, long time_posted) {

        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
            PreparedStatement prep = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            prep.setInt(1, account_id);
            prep.setString(2, message_text);
            prep.setLong(3, time_posted);

            if (prep.executeUpdate() == 0) return null;

            ResultSet keys = prep.getGeneratedKeys();
            if (keys.next()) {
                int message_id = keys.getInt(1);
                return new Message(message_id, account_id, message_text, time_posted);
            }
            return null;


        } catch (SQLException e) {
            return null;
        }
    }
    
    public List<Message> getAllMessages() {
        
        try {
            PreparedStatement prep = conn.prepareStatement("SELECT * FROM message;");
            ResultSet result = prep.executeQuery();
            List<Message> allMessages = new ArrayList<>();

            while (result.next()) {
                int message_id = result.getInt("message_id");
                int posted_by = result.getInt("posted_by");
                String message_text = result.getString("message_text");
                long time_posted = result.getLong("time_posted_epoch");

                allMessages.add(new Message(message_id, posted_by, message_text, time_posted));
            }

            return allMessages;

        } catch (SQLException e) {
            return null;
        }
    }

    public Message getMessage(int message_id) {
        
        try {
            PreparedStatement prep = conn.prepareStatement("SELECT * FROM message WHERE message_id = ?;");
            prep.setInt(1, message_id);

            ResultSet result = prep.executeQuery();

            // Check ResultSet isnt't empty
            if (result.next()) {
                int posted_by = result.getInt("posted_by");
                String message_text = result.getString("message_text");
                long time_posted = result.getLong("time_posted_epoch");

                return new Message(message_id, posted_by, message_text, time_posted);
            }

            return null;

        } catch (SQLException e) {
            return null;
        }
    }

    public boolean deleteMessage(int message_id) {
        
        try {
            PreparedStatement prep = conn.prepareStatement("DELETE FROM message WHERE message_id = ?;");
            prep.setInt(1, message_id);

            // if (prep.executeUpdate() == 0) {
            //     return false;
            // }
    
        } catch (SQLException e) {
            return false;
        }

        return true;
    }

    public boolean updateMessage(int message_id, String message_text) {
        
        try {
            PreparedStatement prep = conn.prepareStatement("UPDATE message SET message_text = ? WHERE message_id = ?;");
            prep.setString(1, message_text);
            prep.setInt(2, message_id);

            if (prep.executeUpdate() == 0) {
                return false;
            }
    
        } catch (SQLException e) {
            return false;
        }

        return true;
    }

    public List<Message> retrieveUserMessages(int user_id) {
        
        try {
            PreparedStatement prep = conn.prepareStatement("SELECT * FROM message WHERE posted_by = ?;");
            prep.setInt(1, user_id);
            
            ResultSet result = prep.executeQuery();
            List<Message> allMessages = new ArrayList<>();

            while (result.next()) {
                int message_id = result.getInt("message_id");
                int posted_by = result.getInt("posted_by");
                String message_text = result.getString("message_text");
                long time_posted = result.getLong("time_posted_epoch");

                allMessages.add(new Message(message_id, posted_by, message_text, time_posted));
            }

            return allMessages;

        } catch (SQLException e) {
            return null;
        }
    }
}
