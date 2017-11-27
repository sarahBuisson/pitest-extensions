package fr.perso.pitest.xebicon.demo.fizzbuzz;

public class FizzBuzz {

    String message = "";

    public String computeMessage(int number) {
        if (number % 3 == 0) {
            appendMessage("Fizz");
        }
        if (number % 5 == 0) {
            appendMessage("Buzz");
        }
        if (message.toString().isEmpty()) {
            appendMessage("" + number);
        }
        if (false){

            appendMessage("code not covered by standard nor mutation");

        }
        if (false){

            appendMessage("code still not covered by standard nor mutation");

        }
        return message;
    }

    void appendMessage(String subMessage) {
        message += subMessage;
    }

    /**
     * Getter for property 'message'.
     *
     * @return Value for property 'message'.
     */
    public String getMessage() {
        return message;
    }
}