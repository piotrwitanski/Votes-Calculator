package com.company.calc.utils;

import com.company.calc.utils.DataBaseUtils;

public class VotesUtils {

    private DataBaseUtils dataBaseUtils;

    public VotesUtils()
    {
        this.dataBaseUtils = new DataBaseUtils();
    }

    public void initVotesInDB()
    {
        try{
            dataBaseUtils.initVotesInDB();
        }
        catch (Exception e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

    }

    public void updateVotesInDB(String name, String party)
    {
        try{
            dataBaseUtils.updateVotesInDB(name, party);
        }
        catch (Exception e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public void updateVotesInDB(int id)
    {
        try{
            dataBaseUtils.updateVotesInDB(id);
        }
        catch (Exception e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public int downloadInvalidVotes()
    {
        try{
            int invalidVotes = dataBaseUtils.downloadInvalidVotes();
            return invalidVotes;
        }
        catch (Exception e)
        {
            System.out.println("ERROR: " + e.getMessage());
            return 0;
        }
    }
}
