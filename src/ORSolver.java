import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.swap;

public class ORSolver {

    private static final double[][] travelCosts = {
            {0, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300},
            {300, 0, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300},
            {300, 300, 0, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300},
            {300, 300, 300, 0, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300},
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
            {300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 0},// Travel costs from city 0

    };
    private static final double[] livingCosts = {184.96, 192.65, 80.10, 76.08, 118.76, 77.03, 112.43, 119.67, 91.90, 83.11, 145.68,
            133.98, 116.94, 79.51, 67.80, 53.40}; // Cost to live in each city
    private static final int workIncome = 1000; // Income earned by working
    private static final int workDuration = 3; // Number of days to work
    private static final int initialBudget = 2000; // Initial budget


    public static void main(String[] args) {
        int[] cities = {0, 1, 2,3,4,5,6,7,8,9,10,11,12,13,14,15}; // Example cities

        List<int[]> permutations = generatePermutations(cities);

        int maxDays = cities.length * (workDuration + 1); // Maximum number of days needed for the journey
        double minCost = Double.MAX_VALUE;
        int[] minPath = null;
        int[] minDays = null;

        for (int[] path : permutations) {
            double cost = calculateTotalCost(path, maxDays);
            if (cost < minCost) {
                minCost = cost;
                minPath = path;
                minDays = calculateDays(path, maxDays);
            }
        }

        if (minPath != null) {
            System.out.println("Minimum cost: " + minCost);
            System.out.println("Optimal path: " + Arrays.toString(minPath));
            System.out.println("Days in each city: " + Arrays.toString(minDays));
        } else {
            System.out.println("No valid path found.");
        }
    }

    private static double calculateTotalCost(int[] path, int maxDays) {
        double totalCost = 0;
        double budget = initialBudget;
        int currentDay = 0;
        int currentCity = 0; // Start from the first city

        while (currentDay < maxDays) {
            int nextCity = path[currentCity];
            double travelCost = travelCosts[currentCity][nextCity];
            if (travelCost > budget) {
                // If the travel cost is more than the budget, work to earn money
                int daysToWork = (int) Math.ceil(travelCost / workIncome);
                currentDay += daysToWork;
                totalCost += workIncome * daysToWork;
                budget += workIncome * daysToWork;
            }

            // Move to the next city
            totalCost += travelCost;
            budget -= travelCost;
            totalCost += livingCosts[nextCity] * (currentDay + 1); // Cost to live in the current city
            currentDay++;

            currentCity = nextCity; // Move to the next city
        }

        return totalCost;
    }

    private static int[] calculateDays(int[] path, int maxDays) {
        int[] daysInEachCity = new int[path.length];
        int currentDay = 0;
        int currentCity = 0; // Start from the first city

        while (currentDay < maxDays) {
            int nextCity = path[currentCity];
            double travelCost = travelCosts[currentCity][nextCity];
            int daysInCity = 0;

            if (travelCost > 0) {
                // If there's a travel cost, move to the next city
                daysInCity++;
                currentDay++;
            } else {
                // If no travel cost, work to earn money
                int daysToWork = (int) Math.ceil(travelCost / workIncome);
                daysInCity += daysToWork;
                currentDay += daysToWork;
            }

            daysInEachCity[nextCity] += daysInCity;
            currentCity = nextCity; // Move to the next city
        }

        return daysInEachCity;
    }

    private static List<int[]> generatePermutations(int[] cities) {
        List<int[]> permutations = new ArrayList<>();
        permute(cities, 0, permutations);
        return permutations;
    }

    private static void permute(int[] arr, int k, List<int[]> permutations) {
        if (k == arr.length) {
            permutations.add(Arrays.copyOf(arr, arr.length));
        } else {
            for (int i = k; i < arr.length; i++) {
                int[] temp = Arrays.copyOf(arr, arr.length);
                swap(temp, i, k);
                permutations.add(temp);
                permute(temp, k + 1, permutations);
            }
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
