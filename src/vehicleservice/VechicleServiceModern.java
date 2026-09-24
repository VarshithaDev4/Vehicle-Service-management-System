package vehicleservice;
package vsms;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class VehicleServiceModern extends JFrame {

    // ================= COLORS =================
    private final Color NAVY = new Color(17, 24, 39);
    private final Color BLUE = new Color(37, 99, 235);
    private final Color BG = new Color(245, 247, 250);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT = new Color(31, 41, 55);
    private final Color MUTED = new Color(107, 114, 128);
    private final Color GREEN = new Color(22, 163, 74);
    private final Color ORANGE = new Color(234, 88, 12);

    private JPanel contentPanel;

    private JLabel customerCount;
    private JLabel vehicleCount;
    private JLabel serviceCount;
    private JLabel completedCount;

    private JTable customerTable;
    private JTable vehicleTable;
    private JTable serviceTable;
    private JTable historyTable;

    // ================= CONSTRUCTOR =================

    public VehicleServiceModern() {

        setTitle("Vehicle Service Management System");
        setSize(1250, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        createTopBar();
        createContent();

        showDashboard();

        setVisible(true);
    }

    // ================= TOP BAR =================

    private void createTopBar() {

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(NAVY);
        top.setPreferredSize(new Dimension(1250, 75));
        top.setBorder(new EmptyBorder(10, 25, 10, 25));

        JLabel logo = new JLabel("🚗  VSMS");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logo.setForeground(Color.WHITE);

        JPanel menu = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        menu.setOpaque(false);

        JButton dashboard = menuButton("Dashboard");
        JButton customers = menuButton("Customers");
        JButton vehicles = menuButton("Vehicles");
        JButton services = menuButton("Services");
        JButton history = menuButton("History");

        dashboard.addActionListener(e -> showDashboard());

        customers.addActionListener(e -> showCustomers());

        vehicles.addActionListener(e -> showVehicles());

        services.addActionListener(e -> showServices());

        history.addActionListener(e -> showHistory());

        menu.add(dashboard);
        menu.add(customers);
        menu.add(vehicles);
        menu.add(services);
        menu.add(history);

        top.add(logo, BorderLayout.WEST);
        top.add(menu, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
    }

    private JButton menuButton(String text) {

        JButton button = new JButton(text);

        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(NAVY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    // ================= CONTENT =================

    private void createContent() {

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BG);

        add(contentPanel, BorderLayout.CENTER);
    }

    private void setContent(JPanel panel) {

        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ================= DASHBOARD =================

    private void showDashboard() {

        JPanel main = new JPanel(new BorderLayout(20, 20));
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(30, 35, 30, 35));

        JPanel heading = new JPanel(new BorderLayout());
        heading.setOpaque(false);

        JLabel title = new JLabel("Vehicle Service Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT);

        JLabel subtitle =
                new JLabel("Manage customers, vehicles and service records");

        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);

        JPanel titles = new JPanel(new GridLayout(2, 1));
        titles.setOpaque(false);
        titles.add(title);
        titles.add(subtitle);

        heading.add(titles, BorderLayout.WEST);

        JButton refresh = new JButton("⟳ Refresh");

        styleButton(refresh, BLUE);

        refresh.addActionListener(e -> updateDashboard());

        heading.add(refresh, BorderLayout.EAST);

        main.add(heading, BorderLayout.NORTH);

        // Cards
        JPanel cards = new JPanel(new GridLayout(1, 4, 20, 20));
        cards.setOpaque(false);

        customerCount = new JLabel("0", JLabel.CENTER);
        vehicleCount = new JLabel("0", JLabel.CENTER);
        serviceCount = new JLabel("0", JLabel.CENTER);
        completedCount = new JLabel("0", JLabel.CENTER);

        cards.add(createCard("Total Customers", customerCount, BLUE));
        cards.add(createCard("Total Vehicles", vehicleCount, new Color(13, 148, 136)));
        cards.add(createCard("Service Records", serviceCount, ORANGE));
        cards.add(createCard("Completed", completedCount, GREEN));

        JPanel center = new JPanel(new BorderLayout(20, 20));
        center.setOpaque(false);

        center.add(cards, BorderLayout.NORTH);

        // Quick actions
        JPanel actions = new JPanel(new GridLayout(1, 3, 20, 20));
        actions.setOpaque(false);

        JButton addCustomer = actionButton(
                "➕ Add Customer",
                e -> addCustomer()
        );

        JButton addVehicle = actionButton(
                "🚗 Add Vehicle",
                e -> addVehicle()
        );

        JButton addService = actionButton(
                "🔧 Add Service",
                e -> addService()
        );

        actions.add(addCustomer);
        actions.add(addVehicle);
        actions.add(addService);

        center.add(actions, BorderLayout.CENTER);

        main.add(center, BorderLayout.CENTER);

        setContent(main);

        updateDashboard();
    }

    private JPanel createCard(String title, JLabel value, Color color) {

        JPanel card = new JPanel(new BorderLayout(5, 10));

        card.setBackground(WHITE);

        card.setBorder(new CompoundBorder(
                new MatteBorder(0, 5, 0, 0, color),
                new EmptyBorder(20, 15, 20, 15)
        ));

        JLabel heading = new JLabel(title, JLabel.CENTER);

        heading.setFont(new Font("Segoe UI", Font.BOLD, 14));
        heading.setForeground(MUTED);

        value.setFont(new Font("Segoe UI", Font.BOLD, 32));
        value.setForeground(TEXT);

        card.add(heading, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);

        return card;
    }

    // ================= DASHBOARD DATA =================

    private void updateDashboard() {

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement()) {

            ResultSet rs;

            rs = st.executeQuery("SELECT COUNT(*) FROM customers");

            if (rs.next())
                customerCount.setText(rs.getString(1));

            rs = st.executeQuery("SELECT COUNT(*) FROM vehicles");

            if (rs.next())
                vehicleCount.setText(rs.getString(1));

            rs = st.executeQuery("SELECT COUNT(*) FROM service_records");

            if (rs.next())
                serviceCount.setText(rs.getString(1));

            rs = st.executeQuery(
                    "SELECT COUNT(*) FROM service_records " +
                    "WHERE service_status='Completed'"
            );

            if (rs.next())
                completedCount.setText(rs.getString(1));

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= CUSTOMER PAGE =================

    private void showCustomers() {

        JPanel panel = createPage("Customer Management");

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JButton add = new JButton("＋ Add Customer");
        styleButton(add, BLUE);

        add.addActionListener(e -> addCustomer());

        top.add(add, BorderLayout.EAST);

        customerTable = createTable();

        loadCustomers();

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(customerTable), BorderLayout.CENTER);

        setContent(panel);
    }

    private void loadCustomers() {

        DefaultTableModel model = new DefaultTableModel(
                new String[]{
                        "ID", "Name", "Phone", "Email", "Address"
                }, 0
        );

        String sql =
                "SELECT customer_id,name,phone,email,address " +
                "FROM customers ORDER BY customer_id";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address")
                });
            }

            customerTable.setModel(model);

        } catch (SQLException e) {

            showError(e);
        }
    }

    // ================= VEHICLE PAGE =================

    private void showVehicles() {

        JPanel panel = createPage("Vehicle Management");

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JButton add = new JButton("＋ Add Vehicle");

        styleButton(add, BLUE);

        add.addActionListener(e -> addVehicle());

        top.add(add, BorderLayout.EAST);

        vehicleTable = createTable();

        loadVehicles();

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(vehicleTable), BorderLayout.CENTER);

        setContent(panel);
    }

    private void loadVehicles() {

        DefaultTableModel model = new DefaultTableModel(
                new String[]{
                        "Vehicle ID",
                        "Vehicle Number",
                        "Model",
                        "Type",
                        "Customer ID",
                        "Owner"
                }, 0
        );

        String sql =
                "SELECT v.vehicle_id,v.vehicle_number," +
                "v.vehicle_model,v.vehicle_type," +
                "c.customer_id,c.name " +
                "FROM vehicles v " +
                "JOIN customers c ON v.customer_id=c.customer_id " +
                "ORDER BY v.vehicle_id";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getString(6)
                });
            }

            vehicleTable.setModel(model);

        } catch (SQLException e) {

            showError(e);
        }
    }

    // ================= SERVICE PAGE =================

    private void showServices() {

        JPanel panel = createPage("Service Management");

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JButton add = new JButton("＋ Add Service");

        styleButton(add, BLUE);

        add.addActionListener(e -> addService());

        top.add(add, BorderLayout.EAST);

        serviceTable = createTable();

        loadServices();

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(serviceTable), BorderLayout.CENTER);

        setContent(panel);
    }

    private void loadServices() {

        DefaultTableModel model = new DefaultTableModel(
                new String[]{
                        "Service ID",
                        "Vehicle Number",
                        "Date",
                        "Service Type",
                        "Cost",
                        "Status"
                }, 0
        );

        String sql =
                "SELECT s.service_id,v.vehicle_number," +
                "s.service_date,s.service_type," +
                "s.service_cost,s.service_status " +
                "FROM service_records s " +
                "JOIN vehicles v ON s.vehicle_id=v.vehicle_id " +
                "ORDER BY s.service_id";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getDate(3),
                        rs.getString(4),
                        rs.getDouble(5),
                        rs.getString(6)
                });
            }

            serviceTable.setModel(model);

        } catch (SQLException e) {

            showError(e);
        }
    }

    // ================= HISTORY =================

    private void showHistory() {

        JPanel panel = createPage("Service History");

        historyTable = createTable();

        DefaultTableModel model = new DefaultTableModel(
                new String[]{
                        "Vehicle",
                        "Service ID",
                        "Date",
                        "Service Type",
                        "Cost",
                        "Status"
                }, 0
        );

        String sql =
                "SELECT v.vehicle_number,s.service_id," +
                "s.service_date,s.service_type," +
                "s.service_cost,s.service_status " +
                "FROM service_records s " +
                "JOIN vehicles v ON s.vehicle_id=v.vehicle_id " +
                "ORDER BY s.service_date DESC";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getString(1),
                        rs.getInt(2),
                        rs.getDate(3),
                        rs.getString(4),
                        rs.getDouble(5),
                        rs.getString(6)
                });
            }

            historyTable.setModel(model);

        } catch (SQLException e) {

            showError(e);
        }

        panel.add(new JScrollPane(historyTable), BorderLayout.CENTER);

        setContent(panel);
    }

    // ================= ADD CUSTOMER =================

    private void addCustomer() {

        JTextField name = new JTextField();
        JTextField phone = new JTextField();
        JTextField email = new JTextField();
        JTextField address = new JTextField();

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));

        form.add(new JLabel("Name:"));
        form.add(name);

        form.add(new JLabel("Phone:"));
        form.add(phone);

        form.add(new JLabel("Email:"));
        form.add(email);

        form.add(new JLabel("Address:"));
        form.add(address);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Add New Customer",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            String sql =
                    "INSERT INTO customers " +
                    "(name,phone,email,address) VALUES (?,?,?,?)";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, name.getText());
                ps.setString(2, phone.getText());
                ps.setString(3, email.getText());
                ps.setString(4, address.getText());

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Customer added successfully!"
                );

                if (customerTable != null)
                    loadCustomers();

                updateDashboard();

            } catch (SQLException e) {

                showError(e);
            }
        }
    }

    // ================= ADD VEHICLE =================

    private void addVehicle() {

        JTextField customerId = new JTextField();
        JTextField number = new JTextField();
        JTextField model = new JTextField();
        JTextField type = new JTextField();

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));

        form.add(new JLabel("Customer ID:"));
        form.add(customerId);

        form.add(new JLabel("Vehicle Number:"));
        form.add(number);

        form.add(new JLabel("Vehicle Model:"));
        form.add(model);

        form.add(new JLabel("Vehicle Type:"));
        form.add(type);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Add New Vehicle",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            String sql =
                    "INSERT INTO vehicles " +
                    "(customer_id,vehicle_number," +
                    "vehicle_model,vehicle_type) " +
                    "VALUES (?,?,?,?)";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, Integer.parseInt(customerId.getText()));
                ps.setString(2, number.getText());
                ps.setString(3, model.getText());
                ps.setString(4, type.getText());

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Vehicle added successfully!"
                );

                if (vehicleTable != null)
                    loadVehicles();

                updateDashboard();

            } catch (Exception e) {

                showError(e);
            }
        }
    }

    // ================= ADD SERVICE =================

    private void addService() {

        JTextField vehicleId = new JTextField();

        JTextField date = new JTextField();
        date.setText("2026-09-23");

        JTextField type = new JTextField();
        JTextField cost = new JTextField();

        String[] statusValues = {
                "Pending",
                "In Progress",
                "Completed"
        };

        JComboBox<String> status =
                new JComboBox<>(statusValues);

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));

        form.add(new JLabel("Vehicle ID:"));
        form.add(vehicleId);

        form.add(new JLabel("Service Date (YYYY-MM-DD):"));
        form.add(date);

        form.add(new JLabel("Service Type:"));
        form.add(type);

        form.add(new JLabel("Service Cost:"));
        form.add(cost);

        form.add(new JLabel("Status:"));
        form.add(status);