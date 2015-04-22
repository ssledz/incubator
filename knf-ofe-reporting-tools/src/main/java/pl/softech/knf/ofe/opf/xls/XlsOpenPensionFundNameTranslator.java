package pl.softech.knf.ofe.opf.xls;

import pl.softech.knf.ofe.opf.OpenPensionFundNameTranslator;

/**
 * Created by ssledz on 23.04.15.
 */
public class XlsOpenPensionFundNameTranslator implements OpenPensionFundNameTranslator {

    @Override
    public String translate(String name) {

        name = name.replace("*)", "")
                .replace("„", "\"")
                .replace("”", "\"")
                .replace("(w likwidacji)", "")
                .replaceAll("\\s+", " ");

        return name.trim();
    }

    public static void main(String[] args) {
        XlsOpenPensionFundNameTranslator instance = new XlsOpenPensionFundNameTranslator();
        System.out.println("OFE  PZU \"Złota Jesień”\n".equals("OFE PZU \"Złota Jesień\"\n"));
        System.out.println(instance.translate("OFE  PZU \"Złota Jesień”\n").equals(instance.translate("OFE PZU \"Złota Jesień\"\n")));

        System.out.println("OFE \"DOM\"\n".equals("OFE \"DOM”\n"));
        System.out.println(instance.translate("").equals(instance.translate("")));

        System.out.println("OFE \"DOM\"\n".equals("OFE \"DOM”\n"));
        System.out.println(instance.translate("").equals(instance.translate("")));
    }

}
