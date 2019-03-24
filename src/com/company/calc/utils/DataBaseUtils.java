package com.company.calc.utils;

import com.company.calc.db.DataBase;
import com.company.calc.dto.Candidate;
import com.company.calc.dto.Elector;
import com.company.calc.dto.Results;


import java.util.ArrayList;
import java.util.List;

public class DataBaseUtils {

    DataBase dataBase;


    public DataBaseUtils()
    {
        this.dataBase = new DataBase();
        //dataBase.connectDataBase();
    }

    public void addCandidatesToDB(List<Candidate> candidates)
    {
        dataBase.open();

        for (int i = 0; i < candidates.size(); i++)
        {
            System.out.println("Candidate: " + candidates.get(i).getName() + ", Party: " + candidates.get(i).getParty());

            dataBase.addStatement(candidates.get(i), (i + 1));

        }
        dataBase.close();
    }



    public void addElectorToDB(Elector elector)
    {
        dataBase.open();

        dataBase.addStatement(elector);

        dataBase.close();
    }

    public boolean checkEledctorInDB(String peselNumber)
    {
        dataBase.open();

        boolean notOnTheList = dataBase.addStatement(peselNumber);

        dataBase.close();

        return notOnTheList;
    }

    public void initVotesInDB()
    {
        dataBase.open();

        dataBase.addStatement();

        dataBase.close();
    }

    public void updateVotesInDB(String name, String party)
    {
        dataBase.open();

        dataBase.addStatement(name, party);

        dataBase.close();
    }
    public void updateVotesInDB(int id)
    {
        dataBase.open();

        dataBase.addStatement(id);

        dataBase.close();
    }

    public List<String> downloadPeselList()
    {
        dataBase.open();
        List<String> list = dataBase.downloadPeselList();
        dataBase.close();

        return list;
    }

    public List<Results> dowloadResult()
    {
        dataBase.open();
        List<Results> list= dataBase.downloadResult();
        dataBase.close();

        return list;
    }

    public int downloadInvalidVotes()
    {
        dataBase.open();
        int invalidVotes = dataBase.downloadInvalidVotes();
        dataBase.close();
        return invalidVotes;
    }
}
