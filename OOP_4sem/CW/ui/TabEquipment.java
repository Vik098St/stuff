package com.company.ui;

import com.company.Contract;
import com.company.Equipment;
import com.company.PrimCont;
import com.company.dif.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.List;


public class TabEquipment extends JPanel {

    private UI app;
    private EntityManager entityManager;
    private DefaultListModel<Equipment> model;
    private int lastSortType = 0;
    private boolean lastCountSortState =false;
    private boolean lastPriseSortState = false;

    public TabEquipment(UI app, EntityManager entityManager) {
        this.app = app;
        this.entityManager = entityManager;


        entityManager.getTransaction().begin();
        app.equip = entityManager.createQuery("from Equipment").getResultList();
        entityManager.getTransaction().commit();
        model = new DefaultListModel<>();
        JList list = new JList(model);
        list.setCellRenderer(new ListItemRenderer("server.jpg"));
        for (Equipment m : app.equip) {
            model.addElement(m);
        }



        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    Rectangle r = list.getCellBounds(0, list.getLastVisibleIndex());
                    if (r != null && r.contains(e.getPoint())) {
                        onClick((Equipment) list.getSelectedValue());
                    }
                }
            }
        });

        setLayout(new BorderLayout());

        JScrollPane scroll = new JScrollPane(list);
        add(scroll, BorderLayout.CENTER);

        JButton addNewEquipButton = new JButton();
        addNewEquipButton.setText("Добавить товар");
        addNewEquipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = new JPanel();
                panel.setLayout(null);

                JFrame frame = new JFrame();

                JLabel newNameLabel = new JLabel("Название:");
                newNameLabel.setBounds(25, 25, 70, 20);

                JTextField newNameText = new JTextField();
                newNameText.setBounds(95, 25, 150, 20);

                JLabel newTypeLabel = new JLabel("Тип:");
                newTypeLabel.setBounds(25, 55, 70, 20);
                panel.add(newTypeLabel);
                final String[] typeArray = {"Server","Platform","Tower","Hard","GPU","Power","Other"};
                JComboBox quipTypeBox = new JComboBox(typeArray);
                quipTypeBox.setBounds(95, 55, 150, 20);
                panel.add(quipTypeBox);
                quipTypeBox.setSelectedIndex(lastSortType);
                quipTypeBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        lastSortType = quipTypeBox.getSelectedIndex();
                    }
                });

                JLabel priceLabel = new JLabel("Стоимость:");
                priceLabel.setBounds(25, 80, 150, 20);

                NumberFormat format = NumberFormat.getInstance();
                NumberFormatter formatter = new NumberFormatter(format);
                formatter.setValueClass(Integer.class);
                formatter.setMinimum(0);
                formatter.setMaximum(Integer.MAX_VALUE);
                formatter.setAllowsInvalid(false);
                JFormattedTextField priceText = new JFormattedTextField(formatter);
                priceText.setBounds(95, 80, 150, 20);

                JLabel descriptionLabel = new JLabel("Описание товара:");
                descriptionLabel.setBounds(25, 110, 150, 20);

                JTextField descriptionText = new JTextField();
                descriptionText.setBounds(25, 135, 250, 50);

                JButton ok = new JButton("Добавить");
                ok.setBounds(100, 220, 100, 20);
                ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Equipment equipment = new Equipment();

                        try {
                            equipment.setName(newNameText.getText());
                            equipment.setType(typeArray[lastSortType]);

                            String price_string = priceText.getText();
                            StringBuilder price_st = new StringBuilder();
                            for (int i = 0 ; i < price_string.length(); i++) {
                                if (Character.isDigit(price_string.charAt(i))) {
                                    price_st.append(price_string.charAt(i));
                                }
                            }
                            int priceOFEquip = Integer.parseInt(price_st.toString());
                            equipment.setPrise(priceOFEquip);
                            equipment.setDescription(descriptionText.getText());

                            entityManager.getTransaction().begin();
                            app.equip.add(equipment);
                            model.addElement(equipment);
                            entityManager.persist(equipment);
                            entityManager.getTransaction().commit();
                            frame.dispose();
                        } catch (ErrorFormException exception) {
                            exception.showErrorOnScreen();
                        }

                    }
                });
                panel.add(newNameLabel);
                panel.add(newNameText);
                panel.add(newTypeLabel);
                panel.add(quipTypeBox);
                panel.add(priceLabel);
                panel.add(priceText);
                panel.add(descriptionLabel);
                panel.add(descriptionText);
                panel.add(ok);

                frame.setPreferredSize(new Dimension(300, 300));
                frame.getContentPane().add(panel);
                frame.setVisible(true);
                frame.setResizable(false);
                frame.setTitle("Добавить оборудование");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLocation(700, 300);
                frame.pack();
            }
        });

        JButton sortButton = new JButton("ТОП продаж");
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(app.equip == null){
                    JOptionPane.showMessageDialog(null,"Список пуст! \n Сортировка невозможна!");
                    return;
                }

                JFrame frame = new JFrame();
                frame.setPreferredSize(new Dimension(440, 300));
                frame.setVisible(true);
                frame.setResizable(false);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLocation(700, 300);
                frame.setTitle("ТОП продаж"); //("Параметры сортировки");
                frame.pack();

                JPanel panel = new JPanel();
                frame.getContentPane().add(panel);
                panel.setLayout(null);

                JRadioButton sortByCountButton = new JRadioButton("По количеству");
                sortByCountButton.setFont(new Font("Arial", Font.PLAIN, 15));
                sortByCountButton.setBounds(140, 40, 300, 20);
                panel.add(sortByCountButton);
                sortByCountButton.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if(sortByCountButton.isSelected()) {
                            lastCountSortState = true;
                            lastPriseSortState = false;
                        }
                    }
                });

                JRadioButton sortByPriceButton = new JRadioButton("По общей сумме");
                sortByPriceButton.setFont(new Font("Arial", Font.PLAIN, 15));
                sortByPriceButton.setBounds(140, 80, 300, 20);
                panel.add(sortByPriceButton);
                sortByPriceButton.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if(sortByPriceButton.isSelected()){
                            lastPriseSortState = true ;
                            lastCountSortState = false;
                        }
                    }
                });

                JRadioButton sortAllButton = new JRadioButton("Пказать все");
                sortAllButton.setFont(new Font("Arial", Font.PLAIN, 15));
                sortAllButton.setBounds(140, 120, 300, 20);
                panel.add(sortAllButton);
                sortAllButton.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if(sortAllButton.isSelected()){
                            lastCountSortState = false;
                            lastPriseSortState =false;
                        }
                    }
                });

                ButtonGroup sortGroup = new ButtonGroup();
                sortGroup.add(sortByCountButton);
                sortGroup.add(sortByPriceButton);
                sortGroup.add(sortAllButton);



                JButton applySort = new JButton("Применить параметры");
                applySort.setBounds(120, 200, 200, 20);
                panel.add(applySort);
                    applySort.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            model.clear();
                            ArrayList<Equipment> sortEquipList = new ArrayList<>();
                            if (lastCountSortState)
                            {
                                for (Equipment equip : app.equip){
                                    if(equip.countEntryInContracts() != 0)sortEquipList.add(equip);
                                }
                                sortEquipList.sort(new Comparator<Equipment>() {
                                    public int compare(Equipment o1, Equipment o2) {
                                        int stFoCompare =0;
                                        if(o1.countEntryInContracts() < o2.countEntryInContracts())stFoCompare = 1;
                                        else if(o1.countEntryInContracts() > o2.countEntryInContracts())stFoCompare = -1;
                                        return stFoCompare;
                                    }
                                });
                                if(!sortEquipList.isEmpty())for (Equipment equi1: sortEquipList)model.addElement(equi1);
                            }
                            else if(lastPriseSortState)
                            {
                                for(Equipment equip : app.equip){
                                    if(equip.countPriceOfEntry()!=0L)sortEquipList.add(equip);
                                }
                                sortEquipList.sort(new Comparator<Equipment>() {
                                    public int compare(Equipment o1, Equipment o2) {
                                        int stFoCompare =0;
                                        if(o1.countPriceOfEntry() < o2.countPriceOfEntry())stFoCompare = 1;
                                        else if(o1.countPriceOfEntry() > o2.countPriceOfEntry())stFoCompare = -1;
                                        return stFoCompare;
                                    }
                                });
                                if(!sortEquipList.isEmpty())for (Equipment equi1: sortEquipList)model.addElement(equi1);
                            }
                            else
                            {
                                for (Equipment equiAll: app.equip)model.addElement(equiAll);
                            }

                            frame.dispose();
                        }
                    });
            }
        });

        JPanel comboPanel = new JPanel();
        comboPanel.add(sortButton);
        comboPanel.add(addNewEquipButton);
        add(comboPanel, BorderLayout.SOUTH);

        UI.logger.info("tab equipment start");
    }

    private void onClick(Equipment curEquip) {
        JFrame frame = new JFrame();
        frame.setPreferredSize(new Dimension(480, 360));
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocation(700, 300);
        frame.setTitle(curEquip.toString());

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        JLabel equipNameOutLabel = new JLabel("Название " + curEquip.getName());
        equipNameOutLabel.setBounds(10, 10, 300, 20);
        panel.add(equipNameOutLabel);

        JLabel equipTypeOutLabel = new JLabel("Тип товара: " + curEquip.getType());
        equipTypeOutLabel.setBounds(10, 40, 300, 20);
        panel.add(equipTypeOutLabel);

        JLabel equipPriceOutLabel = new JLabel("Стоимость товара: " + curEquip.getPrise());
        equipPriceOutLabel.setBounds(10, 70, 300, 20);
        panel.add(equipPriceOutLabel);

        JLabel countHisClients = new JLabel("Описание товара:  ");
        countHisClients.setBounds(10, 85, 200, 70);
        panel.add(countHisClients);

        if (curEquip.getDescription() == null)curEquip.setDescription("--");
        String[] decriptArr = curEquip.getDescription().split("\\w{20}");
        DefaultListModel<String> descriptionOut = new DefaultListModel<>();
        for (String s : decriptArr) descriptionOut.addElement(s);
        JList<String> listOfDescriptionStrings = new JList<String>(descriptionOut);

        JScrollPane scrollPaymentDates = new JScrollPane(listOfDescriptionStrings);
        scrollPaymentDates.setBounds(10, 130, 300, 100);
        panel.add(scrollPaymentDates);


        JButton resetEquip = new JButton("Изменить информацию");
        resetEquip.setBounds(140, 240, 200, 20);
        panel.add(resetEquip);
        resetEquip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JPanel panel = new JPanel();
                panel.setLayout(null);

                JFrame frame = new JFrame();

                JLabel newNameLabel = new JLabel("Название:");
                newNameLabel.setBounds(25, 25, 70, 20);

                JTextField newNameText = new JTextField(curEquip.getName());
                newNameText.setBounds(95, 25, 150, 20);

                JLabel newTypeLabel = new JLabel("Тип:");
                newTypeLabel.setBounds(25, 55, 70, 20);
                panel.add(newTypeLabel);
                final String[] typeArray = {"Server","Platform","Tower","Hard","GPU","Power","Other"};
                JComboBox quipTypeBox = new JComboBox(typeArray);
                quipTypeBox.setBounds(95, 55, 150, 20);
                panel.add(quipTypeBox);
                quipTypeBox.setSelectedIndex(lastSortType);
                quipTypeBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        lastSortType = quipTypeBox.getSelectedIndex();
                    }
                });

                JLabel priceLabel = new JLabel("Стоимость:");
                priceLabel.setBounds(25, 80, 150, 20);

                NumberFormat format = NumberFormat.getInstance();
                NumberFormatter formatter = new NumberFormatter(format);
                formatter.setValueClass(Integer.class);
                formatter.setMinimum(0);
                formatter.setMaximum(Integer.MAX_VALUE);
                formatter.setAllowsInvalid(false);
                JFormattedTextField priceText = new JFormattedTextField(formatter);
                priceText.setValue(curEquip.getPrise());
                priceText.setBounds(95, 80, 150, 20);

                JLabel descriptionLabel = new JLabel("Описание товара:");
                descriptionLabel.setBounds(25, 110, 150, 20);

                JTextField descriptionText = new JTextField(curEquip.getDescription());
                descriptionText.setBounds(25, 135, 250, 50);

                JButton ok = new JButton("Изменить");
                ok.setBounds(100, 220, 100, 20);
                ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            curEquip.setName(newNameText.getText());
                            curEquip.setType(typeArray[lastSortType]);

                            String price_string = priceText.getText();
                            StringBuilder price_st = new StringBuilder();
                            for (int i = 0 ; i < price_string.length(); i++) {
                                if (Character.isDigit(price_string.charAt(i))) {
                                    price_st.append(price_string.charAt(i));
                                }
                            }
                            int priceOFEquip = Integer.parseInt(price_st.toString());
                            curEquip.setPrise(priceOFEquip);
                            curEquip.setDescription(descriptionText.getText());

                            entityManager.getTransaction().begin();
                           // app.equip.add(curEquip);
                            //model.addElement(curEquip);
                            entityManager.persist(curEquip);
                            entityManager.getTransaction().commit();
                            frame.dispose();
                        } catch (ErrorFormException exception) {
                            exception.showErrorOnScreen();
                        }

                    }
                });
                panel.add(newNameLabel);
                panel.add(newNameText);
                panel.add(newTypeLabel);
                panel.add(quipTypeBox);
                panel.add(priceLabel);
                panel.add(priceText);
                panel.add(descriptionLabel);
                panel.add(descriptionText);
                panel.add(ok);

                frame.setPreferredSize(new Dimension(300, 300));
                frame.getContentPane().add(panel);
                frame.setVisible(true);
                frame.setResizable(false);
                frame.setTitle("Добавить оборудование");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLocation(700, 300);
                frame.pack();
            }
        });

        JButton remove = new JButton("Удалить товар");
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
                     if(curEquip.getContracts() ==null || curEquip.getContracts().isEmpty()) {
                         entityManager.getTransaction().begin();
                         entityManager.remove(curEquip);
                         entityManager.getTransaction().commit();

                         model.remove(model.indexOf(curEquip));
                         app.equip.remove(curEquip);
                     }else{
                         JOptionPane.showMessageDialog(null,"Нельзя удалить оборудование, задействованное в контрактах!");
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
