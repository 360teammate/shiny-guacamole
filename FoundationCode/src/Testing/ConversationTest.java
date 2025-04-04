package Testing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import Application.Conversation;
import Application.Message;

public class ConversationTest {

    @Test
    public void testConversationCreation() {
        ArrayList<String> users = new ArrayList<>(List.of("alice", "bob"));
        Conversation convo = new Conversation(users);

        assertNotNull(convo.getUUID());
        assertEquals(2, convo.getUsers().size());
        assertEquals(0, convo.getMessages().size());
    }

    @Test
    public void testAddMessageToConversation() {
        ArrayList<String> users = new ArrayList<>(List.of("carol", "dave"));
        Conversation convo = new Conversation(users);
        Message msg = new Message("Hi!", "carol", convo.getUUID());

        convo.getMessages().add(msg);

        assertEquals(1, convo.getMessages().size());
        assertEquals("Hi!", convo.getMessages().get(0).getContent());
    }

    @Test
    public void testConversationUUIDConsistency() {
        UUID knownUUID = UUID.randomUUID();
        ArrayList<String> users = new ArrayList<>(List.of("eve"));
        ArrayList<Message> messages = new ArrayList<>();
        Conversation convo = new Conversation(knownUUID, users, messages);

        assertEquals(knownUUID, convo.getUUID());
    }
}