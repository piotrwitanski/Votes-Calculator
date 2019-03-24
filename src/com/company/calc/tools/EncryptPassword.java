package com.company.calc.tools;

import org.mindrot.jbcrypt.*;

import java.util.List;

public class EncryptPassword {

    public String createPass(String plain_password)
    {

        String pw_hash = BCrypt.hashpw(plain_password, BCrypt.gensalt());

        return pw_hash;
    }

    public boolean checkPassList(List<String> list, String pesel)
    {
        for (String s : list) {

            if(BCrypt.checkpw(pesel, s))
                return false;
        }

        return true;
    }

}
