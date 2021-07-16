package com.company.ui;

import com.company.dif.*;
import com.company.Contract;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.persistence.EntityManager;
import javax.swing.*;
        import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.List;
import java.util.Vector;

public class TabReporting extends JPanel {

    private int dealsDone,dealsOverdue;
    private long prices;
    private UI app;
    private EntityManager entityManager;
    private JLabel countDeals,countDoneDeals,countOverdueDeals, countSum, countEquipment, countClients;

    public TabReporting(UI app, EntityManager entityManager) {
        this.app = app;
        this.entityManager = entityManager;

        setLayout(null);

        countDeals = new JLabel();
        countDeals.setBounds(10, 10, 300, 20);
        add(countDeals);

        countDoneDeals = new JLabel();
        countDoneDeals.setBounds(10, 40, 300, 20);
        add(countDoneDeals);

        countOverdueDeals = new JLabel();
        countOverdueDeals.setBounds(10, 70, 300, 20);
        add(countOverdueDeals);

        countSum = new JLabel();
        countSum.setBounds(10, 100, 300, 20);
        add(countSum);

        countEquipment = new JLabel();
        countEquipment.setBounds(10, 130, 300, 20);
        add(countEquipment);

        countClients = new JLabel();
        countClients.setBounds(200, 130, 300, 20);
        add(countClients);

        if(app.deals!=null || app.contacts!=null || app.equip!=null)updateInfo();
        else JOptionPane.showMessageDialog(null, "Необходимые даные отсутствуют");

        JLabel labelPeriodWord = new JLabel("Финансовая сводка за периуд:");
        labelPeriodWord.setBounds(10, 160, 300, 20);
        add(labelPeriodWord);

        JLabel periodStart = new JLabel("Начало :");
        periodStart.setBounds(10, 180, 70, 20);
        add(periodStart);

        JLabel periodEnd = new JLabel("Конец :");
        periodEnd.setBounds(10, 210, 70, 20);
        add(periodEnd);

        UtilDateModel modelStart = new UtilDateModel();
        UtilDateModel modelEnd = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanelStart = new JDatePanelImpl(modelStart, p);
        JDatePanelImpl datePanelEnd = new JDatePanelImpl(modelEnd, p);
        JDatePickerImpl datePickerStart = new JDatePickerImpl(datePanelStart, new DateLabelFormatter());
        JDatePickerImpl datePickerEnd = new JDatePickerImpl(datePanelEnd, new DateLabelFormatter());
        datePickerStart.setBounds(80, 180, 150, 30);
        datePickerEnd.setBounds(80, 210, 150, 30);
        add(datePickerStart);
        add(datePickerEnd);

        JLabel periodInfoLabel = new JLabel();
        periodInfoLabel.setBounds(10, 240, 300, 60);
        add(periodInfoLabel);

        JButton periodShow = new JButton("Показать");
        periodShow.setBounds(240, 180, 150, 56);
        add(periodShow);
        periodShow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Contract> allDeals = entityManager.createQuery("from Contract order by sign_date").getResultList();

                if (allDeals == null || allDeals.size() == 0) {
                    JOptionPane.showMessageDialog(null,"Заключенных контрактов нет.");
                    return;
                }

                Date allDealsDateBegin = allDeals.get(0).getSignDate();

                Vector<Integer> uniqueIdsFromStats = new Vector<>();
                for (Contract s : allDeals) {
                    if (!uniqueIdsFromStats.contains(s.getId())) {
                        uniqueIdsFromStats.add(s.getId());
                    }
                }

                long millisCurrentTime = System.currentTimeMillis();

                String s_periodStart = datePickerStart.getJFormattedTextField().getText();
                String s_periodEnd = datePickerEnd.getJFormattedTextField().getText();

                Date datePeriodStart = null;
                Date datePeriodEnd = null;
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                Date dateCurrentTime = new Date(millisCurrentTime);

                try {
                    if (s_periodEnd.isEmpty() || s_periodStart.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Не указан промежуток");
                        throw new ParseException("", 1);
                    }

                    datePeriodStart = f.parse(s_periodStart);
                    datePeriodEnd = f.parse(s_periodEnd);
                } catch (ParseException exp) {
                    exp.printStackTrace();
                }

                if (datePeriodEnd == null || datePeriodStart == null) {
                    JOptionPane.showMessageDialog(null,"Не удалось вычислить дату.");
                    return;
                }

                if (datePeriodStart.after(datePeriodEnd)) {
                    JOptionPane.showMessageDialog(null,"Стартовая дата не может идти после конечной.");
                    return;
                }

                if (datePeriodEnd.after(dateCurrentTime)) {
                    JOptionPane.showMessageDialog(null,"Конечная дата не может быть в будущем.");
                    return;
                }

                if (datePeriodStart.equals(datePeriodEnd)) {
                    JOptionPane.showMessageDialog(null,"Стартовая дата не может совпадать с конечной.");
                    return;
                }


                if (datePeriodStart.before(allDealsDateBegin)) {
                    JOptionPane.showMessageDialog(null,"Начало работы отдела: " + f.format(allDealsDateBegin));
                    return;
                }

                int summaryDealCount =0;
                double summaryPriceCount = 0;
                int summaryDoneDealCount = 0;
                int summaryOverdueDealCount = 0;
                double summaryPriseOfOverdueDeals = 0;
                for(Contract currCont: allDeals){
                    if(datePeriodStart.getTime() <= currCont.getSignDate().getTime()
                    && datePeriodEnd.getTime() >= currCont.getDate().getTime() ){
                        summaryDealCount++;
                        summaryPriceCount+=currCont.getContract_Prise();
                        if(currCont.isDone())summaryDoneDealCount++;
                        else if(currCont.isOverdue()){
                            summaryOverdueDealCount++;
                            summaryPriseOfOverdueDeals+=currCont.getContract_Prise();
                        }
                    }
                    else if(datePeriodEnd.getTime() > currCont.getDate().getTime() &&
                    datePeriodStart.getTime() > currCont.getSignDate().getTime() && currCont.isOverdue())
                    {
                        summaryOverdueDealCount++;
                        summaryPriseOfOverdueDeals+=currCont.getContract_Prise();
                    }
                    else if(datePeriodEnd.getTime() > currCont.getDate().getTime() &&
                    datePeriodStart.getTime() > currCont.getSignDate().getTime() && currCont.isDone()){
                        summaryDoneDealCount++;
                    }
                }

                periodInfoLabel.setText("<html>За этот интервал произошло " +
                        summaryDealCount +
                        " сделок </br> на сумму " +
                        summaryPriceCount + "рублей." + "</br>Выполненно" + summaryDoneDealCount + "контрактов. </br>"
                         + summaryOverdueDealCount + "контракт был просрочен.</br>" +
                        "Убытки составляют " + summaryPriseOfOverdueDeals +"рублей"+
                        ".</html>");

            }
        });

        JButton createReports = new JButton("Создать отчеты");
        createReports.setBounds(10, 330, 200, 20);
        add(createReports);
        createReports.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PdfThread threadDeals = new PdfThread("deals", app.deals);
                PdfThread threadEquipment = new PdfThread("equipment", app.equip);
                PdfThread threadContacts = new PdfThread("clients", app.contacts);

                threadDeals.start();
                threadContacts.start();
                threadEquipment.start();

                threadDeals.join();
                threadContacts.join();
                threadEquipment.join();
            }
        });

        UI.logger.info("tab reporting start");
    }

    public void updateInfo() {
        dealsDone = 0;
        prices = 0;
        if(!app.deals.isEmpty())for(Contract tempCont : app.deals){
            if(tempCont.isDone())dealsDone++;
            if(!tempCont.isOverdue())prices+=tempCont.getContract_Prise();
            if(tempCont.isOverdue())dealsOverdue++;
        }

        countDeals.setText("Общее количесто заключенных контрактов: " + app.deals.size());
        countDoneDeals.setText("Общее количесто выполненных контрактов: " + dealsDone);
        countOverdueDeals.setText("Общее количесто просроченных контрактов: " + dealsOverdue);
        countSum.setText("Общая сумма контрактов: " + prices);
        countEquipment.setText("Количество оборудования: " + app.equip.size());
        countClients.setText("Количество контактов: " + app.contacts.size());
    }

}
