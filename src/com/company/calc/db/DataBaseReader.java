package com.company.calc.db;

import com.company.calc.db.DataBase;

import java.util.List;

public class DataBaseReader {

    private DataBase dataBase;

    public DataBaseReader()
    {
        this.dataBase = new DataBase();
    }

    public List<String> downloadCandidateListFromDB()
    {
        dataBase.open();

        List<String> list = dataBase.addStatement(true);

        dataBase.close();
        return list;
    }
}
