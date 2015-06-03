package cn.scut.qinzhou;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;



import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;

/** Simple command-line based search demo. */
public class SearchFiles {

    private static javax.swing.text.Document doc_show;
    public static Object[][] data;

    public static void set_doc_show(javax.swing.text.Document doc) {
        doc_show = doc;
    }

    private SearchFiles() {
    }

    /**
     * Simple command-line based search demo.
     */
    public static void Search(String index, String field, String queries, int repeat, boolean raw, String queryString, int maxHits) {
        try {

            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
            IndexSearcher searcher = new IndexSearcher(reader);
//            Analyzer analyzer = new StandardAnalyzer();
//            Analyzer analyzer = new CJKAnalyzer();
            Analyzer analyzer = new SmartChineseAnalyzer();
            if (queries == null && queryString == null) {                        // prompt the user
                doc_show.insertString(doc_show.getLength(), "the searchWord and queries could not both be empty!\n", new SimpleAttributeSet());
                return;
            }
            BufferedReader in = null;
            if (!queries.equals("")) {
                in = Files.newBufferedReader(Paths.get(queries), StandardCharsets.UTF_8);
            }
            QueryParser parser = new QueryParser(field, analyzer);

            String line = queryString != null ? queryString : in.readLine();
            line = line.trim();
            if (line.length() <= 0) {
                doc_show.insertString(doc_show.getLength(), "the searchWord could not be space!\n", new SimpleAttributeSet());
            }
            Query query = parser.parse(line);
            doc_show.insertString(doc_show.getLength(), "Searching for:" + query.toString(field) + "\n", new SimpleAttributeSet());

            if (repeat > 0) {                           // repeat & time as benchmark
                Date start = new Date();
                for (int i = 0; i < repeat; i++) {
                    searcher.search(query, 100);
                }
                Date end = new Date();
                doc_show.insertString(doc_show.getLength(), "Time: " + (end.getTime() - start.getTime()) + "ms\n", new SimpleAttributeSet());
            }

            doPagingSearch(in, searcher, query, maxHits, raw, queries == null && queryString == null);

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This demonstrates a typical paging search scenario, where the search engine presents
     * pages of size n to the user. The user can then go to the next page if interested in
     * the next hits.
     * <p>
     * When the query is executed for the first time, then only enough results are collected
     * to fill 5 result pages. If the user wants to page beyond this limit, then the query
     * is executed another time and all hits are collected.
     */
    public static void doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query,
                                           int maxHits, boolean raw, boolean interactive) {
        try {

            // Collect at most maxHits docs
            TopDocs results = searcher.search(query, maxHits);
            ScoreDoc[] hits = results.scoreDocs;

            int numTotalHits = results.totalHits;
            doc_show.insertString(doc_show.getLength(), numTotalHits + " total matching documents\n", new SimpleAttributeSet());

            int show_hits = Math.min(maxHits,numTotalHits);


            data = new Object[show_hits][];
            for (int i = 0; i < show_hits;++i) {
                Document doc = searcher.doc(hits[i].doc);
                String path = doc.get("path");
                double score = hits[i].score;
                int shardIndex = hits[i].shardIndex;
                data[i] = new Object[]{path,score,shardIndex};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

