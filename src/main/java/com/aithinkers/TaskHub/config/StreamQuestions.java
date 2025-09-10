package com.aithinkers.TaskHub.config;

import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.*;

public class StreamQuestions {
    public static void main(String[] args) {
        // Sample data for all questions
        String[] names = {"Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace"};
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        List<String> fruits = Arrays.asList("apple", "banana", "orange", "kiwi", "mango", "apple", "banana");
        List<Integer> ages = Arrays.asList(15, 20, 25, 30, 35, 40, 45, 50, 60, 70);
        List<String> cities = Arrays.asList("Delhi", "Mumbai", "Bangalore", "Kolkata", "Chennai", "Delhi", "Pune");

        List<Employee> employee=Arrays.asList("Delhi", "Mumbai", "Bangalore", "Kolkata", "Chennai", "Delhi", "Pune");
        //
        List<List<Integer>> nestedNumbers = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9)
        );

        // ---------- PRACTICE QUESTIONS ----------

        // Q1: Print all names in uppercase using streams.
        List<String> upperNames = Arrays.stream(names)
                .map(String::toUpperCase)
                .toList();   // Java 16+, or use collect(Collectors.toList())
        System.out.println(upperNames);

        // Q2: Filter names that start with "A" and collect them into a list.
        List<String> Astart=Arrays.stream(names).filter(name -> name.startsWith("A"))
                .toList();
        System.out.println(Astart);

        // Q3: Count how many names have length > 4.
        int counting=(int)Arrays.stream(names).filter(name -> name.length()>4).count();
        System.out.println(counting);


        // Q4: Find the first name that starts with "C".
        Optional<String> Cstart=Arrays.stream(names).filter(name -> name.startsWith("C"))
                .findFirst();
        System.out.println(Cstart);

        String Cstart2 = Arrays.stream(names)
                .filter(name -> name.startsWith("C"))
                .findFirst()
                .orElse("No match found");

        System.out.println(Cstart);

        // Q5: Sort the names alphabetically and print them.
        List<String> sorted=Arrays.stream(names).sorted().toList();
        System.out.println(sorted);

        // Q6: Square each number in 'numbers' and print.
        List<Integer> squared=Arrays.stream(numbers).map(number -> number*number).boxed()
                .toList();
        System.out.println(squared);

        // Q7: Filter even numbers and collect them into a list.
        List<Integer> even=Arrays.stream(numbers).filter(number -> number%2==0).boxed()
                .toList();
        System.out.println(even);
        // Q8: Find the sum of all numbers using streams.
        long sum=Arrays.stream(numbers).sum();
        System.out.println(sum);
        // Q9: Check if any number in 'numbers' is greater than 8.
        List<Integer> eight=Arrays.stream(numbers).filter(number -> number>8).boxed()
                .toList();
        System.out.println(eight);
        // Q10: Find the maximum number in 'numbers'.
        int max = Arrays.stream(numbers)
                .max()
                .orElse(-1); // fallback if array is empty
        System.out.println(max);

        // Q11: Get distinct fruits from the list.
        List<String> distinct=fruits.stream().distinct().toList();
        System.out.println(distinct);
        // Q12: Count how many times each fruit appears (frequency map).
        Map<String, Long> freqMap = fruits.stream()
                .collect(Collectors.groupingBy(
                        fruit -> fruit,
                        Collectors.counting()
                ));

        System.out.println(freqMap);
        // Q13: Filter fruits with length > 5.
        List<String> len5=fruits.stream().filter(n->n.length()>5).toList();
        System.out.println(len5);

        // Q14: Join all fruits into a single string separated by commas.
        String one=fruits.stream().collect(Collectors.joining(","));
        System.out.println(one);

        // Q15: Find the longest fruit name.
        String longest = fruits.stream()
                .max(Comparator.comparingInt(String::length))
                .orElse("No fruits");
        System.out.println(longest);
        // Q16: Find all ages greater than 30.
        // Q17: Compute average age.
        long avg= (long) ages.stream().mapToInt(age->age).average().orElse(0.0);
        System.out.println(avg);
        // Q18: Partition ages into two groups: <=30 and >30.

        // Q19: Find the oldest age.
        long maxage= (long) ages.stream().mapToInt(age->age).max().orElse(0);
        System.out.println(maxage);
        // Q20: Check if all ages are above 10.


        // Q21: Find distinct cities.
        // Q22: Group cities by name and count frequency.
        // Q23: Sort cities by length.
        // Q24: Check if "Pune" exists in the list.
        // Q25: Collect cities into a Set.

        // Q26: Flatten nestedNumbers into a single list of integers.
        // Q27: Find sum of all numbers in nestedNumbers.
        // Q28: Find distinct numbers in nestedNumbers.
        // Q29: Count how many numbers > 5 in nestedNumbers.
        // Q30: Find the maximum number in nestedNumbers.

        // BONUS:
        // Q31: Create a Map<String, Integer> from 'names' where key = name, value = length of name.
        // Q32: From 'ages', group ages by decade (20s, 30s, 40s, etc.).
        // Q33: From 'fruits', group by first letter.
        // Q34: From 'numbers', partition into even and odd.
        // Q35: From 'names', find the distinct characters used across all names.
    }
}
