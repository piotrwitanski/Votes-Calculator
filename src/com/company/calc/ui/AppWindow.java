package com.company.calc.ui;

import com.company.calc.dictionaries.Dictionary;
import com.company.calc.tools.EncryptPassword;
import com.company.calc.utils.ElectorsUtils;
import com.company.calc.utils.VotesUtils;
import com.company.calc.validators.ValidatePesel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class AppWindow extends JFrame{

    private List<String> candidatesList;
    private LoginAppWindow loginAppWindow;
    private List<JCheckBox> checkboxes;
    private VotesUtils votesUtils;
    private EncryptPassword encryptPassword;
    private ValidatePesel validatePesel;
    private ElectorsUtils electorsUtils;
    private ResultAppWindow resultAppWindow;

    public AppWindow( List<String> candidatesList, LoginAppWindow loginAppWindow, ValidatePesel validatePesel)
    {
        super( "Vote Window" );
        this.loginAppWindow = loginAppWindow;
        this.candidatesList = candidatesList;
        checkboxes = new ArrayList<JCheckBox>();
        this.votesUtils = new VotesUtils();
        this.encryptPassword = new EncryptPassword();
        this.validatePesel = validatePesel;
        this.electorsUtils= new ElectorsUtils();
        this.resultAppWindow = new ResultAppWindow();
        initComponents();
    }


    void initComponents()
    {
        this.setBounds(650, 350, Dictionary.B_WIDTH, Dictionary.B_HEIGHT);
        textPanel.add(label);

        buildCandidateList();

        buildButton("Vote");
        buildButton("Logout");

        this.getContentPane().add(switchPanel, BorderLayout.CENTER);
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        this.getContentPane().add(textPanel, BorderLayout.NORTH);

        textPanel.setBackground(new Color(0, 31, 53));
        buttonPanel.setBackground(new Color(0, 31, 53));
        switchPanel.setBackground(new Color(0, 31, 53));
        label.setBackground(new Color(0, 31, 53));
        this.setDefaultCloseOperation(3);
        pack();
        setResizable(false);

    }

    private void buildSwitch(String name)
    {
        JCheckBox checkBox = new JCheckBox(name);
        checkBox.setForeground(Color.WHITE);
        checkBox.setBackground(new Color(0, 31, 53));
        checkboxes.add(checkBox);
        switchPanel.add(checkBox);
    }

    private void buildButton(String name)
    {
        JButton jButton = new JButton(name);

        jButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JButton button = (JButton) e.getSource();
                if(button.getText().equals("Vote"))
                {
                    int option = JOptionPane.showConfirmDialog(null, "Please confirm your choice");
                    if(option == 0 /*&& validatePesel.checkElectorPesel()*/)
                    {

                        if(validatePesel.checkElectorPesel())
                        {
                            List<JCheckBox> checked = new ArrayList<>();
                            for(JCheckBox checkBox : checkboxes)
                            {
                                if (checkBox.isSelected())
                                {
                                    checked.add( checkBox );
                                }
                            }
                            if(checked != null && checked.size() == 1)
                            {
                                addElector();
                                System.out.println("Valid vote");
                                prepareData(checked.get(0).getText());
                            }
                            else
                            {
                                addElector();
                                System.out.println("Invalid vote");
                                votesUtils.updateVotesInDB(0);
                            }

                            System.out.println("Voted");
                            loginAppWindow.setVisible(true);
                            clear();
                            loginAppWindow.clear();
                            setVisible(false);
                            resultAppWindow.setVisible(true);
                            resultAppWindow.setBackground(new Color(0, 31, 53));
                            resultAppWindow.repaint();
                        }
                        else
                        {
                            System.out.println("Already voted");
                        }
                    }
                    else
                    {
                        System.out.println("Not confirmed");

                    }

                }
                else
                {
                    System.out.println("Logout");
                    loginAppWindow.setVisible(true);
                    loginAppWindow.clear();
                    clear();
                    setVisible(false);
                }
            }

        });
        buttonPanel.add(jButton);
    }

    private void buildCandidateList()
    {
        for(int i = 0; i < candidatesList.size(); i++)
        {
            buildSwitch(candidatesList.get(i));
        }
    }

    private void prepareData(String vote)
    {
        String name = vote.substring(0, vote.indexOf(","));
        String party = vote.substring(vote.indexOf(",") + 2);
        votesUtils.updateVotesInDB(name, party);
    }

    private void addElector()
    {
        String firstName = loginAppWindow.jTextfirstName.getText();
        String secondName = loginAppWindow.jTextsecondName.getText();
        String peselNumber = loginAppWindow.jTextPesel.getText();
        peselNumber = encryptPassword.createPass(peselNumber);
        electorsUtils.addElectorToDB(firstName + " " + secondName, peselNumber);
    }

    public void setLoginName()
    {
        String labelText = "Login as: " + loginAppWindow.jTextfirstName.getText() + " " + loginAppWindow.jTextsecondName.getText();
        label.setForeground(Color.WHITE);
        label.setText(labelText);
    }

    public void clear()
    {
        for (JCheckBox checkbox : checkboxes) {
            checkbox.setSelected(false);
        }
    }

    JPanel switchPanel = new JPanel(new GridLayout(10,0));
    JPanel textPanel = new JPanel();
    JPanel buttonPanel = new JPanel();


    JLabel label = new JLabel("Elections");


}
