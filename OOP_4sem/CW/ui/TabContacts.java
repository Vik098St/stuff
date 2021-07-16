package com.company.ui;

import com.company.dif.*;
import com.company.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

public class TabContacts extends JPanel {

    //вкладка контактов
        private UI app;
        private EntityManager entityManager;
        private DefaultListModel<PrimCont> modelList;
        private JList jlist;
        //private int sortLastState = 0;
        private boolean debtorsState = false;
        private boolean isClientState = false;
        private int stateOfSort = 0;

        public TabContacts (UI app, EntityManager entityManager){
            this.app = app;
            this.entityManager = entityManager;

            //get list of contacts from bd
            entityManager.getTransaction().begin();
            app.contacts = entityManager.createQuery("from PrimCont").getResultList();
            entityManager.getTransaction().commit();

            modelList = new DefaultListModel<>();
            jlist = new JList(modelList);
            jlist.setCellRenderer(new ListItemRenderer("client.jpg"));
            for (PrimCont c : app.contacts) {
                modelList.addElement(c);
            }

            jlist.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 1) {
                        Rectangle r = jlist.getCellBounds(0, jlist.getLastVisibleIndex());
                        if (r != null && r.contains(e.getPoint())) {
                            onClickClient((PrimCont) jlist.getSelectedValue());
                        }
                    }
                }
            });

            setLayout(new BorderLayout());

            JScrollPane scroll = new JScrollPane(jlist);
            add(scroll, BorderLayout.CENTER);

            JPanel comboPanel = new JPanel();
            //comboPanel.setLayout(new BorderLayout());

            JButton addNewContactButton = new JButton();
            addNewContactButton.setText("Добавить нового клиента");
            addNewContactButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    /*
                    if (app.contacts.size() == 0) {
                        JOptionPane.showMessageDialog(null,"smth wrong");
                     //   UI.logger.warn("adding new client abort");
                        return;
                    }*/

                    JPanel panel = new JPanel();
                    panel.setLayout(null);

                    JFrame frame = new JFrame();

                    JLabel firstNameLabel = new JLabel("Имя");
                    firstNameLabel.setBounds(25, 25, 70, 20);

                    JTextField firstNameText = new JTextField();
                    firstNameText.setBounds(95, 25, 150, 20);

                    JLabel secondNameLabel = new JLabel("Фамилия");
                    secondNameLabel.setBounds(25, 55, 70, 20);

                    JTextField secondNameText = new JTextField();
                    secondNameText.setBounds(95, 55, 150, 20);

                    JLabel telephoneLabel = new JLabel("Телефон");
                    telephoneLabel.setBounds(25, 85, 70, 20);
                    MaskFormatter phoneFormatter =null;
                    try {
                        phoneFormatter = new MaskFormatter("+#-###-###-##-##");
                        phoneFormatter.setPlaceholderCharacter('0');
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
                    JFormattedTextField sumText = new JFormattedTextField(phoneFormatter);
                    sumText.setColumns(16);
                    sumText.setBounds(95, 85, 150, 20);


                   // JTextField telephoneText = new JTextField();
                   // telephoneText.setBounds(95, 85, 150, 20);

                    JLabel isClientLabel = new JLabel("Статус");
                    isClientLabel.setBounds(25, 110, 70, 20);

                    JRadioButton isClientButton = new JRadioButton("Клиент");
                    isClientButton.setFont(new Font("Arial", Font.PLAIN, 15));
                    isClientButton.setSelected(true);
                    isClientButton.setSize(75, 20);
                    isClientButton.setLocation(25, 130);
                    isClientButton.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            if(isClientButton.isSelected())isClientState = true;
                        }
                    });

                    JRadioButton isNoClientButton = new JRadioButton("Первич. контакт");
                    isNoClientButton.setFont(new Font("Arial", Font.PLAIN, 15));
                    isNoClientButton.setSelected(false);
                    isNoClientButton.setSize(200, 20);
                    isNoClientButton.setLocation(100, 130);
                    isNoClientButton.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            if(isNoClientButton.isSelected())isClientState =false;
                        }
                    });

                    ButtonGroup isClientOrContact = new ButtonGroup();
                    isClientOrContact.add(isClientButton);
                    isClientOrContact.add(isNoClientButton);
                    //Не дойдёт до релиза
                    /*if(isClientState) {
                        JButton addContract = new JButton("Добавить контракт");
                        addContract.setBounds(100, 150, 100, 20);
                        panel.add(addContract);
                        TabContracts.addConract(currentClient)

                    }*/

                    JLabel questionLabel = new JLabel("Информация о контакте");
                    questionLabel.setBounds(25, 170, 200, 20);

                    JTextField questionText = new JTextField();
                    questionText.setBounds(25, 195, 200, 50);

                    JButton ok = new JButton("Добавить");
                    ok.setBounds(100, 270, 100, 20);
                    ok.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            PrimCont client = new PrimCont();
                        try {
                                client.setFirstName(firstNameText.getText());
                                client.setLastName(secondNameText.getText());
                                /*if (Long.parseLong(telephoneText.getText())>80000000000L) {
                                    JOptionPane.showMessageDialog(null, "Неправильно набран номер");
                                    UI.logger.warn("adding new client abort");
                                    return;
                                }*/
                            String sum_string = sumText.getText();
                            StringBuilder sum_st = new StringBuilder();
                            for (int i = 0 ; i < sum_string.length(); i++) {
                                if (Character.isDigit(sum_string.charAt(i))) {
                                    sum_st.append(sum_string.charAt(i));
                                }
                            }
                            long telephoneText = Long.parseLong(sum_st.toString());
                                client.setTelephone(telephoneText);
                                client.setIsClient(isClientState);
                                client.setQuestion(questionText.getText());

                                entityManager.getTransaction().begin();
                                app.contacts.add(client);
                                modelList.addElement(client);
                                entityManager.persist(client);
                                entityManager.getTransaction().commit();
                                frame.dispose();
                            } catch (ErrorFormException exception) {
                                exception.showErrorOnScreen();
                            }

                        }
                    });

                    panel.add(firstNameLabel);
                    panel.add(firstNameText);
                    panel.add(secondNameLabel);
                    panel.add(secondNameText);
                    panel.add(telephoneLabel);
                    panel.add(sumText);
                    panel.add(isClientLabel);
                    panel.add(isClientButton);
                    panel.add(isNoClientButton);
                    panel.add(questionLabel);
                    panel.add(questionText);
                    panel.add(ok);

                    frame.setPreferredSize(new Dimension(300, 360));
                    frame.getContentPane().add(panel);
                    frame.setVisible(true);
                    frame.setResizable(false);
                    frame.setTitle("Добавить нового клиента");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setLocation(700, 300);
                    frame.pack();
                }
            });

//====================================================================================================================
            JButton sortButton = new JButton("Фильтр");
            sortButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    stateOfSort = 0;
                    JFrame frame = new JFrame();
                    frame.setPreferredSize(new Dimension(440, 300));
                    frame.setVisible(true);
                    frame.setResizable(false);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setLocation(700, 300);
                    frame.setTitle("ТОП клиентов"); //("Параметры сортировки");
                    frame.pack();

                    JPanel panel = new JPanel();
                    frame.getContentPane().add(panel);
                    panel.setLayout(null);

                    JRadioButton sortTopButton = new JRadioButton("Топ клиентов");
                    sortTopButton.setFont(new Font("Arial", Font.PLAIN, 15));
                    sortTopButton.setBounds(140, 40, 300, 20);
                    panel.add(sortTopButton);
                    sortTopButton.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            if(sortTopButton.isSelected())stateOfSort = 1;
                        }
                    });

                    JRadioButton sortContactButton = new JRadioButton("Только контакты");
                    sortContactButton.setFont(new Font("Arial", Font.PLAIN, 15));
                    sortContactButton.setBounds(140, 80, 300, 20);
                    panel.add(sortContactButton);
                    sortContactButton.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            if(sortContactButton.isSelected())stateOfSort = -1;
                        }
                    });

                    JRadioButton sortAllButton = new JRadioButton("Пказать все");
                    sortAllButton.setFont(new Font("Arial", Font.PLAIN, 15));
                    sortAllButton.setBounds(140, 120, 300, 20);
                    panel.add(sortAllButton);
                    sortAllButton.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            if(sortAllButton.isSelected())stateOfSort = 0;
                        }
                    });

                    ButtonGroup isClientOrContact = new ButtonGroup();
                    isClientOrContact.add(sortTopButton);
                    isClientOrContact.add(sortContactButton);
                    isClientOrContact.add(sortAllButton);

                    JButton applySort = new JButton("Применить параметры");
                    applySort.setBounds(120, 200, 200, 20);
                    panel.add(applySort);
                    applySort.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            modelList.clear();

                            if (stateOfSort == 1) {//ТОП клиентов
                                ArrayList<PrimCont> topClients = new ArrayList<>();
                                for (PrimCont c : app.contacts) {
                                    if(c.getIsClient())topClients.add(c);
                                }
                                topClients.sort(new Comparator<PrimCont>() {
                                    public int compare(PrimCont o1, PrimCont o2) {
                                        int stFoCompare =0;
                                        if(o1.countDoneContracts() < o2.countDoneContracts())stFoCompare = 1;
                                        else if(o1.countDoneContracts() > o2.countDoneContracts())stFoCompare = -1;
                                        return stFoCompare;
                                    }
                                });
                                for(PrimCont sortedC : topClients)modelList.addElement(sortedC);
                            }
                            else if(stateOfSort == -1){ //только контакты
                                for (PrimCont c : app.contacts) {
                                   if(!c.getIsClient()) modelList.addElement(c);
                                }
                            }
                            else {
                                for (PrimCont c : app.contacts) {
                                    modelList.addElement(c);
                                }
                            }

                            frame.dispose();
                        }
                    });
                }
            });
//====================================================================

            comboPanel.add(sortButton);
            comboPanel.add(addNewContactButton);

            add(comboPanel, BorderLayout.SOUTH);

            UI.logger.info("tab contacts start");
        }

        private void onClickClient(PrimCont currentClient) {
            JFrame frame = new JFrame();
            frame.setPreferredSize(new Dimension(480, 360));
            frame.setVisible(true);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocation(700, 300);
            frame.setTitle(currentClient.toString());

            JPanel panel = new JPanel();
            frame.getContentPane().add(panel);
            panel.setLayout(null);


            JLabel firstName = new JLabel("Имя: " + currentClient.getFirstName());
            firstName.setBounds(10, 10, 300, 20);
            panel.add(firstName);

            JLabel lastName = new JLabel("Фамилия: " + currentClient.getLastName());
            lastName.setBounds(10, 40, 300, 20);
            panel.add(lastName);

            JLabel telephone = new JLabel("Телефон: " + currentClient.getTelephone());
            telephone.setBounds(10, 70, 300, 20);
            panel.add(telephone);

            String stat;
            if (currentClient.getIsClient())
                stat = "клиент";
            else stat = "первич. контакт";
            JLabel status = new JLabel("Статус: " + stat);
            status.setBounds(150, 10, 300, 20);
            panel.add(status);
            if (!currentClient.getIsClient())
            {
                JCheckBox changeStatus = new JCheckBox("Изменить статус");
                changeStatus.setBounds(150, 40, 140, 20);
                panel.add(changeStatus);
                changeStatus.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        currentClient.setIsClient(!currentClient.getIsClient());
                        entityManager.getTransaction().begin();
                        entityManager.persist(currentClient);
                        entityManager.getTransaction().commit();
                    }
                });


                JLabel questionOutLabel = new JLabel("Доп инфо: ");
                questionOutLabel.setBounds(250, 70, 300, 20);
                panel.add(questionOutLabel);

                String[] questionArr = currentClient.getQuestion().split("\\w{20}");
                DefaultListModel<String> questionOut = new DefaultListModel<>();
                for (String s : questionArr) questionOut.addElement(s);
                JList<String> listOfQuestionStrings = new JList<String>(questionOut);

                JScrollPane scrollQuestionOut = new JScrollPane(listOfQuestionStrings);
                scrollQuestionOut.setBounds(150, 100, 300, 100);
                panel.add(scrollQuestionOut);

            }
            else{

                JCheckBox changeStatus = new JCheckBox("Изменить статус");
                changeStatus.setBounds(150, 40, 150, 20);
                panel.add(changeStatus);
                changeStatus.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (currentClient.getContracts().isEmpty()) {
                            currentClient.setIsClient(false);
                            entityManager.getTransaction().begin();
                            entityManager.persist(currentClient);
                            entityManager.getTransaction().commit();
                        }else JOptionPane.showMessageDialog(null,"Сначала удалите контракты клиента!");
                    }
                });

                DefaultListModel<String> contractsOfClient = new DefaultListModel<>();

                JList<String> listPaymentDates = new JList<String>(contractsOfClient);

                JScrollPane scrollPaymentDates = new JScrollPane(listPaymentDates);
                scrollPaymentDates.setBounds(10, 130, 300, 100);
                panel.add(scrollPaymentDates);

                JCheckBox checkForOverdue = new JCheckBox("Показывать только просроченные");
                checkForOverdue.setBounds(10, 100, 300, 20);
                panel.add(checkForOverdue);
                checkForOverdue.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        contractsOfClient.clear();
                        if (checkForOverdue.isSelected()) {
                            LocalDate now = LocalDate.now();
                            Date dateNow = Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant());

                            for (Contract d : app.deals) {
                                if (d.getClient().getId() == currentClient.getId() && d.isOverdue() && d.getDate().before(dateNow)) {
                                    contractsOfClient.addElement("Контракт №" + d.getId() + " Цена:" + d.getContract_Prise());
                                    if(d.isDone())contractsOfClient.addElement("Контракт Выполнен");
                                    if(d.getEquip() != null)for(Equipment p : d.getEquip())
                                        contractsOfClient.addElement("Оборудование" + p.toString());
                                    contractsOfClient.addElement("-----------------");
                                }
                            }
                        } else {
                            for (Contract d : app.deals) {
                                if (d.getClient().getId()== currentClient.getId()) {
                                    contractsOfClient.addElement("Контракт №" + d.getId() + " Цена:" + d.getContract_Prise());
                                    contractsOfClient.addElement("Действует до:" + d.getDate());
                                    if(d.isDone())contractsOfClient.addElement("Контракт Выполнен");
                                    if(d.getEquip() != null)for(Equipment p : d.getEquip())
                                        contractsOfClient.addElement("Оборудование" + p.toString());
                                    contractsOfClient.addElement("-----------------");
                                }
                            }
                        }

                        if (currentClient.getIsClient() && contractsOfClient.isEmpty()) {
                            contractsOfClient.addElement("Нет заключённых контрактов");
                        }

                        if (!currentClient.getIsClient()){
                            contractsOfClient.addElement("Не является клиентом");
                        }

                    }
                });
                checkForOverdue.setSelected(true);
                checkForOverdue.doClick();
            }


            JButton resetCont = new JButton("Изменить информацию");
            resetCont.setBounds(140, 240, 200, 20);
            panel.add(resetCont);
            resetCont.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JPanel panel = new JPanel();
                    panel.setLayout(null);

                    JFrame frame = new JFrame();

                    JLabel firstNameLabel = new JLabel("Имя");
                    firstNameLabel.setBounds(25, 25, 70, 20);

                    JTextField firstNameText = new JTextField(currentClient.getFirstName());
                    firstNameText.setBounds(95, 25, 150, 20);

                    JLabel secondNameLabel = new JLabel("Фамилия");
                    secondNameLabel.setBounds(25, 55, 70, 20);

                    JTextField secondNameText = new JTextField(currentClient.getLastName());
                    secondNameText.setBounds(95, 55, 150, 20);

                    JLabel telephoneLabel = new JLabel("Телефон");
                    telephoneLabel.setBounds(25, 85, 70, 20);
                    MaskFormatter phoneFormatter =null;
                    try {
                        phoneFormatter = new MaskFormatter("+#-###-###-##-##");
                        phoneFormatter.setPlaceholderCharacter('0');
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
                    JFormattedTextField sumText = new JFormattedTextField(phoneFormatter);
                    sumText.setColumns(16);
               //     sumText.setValue(currentClient.getTelephone());
                    sumText.setBounds(95, 85, 150, 20);


                    JLabel questionLabel = new JLabel("Информация о контакте");
                    questionLabel.setBounds(25, 170, 200, 20);

                    JTextField questionText = new JTextField(currentClient.getQuestion());
                    questionText.setBounds(25, 195, 200, 50);

                    JButton ok = new JButton("Изменить");
                    ok.setBounds(100, 270, 100, 20);
                    ok.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            try {
                                currentClient.setFirstName(firstNameText.getText());
                                currentClient.setLastName(secondNameText.getText());
                                /*if (Long.parseLong(telephoneText.getText())>80000000000L) {
                                    JOptionPane.showMessageDialog(null, "Неправильно набран номер");
                                    UI.logger.warn("adding new client abort");
                                    return;
                                }*/
                                String sum_string = sumText.getText();
                                StringBuilder sum_st = new StringBuilder();
                                for (int i = 0 ; i < sum_string.length(); i++) {
                                    if (Character.isDigit(sum_string.charAt(i))) {
                                        sum_st.append(sum_string.charAt(i));
                                    }
                                }
                                long telephoneText = Long.parseLong(sum_st.toString());
                                currentClient.setTelephone(telephoneText);

                                currentClient.setQuestion(questionText.getText());

                                entityManager.getTransaction().begin();
                                entityManager.persist(currentClient);
                                entityManager.getTransaction().commit();
                                frame.dispose();
                            } catch (ErrorFormException exception) {
                                exception.showErrorOnScreen();
                            }

                        }
                    });

                    panel.add(firstNameLabel);
                    panel.add(firstNameText);
                    panel.add(secondNameLabel);
                    panel.add(secondNameText);
                    panel.add(telephoneLabel);
                    panel.add(sumText);
                    panel.add(questionLabel);
                    panel.add(questionText);
                    panel.add(ok);

                    frame.setPreferredSize(new Dimension(300, 360));
                    frame.getContentPane().add(panel);
                    frame.setVisible(true);
                    frame.setResizable(false);
                    frame.setTitle("Добавить нового клиента");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setLocation(700, 300);
                    frame.pack();
                }
            });





            JButton remove = new JButton("Удалить запись");
            remove.setBounds(140, 280, 200, 20);
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
                            if(currentClient.getContracts() ==null || currentClient.getContracts().isEmpty()) {
                                entityManager.getTransaction().begin();
                                entityManager.remove(currentClient);
                                entityManager.getTransaction().commit();

                                modelList.remove(modelList.indexOf(currentClient));
                                app.contacts.remove(currentClient);
                            }else{
                                JOptionPane.showMessageDialog(null,"Нельзя удалить клиента, на котором числятся контракты.\nУдалите сначала контракты.");
                            }
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

            frame.pack();
        }

}
