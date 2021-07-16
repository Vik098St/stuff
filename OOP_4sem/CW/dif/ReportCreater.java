package com.company.dif;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.company.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import java.io.FileOutputStream;
//создание отчётов
public class ReportCreater {
    private static String DEF_REPORT_DIR = "C:\\Users\\Vik_S\\IdeaProjects\\CW_OOP\\itextpdf";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    //private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    //private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    //private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);



    public static PdfPTable reportClients(List<PrimCont> clients) {
        PdfPTable table = new PdfPTable(7);

        table.addCell("ID");
        table.addCell("first name");
        table.addCell("second name");
        table.addCell("telephone");
        table.addCell("status");
        table.addCell("info");
        table.addCell("done contracts");

        if (clients != null) {
            for (PrimCont c : clients) {
                table.addCell(Integer.toString(c.getId()));
                table.addCell(c.getFirstName());
                table.addCell(c.getLastName());
                table.addCell(Long.toString(c.getTelephone()));
                if(c.getIsClient())table.addCell("client");
                else table.addCell("Primary contact");
                if(c.getIsClient()){
                    StringBuilder str = new StringBuilder();
                    for(Contract con : c.getContracts()){
                        str.append(con.toString());
                    }
                    table.addCell(String.valueOf(str));
                }else table.addCell(c.getQuestion());
                if(c.getIsClient()){
                    if(c.countDoneContracts() != 0)table.addCell(Integer.toString(c.countDoneContracts()));
                    else table.addCell("no contracts signed");
                }else table.addCell("the primary contact cannot have contracts");
            }
        }

        //reportKernel("clients", table);
        return table;
    }

    public static PdfPTable reportEquipment(List<Equipment> equipment) {
        PdfPTable table = new PdfPTable(7);

        table.addCell("ID");
        table.addCell("name");
        table.addCell("type");
        table.addCell("price");
        table.addCell("description");
        table.addCell("count entry's in deals");
        table.addCell("expected revenue");

        if (equipment != null) {
            for (Equipment c : equipment) {
                table.addCell(Integer.toString(c.getId()));
                table.addCell(c.getName());
                table.addCell(c.getType());
                table.addCell(Double.toString(c.getPrise()));
                table.addCell(c.getDescription());
                table.addCell(Integer.toString(c.countEntryInContracts()));
                table.addCell(Long.toString(c.countPriceOfEntry()));
            }
        }

        //reportKernel("equipment", table);
        return table;
    }

    public static PdfPTable reportDeals(List<Contract> deals) {
        PdfPTable table = new PdfPTable(7);

        table.addCell("ID");
        table.addCell("client id");
        table.addCell("hardware");
        table.addCell("date sign");
        table.addCell("date of overdue");
        table.addCell("state");
        table.addCell("price");

        if (deals != null) {
            for (Contract c : deals) {
                table.addCell(Integer.toString(c.getId()));
                table.addCell(Integer.toString(c.getClient().getId()));
                StringBuilder str = new StringBuilder();
                for(Equipment eq : c.getEquip()){
                    str.append(eq.toString());
                }
                table.addCell(String.valueOf(str));
                SimpleDateFormat formatter = new SimpleDateFormat("MMM-dd-yyyy");
                String message = formatter.format(c.getSignDate());
                table.addCell(message);
                message = formatter.format(c.getDate());
                table.addCell(message);
                if(c.isOverdue())table.addCell("overdue");
                else if (c.isDone())table.addCell("done");
                else table.addCell("in process");
                table.addCell(Double.toString(c.getContract_Prise()));
            }
        }

        //reportKernel("deals", table);
        return table;
    }

    public static void reportKernel(String which, PdfPTable table) {
        try {
            long millis = System.currentTimeMillis();
            Date currentDate = new Date(millis);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
            String date = formatter.format(currentDate);

            Document document = new Document();
            PdfWriter.getInstance(document,
                    new FileOutputStream(DEF_REPORT_DIR + "\\" + which + "_" + date + ".pdf"));
            document.open();

            document.addTitle("Report");

            Anchor anchor = new Anchor("Report " + which + " " + date, catFont);
//            anchor.setName("Report " + which);

            Chapter catPart = new Chapter(new Paragraph(anchor), 1);

            catPart.add(Chunk.NEWLINE);


            catPart.add(table);


            // now add all this to the document
            document.add(catPart);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
