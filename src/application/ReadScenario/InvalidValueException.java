package application.ReadScenario;

 /**
 * Exception class for invalid value.
 * This excpetion should be thrown when an invalid value is encountered.
 * (If the description contains an out of bounds value)
 */
 public class InvalidValueException extends Exception {
    
    /**
   * Constructs a new InvalidValueException with the specified detail message.
   * @param message the detail message. It will be used to provide information about the cause of the exception.
   */
    public InvalidValueException(String message) {
        super(message);
    }
}