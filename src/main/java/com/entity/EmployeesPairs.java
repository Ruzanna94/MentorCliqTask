package com.entity;

public class EmployeesPairs {
    Employee firstEmployee;
    Employee secondEmployee;
    int matchesPercent;

    public EmployeesPairs(Employee firstEmployee, Employee secondEmployee) {
        this.firstEmployee = firstEmployee;
        this.secondEmployee = secondEmployee;
        this.matchesPercent = 0;
    }

    public Employee getFirstEmployee() {
        return firstEmployee;
    }

    public Employee getSecondEmployee() {
        return secondEmployee;
    }

    public int getMatchesPercent() {
        return matchesPercent;
    }

    public void setMatchesPercent(int matchesPercent) {
        this.matchesPercent = matchesPercent;
    }
}
