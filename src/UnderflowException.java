/* 
 * The UnderflowException class for programming problem 1, hwk 3.
 * 
 *  this class implements a simple underflow exception and extends Exception
 *  class.
 * 
 * Written by Sasha Beltinova, sab2229.
 * 
 */
@SuppressWarnings("serial")
public class UnderflowException extends Exception {

  public UnderflowException() {
    super("Underflow exception encountered");
  }
}
