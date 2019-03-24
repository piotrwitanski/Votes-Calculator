package com.company.calc.tools;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import com.company.calc.dictionaries.Dictionary;
import com.company.calc.dto.Results;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import static com.company.calc.dictionaries.Dictionary.FILE;


public class PDFCreator {

    private Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    private java.util.List<Results> electionResults = null;
    private int [] partyVotes = new int[Dictionary.PARTY_NUMBER];
    private int invalidVotes;

    public void createPDF(java.util.List<Results> electionResults, int [] partyVotes, int invalidVotes)
    {
        try {
            this.electionResults = electionResults;
            System.arraycopy(partyVotes, 0, this.partyVotes, 0, partyVotes.length);

            this.invalidVotes = invalidVotes;
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(FILE));

            document.open();
            addTitlePage(document);
            addContent(document);
            document.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void addTitlePage(Document document) throws DocumentException
    {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Election Results", catFont));

        addEmptyLine(preface, 1);
        preface.add(new Paragraph(
                "Report generated: " + new Date(), smallBold));
        addEmptyLine(preface, 3);
        preface.add(new Paragraph(
                "This document contain elections results ",
                smallBold));

        addEmptyLine(preface, 8);

        document.add(preface);

        document.newPage();
    }

    private void addContent(Document document) throws DocumentException, IOException
    {
        Anchor anchor = new Anchor("Elections results", catFont);
        anchor.setName("Elections results");


        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph subPara = new Paragraph("Candidates results", subFont);
        Section subCatPart = catPart.addSection(subPara);

        createCandidateList(subCatPart);

        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 3);
        subCatPart.add(paragraph);

        subPara = new Paragraph("Party results", subFont);
        subCatPart = catPart.addSection(subPara);
        createPartyList(subCatPart);

        document.add(catPart);


    }

    private void createCandidateList(Section subCatPart) throws IOException, DocumentException
    {
        List list = new List(true, false, 25);

        BaseFont bf = BaseFont.createFont("arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(bf, 16);


        for (int i = 0; i < electionResults.size(); i++) {
            list.add(new ListItem(electionResults.get(i).getCandidateName() +
                    ", " + electionResults.get(i).getPartyName() +
                    "; Votes: " + electionResults.get(i).getVotes(), font));
            if(i == electionResults.size() -1){

                list.add(new ListItem("Invalid votes: " + invalidVotes, font));
            }
        }
        subCatPart.add(list);

    }

    private void createPartyList(Section subCatPart) throws IOException, DocumentException
    {
        List list = new List(true, false, 15);

        BaseFont bf = BaseFont.createFont("arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(bf, 16);

        for (int i = 0; i < partyVotes.length; i++) {

            list.add(new ListItem(Dictionary.PARTY_NAMES[i] + ": " + partyVotes[i], font));
        }
        subCatPart.add(list);
    }

    private void addEmptyLine(Paragraph paragraph, int number)
    {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
