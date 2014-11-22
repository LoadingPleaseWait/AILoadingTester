package us.loadingpleasewait.ailoadingtester;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import wota.gamemaster.GameWorldFactory;
import wota.gamemaster.SimulationParameters;
import wota.gamemaster.StatisticsLogger;
import wota.gameobjects.GameWorld;
import wota.gameobjects.Parameters;

public class AILoadingTester {
	
	private GameWorld gameWorld;
	private GameWorldFactory gameWorldFactory;
	private int wins, losses;
	private boolean finished;
	
	public static void main(String[] args) {
		ArrayList<AILoadingTester> testerList = new ArrayList<AILoadingTester>();
		
		// create 4 testers and run each in a thread
		for(int i = 0; i < 4; i++){
			AILoadingTester tester = new AILoadingTester();
			tester.setUp();
			testerList.add(tester);
		}
		
		for(AILoadingTester tester : testerList){
			new Thread(() -> tester.runGame()).start();
		}
		
		// wait for all tests to finish
		boolean allTestsFinished = false;
		while(!allTestsFinished){
			allTestsFinished = true;
			for(AILoadingTester tester : testerList){
				allTestsFinished &= tester.finished;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		
		int totalWins = 0, totalLosses = 0;
		for(AILoadingTester tester : testerList){
			totalWins += tester.wins;
			totalLosses += tester.losses;
		}
		System.out.println("AILoading won " + totalWins + " games and lost " + totalLosses + " out of " + (totalLosses + totalWins) + " games");
	}

	/**
	 * Set up the GameWorldFactory before the game
	 */
	public void setUp(){
		// create paramater objects
		Properties settingsProperties = new Properties();
		Properties parametersProperties = new Properties();
		
		settingsProperties.setProperty("IS_GRAPHICAL", "false");
		settingsProperties.setProperty("AI_PACKAGE_NAMES", "loadingpleasewait, bonnmath, bvb, clonewarriors, dichterunddenker, pwahs03, solitary");
		settingsProperties.setProperty("NUMBER_OF_ROUNDS", "2");
		settingsProperties.setProperty("HOME_AND_AWAY", "true");
		settingsProperties.setProperty("TOURNAMENT", "true");
		settingsProperties.setProperty("FRAMES_PER_SECOND", "0");
		settingsProperties.setProperty("INITIAL_TICKS_PER_SECOND", "100");
		settingsProperties.setProperty("DISPLAY_WIDTH", "700");
		settingsProperties.setProperty("DISPLAY_HEIGHT", "700");
		
		SimulationParameters simulationParameters = new SimulationParameters(settingsProperties);
		
		parametersProperties.setProperty("SIZE_X", "1000");
		parametersProperties.setProperty("SIZE_Y", "1000");
		parametersProperties.setProperty("HILL_RADIUS", "20");
		parametersProperties.setProperty("ATTACK_RANGE", "15");
		parametersProperties.setProperty("VULNERABILITY_WHILE_CARRYING", "5");
		parametersProperties.setProperty("INITIAL_SUGAR_RADIUS", "10");
		parametersProperties.setProperty("INITIAL_SUGAR_IN_SOURCE", "500");
		parametersProperties.setProperty("ANT_COST", "20");
		parametersProperties.setProperty("STARTING_FOOD", "300");
		parametersProperties.setProperty("TICKS_BETWEEN_PICK_UPS_AT_SOURCE", "5");
		parametersProperties.setProperty("CORPSE_DECAY_TIME", "500");
		parametersProperties.setProperty("MINIMUM_DISTANCE_BETWEEN_HILLS", "400");
		parametersProperties.setProperty("MINIMUM_STARTING_SUGAR_DISTANCE", "300");
		parametersProperties.setProperty("MAXIMUM_STARTING_SUGAR_DISTANCE", "400");
		parametersProperties.setProperty("MINIMUM_STARTING_SUGAR_DISTANCE_TO_OTHER_HILLS", "150");
		parametersProperties.setProperty("MINIMUM_SUGAR_DISTANCE", "200");
		parametersProperties.setProperty("MINIMUM_SUGAR_DISTANCE_TO_OTHER_SUGAR", "22");
		parametersProperties.setProperty("SUGAR_SOURCES_PER_PLAYER", "3");
		parametersProperties.setProperty("FRACTION_OF_ALL_ANTS_NEEDED_FOR_VICTORY", "0.95");
		parametersProperties.setProperty("DONT_CHECK_VICTORY_CONDITION_BEFORE", "500");
		parametersProperties.setProperty("MAX_TICKS_BEFORE_END", "10000");
		
		Parameters parameters = new Parameters(parametersProperties, 7);
		gameWorldFactory = new GameWorldFactory(new Random().nextLong(), parameters, simulationParameters);
		
		createGame();
	}

	/**
	 * Create the next GameWorld.
	 */
	public void createGame(){
		boolean skipGame;
		do{
			gameWorld = gameWorldFactory.nextGameWorld();
			if(gameWorld != null)
				gameWorld.setLogger(new StatisticsLogger(gameWorld.getPlayers()));
			skipGame = gameWorld != null && !gameWorld.getPlayers().parallelStream().anyMatch(player -> player.name.equals("AILoading"));// games without an AI named AILoading
		}while(skipGame); // skip games without AILoading in them
	}
	
	/**
	 * Play the test game
	 */
	public void runGame(){
		while(gameWorld != null){
			while(gameWorld.getWinner().isEmpty()){
				gameWorld.tick();
			}
			System.out.println(gameWorld.getWinner().get(0).name + " - tick " + gameWorld.tickCount());
			if(gameWorld.getWinner().get(0).name.equals("AILoading"))
				wins++;
			else
				losses++;
			createGame();// create next game
		}
		finished = true;
	}
}
