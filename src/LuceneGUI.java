import cn.scut.qinzhou.IndexFiles;
import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.nio.file.*;

/**
 * Created by Qin on 5/16/2015.
 */
public class LuceneGUI {
    private JTabbedPane tabbedPane1;
    private JTextField Index_DocDir_TextField;
    private JButton Index_DocDir_browseButton;
    private JButton Index_IdxDir_browseButton;
    private JTextField Index_IdxDir_TextField;
    private JButton Index_startBuildingButton;
    private JCheckBox updateOnlyCheckBox;
    private JTextPane Index_textPane;
    private JTextField Search_IdxDir_TextField;
    private JButton Search_browseButton;
    private JTextField Search_SW_TextField;
    private JButton Search_searchButton;
    private JTable Index_Table;

    public LuceneGUI() {
        Index_DocDir_browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Document Dir Chooser");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    Index_DocDir_TextField.setText(chooser.getSelectedFile().toString());
                }
            }
        });
        Index_IdxDir_browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Index Dir Chooser");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    Index_IdxDir_TextField.setText(chooser.getSelectedFile().toString());
                }
            }
        });

        Index_startBuildingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Path docsPath = Paths.get(Index_DocDir_TextField.getText());
                if (docsPath == null || !Files.isReadable(docsPath)) {
                    Index_textPane.setText("error! the Document Director is not exists or readable");
                    return;
                }
                Path indexPath = Paths.get(Index_IdxDir_TextField.getText());
                if (indexPath == null || !Files.isWritable(indexPath)) {
                    Index_textPane.setText("error! the Index Director is not exists or writable");
                    return;
                }
                IndexFiles.sett_doc_show(Index_textPane.getDocument());
                IndexFiles.Index(Index_DocDir_TextField.getText(), Index_IdxDir_TextField.getText(),
                        !updateOnlyCheckBox.isSelected());


            }
        });

        Search_browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Index Dir Chooser");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    Index_IdxDir_TextField.setText(chooser.getSelectedFile().toString());
                }
            }
        });

        Search_searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }



    private static void createAndShowGUI(){
        JFrame frame = new JFrame("LuceneGUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        LuceneGUI lg = new LuceneGUI();
        frame.add(lg.tabbedPane1);
        frame.pack();
        frame.setVisible(true);

    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
//                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }
}
