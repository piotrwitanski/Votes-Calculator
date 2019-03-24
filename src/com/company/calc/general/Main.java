package com.company.calc.general;

import com.company.calc.db.DataBase;
import com.company.calc.db.DataBaseReader;
import com.company.calc.dto.Candidate;
import com.company.calc.dto.Results;
import com.company.calc.tools.JSONReader;
import com.company.calc.ui.LoginAppWindow;
import com.company.calc.ui.ResultAppWindow;
import com.company.calc.utils.CandidatesUtils;
import com.company.calc.utils.ResultUtils;
import com.company.calc.utils.VotesUtils;
import com.company.calc.validators.ValidatePesel;

import java.util.ArrayList;
import java.util.List;

import static com.company.calc.dictionaries.TypesOfData.*;

public class Main {

    public static void main(String[] args)
    {

        //database init
        DataBase dataBase = new DataBase();
        dataBase.initDataBase();

        //JSON Candidates
        JSONReader reader = new JSONReader();

        //download and save candidates to DB
        CandidatesUtils candidatesUtils = new CandidatesUtils();
        candidatesUtils.addCandidateToDB(reader.download(CANDIDATE));

        //send candidate list to window app
        DataBaseReader dataBaseReader = new DataBaseReader();
        List<String> candList = dataBaseReader.downloadCandidateListFromDB();

        //app window
        LoginAppWindow loginAppWindow = new LoginAppWindow(candList);


        //init votes
        VotesUtils votesUtils = new VotesUtils();
        votesUtils.initVotesInDB();

        //download results
        ResultUtils resultUtils = new ResultUtils();
        List<Results> list = resultUtils.downloadResult();

        //download invalid votes
        System.out.println(votesUtils.downloadInvalidVotes());




    }
}
