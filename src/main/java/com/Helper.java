package com;

import com.entity.EmployeesPairs;

public final class Helper {

    public static int MATCH_BY_DIVISION=30;

    public static boolean employeePairsPredicateBYDivision(EmployeesPairs men){
        return  men.getFirstEmployee().getDivision().equals(men.getSecondEmployee().getDivision());
    }

    public static int MATCH_BY_AGE=30;

    public  static boolean employeePairsPredicateBYAge(EmployeesPairs employeesPairs){
      return   Math.abs(employeesPairs.getFirstEmployee().getAge()- employeesPairs.getSecondEmployee().getAge())<=5;
    }

    public static int MATCH_BY_TIMEZONE=30;

    public static boolean employeePairsPredicateBYTimezone(EmployeesPairs men){
       return men.getFirstEmployee().getTimezone()==men.getSecondEmployee().getTimezone();
    }


}
