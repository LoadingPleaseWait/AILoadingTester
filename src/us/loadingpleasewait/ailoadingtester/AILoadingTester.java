package us.loadingpleasewait.ailoadingtester;

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
	
	public static void main(String[] args) {
		AILoadingTester tester = new AILoadingTester();
		tester.setUp();
		tester.runGame();
		tester.showResults();
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
		parametersProperties.setProperty("FRACTION_OF_ALL_ANTS_NEEDED_FOR_VICTORY", "0.75");
		parametersProperties.setProperty("DONT_CHECK_VICTORY_CONDITION_BEFORE", "500");
		parametersProperties.setProperty("MAX_TICKS_BEFORE_END", "1000");
		
		Parameters parameters = new Parameters(parametersProperties, 7);
		gameWorldFactory = new GameWorldFactory(new Random().nextLong(), parameters, simulationParameters);
		
		createGame();
	}

	/**
	 * Create the next GameWorld.
	 */
	public void createGame(){
		do{
			gameWorld = gameWorldFactory.nextGameWorld();
			if(gameWorld != null)
				gameWorld.setLogger(new StatisticsLogger(gameWorld.getPlayers()));
		}while(gameWorld != null && !gameWorld.getPlayers().get(0).name.equals("AILoading")); // skip games without AILoading in them
	}
	
	/**
	 * Play the test game
	 */
	public void runGame(){
		while(gameWorld != null){
			while(gameWorld.getWinner().isEmpty()){
				gameWorld.tick();
			}
			System.out.println(gameWorld.getWinner().get(0).name);
			if(gameWorld.getWinner().get(0).name.equals("AILoading"))
				wins++;
			else
				losses++;
			createGame();// create next game
		}
	}
	
	/**
	 * Show results of the test
	 */
	public void showResults(){
		System.out.println("AILoading won " + wins + " games and lost " + losses + " out of " + (losses + wins) + " games");
	}
}
