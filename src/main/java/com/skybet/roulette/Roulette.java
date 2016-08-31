package com.skybet.roulette;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.List;

public class Roulette 
{
	private int[] numbers;
	public static final int EUROPEAN_TOTAL = 37;
	private List<String> outsideBetTypes = Arrays.asList("low", "high", "even", "odd", "red", "black", 
			"dozen1", "dozen2", "dozen3", "column1", "column2", "column3", "basket");
	
	private final Random rnd = new Random();
	
	protected int currentNumber;
	private boolean canPlaceBet = true;
	HashMap<Integer, Bet> betTable = new HashMap<Integer, Bet>();
	
	/**
	 * Default constructor to initiate an European roulette
	 */
	public Roulette(){
		initRoulette(EUROPEAN_TOTAL);
	}

	public Roulette(int noOfNumbers){
		initRoulette(noOfNumbers);
		
	}
	
	private void initRoulette(int noOfNumbers) {
		this.numbers = new int[noOfNumbers];
		for(int i=0; i<noOfNumbers; i++){
			this.numbers[i] = i;
		}
	}

	public int getNoOfNumbers(){
		return numbers.length;
	}
	
	public int spin() {
		currentNumber = rnd.nextInt(getNoOfNumbers());
		return currentNumber;
	}
	
	public int getCurrentNumber(){
		return currentNumber;
	}

	/**
	 * Place a bet into bet table. Used for outside bets.
	 * @param betId bet ID. Used as key to store the bet
	 * @param betType Bet type. Case sensitive. For full list of possible value, call getOutsideBetTypes()
	 * @param betAmount
	 * @return true if bet successfully placed
	 * @throws Exception Duplicated betId
	 */
	public  boolean placeBet(int betId, String betType, double betAmount) throws Exception {
		if (!isOutsideBet(betType))		return  false;
		if(!canPlaceBet) 	return false;
		if(betTable.put(betId, new Bet(betType, betAmount)) != null){
			throw new Exception();
		}
		return true;
	}
	
	/**
	 * Place a bet into bet table. Used for inside bets.
	 * @param betId bet ID. Used as key to store the bet
	 * @param insideBets Integer array in ascending order
	 * @param betAmount
	 * @return true if bet successfully placed
	 * @throws Exception thrown when duplicated betId
	 */
	public synchronized boolean placeBet(int betId, int[] insideBets, double betAmount) throws Exception {
		if(!canPlaceBet) 	return false;
		if(betTable.put(betId, new Bet(insideBets, betAmount)) != null){
			throw new Exception();
		}
		return true;
	}
	
	/**
	 * Calculate the payout of the given bet then remove the bet from table
	 * @param betId ID of the bet
	 * @return payout total sum of money return from the table
	 * @throws Exception bet not found
	 */
	public synchronized double pay(int betId) throws Exception {
		double payOut = 0;
		
		Bet bet = betTable.get(betId);
		if(bet == null) {
			throw new Exception();
		}
		String betType = bet.getBetType();
		if(betType == null){
			payOut = getInsideBetPayout(betType, bet.getInsideBets());
		} else{
			payOut = getOutsideBetPayout(betType);
		}
		
		betTable.remove(betId);
		return payOut * bet.getBetAmount();
	}

	private double getInsideBetPayout(String betType, int[] insideBets) {
		if(Arrays.stream(insideBets).anyMatch(singleBet -> singleBet == currentNumber)){
			return (numbers.length - 1) / insideBets.length;
		}
		return 0;
	}

	/**
	 * Check is the bet type an outside bet (Case sensitive)
	 * @param betType bet type
	 * @return True if it is an outside bet
	 */
	private boolean isOutsideBet(String betType) {
		return outsideBetTypes.stream().anyMatch(bt -> betType.equals(bt));
	}

	/**
	 * Calculate the payout of any given outside bet
	 * @param betType
	 * @return the payout of any given outside bet. Return 0 if not hit.
	 */
	private double getOutsideBetPayout(String betType){
		switch (betType){
		case "low":
			if(currentNumber != 0 && currentNumber <= 18)
				return 2;
			break;
		case "high":
			if(currentNumber != 0 && currentNumber >= 19)
				return 2;
			break;
		case "even":
			if(currentNumber != 0 && currentNumber%2 == 0)
				return 2;
			break;
		case "odd":
			if(currentNumber != 0 && currentNumber%2 == 1)
				return 2;
			break;
		case "red":
			List<Integer> redNumbers = Arrays.asList(
							    1, 3, 5, 7, 9, 12,
								14, 16, 18, 19, 21, 23,
								25, 27, 30, 32, 34, 36); 
			if(redNumbers.stream().anyMatch(redNumber -> currentNumber == redNumber)){
				return 2;
			}
			break;
		case "black":
			List<Integer> blackNumbers = Arrays.asList(
								  2, 4, 6, 8, 10, 11,
								  13, 15, 17, 20, 22, 24,
								  26, 28, 29, 31, 33, 35); 
			if(blackNumbers.stream().anyMatch(blackNumber -> currentNumber == blackNumber)){
				return 2;
			}
			break;
		case "dozen1":
			if(currentNumber >= 1 && currentNumber <= 12)
				return 3;
			break;
		case "dozen2":
			if(currentNumber >= 13 && currentNumber <= 24)
				return 3;
			break;
		case "dozen3":
			if(currentNumber >= 25 && currentNumber <= 36)
				return 3;
			break;
		case "column1":
			if(currentNumber%3==1)
				return 3;
			break;
		case "column2":
			if(currentNumber%3==2)
				return 3;
			break;
		case "column3":
			if(currentNumber%3==0 && currentNumber!=0)
				return 3;
			break;
		case "basket":
			if(currentNumber >= 0 && currentNumber <= 3)
				return 7;
			break;
		default: 
			return 0;
				
		}
		return 0;
	}
	

	/**
	 * Return all possible outside bet types. Possible values as follow:
	 * low/high
	 * even/odd
	 * red/black
	 * dozen1: 1~12, dozen2: 13~24, dozen3:25~36
	 * column1/column2/column3 Vertical columns
	 * basket: 0,1,2,3
	 * @return all possible outside bet types
	 */
	public String[] getOutsideBetTypes() {
		return (String[]) outsideBetTypes.toArray();
	}


	/**
	 * A bet storing where is the bet(betType), and how much placed(betAmount); 
	 * @author benny
	 *
	 */
	class Bet{
		private String betType;
		private int[] insideBets;
		private double betAmount;
		
		public Bet(String betType, double betAmount){
			setBetType(betType); 
			setBetAmount(betAmount);
		}

		public Bet(int[] insideBets, double betAmount){
			setInsideBets(insideBets);
			setBetAmount(betAmount);
		}
		
		public String getBetType() {
			return betType;
		}

		public void setBetType(String betType) {
			this.betType = betType;
		}

		public double getBetAmount() {
			return betAmount;
		}

		public void setBetAmount(double betAmount) {
			this.betAmount = betAmount;
		}

		public int[] getInsideBets() {
			return insideBets;
		}

		public void setInsideBets(int[] insideBets) {
			this.insideBets = insideBets;
		}
	}


	
}

