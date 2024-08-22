package Controller;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import Model.Account;
import Model.Message;
import Service.AccountServiceImpl;
import Service.MessageServiceImpl;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {

    private static AccountServiceImpl accountService = new AccountServiceImpl();
    private static MessageServiceImpl messageService = new MessageServiceImpl();

    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::createAccount);
        app.post("/login", this::userLogin);
        app.post("/messages", this::postMessage);
        
        app.get("/messages/{message_id}", this::getMessageById);
        app.get("/messages", this::getAllMessages);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("/accounts/{account_id}/messages", this::getUserMessages);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    public void createAccount(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        if (account.getUsername() == null || account.getUsername().isEmpty() || account.getPassword() == null ||
                account.getPassword().length() < 4
                || accountService.findAccountByUsername(account.getUsername()) != null) {
            ctx.status(400);
            return;
        }
        Account createdAccount = accountService.createAccount(account);
        if (createdAccount == null) {
            ctx.status(400);
            return;
        }
        ctx.status(200).json(createdAccount);
    }

    public void userLogin(Context ctx) throws JsonProcessingException {
        Account account = ctx.bodyAsClass(Account.class);
        Account foundAccount = accountService.findAccountByUsername(account.getUsername());
        if (foundAccount == null || !account.getPassword().equals(foundAccount.getPassword())) {
            ctx.status(401);
            return;
        }

        ctx.status(200).json(foundAccount);
    }

    public void postMessage(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
        if (accountService.getAccountById(message.getPosted_by()) == null || message.getMessage_text().isEmpty()
                || message.getMessage_text().length() > 255) {
            ctx.status(400);
            return;
        }
        Message postedMessage = messageService.createMessage(message);
        if (postedMessage == null) {
            ctx.status(400);
            return;
        }
        ctx.status(200).json(postedMessage);
    }

    public void getMessageById(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getByMessageId(messageId);
        if (message == null) {
            ctx.status(200).json("");
            return;
        }
        ctx.status(200).json(message);
    }

    public void getAllMessages(Context ctx) {
        List<Message> messages = new ArrayList<>();

        messages = messageService.getAllMessages();
        if (messages == null) {
            ctx.status(200).json("");
            return;
        }
        ctx.status(200).json(messages);
    }

    public void deleteMessage(Context ctx) {
        Message message = messageService.getByMessageId(Integer.parseInt(ctx.pathParam("message_id")));
        if (message == null) {
            ctx.status(200).json("");
            return;
        }
        messageService.deleteMessage(message.getMessage_id());
        ctx.status(200).json(message);
    }

    public void updateMessage(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = ctx.bodyAsClass(Message.class);
        Message message = messageService.getByMessageId(messageId);

        if (message == null || updatedMessage.getMessage_text().isEmpty()
                || updatedMessage.getMessage_text().length() > 255) {
            ctx.status(400);
            return;
        }
        Message insertedMessage = messageService.updateMessage(messageId, updatedMessage);
        if (insertedMessage == null) {
            ctx.status(400);
            return;
        }
        ctx.status(200).json(insertedMessage);

    }

    public void getUserMessages(Context ctx) {

        List<Message> messages = new ArrayList<>();

        messages = messageService.getAllMessagesByAccountId(Integer.parseInt(ctx.pathParam("account_id")));
        if (messages == null) {
            ctx.status(200).json("");
            return;
        }
        ctx.status(200).json(messages);

    }
}