package com.company.calc.ui;

import com.company.calc.dictionaries.Dictionary;
import com.company.calc.dto.Results;
import com.company.calc.utils.ResultUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ChartAppWindow extends JFrame{

    private ResultUtils resultUtils;
    private JFreeChart barChart;
    private ChartPanel chartPanel;

    public ChartAppWindow() {
        super("Votes for candidates");
        resultUtils = new ResultUtils();
        initComponents();

    }

    public void initComponents()
    {

        //chart
        barChart = ChartFactory.createBarChart(
                "Votes",
                "Candidates",
                "Votes",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel( barChart );


        final JPanel content = new JPanel(new BorderLayout());
        content.add(chartPanel);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(content);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private CategoryDataset createDataset()
    {

        final DefaultCategoryDataset dataset =
                new DefaultCategoryDataset();

        List<Results> electionResults = resultUtils.downloadResult();

        for (Results electionResult : electionResults) {

            switch(electionResult.getPartyName()){

                case Dictionary.PARTY_1:
                    dataset.addValue(electionResult.getVotes(), electionResult.getCandidateName(), Dictionary.PARTY_1);
                    break;
                case Dictionary.PARTY_2:
                    dataset.addValue(electionResult.getVotes(), electionResult.getCandidateName(), Dictionary.PARTY_2);
                    break;
                case Dictionary.PARTY_3:
                    dataset.addValue(electionResult.getVotes(), electionResult.getCandidateName(), Dictionary.PARTY_3);
                    break;
                case Dictionary.PARTY_4:
                    dataset.addValue(electionResult.getVotes(), electionResult.getCandidateName(), Dictionary.PARTY_4);
                    break;
            }
        }


        return dataset;
    }

    public void refreshChartData()
    {
        barChart = null;
        barChart = ChartFactory.createBarChart(
                "",
                "Candidates",
                "Votes",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);
        chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560 , 367));
        setContentPane(chartPanel);
        pack();
        RefineryUtilities.centerFrameOnScreen(this);
    }
}
