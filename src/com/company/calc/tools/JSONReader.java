package com.company.calc.tools;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.company.calc.dictionaries.TypesOfData;
import com.company.calc.dto.Candidate;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONReader {

    private static String readAll(Reader rd) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        int cp;
        while((cp = rd.read()) != -1)
        {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException
    {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    private static JSONObject readJsonFromFile(String path) throws IOException, JSONException
    {
        InputStream is = new FileInputStream(new File(path));

        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("ISO-8859-2")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public List<Object> download(TypesOfData typeOfDataToDownload)
    {
        try
        {
            return downloadType(typeOfDataToDownload);
        }
        catch (Exception e)
        {
            System.out.println("ERROR can't read data: " + e);
        }
        return null;
    }

    private List<Object> downloadType(TypesOfData typeOfDataToDownload) throws Exception
    {
        switch (typeOfDataToDownload)
        {
            case CANDIDATE:
                return downloadCandidates();
            case PESELS:
                return downloadPESELS();
            default:
                throw new Exception( "Incorrect data format" );
        }
    }

    private List<Object> downloadCandidates() throws JSONException, IOException
    {
        List<Object> candidates = new ArrayList<>();

//        JSONObject json = readJsonFromUrl("http://");
        JSONObject json = readJsonFromFile("candidates.txt");
        JSONArray array = json.getJSONObject("candidates").getJSONArray("candidate");

        Gson gson = new Gson();
        for (int i = 0; i < array.length(); i++)
        {
            candidates.add(gson.fromJson(array.get(i).toString(), Candidate.class));
        }

        return candidates;
    }

    private List<Object> downloadPESELS() throws JSONException, IOException
    {

        List<Object> peselBlackList = new ArrayList<>();

//        JSONObject json = readJsonFromUrl("http://");
        JSONObject json = readJsonFromFile("blacklist.txt");
        JSONArray array = json.getJSONObject("disallowed").getJSONArray("person");
        Gson gson = new Gson();
        for (int i = 0; i < array.length(); i++)
        {
            peselBlackList.add(gson.fromJson(array.getJSONObject(i).get("pesel").toString(), String.class));
        }

        return peselBlackList;
    }
}