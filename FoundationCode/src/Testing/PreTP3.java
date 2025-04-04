package Testing;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;

import Application.Question;

public class PreTP3 {
	
	@Test
	public void questionConstruct() {
		UUID testID = UUID.randomUUID();
		Question testQ = new Question(testID, "Test1", "Test2", "Test3");
		assertEquals(testID, testQ.getUUID());
		assertEquals("Test1", testQ.getTitle());
		assertEquals("Test2", testQ.getBodyText());
		assertEquals("Test3", testQ.getAuthor());
		
	}
	
	@Test
	public void questionEditing() {
		Question testQ = new Question(UUID.randomUUID(), "Test1", "Test2", "Test3");
		testQ.editBodyText("Edit1");
		testQ.editTitle("Edit2");
		assertEquals("Edit1", testQ.getBodyText());
		assertEquals("Edit2", testQ.getTitle());
	}
	
	@Test 
	public void questionResolving() {
		Question testQ = new Question(UUID.randomUUID(), "Test1", "Test2", "Test3");
		testQ.resolveQuestion(UUID.randomUUID());
		assertEquals(true, testQ.getResolved());
	}

}
