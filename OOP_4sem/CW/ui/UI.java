package com.company.ui;

        import com.company.Contract;
        import com.company.Equipment;
        import com.company.PrimCont;
        import org.apache.log4j.Logger;
        import javax.persistence.EntityManager;
        import javax.persistence.EntityManagerFactory;
        import javax.persistence.Persistence;
        import javax.swing.*;
        import javax.swing.event.ChangeEvent;
        import javax.swing.event.ChangeListener;
        import java.awt.*;
        import java.util.List;
       // import java.util.logging.Logger;

public class UI extends JFrame {
    public static final int WIDTH  = 640;
    public static final int HEIGHT = 480;
    private static final String TITLE = "SERVER CENTER";

    private static int SCREEN_WIDTH;
    private static int SCREEN_HEIGHT;

    public static final String TAB_CONTACTS  = "Контакты";
    public static final String TAB_EQUIPMENT = "Оборудование";
    public static final String TAB_CONTRACTS = "Контракты";
    public static final String TAB_REPORTING = "Отчётность";
    //все контакты, клиенты...
    public List<PrimCont> contacts;
    public List<Contract> deals;
    public List<Equipment> equip;

    public final static Logger logger = Logger.getLogger(UI.class);


    public UI() {
        super(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        SCREEN_WIDTH = dimension.width;
        SCREEN_HEIGHT = dimension.height;
        setLocation(SCREEN_WIDTH/2 - WIDTH/2, SCREEN_HEIGHT/2 - HEIGHT/2);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("test_persistence");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(TAB_CONTACTS, new TabContacts(this, entityManager));
        tabbedPane.addTab(TAB_CONTRACTS, new TabContracts(this, entityManager));
        tabbedPane.addTab(TAB_EQUIPMENT, new TabEquipment(this, entityManager));
        TabReporting reporting = new TabReporting(this, entityManager);
        tabbedPane.addTab(TAB_REPORTING, reporting);

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() == 3) {
                    reporting.updateInfo();
                    reporting.repaint();
                }
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(tabbedPane, BorderLayout.CENTER);
        getContentPane().add(panel);

        setVisible(true);
        pack();

        logger.info("app start");
    }
}
