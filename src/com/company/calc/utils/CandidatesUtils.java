package com.company.calc.utils;

import com.company.calc.dto.Candidate;

import java.util.List;

public class CandidatesUtils {

    private DataBaseUtils dataBaseUtils;

    public CandidatesUtils()
    {
        dataBaseUtils = new DataBaseUtils();
    }

    public void addCandidateToDB(List<Object> list)
    {
        if (Candidate.class.equals(list.get(0).getClass()))
        {
            List<Candidate> candidates = (List<Candidate>) (List<?>) list;
            try{
                dataBaseUtils.addCandidatesToDB(candidates);
            }
            catch (Exception e)
            {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }
}
