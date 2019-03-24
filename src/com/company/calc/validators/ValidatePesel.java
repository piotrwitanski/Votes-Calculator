package com.company.calc.validators;

import com.company.calc.dto.Elector;
import com.company.calc.tools.EncryptPassword;
import com.company.calc.tools.JSONReader;
import com.company.calc.utils.ElectorsUtils;
import com.company.calc.utils.PeselListUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.company.calc.dictionaries.TypesOfData.*;

public class ValidatePesel implements Validator {

    private byte[] PeselNumber = new byte[11];
    private boolean valid = false;
    private String pesel;

    private int electionYear;
    private int electionMonth;
    private int electionDay;

    private Elector elector;
    private ElectorsUtils electorsUtils;
    private EncryptPassword encryptPassword;
    private JSONReader jsonReader;
    private PeselListUtils peselListUtils;

    public ValidatePesel()
    {
        checkCurrentDate();
    }


    public void setPesel(String pesel)
    {
        this.pesel = pesel;
        encryptPassword = new EncryptPassword();
        electorsUtils = new ElectorsUtils();
        peselListUtils = new PeselListUtils();
    }

    @Override
    public boolean validate()
    {
        return validate(pesel);
    }

    public boolean validate(String peselNumber)
    {
        if(peselNumber.length() != 11 || checkPeselBlackList(peselNumber))
        {
            valid = false;
        }
        else
        {
            for (int i = 0; i < 11; i++)
            {
                PeselNumber[i] = Byte.parseByte(peselNumber.substring(i, i+1));
            }
            if (checkSum() && checkMonth() && checkDay() && checkAge())
            {
                valid = true;
            }
            else
            {
                valid = false;
            }
        }
        return valid;
    }

    public int getYear()
    {
        int year;
        int month;
        year = 10 * PeselNumber[0];
        year += PeselNumber[1];
        month = 10 * PeselNumber[2];
        month += PeselNumber[3];
        if(month > 80 && month < 93)
        {
            year += 1800;
        }
        else if(month > 0 && month < 13)
        {
            year += 1900;
        }
        else if(month > 20 && month < 33)
        {
            year += 2000;
        }
        else if(month > 40 && month < 53)
        {
            year += 2100;
        }
        else if(month > 60 && month < 73)
        {
            year += 2200;
        }
        return year;
    }

    public int getMonth()
    {
        int month;
        month = 10 * PeselNumber[2];
        month += PeselNumber[3];
        if(month > 80 && month < 93)
        {
            month -= 80;
        }
        else if(month > 20 && month < 33)
        {
            month -= 20;
        }
        else if(month > 40 && month < 53)
        {
            month -= 40;
        }
        else if(month > 60 && month < 73)
        {
            month -= 60;
        }
        return month;
    }

    public int getDay()
    {
        int day;
        day = 10 * PeselNumber[4];
        day += PeselNumber[5];

        return day;
    }

    private boolean checkSum()
    {
        int sum = 1 * PeselNumber[0] +3 * PeselNumber[1] + 7 * PeselNumber[2] + 9 * PeselNumber[3] + 1 * PeselNumber[4]
                + 3 * PeselNumber[5] + 7 * PeselNumber[6] + 9 * PeselNumber[7] + 1 * PeselNumber[8] + 3 * PeselNumber[9];
        sum %= 10;
        sum = 10 - sum;
        sum %= 10;

        if(sum == PeselNumber[10])
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean checkMonth()
    {
        int month = getMonth();

        if(month > 0 && month < 13)
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    private boolean checkDay()
    {
        int year = getYear();
        int month = getMonth();
        int day = getDay();

        if((day > 0 && day < 32) &&
                (month == 1 || month == 3 || month == 5 ||
                        month == 7 || month == 8 || month == 10 ||
                        month == 12))
        {
            return true;
        }
        else if((day > 0 && day < 31) &&
                (month == 4 || month == 6 || month == 9 ||
                        month == 11))
        {
            return true;
        }
        else if((day > 0 && day < 30 && leapYear(year)) ||
                (day > 0 && day < 29 && !leapYear(year)))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean checkAge()
    {

        int ageValidator = (getYear() * 10000 + getMonth() * 100 + getDay()) -
                            (electionYear * 10000 + electionMonth * 100 + electionDay - 180000);
        if(ageValidator < 0) {
            return true;
        }
        return false;

    }

    private void checkCurrentDate()
    {
        LocalDate localDate = LocalDate.now();
        this.electionDay  = localDate.getDayOfMonth();
        this.electionMonth = localDate.getMonthValue();
        this.electionYear= localDate.getYear();

    }

    private boolean leapYear(int year)
    {
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean checkPeselBlackList(String peselNumber)
    {
        boolean isOnTheList = false;
        List<String> peselBlackList= new ArrayList<>();
        peselBlackList = downloadPeselBlackList();

        for (String s : peselBlackList) {
            if(s.equals(peselNumber))
            {
                isOnTheList = true;
            }
        }
        return isOnTheList;
    }

    public boolean checkElectorPesel()
    {
        if(validate())
        {
             return checkPeselList(peselListUtils.downloadPeselList());
        }
        else
            return false;
    }

    private boolean checkPeselList(List<String> list)
    {
        return encryptPassword.checkPassList(list, pesel);
    }

    private List<String> downloadPeselBlackList()
    {
        List<String> peselBlackList = (List<String>) (List<?>) new JSONReader().download(PESELS);
        return peselBlackList;
    }



}
