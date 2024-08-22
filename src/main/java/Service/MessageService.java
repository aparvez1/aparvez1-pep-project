package Service;

import java.util.List;

import Model.Message;

public interface MessageService {
    Message createMessage(Message message);
    List<Message> getAllMessages();
    Message getByMessageId(int message_id);
    boolean deleteMessage(int message_id);
    Message updateMessage(int message_id, Message message);
    List<Message> getAllMessagesByAccountId(int account_id);
    
}
