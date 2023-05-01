package application.ReadScenario;

/**
 * Exception class for invalid game description.
 * This excpetion should be thrown when an invalid game description is encountered.
 * (If the description does not include the 4 expected lines)
 */
public class InvalidDescriptionException extends Exception {
  
  /**
   * Constructs a new InvalidDescriptionException with the specified detail message.
   * @param message the detail message. It will be used to provide information about the cause of the exception.
   */
  public InvalidDescriptionException(String message) {
    super(message);
  }
}