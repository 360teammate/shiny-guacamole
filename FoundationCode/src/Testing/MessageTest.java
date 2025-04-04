package Testing;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.UUID;

import org.junit.Test;

import Application.Message;

public class MessageTest {

    @Test
    public void testMessageCreation() {
        UUID convoId = UUID.randomUUID();
        Message msg = new Message("Hello!", "alice", convoId);

        assertEquals("Hello!", msg.getContent());
        assertEquals("alice", msg.getAuthor());
        assertEquals(convoId, msg.getConversation());
        assertNotNull(msg.getUUID());
        assertNotNull(msg.getTimestamp());
    }

    @Test
    public void testMessageUUIDConsistency() {
        UUID uuid = UUID.randomUUID();
        UUID convoId = UUID.randomUUID();
        Date now = new Date();

        Message msg = new Message(uuid, "Test", "bob", convoId, now);
        assertEquals(uuid, msg.getUUID());
    }
}