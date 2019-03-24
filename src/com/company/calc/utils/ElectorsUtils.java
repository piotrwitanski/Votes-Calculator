package com.company.calc.utils;

import com.company.calc.dto.Elector;
import com.company.calc.utils.DataBaseUtils;

public class ElectorsUtils {

    private DataBaseUtils dataBaseUtils;
    private Elector elector;

    public ElectorsUtils()
    {
        this.dataBaseUtils = new DataBaseUtils();
        this.elector = new Elector();
    }

    public void addElectorToDB(String name, String pesel)
    {
            elector.setName(name);
            elector.setPeselNumber(pesel);
            try{
                dataBaseUtils.addElectorToDB(elector);
            }
            catch (Exception e)
            {
                System.out.println("ERROR: " + e.getMessage());
            }

    }
    public boolean checkElector(String peselNumber)
    {
        return dataBaseUtils.checkEledctorInDB(peselNumber);
    }
}
