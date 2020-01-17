import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import objects.*;

class Editor implements ActionListener {
    private JFrame f;
    private JMenuBar menuBar;
    private JTree tree;
    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;

    Editor(){
        f = new JFrame("IF Engine Editor");

        // Create the menu(s)
        menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem menuItem = new JMenuItem("Open", KeyEvent.VK_O);
        menuItem.addActionListener(this);
        fileMenu.add(menuItem);

        // View menu
        JMenu viewMenu = new JMenu("View");

        viewMenu.addSeparator();
        JMenu sMenu = new JMenu("Theme");
        sMenu.setMnemonic(KeyEvent.VK_T);

        JMenuItem sMenuItem = new JMenuItem("Light");
        sMenuItem.addActionListener(this);
        sMenu.add(sMenuItem);

        sMenuItem = new JMenuItem("Dark");
        sMenuItem.addActionListener(this);
        sMenu.add(sMenuItem);
        viewMenu.add(sMenu);

        menuBar.add(viewMenu);

        // Set the menu bar
        f.setJMenuBar(menuBar);


        // Set up the right half of the screen
        JTextArea attributes = new JTextArea();
        JScrollPane attributesPane = new JScrollPane(attributes);
        attributesPane.setBounds(300,15,465,500);


        // Set up the left half of the screen (a tree)
        root = new DefaultMutableTreeNode("Everything");
        treeModel = new DefaultTreeModel(root);
        treeModel.addTreeModelListener(new CustomTreeModelListener());

        tree = new JTree(treeModel);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        // Handles everything when a tree node is selected
        // cool lambda!
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                    tree.getLastSelectedPathComponent();

            if(node == null) return;

            Object info = node.getUserObject();

            if(info instanceof Room){
                // Do something
            }

        });

        JScrollPane treePane = new JScrollPane(tree);
        treePane.setBounds(10,15,280,500);

        // Set up the split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePane, attributesPane);
        splitPane.setBounds(10,15,760,500);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(300);

        treePane.setMinimumSize(new Dimension(100,500));
        attributesPane.setMinimumSize(new Dimension(465,500));

        f.add(splitPane);

        // Apparently this has to go at the bottom
        f.setSize(800,600);
        f.setResizable(false);
        f.setLayout(null);
        f.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "Open":
                final JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
                int returnVal = fc.showOpenDialog(f);

                if(returnVal == JFileChooser.APPROVE_OPTION){
                    File file = fc.getSelectedFile();
                    try {
                        create_nodes(file, root);
                    } catch(IOException ex) {
                        ex.printStackTrace();
                    }
                }

                break;
            case "Light":
                // Very beta
                f.getContentPane().setBackground(null);
                System.out.println("Theme has been changed to Light.");
                break;
            case "Dark":
                // Very beta
                f.getContentPane().setBackground(Color.DARK_GRAY);
                System.out.println("Theme has been changed to Dark.");
                break;
            default:
                break;
        }
    }

    private void create_nodes(File file, DefaultMutableTreeNode root) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));

        DefaultMutableTreeNode currentRoom = null;

        // Do something for each string
        String st;
        while((st = br.readLine()) != null){
            System.out.println(st);

            // Trim the line
            st = st.trim();

            if(st.substring(0,4).equals("Room")){

                String[] split = st.split(" @@@ ");

                currentRoom = addObject(root, split[0].substring(5, split[0].length()));

            } else if(st.substring(0,6).equals("Object")){

                String[] split = st.split(" @@@ ");

                if(currentRoom != null) {
                    addObject(currentRoom, split[0].substring(7, split[0].length()));
                }

            }
        }
    }

    private DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child) {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
        treeModel.insertNodeInto(childNode, parent, parent.getChildCount());
        tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        
        return childNode;
    }
}

// Custom TreeModelListener (could've used lambda but this is perhaps better (maybe))
class CustomTreeModelListener implements TreeModelListener {

    @Override
    public void treeNodesChanged(TreeModelEvent e) {

    }

    @Override
    public void treeNodesInserted(TreeModelEvent e) {
        System.out.println("A node has been inserted.");
    }

    @Override
    public void treeNodesRemoved(TreeModelEvent e) {

    }

    @Override
    public void treeStructureChanged(TreeModelEvent e) {

    }
}
