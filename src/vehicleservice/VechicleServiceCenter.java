package vehicleservice;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class VehicleServiceCenter extends JFrame {

    // =====================================================
    // COLORS
    // =====================================================

    private final Color NAVY = new Color(15, 23, 42);
    private final Color BLUE = new Color(30, 64, 175);
    private final Color LIGHT_BG = new Color(241, 245, 249);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT = new Color(30, 41, 59);
    private final Color MUTED = new Color(100, 116, 139);
    private final Color GREEN = new Color(22, 163, 74);
    private final Color ORANGE = new Color(234, 88, 12);
    private final Color PURPLE = new Color(124, 58, 237);

    // =====================================================
    // DASHBOARD COMPONENTS
    // =====================================================

    private JLabel lblCustomers;
    private JLabel lblVehicles;
    private JLabel lblServices;
    private JLabel lblCompleted;

    private JPanel contentPanel;
    private CardLayout cardLayout;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public VehicleServiceCenter() {

        setTitle("Vehicle Service Center");

        setSize(1200, 750);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLayout(new BorderLayout());

        // Sidebar
        add(
                createSidebar(),
                BorderLayout.WEST
        );

        // Content
        cardLayout = new CardLayout();

        contentPanel = new JPanel(
                cardLayout
        );

        contentPanel.setBackground(
                LIGHT_BG
        );

        contentPanel.add(
                createDashboard(),
                "Dashboard"
        );

        contentPanel.add(
                createCustomersPanel(),
                "Customers"
        );

        contentPanel.add(
                createVehiclesPanel(),
                "Vehicles"
        );

        contentPanel.add(
                createServicesPanel(),
                "Services"
        );

        add(
                contentPanel,
                BorderLayout.CENTER
        );

        loadDashboardData();

        setVisible(true);
    }

    // =====================================================
    // SIDEBAR
    // =====================================================

    private JPanel createSidebar() {

        JPanel sidebar = new JPanel(
                new BorderLayout()
        );

        sidebar.setPreferredSize(
                new Dimension(220, 750)
        );

        sidebar.setBackground(NAVY);

        JPanel logo = new JPanel(
                new GridLayout(3, 1)
        );

        logo.setBackground(NAVY);

        logo.setBorder(
                new EmptyBorder(
                        25, 15, 25, 15
                )
        );

        JLabel car = new JLabel(
                "🚗",
                JLabel.CENTER
        );

        car.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        35
                )
        );

        JLabel title = new JLabel(
                "SERVICE CENTER",
                JLabel.CENTER
        );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel(
                "Management System",
                JLabel.CENTER
        );

        sub.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        sub.setForeground(
                new Color(148, 163, 184)
        );

        logo.add(car);
        logo.add(title);
        logo.add(sub);

        sidebar.add(
                logo,
                BorderLayout.NORTH
        );

        JPanel menu = new JPanel(
                new GridLayout(
                        6,
                        1,
                        0,
                        8
                )
        );

        menu.setBackground(NAVY);

        menu.setBorder(
                new EmptyBorder(
                        10, 12, 10, 12
                )
        );

        JButton dashboard =
                createMenuButton(
                        "⌂   Dashboard"
                );

        JButton customers =
                createMenuButton(
                        "👤   Customers"
                );

        JButton vehicles =
                createMenuButton(
                        "🚘   Vehicles"
                );

        JButton services =
                createMenuButton(
                        "🔧   Services"
                );

        JButton history =
                createMenuButton(
                        "📋   Service History"
                );

        JButton exit =
                createMenuButton(
                        "✕   Exit"
                );

        dashboard.addActionListener(
                e -> showPage("Dashboard")
        );

        customers.addActionListener(
                e -> showPage("Customers")
        );

        vehicles.addActionListener(
                e -> showPage("Vehicles")
        );

        services.addActionListener(
                e -> showPage("Services")
        );
