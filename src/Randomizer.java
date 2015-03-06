import java.io.*;
import java.util.Scanner;

public class Randomizer {
	private static final int YEARS = 25;
	private static Team[] bracket;
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);	 
	    System.out.println("Enter 1 for seed based bracket or 2 "
	    					+ "for smart trend based bracket: ");
	    int choice = in.nextInt();
	    System.out.println(choice);
	    in.close();
	
		createBracket();
		
		//Runs the bracket until there is only one team left.
		while (bracket.length > 1) {
			Team[] tempBracket = new Team[bracket.length / 2];
			for (int i = 0; i < bracket.length / 2; i++) {
				Team winner = null;
				Team team1 = bracket[2 * i];
				Team team2 = bracket[2 * i + 1];
				if (choice == 2 && bracket.length == 64) {
					winner = smartFindWinnerRound1(team1, team2);
				} else if (choice == 2 && bracket.length == 32) {
					winner = smartFindWinnerRound2(team1, team2);
				} else {
					winner = findWinner(team1, team2);
				}
				tempBracket[i] = winner;
				System.out.print(winner);				
			}
			bracket = tempBracket;
			System.out.println();
		}		
	}

	/**
	 * Using the trends from the past 25 years, determines how a low seeded team 
	 * will do in the second round
	 * @param team1
	 * @param team2
	 * @return winning team
	 */
	private static Team smartFindWinnerRound2(Team team1, Team team2) {
		int team2Wins = 0;
		int team1Wins = 0;
		
		Team highSeed;
		Team lowSeed;
		if ((team2.seed >= 11 && team1.seed >= 11) || (team2.seed < 11 && team1.seed < 11)) {
			return findWinner(team1, team2);
		} else if (team2.seed >= 11) {
			highSeed = team2;
			lowSeed = team1;
		} else {
			highSeed = team1;
			lowSeed = team2;
		}
		switch (highSeed.seed) {
        case 16:
        		team1Wins = 1; //Never actually happened because a #1 has never lost to #16	
        		team2Wins = 0;
                break;
        case 15:	
        		team1Wins = 6;
        		team2Wins = 1;
				break;
        case 14:
        		team1Wins = 15;
        		team2Wins = 2;
		        break;
		case 13:	
				team1Wins = 19;
				team2Wins = 6;
				break;
		case 12:	
				team1Wins = 21;
				team2Wins = 20;
	            break;
	    case 11:	
	    		team1Wins = 24;
	    		team2Wins = 15;
				break;
        default: System.err.println("Seeding Error");
        		 System.exit(1);
                 break;
		}
		return randomWinner(highSeed, lowSeed, team1Wins + team2Wins, team2Wins);
	}

	/**
	 * Using the trends from the past 25 years, determines how a low seeded team 
	 * will do in the first round.
	 * @param team1
	 * @param team2
	 * @return winning team
	 */
	private static Team smartFindWinnerRound1(Team team1, Team team2) {	
		int team2Wins = 0;
        switch (team2.seed) {
            case 16:	
            		team2Wins = 0;
                    break;
            case 15:	
            		team2Wins = 7;
    				break;
            case 14:	
            		team2Wins = 17;
			        break;
			case 13:	
					team2Wins = 25;
					break;
			case 12:	
					team2Wins = 41;
		            break;
		    case 11:	
		    		team2Wins = 39;
					break;
		    case 10:	
		    		team2Wins = 50;
				    break;
			case 9:		
					team2Wins = 58;
					break;
            default: System.err.println("Seeding Error");
            		 System.exit(1);;
                     break;
        }
        
        return randomWinner(team2, team1, YEARS * 4, team2Wins);
	}

	/**
	 * Finds the winner of a matchup based on how they are seeded
	 * @param team1
	 * @param team2
	 * @return winning team
	 */
	private static Team findWinner(Team team1, Team team2) {
		return randomWinner(team1, team2, team1.seed + team2.seed, team2.seed);
	}

	/**
	 * Common code used in all winner methods to determin the winning team based on
	 * the generated random number.
	 * @param team1
	 * @param team2
	 * @param multiplier
	 * @param team2Value
	 * @return winning team
	 */
	private static Team randomWinner(Team team1, Team team2, int multiplier, int team2Value) {
		double random = Math.random() * multiplier;
		if (team2Value > random) {
			return team1;
		} else  {
			return team2;
		}
	}

	/**
	 * Creates the original bracket based on a text file with all the teams and seeds.
	 */
	private static void createBracket() {
		bracket = new Team[64];	
        File file = new File("seeds.txt");
        try {
            Scanner scanner = new Scanner(file);
            int currIndex = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] lineArr = line.split(",");
                Team current = new Team(Integer.parseInt(lineArr[0]), lineArr[1]);
                bracket[currIndex] = current;
                currIndex++;
                System.out.print(current);
            }
            System.out.println();
            scanner.close();
        } catch (FileNotFoundException e) {
        	System.err.println("Error opening/reading/writing input or output file.");
            System.exit(1);
        }	
	}
}
