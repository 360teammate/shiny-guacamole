package questions;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import application.StartCSE360; // Importing main application class, likely containing the database helper

/**
 * Manages a collection of answers, providing methods to add, edit, delete, 
 * and retrieve answers. It also supports a subset of filtered answers.
 */
public class AnswerList {
    private HashMap<UUID, Answer> answers; // Stores all answers by their UUID
    private HashMap<UUID, Answer> answersSubset; // Stores a filtered subset of answers

    /**
     * Constructor for initializing the answer list with a given set of answers.
     *
     * @param answers A HashMap containing answers indexed by their UUID.
     */
    public AnswerList(HashMap<UUID, Answer> answers) {
        this.answers = answers;
        this.answersSubset = new HashMap<>(); // Initialize an empty subset of answers
    }

    /**
     * Creates a new answer, adds it to the answer list, and inserts it into the database.
     *
     * @param parent   The parent question to which the answer belongs.
     * @param bodyText The text content of the answer.
     * @param author   The author of the answer.
     * @return The UUID of the newly created answer.
     */
    public UUID newAnswer(Question parent, String bodyText, String author) {
        Answer newAnswer = new Answer(UUID.randomUUID(), bodyText, author);
        this.answers.put(newAnswer.getUUID(), newAnswer); // Store the new answer in the HashMap

        try {
            // Insert the new answer into the database using the helper class
            StartCSE360.databaseHelper.insertAnswer(newAnswer);
        } catch (SQLException e) {
            // Print stack trace for debugging in case of database failure
            e.printStackTrace();
        }
        return newAnswer.getUUID();
    }

    /**
     * Retrieves an answer by its UUID.
     *
     * @param uuid The UUID of the answer to retrieve.
     * @return The corresponding Answer object, or null if not found.
     */
    public Answer getAnswer(UUID uuid) {
        return this.answers.get(uuid);
    }

    /**
     * Edits the body text of an existing answer.
     *
     * @param uuid        The UUID of the answer to be edited.
     * @param newBodyText The new content for the answer.
     */
    public void editAnswer(UUID uuid, String newBodyText) {
        this.answers.get(uuid).editBodyText(newBodyText);
    }

    /**
     * Deletes an answer from the answer list.
     *
     * @param uuid The UUID of the answer to be removed.
     */
    public void deleteAnswer(UUID uuid) {
        this.answers.remove(uuid);
    }

	public HashMap<UUID, Answer> getAllAnswers() { return this.answers; }
	public HashMap<UUID, Answer> getFoundAnswers() { return this.answersSubset; }
}
