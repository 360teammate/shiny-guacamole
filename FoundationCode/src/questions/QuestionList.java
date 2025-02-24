package questions;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import application.StartCSE360; // Importing the main application class, likely containing the database helper

/**
 * Manages a collection of questions, providing methods to add, edit, delete, 
 * and retrieve questions. Also supports storing a subset of filtered questions.
 */
public class QuestionList {
    private HashMap<UUID, Question> questions; // Stores all questions mapped by UUID
    private HashMap<UUID, Question> questionsSubSet; // Stores a subset of filtered questions

    /**
     * Constructor to initialize the question list with an existing set of questions.
     *
     * @param questions A HashMap containing questions indexed by UUID.
     */
    public QuestionList(HashMap<UUID, Question> questions) {
        this.questions = questions;
        this.questionsSubSet = new HashMap<>(); // Initialize an empty subset of questions
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
	
	public HashMap<UUID, Question> getAllQuestions() { return this.questions; }
	public HashMap<UUID, Question> getFoundQuestions() { return this.questionsSubSet; }
}
