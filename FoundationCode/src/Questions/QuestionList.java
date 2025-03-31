package Questions;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import Application.StartCSE360;

/**
 * Manages a collection of questions, providing methods to add, edit, delete, 
 * and retrieve questions. Also supports storing a subset of filtered questions.
 */
public class QuestionList {
    private HashMap<UUID, Question> questions; // Stores all questions mapped by UUID
    private HashMap<UUID, Question> questionsSubset; // Stores a subset of filtered questions

    /**
     * Constructor to initialize the question list with an existing set of questions.
     *
     * @param questions A HashMap containing questions indexed by UUID.
     */
    public QuestionList(HashMap<UUID, Question> questions) {
        this.questions = questions;
        this.questionsSubset = new HashMap<>(); // Initialize an empty subset of questions
    }

    /**
     * Creates a new question, adds it to the list, and inserts it into the database.
     *
     * @param title     The title of the question.
     * @param bodyText  The text content of the question.
     * @param author    The author of the question.
     * @return The UUID of the newly created question.
     */
    public UUID newQuestion(String title, String bodyText, String author) {
        Question newQuestion = new Question(UUID.randomUUID(), title, bodyText, author);
        this.questions.put(newQuestion.getUUID(), newQuestion); // Store the new question in the HashMap

        try {
            // Insert the new question into the database using the helper class
            StartCSE360.databaseHelper.insertQuestion(newQuestion);
        } catch (SQLException e) {
            // Print stack trace for debugging in case of a database failure
            e.printStackTrace();
        }
        return newQuestion.getUUID();
    }

    /**
     * Retrieves a question by its UUID.
     *
     * @param uuid The UUID of the question to retrieve.
     * @return The corresponding Question object, or null if not found.
     */
    public Question getQuestion(UUID uuid) {
        return this.questions.get(uuid);
    }

    /**
     * Edits the body text of an existing question.
     *
     * @param uuid        The UUID of the question to be edited.
     * @param newBodyText The new content for the question.
     */
    public void editQuestion(UUID uuid, String newBodyText) {
        if (this.questions.containsKey(uuid)) {
            this.questions.get(uuid).editBodyText(newBodyText);
        } else {
            System.out.println("Question not found: " + uuid); // Debug message
        }
    }

    /**
     * Deletes a question from the list.
     *
     * @param uuid The UUID of the question to be removed.
     */
    public void deleteQuestion(UUID uuid) {
        if (this.questions.containsKey(uuid)) {
            this.questions.remove(uuid);
            // Consider also removing from the database if applicable
        } else {
            System.out.println("Attempted to delete a non-existing question: " + uuid);
        }
    }
	
    /**
     * 
     * @param term Search for a phrase in the title or body text of a question.
     * @param author Search for a post by a specific user.
     * @param resolved Search for a resolved tag of some kind.
     * @return A matching subset of the query
     */
    public HashMap<UUID, Question> searchAll(String term, String author, Boolean resolved) {
    	this.questionsSubset = new HashMap<>();
    	for (Map.Entry<UUID, Question> entry : this.questions.entrySet()) {
    		Question currQuestion = entry.getValue();
    		if (term != null && !currQuestion.getTitle().contains(term) && !currQuestion.getBodyText().contains(term)) {
    			continue;
    		}
    		if (author != null && !currQuestion.getAuthor().contains(author)) {
				continue;
			}
    		if (resolved != null && currQuestion.getResolved() != resolved) {
				continue;
			}
    		this.questionsSubset.put(entry.getKey(), currQuestion);
    	}
    	return this.questionsSubset;
    }
    
    /**
     * Searches all the questions for a term. Looks for the term in
     * the question title and question text.
     * 
     * @param term A target String to find within all the questions
     * @return The resulting query subset 
     */
    private HashMap<UUID, Question> searchForQuestion(String term) {
    	this.questionsSubset = new HashMap<>();
    	// iterate through all entries in all questions map
    	for (Map.Entry<UUID, Question> entry : this.questions.entrySet()) {
    		Question currQuestion = entry.getValue();
    		// found if term matches title or text
    		if (currQuestion.getTitle().contains(term)
    		 || currQuestion.getBodyText().contains(term)) {
    			this.questionsSubset.put(entry.getKey(), currQuestion);
    		}
    	}
    	return this.questionsSubset;
    }
    
    /**
     * Searches all the questions written by a username
     * 
     * @param username The target person to be searching for
     * @return The resulting query subset
     */
    private HashMap<UUID, Question> searchForAuthor(String username) {
    	this.questionsSubset = new HashMap<>();
    	// iterate through all entries in all questions map
    	for (Map.Entry<UUID, Question> entry : this.questions.entrySet()) {
    		Question currQuestion = entry.getValue();
    		// found if term matches author's username
    		if (currQuestion.getAuthor().contains(username)) {
    			this.questionsSubset.put(entry.getKey(), currQuestion);
    		}
    	}
    	return this.questionsSubset;
    }
    
    /**
     * Searches all the questions with a resolved status matching state.
     * 
     * @param state Searches for a subset of questions with a resolved status of state.
     * @return The resulting query subset
     */
    private HashMap<UUID, Question> getAnsweredQuestions(boolean state) {
    	this.questionsSubset = new HashMap<>();
    	for (Map.Entry<UUID, Question> entry : this.questions.entrySet()) {
    		Question currQuestion = entry.getValue();
    		if (currQuestion.getResolved() == state) {
    			this.questionsSubset.put(entry.getKey(), currQuestion);
    		}
    	}
    	return this.questionsSubset;
    }
    
	public HashMap<UUID, Question> getAllQuestions() { return this.questions; }
	public HashMap<UUID, Question> getFoundQuestions() { return this.questionsSubset; }
	public void clearFoundQuestions() { this.questionsSubset = null; }
}
