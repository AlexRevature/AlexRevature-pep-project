package Controller;

import java.util.List;
import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.*;
import Service.*;

public class SocialMediaController {

    MessageService service;

    public SocialMediaController() {
        service = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.get("example-endpoint", this::exampleHandler);

        app.post("register", this::postRegister);
        app.post("login", this::postLogin);
        app.post("messages", this::postMessage);
        app.get("messages", this::getMessages);
        app.get("messages/{message_id}", this::getMessage);
        app.delete("messages/{message_id}", this::deleteMessage);
        app.patch("messages/{message_id}", this::patchMessage);
        app.get("accounts/{account_id}/messages", this::getUserMessages);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void postRegister(Context context) {
        Account acc = context.bodyAsClass(Account.class);
        if (acc.getUsername().length() == 0 || acc.getPassword().length() < 4) {
            context.status(400);
            return;
        }

        Account ret = service.createUser(acc.getUsername(), acc.getPassword());

        if (ret != null) {
            context.status(200);
            context.json(ret);
        } else {
            context.status(400);
        }
    }

    private void postLogin(Context context) {
        Account acc = context.bodyAsClass(Account.class);
        Account ret = service.verifyUser(acc.getUsername(), acc.getPassword());

        if (ret != null) {
            context.status(200);
            context.json(ret);
        } else {
            context.status(401);
        }
    }

    private void postMessage(Context context) {
        Message mes = context.bodyAsClass(Message.class);
        if (mes.getMessage_text().length() == 0) {
            context.status(400);
            return;
        }

        Message ret = service.createMessage(mes.getPosted_by(), mes.getMessage_text(), mes.getTime_posted_epoch());

        if (ret != null) {
            context.status(200);
            context.json(ret);
        } else {
            context.status(400);
        }
    }

    private void getMessages(Context context) {
        List<Message> allMessages = service.getAllMessages();
        if (allMessages != null) {
            context.status(200);
            context.json(allMessages);
        } else {
            context.status(400);
        }
    }

    private void getMessage(Context context) {
        String pathParam = context.pathParam("message_id");
        int message_id = Integer.parseInt(pathParam);

        Message mes = service.getMessage(message_id);
        context.status(200);

        if (mes != null) {
            context.json(mes);
        }
    }

    private void deleteMessage(Context context) {
        String pathParam = context.pathParam("message_id");
        int message_id = Integer.parseInt(pathParam);

        Message mes = service.deleteMessage(message_id);
        context.status(200);

        if (mes != null) {
            context.json(mes);
        }
    }

    private void patchMessage(Context context) {

        Message mes = context.bodyAsClass(Message.class);
        if (mes.getMessage_text().length() == 0) {
            context.status(400);
            return;
        }

        String pathParam = context.pathParam("message_id");
        int message_id = Integer.parseInt(pathParam);

        Message ret = service.updateMessage(message_id, mes.getMessage_text());
        if (ret != null) {
            context.status(200);
            context.json(ret);
        } else {
            context.status(400);
        }
    }

    private void getUserMessages(Context context) {
        String pathParam = context.pathParam("account_id");
        int account_id = Integer.parseInt(pathParam);

        List<Message> mes = service.retrieveUserMessages(account_id);
        context.status(200);

        if (mes != null) {
            context.json(mes);
        }
    }

}