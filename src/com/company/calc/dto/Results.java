package com.company.calc.dto;

public class Results {

    private String candidateName;
    private String partyName;
    private int votes;

    public String getCandidateName()
    {
        return candidateName;
    }

    public void setCandidateName(String candidateName)
    {
        this.candidateName = candidateName;
    }

    public String getPartyName()
    {
        return partyName;
    }

    public void setPartyName(String partyName)
    {
        this.partyName = partyName;
    }

    public int getVotes()
    {
        return votes;
    }

    public void setVotes(int votes)
    {
        this.votes = votes;
    }
}
