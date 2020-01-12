import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Editor implements ActionListener {
    private JFrame f;
    private JTree tree;

    Editor(){
        f = new JFrame("IF Engine Editor");

        // Create the menu
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("File");
        menu.getAccessibleContext().setAccessibleDescription("Choose files to edit here!");
        menuBar.add(menu);

        JMenuItem menuItem = new JMenuItem("Open", KeyEvent.VK_O);
        menuItem.addActionListener(this);
        menu.add(menuItem);

        f.setJMenuBar(menuBar);

        // Set up the left half of the screen (a tree)
        tree = new JTree(new DefaultMutableTreeNode("Everything"));
        JScrollPane treePane = new JScrollPane(tree);
        treePane.setBounds(10,15,280,500);

        // Set up the right half of the screen
        JTextArea attributes = new JTextArea();
        JScrollPane attributesPane = new JScrollPane(attributes);
        attributesPane.setBounds(300,15,465,500);

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
                        create_nodes(file, tree);
                    } catch(IOException ex) {
                        ex.printStackTrace();
                    }
                }

                break;
            default:
                break;
        }
    }

    private void create_nodes(File file, JTree tree) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while((st = br.readLine()) != null){
            System.out.println(st);
        }
    }
}
