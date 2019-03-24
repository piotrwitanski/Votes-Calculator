package com.company.calc.utils;

import com.company.calc.dto.Results;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.List;

public class ResultUtils {

    private DataBaseUtils dataBaseUtils;

    public ResultUtils()
    {
        this.dataBaseUtils = new DataBaseUtils();
    }

    public List<Results> downloadResult()
    {
        try {
            List<Results> list = dataBaseUtils.dowloadResult();
            return list;
        }
        catch (Exception e)
        {
            System.out.println("ERROR: " + e.getMessage());
            return null;
        }


    }
}
