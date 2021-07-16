package com.company.ui;

import com.company.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class TabContracts extends JPanel {

    private UI app;
    private EntityManager entityManager;
    private DefaultListModel<Contract> modelList;
    private JList jlist;
    private int lastSortIndClient = 0;
    private boolean lastOverdueDealState = false;
    private boolean lastDoneDealState = false;

    public TabContracts (UI app, EntityManager entityManager) {
        this.app = app;
        this.entityManager = entityManager;

        entityManager.getTransaction().begin();
        app.deals = entityManager.createQuery("from Contract").getResultList();
        entityManager.getTransaction().commit();
        //обновление статуса кнтрактов
        if(app.deals!=null)for(Contract cOn :app.deals){
            Date todayDate = new Date();
            if(todayDate.after(cOn.getDate()) && !cOn.isDone())cOn.setState(-1);
        }

        modelList = new DefaultListModel<>();
        jlist = new JList(modelList);
        jlist.setCellRenderer(new ListItemRenderer("contract.jpg"));
        for (Contract d : app.deals) {
            modelList.addElement(d);
        }
        jlist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    Rectangle r = jlist.getCellBounds(0, jlist.getLastVisibleIndex());
                    if (r != null && r.contains(e.getPoint())) {
                        onClickDeal((Contract) jlist.getSelectedValue());
                    }
                }
            }
        });

        setLayout(new BorderLayout());

        JScrollPane scroll = new JScrollPane(jlist);
        add(scroll, BorderLayout.CENTER);

        JButton addNewDealButton = new JButton();
        addNewDealButton.setText("Добавить новый контракт");
        addNewDealButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (app.equip.size() == 0) {
                    JOptionPane.showMessageDialog(null,"Сначала добавьте оборудование");
                    UI.logger.warn("adding new contract abort");
                    return;
                }
                else if(app.contacts.size() == 0) {
                    JOptionPane.showMessageDialog(null,"Сначала добавьте клиентов");
                    UI.logger.warn("adding new contract abort");
                    return;
                }

                JPanel panel = new JPanel();
                panel.setLayout(null);

                JFrame frame = new JFrame();

                JLabel client = new JLabel("Клиент");
                client.setBounds(25, 25, 200, 20);

                ArrayList<PrimCont> personsOfNewContract = new ArrayList<>();
                for(PrimCont primarCon : app.contacts)if(primarCon.getIsClient())personsOfNewContract.add(primarCon);
                String[] arr = new String[personsOfNewContract.size()];
                for (int i = 0; i < personsOfNewContract.size(); i++) {
                        arr[i] = personsOfNewContract.get(i).toString();
                }
                JComboBox box = new JComboBox(arr);
                box.setBounds(25, 50, 220, 20);


                JLabel dateLabel1 = new JLabel("Дата просрочки");
                UtilDateModel modell = new UtilDateModel();
                Properties p = new Properties();
                p.put("text.today", "Today");
                p.put("text.month", "Month");
                p.put("text.year", "Year");
                JDatePanelImpl datePanel = new JDatePanelImpl(modell, p);
                JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

                dateLabel1.setBounds(25, 75, 220, 20);
                datePicker.setBounds(25, 100, 220, 30);

                String[] arrq = new String[app.equip.size()];
                for (int i = 0; i < app.equip.size(); i++) {
                    arrq[i] = app.equip.get(i).toString();
                }
                JComboBox box1 = new JComboBox(arrq);
                box1.setBounds(25, 135, 220, 20);

                DefaultListModel<String> questionOut = new DefaultListModel<>();
                JList<String> listOfQuestionStrings = new JList<String>(questionOut);

                JScrollPane scrollQuestionOut = new JScrollPane(listOfQuestionStrings);
                scrollQuestionOut.setBounds(25, 160, 200, 40);
                panel.add(scrollQuestionOut);

                List<Equipment> newEquipList = new ArrayList<>();
                JButton equipmanager = new JButton("Добавить оборудование");
                equipmanager.setBounds(25, 210, 200, 20);
                equipmanager.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        questionOut.addElement(arrq[box1.getSelectedIndex()]);
                        newEquipList.add(app.equip.get(box1.getSelectedIndex()));
                        //sumOfCurrContract += app.equip.get(box1.getSelectedIndex()).getPrise();
                    }
                });


                JLabel loanLabel = new JLabel("Общая стоимость: " );
                loanLabel.setBounds(25, 250, 200, 20);


                NumberFormat format = NumberFormat.getInstance();
                NumberFormatter formatter = new NumberFormatter(format);
                formatter.setValueClass(Integer.class);
                formatter.setMinimum(0);
                formatter.setMaximum(Integer.MAX_VALUE);
                formatter.setAllowsInvalid(false);
                JFormattedTextField sumText = new JFormattedTextField(formatter);
                sumText.setBounds(25, 270, 220, 20);


                JButton ok = new JButton("Добавить");
                ok.setBounds(100, 305, 100, 20);
                ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Contract deal = new Contract();

                        //получение даты заключения договора
                        long dateBeforeOverdue = -1;
                        Date d = new Date();
                        Date today_d = new Date();
                        String string_date = datePicker.getJFormattedTextField().getText();
                        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            d = f.parse(string_date);
                            dateBeforeOverdue = d.getTime();
                        } catch (ParseException exp) {
                            exp.printStackTrace();
                        }

                        if (
                                app.equip.size() > 0 && dateBeforeOverdue != -1 && !newEquipList.isEmpty()
                                && dateBeforeOverdue > today_d.getTime()
                        ) {
                            //получение индексa клиента
                            int ind = box.getSelectedIndex();
                            deal.setClient(personsOfNewContract.get(ind));
                            //обновление поля "контракты", для клиента
                            personsOfNewContract.get(ind).setOneContract(deal);

                            //получение суммы договора
                            String sum_string = sumText.getText();
                            StringBuilder sum_st = new StringBuilder();
                            for (int i = 0 ; i < sum_string.length(); i++) {
                                if (Character.isDigit(sum_string.charAt(i))) {
                                    sum_st.append(sum_string.charAt(i));
                                }
                            }
                            long sumOfEquipPrices = Integer.parseInt(sum_st.toString());


                            //установка суммы
                            deal.setContract_Prise(sumOfEquipPrices);
                            //установка состояния
                            deal.setState(0);
                            //установка даты заключения договора
                            deal.setDate(new java.sql.Date(dateBeforeOverdue));
                            //установка выбранного оборудования
                            deal.setEquip(newEquipList);
                            /*//обновление поля "контракты", для каждого оборудования
                            for(Equipment eq : newEquipList){
                                eq.setOneContract(deal);
                            }*/
                            //установка дня заключения контракта
                            deal.setSignDate(new java.sql.Date(today_d.getTime()));

                            entityManager.getTransaction().begin();
                            app.deals.add(deal);
                            modelList.addElement(deal);
                            entityManager.persist(deal);
                            entityManager.persist(app.contacts.get(ind));
                            for(Equipment eq : newEquipList){
                               entityManager.persist(eq);

                            }
                            entityManager.getTransaction().commit();
                            frame.dispose();
                        }
                        else if(dateBeforeOverdue <= today_d.getTime()){
                            JOptionPane.showMessageDialog(null, "Невозможно создать просроченный контракт");
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Ошибка в данных");
                        }
                    }
                });

                panel.add(dateLabel1);
                panel.add(datePicker);
                panel.add(client);
                panel.add(box);
                panel.add(loanLabel);
                panel.add(sumText);
                panel.add(equipmanager);
                panel.add(box1);
                panel.add(ok);

                frame.setPreferredSize(new Dimension(300, 400));
                frame.getContentPane().add(panel);
                frame.setVisible(true);
                frame.setResizable(false);
                frame.setTitle("Добавить новый договор");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLocation(700, 300);
                frame.pack();
            }
        });


        JButton sortButton = new JButton("Фильтр");
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame();
                frame.setPreferredSize(new Dimension(480, 360));
                frame.setVisible(true);
                frame.setResizable(false);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLocation(700, 300);
                frame.setTitle("Параметры фильтрации");
                frame.pack();

                JPanel panel = new JPanel();
                frame.getContentPane().add(panel);
                panel.setLayout(null);

                JLabel showDealsForOneClient = new JLabel("Показать договоры выбранного клиента");
                showDealsForOneClient.setBounds(140, 40, 300, 20);
                panel.add(showDealsForOneClient);

                ArrayList<PrimCont> personsForSortContract = new ArrayList<>();
                for(PrimCont primarCon : app.contacts)if(primarCon.getIsClient())personsForSortContract.add(primarCon);
                String[] clientsArray = new String[personsForSortContract.size() + 1];
                clientsArray[0] = "Для всех";
                for (int i = 0; i < personsForSortContract.size(); i++) {
                    clientsArray[i + 1] = personsForSortContract.get(i).toString();
                }
                JComboBox dealsClientBox = new JComboBox(clientsArray);
                dealsClientBox.setBounds(140, 70, 220, 20);
                panel.add(dealsClientBox);
                dealsClientBox.setSelectedIndex(lastSortIndClient);
                dealsClientBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        lastSortIndClient = dealsClientBox.getSelectedIndex();
                    }
                });

                JRadioButton sortOverdueButton = new JRadioButton(" Только просроченные");
                sortOverdueButton.setFont(new Font("Arial", Font.PLAIN, 15));
                sortOverdueButton.setBounds(140, 100, 300, 20);
                panel.add(sortOverdueButton);
                //sortOverdueButton.setSelected(lastOverdueDealState);
                sortOverdueButton.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if(sortOverdueButton.isSelected())lastOverdueDealState = true;
                    }
                });

                JRadioButton sortDoneButton = new JRadioButton("Только выполненные");
                sortDoneButton.setFont(new Font("Arial", Font.PLAIN, 15));
                sortDoneButton.setBounds(140, 140, 300, 20);
                panel.add(sortDoneButton);
               // sortDoneButton.setSelected(lastDoneDealState);
                sortDoneButton.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if(sortDoneButton.isSelected())lastDoneDealState = true ;
                    }
                });


                JRadioButton sortAllButton = new JRadioButton("Пказать все");
                sortAllButton.setFont(new Font("Arial", Font.PLAIN, 15));
                sortAllButton.setBounds(140, 180, 300, 20);
                panel.add(sortAllButton);
                sortAllButton.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if(sortAllButton.isSelected()){
                            lastDoneDealState = false;
                             lastOverdueDealState =false;
                        }
                    }
                });

                ButtonGroup filterGroup = new ButtonGroup();
                filterGroup.add(sortOverdueButton);
                filterGroup.add(sortDoneButton);
                filterGroup.add(sortAllButton);

                JButton applySort = new JButton("Применить параметры");
                applySort.setBounds(140, 280, 200, 20);
                panel.add(applySort);
                applySort.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        modelList.clear();

                        DefaultListModel<Contract> temp_list = new DefaultListModel<>();

                        if (lastOverdueDealState) {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            String today_date =  "'" + format.format(new Date(System.currentTimeMillis())) + "'";
                            String queryStr = "from Contract where ( date < " + today_date + " and state = -1 )";

                            List<Contract> overdueDeals = entityManager.createQuery(queryStr).getResultList();

                            for (Contract d : overdueDeals) {
                                temp_list.addElement(d);
                            }
                        }
                        else if(lastDoneDealState){
                           // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                          //  String today_date =  "'" + format.format(new Date(System.currentTimeMillis())) + "'";
                            String queryStr = "from Contract where state > 0";

                            List<Contract> doneDeals = entityManager.createQuery(queryStr).getResultList();
                            for (Contract d : doneDeals) {
                                temp_list.addElement(d);

                            }
                        }
                        else {
                            for (Contract d : app.deals) {
                                temp_list.addElement(d);
                            }
                        }

                        if (lastSortIndClient != 0) {
                            PrimCont chosenClient = personsForSortContract.get(lastSortIndClient - 1);
                            for (int i = 0; i < temp_list.size(); i++) {
                                if (temp_list.get(i).getClient().getId() != chosenClient.getId()) {
                                    temp_list.remove(i);
                                    i--;
                                }
                            }
                        }

                        for (int i = 0; i < temp_list.size(); i++) {
                            modelList.addElement(temp_list.get(i));
                        }

                        frame.dispose();
                    }
                });
            }
        });

        JPanel comboPanel = new JPanel();
        comboPanel.add(sortButton);
        comboPanel.add(addNewDealButton);
        add(comboPanel, BorderLayout.SOUTH);

        UI.logger.info("tab of contracts start");
    }


    private void onClickDeal(Contract deal) {
        JFrame frame = new JFrame();
        frame.setPreferredSize(new Dimension(480, 460));
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocation(700, 300);
        frame.setTitle(deal.toString());

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel);
        panel.setLayout(null);


        JButton remove = new JButton("Удалить договор");
        remove.setBounds(140, 380, 200, 20);
        panel.add(remove);
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel removePanel = new JPanel();
                removePanel.setLayout(null);

                JFrame removeFrame = new JFrame();
                JButton removeIt = new JButton("Да");
                removeIt.setBounds(50, 10, 60, 20);
                removePanel.add(removeIt);
                removeIt.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                            PrimCont client = null;
                            for (PrimCont c : app.contacts) {
                                if (deal.getClient().getId() == c.getId()) {
                                    client = c;
                                    break;
                                }
                            }
                            if(client != null) client.getContracts().remove(deal);
                            for (Equipment equi : deal.getEquip()){
                                equi.getContracts().remove(deal);
                            }
                            modelList.remove(modelList.indexOf(deal));

                            app.deals.remove(deal);

                            entityManager.getTransaction().begin();
                            entityManager.persist(client);
                            entityManager.remove(deal);
                            entityManager.getTransaction().commit();

                        frame.dispose();
                        removeFrame.dispose();
                    }
                });

                JButton deniedRemoveIt = new JButton("Нет");
                deniedRemoveIt.setBounds(180, 10, 60, 20);
                removePanel.add(deniedRemoveIt);
                deniedRemoveIt.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        removeFrame.dispose();
                    }
                });
                removeFrame.setPreferredSize(new Dimension(300, 80));
                removeFrame.getContentPane().add(removePanel);
                removeFrame.setVisible(true);
                removeFrame.setResizable(false);
                removeFrame.setTitle("Удалить элемент?");
                removeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                removeFrame.setLocation(700, 300);
                removeFrame.pack();

            }
        });



        PrimCont client = null;
        for (PrimCont c : app.contacts) {
            if (deal.getClient().getId() == c.getId()) {
                client = c;
                break;
            }
        }

        ArrayList<Equipment> equipment = new ArrayList<>();
        for (int i=0; i<deal.getEquip().size();i++)
            for(Equipment equ : app.equip){
                if (deal.getEquip().get(i).getId() == equ.getId()) {
                    equipment.add(equ);
                    break;
                }
            }


        assert client != null;
        JLabel clientLabel = new JLabel("Клиент: " + client.toString());
        clientLabel.setBounds(10, 10, 300, 20);
        panel.add(clientLabel);

        JLabel dateSign = new JLabel("Договор действителен до: " + deal.getDate().toString());
        dateSign.setBounds(10, 40, 300 , 20);
        panel.add(dateSign);

        JLabel contractPrice = new JLabel("Сумма в договоре: " + deal.getContract_Prise());
        contractPrice.setBounds(10, 70, 300, 20);
        panel.add(contractPrice);

        JLabel overdueState = new JLabel("Статус просрочки: " + deal.isOverdue());
        overdueState.setBounds(10, 100, 300, 20);
        panel.add(overdueState);

        JLabel showEquipLabel = new JLabel("Оборудование: ");
        showEquipLabel.setBounds(10, 130, 300, 20);
        panel.add(showEquipLabel);

        DefaultListModel<String> equipInContract = new DefaultListModel<>();
        for(Equipment eq : equipment){
            //equipInContract.addElement(eq.toString());
            equipInContract.addElement("Название: " + eq.getName() + " Тип: " + eq.getType());
            equipInContract.addElement("Цена: " + eq.getPrise() + " Описание: " + eq.getDescription());
            equipInContract.addElement("-------------");
        }

        JList<String> showEquip = new JList<String>(equipInContract);
       // equipInContract.clear();

        JScrollPane scrollPaymentDates = new JScrollPane(showEquip);
        scrollPaymentDates.setBounds(10, 160, 200, 50);
        panel.add(scrollPaymentDates);


        JLabel redeemedLabel = new JLabel("Статус выполнения: " + deal.isDone());
        redeemedLabel.setBounds(10, 220, 300, 20);
        panel.add(redeemedLabel);

        if(!deal.isDone() && !deal.isOverdue()){
            JLabel newPay = new JLabel("Внести платеж: ");
            newPay.setBounds(10, 250, 100, 20);
            panel.add(newPay);

            NumberFormat format = NumberFormat.getInstance();
            NumberFormatter formatter = new NumberFormatter(format);
            formatter.setValueClass(Integer.class);
            formatter.setMinimum(0);
            formatter.setMaximum(Integer.MAX_VALUE);
            formatter.setAllowsInvalid(false);
            JFormattedTextField payText = new JFormattedTextField(formatter);
            payText.setBounds(110, 250, 150, 20);
            panel.add(payText);

            JButton payEnter = new JButton("Выполнить");
            payEnter.setBounds(270, 250, 100, 20);
            panel.add(payEnter);
            payEnter.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    String pay_string = payText.getText();
                    String pay_st = "";
                    for (int i = 0 ; i < pay_string.length(); i++) {
                        if (Character.isDigit(pay_string.charAt(i))) {
                            pay_st += pay_string.charAt(i);
                        }
                    }
                    int pay = Integer.parseInt(pay_st);

                    if(pay == deal.getContract_Prise()){
                        deal.setContract_Prise(0);
                        deal.setState(1);
                        deal.setDate(new Date());
                        entityManager.getTransaction().begin();
                        entityManager.persist(deal);
                        entityManager.getTransaction().commit();
                        frame.dispose();
                    }
                    else if(pay < deal.getContract_Prise() && pay >= 1000){
                        deal.setContract_Prise(deal.getContract_Prise()-pay);
                        entityManager.getTransaction().begin();
                        entityManager.persist(deal);
                        entityManager.getTransaction().commit();
                        frame.dispose();
                    }
                    else if(pay < 1000){
                        JOptionPane.showMessageDialog(null,"Внести можно  от 1000 руб!");
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Введённая сумма превышает указанную");
                    }

                }
            });
        }



        frame.pack();
    }


}
