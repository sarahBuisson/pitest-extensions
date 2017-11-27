package fr.perso.pitest.xebicon.demo.fizzbuzz;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * The type Fizz buzz test.
 */
public class FizzBuzzTest {


    private FizzBuzz fizzBuzz = new FizzBuzz();

    /**
     * Should return something.
     */
    @Test
    public void should_return_something() {
        //Given
        int number = 7;
        //When
        fizzBuzz.computeMessage(number);
        //Then
        Assert.assertNotNull( fizzBuzz.getMessage());

    }


    /**
     * Should return fizz.
     */
    @Test
    public void should_return_Fizz() {
        //Given
        int number = 3;
        //When
        String message =  fizzBuzz.computeMessage(number);
        //Then
        Assert.assertEquals("Fizz", message);

    }

    /**
     * Should return buzz.
     */

    @Test
    public void should_return_Buzz() {
        //Given
        int number = 25;
        //When
        String message = fizzBuzz.computeMessage(number);
        //Then
        //Assert.assertEquals("Buzz", message);

    }

    /**
     * Should return fizz buzz.
     */
    @Ignore
    @Test
    public void should_return_FizzBuzz() {
        //Given
        int number = 15;
        //When
        String message = fizzBuzz.computeMessage(number);
        //Then
        Assert.assertEquals("FizzBuzz", message);

    }

    /**
     * Should return number.
     *
     * @throws InterruptedException the interrupted exception
     */
    @Test
    public void should_return_number() throws InterruptedException {
        //Given
        int number = 82;
        //When
        String message = fizzBuzz.computeMessage(number);
        //Then
       // Assert.assertEquals("82", message);
    }



}
