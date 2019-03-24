package com.company.calc.db;

import com.company.calc.dto.Candidate;
import com.company.calc.dto.Elector;
import com.company.calc.dto.Results;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.company.calc.dictionaries.Dictionary.*;

public class DataBase {


    private Connection conn;
    private Statement statement;

    //first connection to database
    public void initDataBase()
    {
        try{

            conn = DriverManager.getConnection(CONNECTION_STRING);
            statement = conn.createStatement();


//            statement.execute("DROP TABLE IF EXISTS " + TABLE_ELECTORS);

            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_ELECTORS +
                    " (" + COLUMN_ELECTORS_NAME + " TEXT, " +
                    COLUMN_ELECTORS_PESEL + " TEXT " +
                    ")");

            statement.execute("DROP TABLE IF EXISTS " + TABLE_CANDIDATES);

            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_CANDIDATES +
                    " (" + COLUMN_CANDIDATES_NAME + " TEXT, " +
                    COLUMN_CANDIDATES_PARTY + " TEXT, " +
                    COLUMN_CANDIDATES_ID + " INTEGER" +
                    ")");

            //statement.execute("DROP TABLE IF EXISTS " + TABLE_VOTES);

            statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_VOTES +
                    " (" + COLUMN_VOTES_CANDIDATE + " TEXT, " +
                    COLUMN_VOTES_QUANTITY + " INTEGER" +
                    ")");


            statement.execute("CREATE VIEW IF NOT EXISTS candidate_result AS " +
                    "SELECT candidates.name, candidates.party, votes.quantity FROM " + TABLE_CANDIDATES +
                    " INNER JOIN " + TABLE_VOTES  + " ON votes.candidate = candidates._id"
                    );


            statement.close();
            conn.close();
        }
        catch (SQLException e)
        {
            System.out.println("Something went wrong " + e.getMessage());
            e.printStackTrace();
        }
    }


    public boolean open()
    {
        try{
            conn = DriverManager.getConnection(CONNECTION_STRING);
            return true;
        }
        catch(SQLException e)
        {
            System.out.println("Couldn't connect to database" + e.getMessage());
            return false;
        }
    }

    public void close()
    {
        try {
            if(conn != null)
                conn.close();
        }
        catch (SQLException e)
        {
            System.out.println("Couldn't close connection:" + e.getMessage());
        }
    }

    public void addStatement(Candidate candidate, int id)
    {
        try(Statement statement = conn.createStatement()){

            statement.execute("INSERT INTO " + TABLE_CANDIDATES +
                    " (" + COLUMN_CANDIDATES_NAME + ", " +
                    COLUMN_CANDIDATES_PARTY + ", " +
                    COLUMN_CANDIDATES_ID +
                    ")" +
                    "VALUES('" + candidate.getName() + "', '" + candidate.getParty() + "', " + id + ")");
        }
        catch (SQLException e)
        {
            System.out.println("Add statement ERROR: " + e.getMessage());
        }
    }

    public void addStatement(Elector elector)
    {
        try(Statement statement = conn.createStatement()){

            statement.execute("INSERT INTO " + TABLE_ELECTORS +
                    " (" + COLUMN_ELECTORS_NAME + ", " +
                    COLUMN_ELECTORS_PESEL +
                    ")" +
                    "VALUES('" + elector.getName() + "', '" + elector.getPeselNumber() +  "')");
        }
        catch (SQLException e)
        {
            System.out.println("Add statement ERROR: " + e.getMessage());
        }
    }

    public boolean addStatement(String peselNumber)
    {
        boolean isOnTheList = true;
        try(Statement statement = conn.createStatement()){

            ResultSet result = statement.executeQuery("SELECT " + COLUMN_ELECTORS_PESEL +
                                                            " FROM " + TABLE_ELECTORS +
                                                            " WHERE " + COLUMN_ELECTORS_PESEL +
                                                            "='" + peselNumber + "'");
            if(result.next())
                isOnTheList = false;

            result.close();
        }
        catch (SQLException e)
        {
            System.out.println("Add statement ERROR: " + e.getMessage());
        }
        return isOnTheList;
    }

    public List<String> downloadPeselList()
    {
        List<String> peselList = new ArrayList<>();
        try(Statement statement = conn.createStatement()){

            ResultSet result = statement.executeQuery("SELECT " + COLUMN_ELECTORS_PESEL +
                    " FROM " + TABLE_ELECTORS );
            while(result.next())
            {
                peselList.add(result.getString(COLUMN_ELECTORS_PESEL));
            }

            result.close();
        }
        catch (SQLException e)
        {
            System.out.println("Add statement ERROR: " + e.getMessage());
        }
        return peselList;
    }

    public void addStatement()
    {
        try(Statement statement = conn.createStatement()){

            ResultSet resultSet = statement.executeQuery("SELECT COUNT(" + COLUMN_VOTES_QUANTITY + ") AS row FROM " + TABLE_VOTES);
            resultSet.next();

            if(resultSet.getInt("row") == 0)
            {
                ResultSet result = statement.executeQuery("SELECT COUNT(" + COLUMN_CANDIDATES_ID + ") AS rowcount FROM " + TABLE_CANDIDATES);
                result.next();


                int count = result.getInt("rowcount");

                System.out.println("MyTable has " + count + " row(s).");

                for(int i = 0; i < count; i++)
                {
                    statement.execute("INSERT INTO " + TABLE_VOTES +
                            " (" + COLUMN_VOTES_CANDIDATE + ", " +
                            COLUMN_VOTES_QUANTITY +
                            ")" +
                            "VALUES(" + (i+1) + ", " + 0 +  ")");
                }
                //invalid vote
                statement.execute("INSERT INTO " + TABLE_VOTES +
                        " (" + COLUMN_VOTES_CANDIDATE + ", " +
                        COLUMN_VOTES_QUANTITY +
                        ")" +
                        "VALUES(" + 0 + ", " + 0 +  ")");


                result.close();
            }
            resultSet.close();
        }
        catch(SQLException e)
        {
            System.out.println("Add statement ERROR: " + e.getMessage());
        }
    }

    public List<Results> downloadResult()
    {
        try(Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM candidate_result")){



            List<Results> resultList = new ArrayList<>();
            while(result.next())
            {
                Results candidateResult = new Results();
                candidateResult.setCandidateName(result.getString(COLUMN_CANDIDATES_NAME));
                candidateResult.setPartyName(result.getString(COLUMN_CANDIDATES_PARTY));
                candidateResult.setVotes(result.getInt(COLUMN_VOTES_QUANTITY));

                resultList.add(candidateResult);
            }

            return resultList;
        }
        catch(SQLException e)
        {
            System.out.println("Add statement ERROR: " + e.getMessage());
            return null;
        }
    }

    public int downloadInvalidVotes()
    {
        try(Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT " + COLUMN_VOTES_QUANTITY +
                            " FROM " + TABLE_VOTES +
                            " WHERE " + COLUMN_VOTES_CANDIDATE + " = 0")){

            result.next();

            int invalidVotes = result.getInt(COLUMN_VOTES_QUANTITY);

            return invalidVotes;
        }
        catch(SQLException e)
        {
            System.out.println("Add statement ERROR: " + e.getMessage());
            return 0;
        }
    }

    public void addStatement(String name, String party)
    {
        try(Statement statement = conn.createStatement()){

            ResultSet result = statement.executeQuery("SELECT " + COLUMN_CANDIDATES_ID +
                    " FROM " + TABLE_CANDIDATES +
                    " WHERE " + COLUMN_CANDIDATES_NAME +
                    "='" + name + "'" +
                    " AND " + COLUMN_CANDIDATES_PARTY +
                    "='" + party + "'");
            result.next();
            int idCandidate = result.getInt(COLUMN_CANDIDATES_ID);
            System.out.println(idCandidate);

            statement.execute("UPDATE " + TABLE_VOTES+ " SET " +
                    COLUMN_VOTES_QUANTITY + " = " + COLUMN_VOTES_QUANTITY + "+ 1" +
                    " WHERE " + COLUMN_VOTES_CANDIDATE + " = " + idCandidate);


        }
        catch(SQLException e)
        {
            System.out.println("Add statement ERROR: " + e.getMessage());
        }

    }

    public void addStatement(int idCandidate)
    {
        try(Statement statement = conn.createStatement()){

            statement.execute("UPDATE " + TABLE_VOTES+ " SET " +
                    COLUMN_VOTES_QUANTITY + " = " + COLUMN_VOTES_QUANTITY + "+ 1" +
                    " WHERE " + COLUMN_VOTES_CANDIDATE + " = " + idCandidate);
        }
        catch(SQLException e)
        {
            System.out.println("Add statement ERROR: " + e.getMessage());
        }

    }

    public List<String> addStatement(boolean state)
    {
        try(Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + TABLE_CANDIDATES)){

            List<String> candidateList = new ArrayList<>();
            while(result.next())
            {
                String name = new String();
                String party = new String();
                name =(result.getString(COLUMN_CANDIDATES_NAME));
                party =(result.getString(COLUMN_CANDIDATES_PARTY));

                candidateList.add(name + ", " + party);
            }
            return candidateList;
        }
        catch(SQLException e)
        {
            System.out.println("Add statement ERROR: " + e.getMessage());
            return null;
        }
    }

}

