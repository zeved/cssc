package cssc;

import java.io.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;
import org.w3c.dom.css.CSSRule;

public class Scanner {
    private int[] classesStatus;
    private int[] idsStatus;
    private int unusedClasses, unusedIDs;
    private Parser parser;

    public Scanner(Parser parser) {
        this.parser = parser;
        this.classesStatus = new int[parser.classes.size()];
        this.idsStatus = new int[parser.ids.size()];
        this.scanHTMLFiles();
    }

    private void scanHTMLFiles() {
        System.out.println("[i] scanning HTML/PHP files...");

        for (File file : this.parser.HTMLFiles) {
            System.out.println("\t" + file.getName());
            try {
                Document doc = Jsoup.parse(file, "UTF-8", "");
                for (CSSRule classDef : this.parser.classes) {
                    String rule = classDef.getCssText().split("\\{")[0];
                    Elements e = new Elements();
                    try {
                        e = doc.select(rule);
                    } catch (Selector.SelectorParseException exception) {
                        this.classesStatus[this.parser.classes.indexOf(classDef)] = 0xff;
                    } finally {
                        if (this.classesStatus[this.parser.classes.indexOf(classDef)] < 1 && this.classesStatus[this.parser.classes.indexOf(classDef)] != 0xff) {
                            if (e.size() > 0)
                                this.classesStatus[this.parser.classes.indexOf(classDef)] = 1;
                            else
                                this.classesStatus[this.parser.classes.indexOf(classDef)] = -1;
                        }
                    }
                }

                for (CSSRule idDef : this.parser.ids) {
                    String rule = idDef.getCssText().split("\\{")[0];
                    Elements e = new Elements();
                    try {
                        e = doc.select(rule);
                    } catch (Selector.SelectorParseException exception) {
                        this.idsStatus[this.parser.ids.indexOf(idDef)] = 0xff;
                    } finally {
                        if (this.idsStatus[this.parser.ids.indexOf(idDef)] < 1 && this.idsStatus[this.parser.ids.indexOf(idDef)] != 0xff) {
                            if (e.size() > 0)
                                this.idsStatus[this.parser.ids.indexOf(idDef)] = 1;
                            else
                                this.idsStatus[this.parser.ids.indexOf(idDef)] = -1;
                        }
                    }
                }
            } catch (IOException exception) {
                System.out.println("[-] failed scanning: " + exception.getMessage());
            }
        }

        for (int i = 0; i < this.classesStatus.length; i++)
            if (this.classesStatus[i] == -1)
                this.unusedClasses++;

        for (int i = 0; i < this.idsStatus.length; i++)
            if (this.idsStatus[i] == -1)
                this.unusedIDs++;

        System.out.println("[+] found " + this.unusedClasses + " class and " + this.unusedIDs + " ID unused definitions.");
    }
}
