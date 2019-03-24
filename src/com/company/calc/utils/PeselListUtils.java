package com.company.calc.utils;

import java.util.List;

public class PeselListUtils {

    private DataBaseUtils dataBaseUtils;

    public PeselListUtils()
    {
        this.dataBaseUtils = new DataBaseUtils();
    }

    public List<String> downloadPeselList()
   {
       List<String> list = dataBaseUtils.downloadPeselList();
       return list;
   }
}
