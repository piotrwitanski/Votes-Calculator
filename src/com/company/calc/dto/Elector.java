package com.company.calc.dto;

public class Elector {

    private String name;
    private String peselNumber;

    public Elector()
    {

    }
    public Elector(String name, String peselNumber)
    {
        this.name = name;
        this.peselNumber = peselNumber;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPeselNumber()
    {
        return peselNumber;
    }

    public void setPeselNumber(String peselNumber)
    {
        this.peselNumber = peselNumber;
    }
}
