import java.util.*;

public class TravelWithBudget {

    private static final int[] flightCosts = {
            4162, 8305, 1212, 1460, 3213, 6288, 9138, 5035, 11870, 9454, 1142, 1788, 1501, 5117
    };

    private static final double[] livingCosts = {
            184.96, 192.65, 80.10, 76.08, 118.76, 77.03, 112.43, 119.67, 83.11, 133.98, 116.94, 79.51, 67.80, 53.40
    };

    private static final int workIncome = 2000; // Income earned by working
    private static final int workDuration = 3; // Number of days to work
    private static final double initialBudget = 10000; // Initial budget

    public static void main(String[] args) {
        double totalCost = 0;
        int[] nightsStayed = new int[livingCosts.length];

        double budget = initialBudget;
        int currentCity = 0; // Start from the first city

        while (currentCity < flightCosts.length) {
            if (flightCosts[currentCity] <= budget) {
                // Move to the next city
                budget -= flightCosts[currentCity]; // Deduct flight cost
                totalCost += livingCosts[currentCity]; // Cost of staying one night
                nightsStayed[currentCity] += 1; // Increment nights stayed
                currentCity++;
            } else {
                // Stay and work in the current city until enough budget to move to the next city
                int workDays = workDuration;
                totalCost += livingCosts[currentCity] * workDays; // Cost of staying and working
                nightsStayed[currentCity] += workDays;
                budget += workDays * workIncome; // Income earned by working
                budget -= livingCosts[currentCity] * workDays; // Cost of living during work
            }
        }
        totalCost += Arrays.stream(flightCosts).sum();

        System.out.println("Total cost: " + totalCost);
        for (int i = 0; i < nightsStayed.length; i++) {
            System.out.println("Number of Work Sessions in City " + (i) + ": " + (nightsStayed[i] -1));
        }
    }
}


