package Service;

import java.util.List;
import DAO.*;
import Model.*;

public class MessageService {

    MessageDAO dao;
    
    public MessageService() {
        dao = new MessageDAO();
    }

    public Account createUser(String username, String password) {
        return dao.createUser(username, password);
    }

    public Account verifyUser(String username, String password) {
        return dao.verifyUser(username, password);
    }

    public Message createMessage(int account_id, String message_text, long time_posted) {
        return dao.createMessage(account_id, message_text, time_posted);
    }
    
    public List<Message> getAllMessages() {
        return dao.getAllMessages();
    }

    public Message getMessage(int message_id) {
        return dao.getMessage(message_id);
    }

    public Message deleteMessage(int message_id) {
        Message ret = dao.getMessage(message_id);
        if (ret != null) {
            if (dao.deleteMessage(message_id)) {
                return ret;
            }
        }
        return null;
    }

    public Message updateMessage(int message_id, String message_text) {
        if (dao.updateMessage(message_id, message_text)) {
            return dao.getMessage(message_id);
        }
        return null;
    }

    public List<Message> retrieveUserMessages(int user_id) {
        return dao.retrieveUserMessages(user_id);
    }
}
