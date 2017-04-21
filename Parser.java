package cssc;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;
import org.apache.*;
import org.jsoup.*;

public class Parser {
    public List<CSSRule> classes, ids;
    public List<File> HTMLFiles;
    public List<File> JSFiles;
    private CSSOMParser parser;
    private org.w3c.css.sac.InputSource data;
    private CSSStyleSheet sheet;
    private Reader fileReader;


    Parser(String cssPath, String docPath) {
        this.classes = new ArrayList<CSSRule>();
        this.ids = new ArrayList<CSSRule>();

        Reader reader = null;

        System.out.println("[i] opening CSS file " + cssPath + " ...");

        try {
            reader = new FileReader(cssPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("[i] parsing CSS...");
        this.data = new org.w3c.css.sac.InputSource(reader);
        this.parser = new CSSOMParser(new SACParserCSS3());
        try {
            this.sheet = parser.parseStyleSheet(this.data, null, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        CSSRuleList rules = this.sheet.getCssRules();
        for (int i = 0; i < rules.getLength(); i++) {
            String selectors = rules.item(i).getCssText().trim();
            switch (selectors.charAt(0)) {
                case '.':
                    this.classes.add(rules.item(i));
                    break;
                case '#':
                    this.ids.add(rules.item(i));
                    break;
                default:
                    break;
            }
        }

        System.out.println("\t[+] found " + this.classes.size() + " class and " + this.ids.size() + " id declarations");

        this.HTMLFiles = new ArrayList<File>();
        this.JSFiles = new ArrayList<File>();

        String paths[] = docPath.split(";");

        System.out.println("[i] scanning " + paths[0] + " for HTML/PHP files");
        if (paths.length > 1)
            System.out.println("         and " + paths[1] + " for JS files");

        this.getFiles(paths[0], this.HTMLFiles);
        if (paths.length > 1)
            this.getFiles(paths[1], this.JSFiles);

        System.out.println("[+] found: " + this.HTMLFiles.size() + " HTML/PHP files " + ((paths.length > 1) ? " and " + this.JSFiles.size() + " CSS files" : ""));
    }

    private void getFiles(String path, List<File> result) {
        File dir = new File(path);
        File[] filesList = dir.listFiles();

        if (filesList == null) {
            throw new AssertionError();
        }
        for (File file : filesList) {
            if (file.isFile()) {
                if (this.extensionIsOkHTML(FilenameUtils.getExtension(file.getName())))
                    this.HTMLFiles.add(file);
                if (this.extensionIsOkJS(FilenameUtils.getExtension(file.getName())))
                    this.JSFiles.add(file);
            } else if (file.isDirectory())
                getFiles(file.getAbsolutePath(), result);
        }
    }

    private boolean extensionIsOkHTML(String extension) {
        return extension.equals("php") || extension.equals("html");
    }

    private boolean extensionIsOkJS(String extension) { return extension.equals("js"); }
}
