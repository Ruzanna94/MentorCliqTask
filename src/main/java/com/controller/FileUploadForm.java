package com.controller;

import com.Helper;
import com.entity.Employee;
import com.entity.EmployeesPairs;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.*;

public class FileUploadForm {
    private boolean inValid =false;
    private List<String> fileErrorMessages;
    private List<Employee> employees;
    private Map<Integer,List<EmployeesPairs>> possibleVariants;
    private List<EmployeesPairs> bestPairsVariant;
    private double bestAverageScore=0;


    void readFile(FileInputStream  is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                if(isDataInvalid(csvRecord)){
                    return;
                }
                Employee employee= createEmployee(csvRecord);
                getEmployees().add(employee);
            }

        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
        findPairCombination(getEmployees());
        calculateCompatibleEmployeesMatches();
    }

   Employee createEmployee( CSVRecord csvRecord){
       String name =csvRecord.get(0);
       String email =csvRecord.get(1);
       String division=csvRecord.get(2);
       int age = Integer.parseInt(csvRecord.get(3));
       int timezone = Integer.parseInt(csvRecord.get(4));
       return  new Employee(name,email,division,age,timezone);
   }

    public List<Employee> getEmployees() {
        if(employees==null){
            employees= new ArrayList<>();
        }
        return employees;
    }

    public Map<Integer, List<EmployeesPairs>> getPossibleVariants() {
        if(possibleVariants==null){
            possibleVariants= new HashMap<>();
        }
        return possibleVariants;
    }

   public void clearData(){
        employees=null;
       fileErrorMessages=null;
       possibleVariants=null;
    }

    boolean isDataInvalid(CSVRecord csvRecord) {
        if (Integer.parseInt(csvRecord.get(3)) < 1) {
            inValid = true;
            getFileErrorMessages().add("Age is invalid.");
        }
        if (Integer.parseInt(csvRecord.get(4)) > 12 && Integer.parseInt(csvRecord.get(4)) < -12) {

            {
                inValid = true;
                getFileErrorMessages().add("Time zone must  be [-12,12]");
            }
        }
        return inValid;
    }

    public List<String> getFileErrorMessages() {
        if(fileErrorMessages==null){
            fileErrorMessages=new ArrayList<>();
        }
        return fileErrorMessages;
    }

    public boolean isInValid() {
        return inValid;
    }

    public List<EmployeesPairs> getBestPairsVariant() {
        return bestPairsVariant;
    }

    public double getBestAverageScore() {
        return bestAverageScore;
    }

    void findPairCombination(List<Employee> employees)
    {
        int employeeSize = employees.size();
        int variantsCount = employeeSize - 1;
        int halfSize = employeeSize / 2;
        List<Employee> variants = new ArrayList<>(employees);
        variants.remove(0);
        int variantSize = variants.size();
        for (int variant = 0; variant < variantsCount; variant++) {
            int variantIdx = variant % variantSize;
            getPossibleVariants().put(variant + 1, new ArrayList<>());
            getPossibleVariants().get(variant + 1).add(new EmployeesPairs(variants.get(variantIdx), employees.get(0)));
            for (int idx = 1; idx < halfSize; idx++) {
                int firstVariant = (variant + idx) % variantSize;
                int secondVariant = (variant + variantSize - idx) % variantSize;
                getPossibleVariants().get(variant + 1).add(new EmployeesPairs(variants.get(firstVariant), variants.get(secondVariant)));
            }
        }
    }

    void calculateCompatibleEmployeesMatches(){
        getPossibleVariants().values().forEach(variantArrayList -> variantArrayList.forEach(
                pairEmployee -> {
                    if (Helper.employeePairsPredicateBYAge(pairEmployee)) {
                        pairEmployee.setMatchesPercent(pairEmployee.getMatchesPercent()+Helper.MATCH_BY_AGE);
                    }
                    if (Helper.employeePairsPredicateBYDivision(pairEmployee)) {
                        pairEmployee.setMatchesPercent(pairEmployee.getMatchesPercent()+Helper.MATCH_BY_DIVISION);
                    }
                    if (Helper.employeePairsPredicateBYTimezone(pairEmployee)) {
                        pairEmployee.setMatchesPercent(pairEmployee.getMatchesPercent()+Helper.MATCH_BY_TIMEZONE);
                    }
                }
        ));
    }

    public double average(List<EmployeesPairs> employeesPairs) {
        double average = employeesPairs.stream()
                .mapToInt(EmployeesPairs::getMatchesPercent)
                .average().getAsDouble();
        if (average > bestAverageScore) {
            bestAverageScore = average;
            bestPairsVariant = employeesPairs;
        }
        return average;
    }
}
