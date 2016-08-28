package com.skybet.roulette;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;

/**
 * Unit test for Roulette
 */
public class RouletteTest 
{
    

    /**
     * Base case to test creating a new default Roulette instance
     */
	@Test
    public void testBase(){
    	Roulette simpleRoulette = new Roulette();
    	assertNotNull(simpleRoulette);
    }
    
    /**
     * Test constructors with parameters
     */
	@Test
    public void testConstructorsWithParm(){
    	Roulette americanRoulette = new Roulette(38);
    	assertNotNull(americanRoulette);
    }
    
    /**
     * Spin return random number within range
     */
	@Test
    public void testSpin(){
    	Roulette simpleRoulette = new Roulette();
    	int result = simpleRoulette.spin();
    	assertTrue(result >=0 && result <= Roulette.EUROPEAN_TOTAL);
    	
    	Roulette americanRoulette = new Roulette(38);
    	result = americanRoulette.spin();
    	assertTrue(result >=0 && result <= 38);
    }

    /**
     * Spin lots of times and return random number within range
     */
	@Test
    public void testSpinEvenly(){
    	Roulette simpleRoulette = new Roulette();
    	int totalRuns = 500000;
    	int[] result = new int[Roulette.EUROPEAN_TOTAL];
    	for(int i=0; i<totalRuns; i++){
    		result[simpleRoulette.spin()]++;
    	}
    	
    	double average = totalRuns / Roulette.EUROPEAN_TOTAL;
    	double acceptanceRange = 0.05;
    	
    	for(int i=0; i<Roulette.EUROPEAN_TOTAL; i++){
    		assertTrue(result[i] >= average*(1-acceptanceRange) && 
    				result[i] <= average*(1+acceptanceRange));
    	}
    }


    /**
     * Test duplicated bet ID. System error expected.
     * @throws Exception 
     */
	@Test(expected = Exception.class)
    public void testDupBetIdError() throws Exception{
    	TestingRoulette testingRoulette = new TestingRoulette();
    	int betID = 123;
    	double betAmount = 500;
		assertTrue(testingRoulette.placeBet(betID, "low", betAmount));
		assertTrue(testingRoulette.placeBet(betID, "red", betAmount));
    }
	
	
    /**
     * Simulate normal simplified situation of a game play
     * @throws Exception 
     */
	@Test
    public void testBasicFlow() throws Exception{
    	TestingRoulette testingRoulette = new TestingRoulette();
    	int betID = 123;
    	int spinToNumber = 5;
    	double betAmount = 500;
    	
    	try { 
    		assertTrue(testingRoulette.placeBet(betID, "low", betAmount));
		} catch (Exception e) {	e.printStackTrace();}
    	
    	testingRoulette.spin(spinToNumber);
    	assertEquals(betAmount*2, testingRoulette.pay(betID), 0);
    	
    }

    /**
     * Test on outside bets
     * @throws Exception 
     */
	@Test
    public void testOutsideBet1() throws Exception{
    	TestingRoulette testingRoulette = new TestingRoulette();
    	int firstBetId = 1000;
    	int spinToNumber = 15;
    	int betAmount = 500;
    	int betId = firstBetId;
    	
    	try {  
    		 assertTrue(testingRoulette.placeBet(betId++, "low", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "high", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "even", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "odd", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "red", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "black", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "dozen1", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "dozen2", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "dozen3", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "column1", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "column2", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "column3", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "basket", betAmount));
		} catch (Exception e) {	e.printStackTrace();}
    	
    	testingRoulette.spin(spinToNumber);
    	betId = firstBetId;

    	//low, high
    	assertEquals( betAmount*2, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	//even, odd                
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*2, testingRoulette.pay(betId++), 0);
    	//red, black               
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*2, testingRoulette.pay(betId++), 0);
    	//dozen                    
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*3, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	//column                   
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*3, testingRoulette.pay(betId++), 0);
    	//basket                   
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	
    }

    /**
     * More test on outside bets
     * @throws Exception 
     */
	@Test
    public void testOutsideBet2() throws Exception{
    	TestingRoulette testingRoulette = new TestingRoulette();
    	int firstBetId = 1000;
    	int spinToNumber = 32;
    	int betAmount = 500;
    	int betId = firstBetId;
    	
    	try {  
    		 assertTrue(testingRoulette.placeBet(betId++, "low", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "high", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "even", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "odd", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "red", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "black", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "dozen1", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "dozen2", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "dozen3", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "column1", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "column2", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "column3", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "basket", betAmount));
		} catch (Exception e) {	e.printStackTrace();}
    	
    	testingRoulette.spin(spinToNumber);
    	betId = firstBetId;

    	//low, high
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*2, testingRoulette.pay(betId++), 0);
    	//even, odd                
    	assertEquals( betAmount*2, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	//red, black               
    	assertEquals( betAmount*2, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	//dozen                    
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*3, testingRoulette.pay(betId++), 0);
    	//column                   
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*3, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	//basket                   
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	
    }

	/**
	 * Zero case
	 * @throws Exception 
	 */
	@Test
    public void testOutsideBet0() throws Exception{
    	TestingRoulette testingRoulette = new TestingRoulette();
    	int firstBetId = 1000;
    	int spinToNumber = 0;
    	int betAmount = 500;
    	int betId = firstBetId;
    	
    	try {  
    		 assertTrue(testingRoulette.placeBet(betId++, "low", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "high", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "even", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "odd", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "red", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "black", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "dozen1", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "dozen2", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "dozen3", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "column1", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "column2", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "column3", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "basket", betAmount));
		} catch (Exception e) {	e.printStackTrace();}
    	
    	testingRoulette.spin(spinToNumber);
    	betId = firstBetId;

    	//low, high
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	//even, odd                
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	//red, black               
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	//dozen                    
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	//column                   
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	//basket                   
    	assertEquals( betAmount*7, testingRoulette.pay(betId++), 0);
    	
    }

	/**
	 * Test inside bets
	 * @throws Exception 
	 */
	@Test
    public void testInsideBet() throws Exception{
    	TestingRoulette testingRoulette = new TestingRoulette();
    	int firstBetId = 1000;
    	int spinToNumber = 8;
    	int betAmount = 100;
    	int betId = firstBetId;
    	
    	try {  
    		 assertTrue(testingRoulette.placeBet(betId++, new int[]{8}, betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, new int[]{4,5}, betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, new int[]{8,11}, betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, new int[]{7,8,10,11}, betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, new int[]{4,5,6,7,8,9}, betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, new int[]{31,32,34,35}, betAmount));
		} catch (Exception e) {	e.printStackTrace();}
    	
    	testingRoulette.spin(spinToNumber);
    	betId = firstBetId;

    	assertEquals( betAmount*36, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*18, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*9, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*6, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	
    }
	
    /**
     * Test undefined bet type and bet not found
     * @throws Exception 
     */

	@Test(expected = Exception.class)
    public void testUndefinedOutsideBet() throws Exception{
    	TestingRoulette testingRoulette = new TestingRoulette();
    	int firstBetId = 1000;
    	int spinToNumber = 15;
    	int betAmount = 500;
    	int betId = firstBetId;
    	
    	try {  
    		 assertFalse(testingRoulette.placeBet(betId++, "rouge", betAmount));
		} catch (Exception e) {	e.printStackTrace();}
    	
    	testingRoulette.spin(spinToNumber);
    	betId = firstBetId;
    	testingRoulette.pay(betId++);
    	
    }
	
	/**
	 * Test bets are removed from bet table after pay
	 */
	@Test(expected = Exception.class)
    public void testBetRemovedAfterPay() throws Exception{
    	TestingRoulette testingRoulette = new TestingRoulette();
    	int firstBetId = 1000;
    	int spinToNumber = 1;
    	int betAmount = 500;
    	int betId = firstBetId;
    	
    	try {  
    		 assertTrue(testingRoulette.placeBet(betId++, "low", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "high", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "even", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "odd", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "red", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "black", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "dozen1", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "dozen2", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "dozen3", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "column1", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "column2", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "column3", betAmount));
    		 assertTrue(testingRoulette.placeBet(betId++, "basket", betAmount));
		} catch (Exception e) {	e.printStackTrace();}
    	
    	testingRoulette.spin(spinToNumber);
    	betId = firstBetId;

    	//low, high
    	assertEquals( betAmount*2, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	//even, odd                
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*2, testingRoulette.pay(betId++), 0);
    	//red, black               
    	assertEquals( betAmount*2, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	//dozen                    
    	assertEquals( betAmount*3, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	//column                   
    	assertEquals( betAmount*3, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	assertEquals( betAmount*0, testingRoulette.pay(betId++), 0);
    	//basket                   
    	assertEquals( betAmount*7, testingRoulette.pay(betId++), 0);

    	testingRoulette.pay(firstBetId);
    }
    
    /**
     * Test multi-thread case. (Which makes much more sense in real world!!)
     * @throws Exception 
     */
	@Test
	public void testMultiThread() throws Exception{
    	TestingRoulette testingRoulette = new TestingRoulette();
    	int firstBetId = 0;
    	int betId = firstBetId;
    	int noOfBetsToPlace = 10000;

    	ArrayList<Thread> threadList = new ArrayList<Thread>();
    	for(int i=0; i<noOfBetsToPlace; i++){
    		threadList.add(new TestingPlaceBetThread(betId++, testingRoulette));
    	}
    	for(Thread t : threadList){
    		t.start();
    	}
    	
    	try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {e.printStackTrace();}

    	testingRoulette.spin();
    	betId = firstBetId;
    	
    	assertEquals(noOfBetsToPlace, testingRoulette.betTable.size());
	}

	/**
	 * Thread to place a bet
	 * @author benny
	 *
	 */
    class TestingPlaceBetThread extends Thread{
    	private int betId;
    	Random rnd = new Random();
    	private Roulette testingRoulette;
    	public TestingPlaceBetThread(int betId, Roulette testingRoulette){
    		this.betId = betId;
    		this.testingRoulette = testingRoulette;
    	}
    	public void run(){
    		try {
				sleep(new Long(rnd.nextInt(100)));
				testingRoulette.placeBet(betId, new int[]{rnd.nextInt(Roulette.EUROPEAN_TOTAL)}, 100);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    }
    
    /**
     * Roulette for testing purpose. allowing to spin to any given number
     */
    class TestingRoulette extends Roulette{
    	public int spin(int spinToNumber){
    		currentNumber = spinToNumber;
    		return currentNumber;
    	}
    }
}
