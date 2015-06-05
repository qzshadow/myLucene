package cn.scut.qinzhou;

import cn.scut.qinzhou.IndexFiles;
import cn.scut.qinzhou.SearchFiles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JButton Search_IdxDir_browseButton;
    private JTextField Search_SW_TextField;
    private JButton Search_searchButton;
    private JTextField Search_field_textField;
    private JTextField Search_repeat_textField;
    private JTextField Search_queries_textField;
    private JCheckBox rawCheckBox;
    private JTextArea Search_textArea;
    private JTextField Search_MaxHits_textField;
    private JButton Search_queries_browerButton;
    private JScrollPane table_ScrollPane;

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

        Search_IdxDir_browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Index Dir Chooser");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    Search_IdxDir_TextField.setText(chooser.getSelectedFile().toString());
                }
            }
        });
        Search_queries_browerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("queries File Chooser");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    Search_queries_textField.setText(chooser.getSelectedFile().toString());
                }
            }
        });

        Search_searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String index = Search_IdxDir_TextField.getText();
                String field = Search_field_textField.getText();
                String queries = Search_queries_textField.getText();
                String queryString = Search_SW_TextField.getText();
                int repeat = Integer.parseInt(Search_repeat_textField.getText().equals("null") ? "0" : Search_repeat_textField.getText());
                boolean raw = rawCheckBox.isSelected();
                int MaxHits = Integer.parseInt(Search_MaxHits_textField.getText().equals("null") ? "10" : Search_MaxHits_textField.getText());
                MaxHits = MaxHits < 1 ? 1 : MaxHits;
                SearchFiles.set_doc_show(Search_textArea.getDocument());
                SearchFiles.Search(index, field, queries, repeat, raw, queryString, MaxHits);
                String[] columnNames = {"path", "score", "shareIndex"};
                JTable Search_table = new JTable(SearchFiles.data, columnNames);
                table_ScrollPane.setViewportView(Search_table);
//                Search_table.setRowHeight(50);

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
