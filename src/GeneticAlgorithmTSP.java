import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithmTSP {

    private static final int POPULATION_SIZE = 100;
    private static final int MAX_GENERATIONS = 1000;
    private static final double MUTATION_RATE = 0.01;

    private static final double[][] travelCosts = {
            {0, 1, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300},
            {300, 0, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300},
            {300, 300, 0, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300},
            {300, 300, 300, 0, 1, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300},
            {300, 300, 300, 300, 0, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300},
            {300, 300, 300, 300, 300, 0, 300, 300, 300, 300, 300, 300, 300, 300, 300},
            {300, 300, 300, 300, 300, 300, 0, 300, 300, 300, 300, 300, 300, 300, 300},
            {300, 300, 300, 300, 300, 300, 300, 0, 300, 300, 300, 300, 300, 300, 300},
            {300, 300, 300, 300, 300, 300, 300, 300, 0, 300, 300, 300, 300, 300, 300},
            {300, 300, 300, 300, 300, 300, 300, 300, 300, 0, 300, 300, 300, 300, 300},
            {300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 0, 300, 300, 300, 300},
            {300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 0, 300, 300, 300},
            {300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 0, 300, 300},
            {300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 0, 300},
            {300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 0},
    };


public static void main(String[] args) {
        int numCities = travelCosts.length;
        List<int[]> population = generateInitialPopulation(numCities, POPULATION_SIZE);

        for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
        population = evolvePopulation(population);
        }

        int[] bestPath = population.get(0);
        double bestCost = calculateTotalCost(bestPath);

        System.out.println("Best path: " + Arrays.toString(bestPath));
        System.out.println("Total cost: " + bestCost);
        }

private static List<int[]> generateInitialPopulation(int numCities, int populationSize) {
        List<int[]> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
        int[] path = generateRandomPath(numCities);
        population.add(path);
        }
        return population;
        }

private static int[] generateRandomPath(int numCities) {
        List<Integer> path = new ArrayList<>();
        for (int i = 0; i < numCities; i++) {
        path.add(i);
        }
        Collections.shuffle(path);
        return path.stream().mapToInt(i -> i).toArray();
        }

private static List<int[]> evolvePopulation(List<int[]> population) {
        List<int[]> newPopulation = new ArrayList<>();
        newPopulation.add(getFittest(population)); // Elitism: Keep the best individual from previous generation

        while (newPopulation.size() < POPULATION_SIZE) {
        int[] parent1 = selectParent(population);
        int[] parent2 = selectParent(population);
        int[] offspring = crossover(parent1, parent2);
        mutate(offspring);
        newPopulation.add(offspring);
        }

        return newPopulation;
        }

private static int[] selectParent(List<int[]> population) {
        Random random = new Random();
        return population.get(random.nextInt(population.size()));
        }

private static int[] crossover(int[] parent1, int[] parent2) {
        Random random = new Random();
        int[] offspring = new int[parent1.length];
        int startPos = random.nextInt(parent1.length);
        int endPos = random.nextInt(parent1.length);

        for (int i = 0; i < parent1.length; i++) {
        if (startPos < endPos && i >= startPos && i <= endPos) {
        offspring[i] = parent1[i];
        } else if (startPos > endPos && !(i <= startPos && i >= endPos)) {
        offspring[i] = parent1[i];
        }
        }

        for (int i = 0; i < parent2.length; i++) {
        if (!contains(offspring, parent2[i])) {
        for (int j = 0; j < offspring.length; j++) {
        if (offspring[j] == 0) {
        offspring[j] = parent2[i];
        break;
        }
        }
        }
        }

        return offspring;
        }



private static void mutate(int[] offspring) {
        Random random = new Random();
        if (random.nextDouble() < MUTATION_RATE) {
        int pos1 = random.nextInt(offspring.length);
        int pos2 = random.nextInt(offspring.length);

        int temp = offspring[pos1];
        offspring[pos1] = offspring[pos2];
        offspring[pos2] = temp;
        }
        }

private static boolean contains(int[] arr, int element) {
        for (int i : arr) {
        if (i == element) {
        return true;
        }
        }
        return false;
        }

private static int[] getFittest(List<int[]> population) {
        double minCost = Double.MAX_VALUE;
        int[] fittest = null;
        for (int[] individual : population) {
        double cost = calculateTotalCost(individual);
        if (cost < minCost) {
        minCost = cost;
        fittest = individual;
        }
        }
        return fittest;
        }

private static double calculateTotalCost(int[] path) {
        double totalCost = 0;
        for (int i = 0; i < path.length - 1; i++) {
        totalCost += travelCosts[path[i]][path[i + 1]];
        }
        totalCost += travelCosts[path[path.length - 1]][path[0]]; // Return to the starting city
        return totalCost;
        }
        }
