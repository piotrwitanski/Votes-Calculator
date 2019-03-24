package com.company.calc.ui;

import com.company.calc.dictionaries.Dictionary;
import com.company.calc.dto.Results;
import com.company.calc.tools.EncryptPassword;
import com.company.calc.tools.PDFCreator;
import com.company.calc.utils.ElectorsUtils;
import com.company.calc.utils.ResultUtils;
import com.company.calc.utils.VotesUtils;
import com.company.calc.validators.ValidatePesel;
import javafx.scene.layout.CornerRadii;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static com.company.calc.dictionaries.Dictionary.*;

public class ResultAppWindow extends JFrame{

    private VotesUtils votesUtils;
    private ResultUtils resultUtils;
    private ChartAppWindow chartAppWindow;
    private PDFCreator pdfCreator;

    private int [] partyVotes = new int[Dictionary.PARTY_NUMBER];

    public ResultAppWindow()
    {
        super("Vote Window");
        this.votesUtils = new VotesUtils();
        this.resultUtils = new ResultUtils();
        this.chartAppWindow = new ChartAppWindow();
        this.pdfCreator = new PDFCreator();
        initComponents();
    }

    public ResultAppWindow(List<String> candidatesList, LoginAppWindow loginAppWindow)
    {
        super("Vote Window");
        this.votesUtils = new VotesUtils();
        this.resultUtils = new ResultUtils();
        initComponents();
    }


    void initComponents()
    {
        this.setBounds(650, 350, B_WIDTH, B_HEIGHT);
        label.setForeground(Color.WHITE);
        textPanel.add(label);

        buildButton("Show chart");

        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        textPanel.setBackground(new Color(0, 31, 53));
        buttonPanel.setBackground(new Color(0, 31, 53));

        this.setDefaultCloseOperation(3);

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
                JButton button = (JButton) e.getSource();
                if (button.getText().equals("Show chart"))
                {
                    System.out.println("Show chart");

                    setVisible(false);
                    chartAppWindow.refreshChartData();
                    chartAppWindow.setVisible(true);
                }
            }

        });
        buttonPanel.add(jButton);
    }

    @Override
    public void paint(Graphics g)
    {

        super.paint(g);
        String title = "Current elections results";
        Font sansSerifLarge = new Font("SansSerif", Font.BOLD, 18);
        Font sansSerifSmall = new Font("SansSerif", Font.BOLD, 12);
        FontMetrics fontLarge = getFontMetrics(sansSerifLarge);

        g.setColor(new Color(0, 31, 53));
        g.fillRect(0,0, getWidth(), getHeight());

        g.setFont(sansSerifLarge);
        g.setColor(Color.WHITE);
        g.drawString(title, (B_WIDTH - fontLarge.stringWidth(title)) / 2,
                B_HEIGHT / 2 - 200);
        g.setFont(sansSerifSmall);
        List<Results> electionResults = resultUtils.downloadResult();

        for (int i = 0; i < electionResults.size(); i++)
        {
            g.drawString((i + 1) + ". " + electionResults.get(i).getCandidateName() +
                    ", " + electionResults.get(i).getPartyName() +
                    "; Votes: " + electionResults.get(i).getVotes(),60, 100 + i* 20);
            if(i == electionResults.size() -1)
            {
                g.setFont(sansSerifLarge);
                g.drawString("Invalid votes: " + votesUtils.downloadInvalidVotes(), 60, 160 + i * 20);
            }
        }

        preparePartyVotes(electionResults);
        exportToPDF(electionResults);

        for (int i = 0; i < partyVotes.length; i++)
        {
            g.drawString(Dictionary.PARTY_NAMES[i] + ": " + partyVotes[i], 600, 100 + i * 40);
        }

    }

    private void preparePartyVotes(List<Results> electionResults)
    {
        //reset table before display
        for(int i = 0; i < partyVotes.length; i++)
        {
            partyVotes[i] = 0;
        }
        for(int i = 0; i < electionResults.size(); i++)
        {

            switch(electionResults.get(i).getPartyName())
            {

                case Dictionary.PARTY_1:
                    partyVotes[0] += electionResults.get(i).getVotes();
                    break;
                case Dictionary.PARTY_2:
                    partyVotes[1] += electionResults.get(i).getVotes();
                    break;
                case Dictionary.PARTY_3:
                    partyVotes[2] += electionResults.get(i).getVotes();
                    break;
                case Dictionary.PARTY_4:
                    partyVotes[3] += electionResults.get(i).getVotes();
                    break;
            }
        }
    }

    private void exportToPDF(List<Results> electionResults)
    {
        pdfCreator.createPDF(electionResults, partyVotes, votesUtils.downloadInvalidVotes());
    }

    JPanel switchPanel = new JPanel(new GridLayout(10,0));
    JPanel textPanel = new JPanel();
    JPanel buttonPanel = new JPanel();


    JLabel label = new JLabel("Elections");
}
