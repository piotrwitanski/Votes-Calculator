package com.company.calc.dictionaries;

public class Dictionary {

    public static final String DB_NAME = "database.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:db\\" + DB_NAME;

    public static final String TABLE_ELECTORS = "electors";
    public static final String COLUMN_ELECTORS_VOTED = "voted";
    public static final String COLUMN_ELECTORS_NAME = "name";
    public static final String COLUMN_ELECTORS_PESEL= "pesel";

    public static final String TABLE_CANDIDATES = "candidates";
    public static final String COLUMN_CANDIDATES_NAME = "name";
    public static final String COLUMN_CANDIDATES_PARTY = "party";
    public static final String COLUMN_CANDIDATES_ID = "_id";

    public static final String TABLE_VOTES = "votes";
    public static final String COLUMN_VOTES_CANDIDATE= "candidate";// połączone z _id w candidates
    public static final String COLUMN_VOTES_QUANTITY = "quantity";

    public static final String [] PARTY_NAMES = {
            "Piastowie", "Dynastia Jagiellonów", "Elekcyjni dla Polski",  "Wazowie"
    };
    public static final String PARTY_1 = "Piastowie";
    public static final String PARTY_2 = "Dynastia Jagiellonów";
    public static final String PARTY_3 = "Elekcyjni dla Polski";
    public static final String PARTY_4 = "Wazowie";
    public static final int PARTY_NUMBER = 4;

    public static int B_WIDTH = 1000;
    public static int B_HEIGHT = 500;

    public static String FILE = "ElectionResults.pdf";
}
