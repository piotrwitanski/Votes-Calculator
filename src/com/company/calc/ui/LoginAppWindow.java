package com.company.calc.ui;

import com.company.calc.dictionaries.Dictionary;
import com.company.calc.validators.ValidatePesel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LoginAppWindow extends JFrame{


    private List<String> candidatesList;
    AppWindow appWindow ;
    private ValidatePesel validatePesel;

    public LoginAppWindow() {
        super("Login Window");
        initComponents();
        setVisible(true);
    }
     public LoginAppWindow(List<String> candidatesList)
    {
        super("Login Window");
        this.candidatesList = candidatesList;

        this.validatePesel = new ValidatePesel();
        appWindow = new AppWindow(candidatesList, this, validatePesel);
        initComponents();
        setVisible(true);



    }


    void initComponents()
    {
        this.setBounds(650, 450, Dictionary.B_WIDTH, Dictionary.B_HEIGHT);
        titlePanel.add(label);

        firstName.setForeground(Color.WHITE);
        lastName.setForeground(Color.WHITE);
        peselNumber.setForeground(Color.WHITE);
        textPanel.add(firstName);
        textPanel.add(jTextfirstName);
        textPanel.add(lastName);
        textPanel.add(jTextsecondName);
        textPanel.add(peselNumber);
        textPanel.add(jTextPesel);
        buildButton("Login");

        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        this.getContentPane().add(textPanel, BorderLayout.NORTH);

        textPanel.setBackground(new Color(0, 31, 53));
        buttonPanel.setBackground(new Color(0, 31, 53));
        this.setDefaultCloseOperation(3);
        pack();
        setResizable(false);
    }


    private void buildButton(String name)
    {
        JButton jButton = new JButton(name);

        jButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {

                validatePesel.setPesel(jTextPesel.getText());
                if(validatePesel.checkElectorPesel() && checkName())
                {

                    JOptionPane.showMessageDialog(null, "Succeed!");
                    setVisible(false);
                    appWindow.setLoginName();
                    appWindow.show();

                }
                else
                    JOptionPane.showMessageDialog(null, "Failed. Invalid PESEL number or empty name");

            }

        });
        buttonPanel.add(jButton);
    }

    private boolean checkName()
    {
        if(jTextfirstName.getText().equals("") || jTextsecondName.getText().equals(""))
            return false;
        else
            return true;
    }

    public void clear()
    {
        jTextfirstName.setText("");
        jTextsecondName.setText("");
        jTextPesel.setText("");
    }

    JPanel titlePanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JPanel textPanel = new JPanel();

    JTextField jTextfirstName = new JTextField(12);
    JTextField jTextsecondName = new JTextField(12);
    JTextField jTextPesel = new JTextField(12);

    JLabel label = new JLabel("Elections");
    JLabel firstName = new JLabel("First Name: ");
    JLabel lastName = new JLabel("Last Name: ");
    JLabel peselNumber = new JLabel("PESEL: ");
}
