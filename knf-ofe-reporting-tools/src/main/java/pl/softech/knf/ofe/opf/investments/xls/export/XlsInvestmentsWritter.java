package pl.softech.knf.ofe.opf.investments.xls.export;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.investments.Instrument;
import pl.softech.knf.ofe.opf.investments.jdbc.InstrumentRepository;
import pl.softech.knf.ofe.opf.xls.AbstractXlsWritter;
import pl.softech.knf.ofe.opf.xls.XlsWritter;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class XlsInvestmentsWritter implements XlsWritter {

    private static Map<Instrument, Instrument> instrument2instrument = new HashMap<>();
    private static Map<Instrument, Set<Instrument>> instrument2Set = new HashMap<>();

    static {
        map(
                instrument(
                        "obligacje, bony i inne papiery wartościowe, emitowane przez Skarb Państwa lub Narodowy Bank Polski, a także " +
                                "pożyczki i kredyty udzielonych tym podmiotom"
                ),
                to("ae7ce6167d6135dc94fc925ce02f5fe0", "Dłużne skarbowe"),
                to("83067d03e972277b58a368667cd068e4", "1. Obligacje, bony i inne papiery wartościowe emitowane przez Skarb Państwa, NBP," +
                        " a także pożyczki i kredyty udzielane tym podmiotom"),
                to("6869b10efffeec59fd859a3c7f48640f", "Obligacje"),
                to("7c04685f5c82255f015d569ab73a2610", "Bony skarbowe"),
                to("bb5010a9054ec73131b5b4676b0e15e1", "Obligacje, bony skarbowe")


        );

        map(
                instrument(
                        "obligacje i inne dłużne papiery wartościowe, opiewające na świadczenia pieniężne, gwarantowane lub poręczne " +
                                "przez Skarb Państwa albo Narodowy Bank Polski, a także depozyty, kredyty i pożyczki gwarantowane i " +
                                "poręczane przez te podmioty"
                ),
                to("fd2221b687890a4e0d7904f980fb289a", "Dłużne gwarantowane lub poręczone przez SP lub bank centralny"),
                to("fd669e6cefe754c572c511f1ccb928db", "3. Obligacje i inne dłużne papiery wartościowe, opiewające na świadczenia " +
                        "pieniężne, gwarantowane lub poręczane przez Skarb Państwa albo NBP, a także depozyty, kredyty i pożyczki " +
                        "gwarantowane lub poręczane przez te podmioty"),
                to("11582b44b24a6b9f42a2abca5707a345", "Dłużne p.w. gwarantowane lub poręczane przez SP lub NBP")
        );

        map(
                instrument(
                        "depozyty bankowe i bankowe papiery wartościowe, w walucie polskiej"
                ),
                to("50b01c6c044d7d520c8b815a773593b3", "Depozyty i bankowe papiery wartościowe w walucie krajowej"),
                to("a8382e9eaec58f616c2bbdea5e0f31b6", "5. Depozyty bankowe w walucie polskiej"),
                to("8a5aebcd656825573d5c049478201d22", "Depozyty bankowe i bankowe papiery wartościowe"),
                to("b1b970002619a0d94e481cf1404979f8", "Depozyty, bankowe p.w.\n(w walucie polskiej)"),
                to("6d5ee716d07c18c1840f92baee657833", "Depozyty, bankowe p.w.")
        );

        map(
                instrument(
                        "depozyty bankowe i bankowe papiery wartościowe, w walutach państw będących członkami OECD oraz innych państw, z " +
                                "którymi Rzeczypospolita Polska zawarła umowy o popieraniu i wzajemnej ochronie inwestycji, z tym że " +
                                "waluty te nie mogą być nabywane wyłącznie w celu rozliczenia bieżących zobowiązań funduszu"
                ),
                to("73e2a3c8c2b0ac9380689f06d59534aa", "6. Depozyty bankowe denominowane w walutach państw UE, EOG i OECD"),
                to("607f7edd2e139f9e5c4bbf84dcb9c5cb", "Depozyty, bankowe p.w. (w walutach państw obcych)"),
                to("fde741d09c0a2e3b208765e2104702f5", "Depozyty, bankowe p.w.\n(w walucie obcej)")
        );

        map(
                instrument(
                        "akcje spółek notowanych na regulowanym rynku giełdowym, a także notowane na regulowanym rynku giełdowym prawa " +
                                "poboru, prawa do akcji oraz obligacje zamienne na akcje"
                ),
                to("34bedd579f4e289642bfa5a203f13dcb", "Akcje i prawa z nimi związane spółek notowanych na regulowanym rynku giełdowym"),
                to("63f571a7bb76695e2cdf959b65ef377e", "7. Akcje spółek notowanych na krajowym rynku regulowanym oraz obligacje zamienne " +
                        "na akcje tych spółek, a także notowane na tym rynku prawa poboru i prawa do akcji"),
                to("c8a4dcaef31917e8e926ae8740352e48", "Akcje spółek notowanych na regulowanym rynku giełdowym"),
                to("55cbce26e6bc425594a9b9d6447fe288", "Akcje  notowane na regulowanym rynku giełdowym")
        );

        map(
                instrument(
                        "akcje spółek notowanych na regulowanym rynku pozagiełdowym lub zdematerializowane zgodnie z przepisami ustawy z " +
                                "dnia 29 lipca 2005 r. o obrocie instrumentami finansowym akcje spółek niebędących przedmiotem obrotu na " +
                                "rynku regulowanym, a także notowane na regulowanym rynku pozagiełdowym lub zdematerializowane, lecz " +
                                "nienotowane na rynku regulowanym prawa poboru, prawa do akcji oraz obligacji zamiennych na akcje tych " +
                                "spółek"
                ),
                to("da9b39fcf3e320d6e3b14d960079c276", "Akcje i prawa z nimi związane spółek notowanych na regulowanym rynku " +
                        "pozagiełdowym lub nienotowane"),
                to("9fe9d0d11d3324fdc5e6e63ff8e06e3d", "8. Akcje, prawa poboru i prawa do akcji będące przedmiotem krajowej oferty " +
                        "publicznej, nienotowane na rynku regulowanym"),
                to("d1eda75524abb7efe341df2806bdf3dc", "Akcje  notowane na regulowanym rynku pozagiełdowym, nie notowane dopuszczone do " +
                        "publicznego obrotu"),
                to("14f8e84b91b9cbfa8e5ed0a3486218f7", "Akcje  notowane na regulowanym rynku pozagiełdowym, nie notowane " +
                        "zdematerializowane"),
                to("b6f60f5cc3120d54fb80b1c6ac1d608d", "Akcje  notowane na regulowanym rynku pozagiełdowy, nie notowane dopuszczone do " +
                        "publicznego obrotu")
        );

        map(
                instrument(
                        "certyfikaty inwestycyjne emitowane przez fundusze inwestycyjne zamknięte"
                ),
                to("20d5208a21920883a68e753ed5be076d", "Certyfikaty inwestycyjne"),
                to("3e4103ce7a46135795b125558f34ca65", "11. Certyfikaty inwestycyjne emitowane przez fundusze inwestycyjne zamknięte"),
                to("a3400907719f99bfbfb16649b00c3e2f", "Certyfikaty inwestycyjne funduszy inwestycyjnych")
        );

        map(
                instrument(
                        "obligacje i inne dłużne papiery wartościowe, emitowane przez jednostki samorządu terytorialnego, ich związki lub" +
                                " miasto stołeczne Warszawa, zdematerializowane zgodnie z przepisami ustawy, o której mowa w  pkt. 5"
                ),
                to("6986d27447c295596a0b55582d4c0c0c", "Dłużne komunalne, zdematerializowane"),
                to("60866c3b90b2d91d3cdfd71a59cb8829", "15. Obligacje i inne dłużne papiery wartościowe emitowane przez jednostki " +
                        "samorządu terytorialnego lub ich związki, będące przedmiotem krajowej oferty publicznej"),
                to("6afabcca5d19ddb5f4808aa9700a32f6", "Dłużne pozaskarbowe p.w.  dopuszczone do publicznego obrotu"),
                to("7f9389b6b03a093b5ac1a6078325bb68", "Dłużne zdematerializowane pozaskarbowe p.w.")
        );

        map(
                instrument(
                        "inne niż zdematerializowane obligacje i inne dłużne papiery wartościowe emitowane przez jednostki samorządu " +
                                "terytorialnego, ich związki lub miasto stołeczne Warszawa"
                ),
                to("76b6d8b383dceda1808d408cd8ac1590", "Dłużne komunalne, niezdematerializowane"),
                to("f3b80e97790ef26e50988fb831758b19", "17. Inne niż będące przedmiotem oferty publicznej obligacje i inne dłużne papiery" +
                        " wartościowe emitowane przez jednostki samorządu terytorialnego lub ich związki"),
                to("4f793716a0008f0195c0dbb9c25d036f", "Dłużne pozaskarbowe p.w. nie dopuszczone do publicznego obrotu"),
                to("c0a873bfcdb674e56b7e1969805e078e", "Inne pozaskarbowe p.w.")
        );

        map(
                instrument(
                        "obligacje przychodowe, o których mowa w ustawie z dnia 29 czerwca 1995 r. o obligacjach (Dz. U. z 2001 r. Nr " +
                                "120, poz. 1300, z późn. zm.)"
                ),
                to("b6bb1a6ddc57857e8cccf0cf71757c78", "Obligacje przychodowe"),
                to("626a3b2c07eb9729d24546c41d33eee5", "19. Krajowe obligacje przychodowe")
        );

        map(
                instrument(
                        "zdematerializowane zgodnie z przepisami ustawy, o której mowa w pkt 5, obligacje  emitowane przez inne podmioty " +
                                "niż jednostki samorządu terytorialnego, ich związki, miasto stołeczne Warszawa, które zostały " +
                                "zabezpieczone w wysokości odpowiadającej wartości nominalnej wraz z ewentualnym oprocentowaniem"
                ),
                to("5625ef3d64d1047bb11d2f352121f290", "Obligacje całkowicie zabezpieczone, zdematerializowane"),
                to("d88635a124b856719c1d5ee71a6ce25d", "21. Obligacje emitowane przez inne podmioty niż jednostki samorządu " +
                        "terytorialnego lub ich związki, które zostały zabezpieczone w wysokości odpowiadającej pełnej wartości " +
                        "nominalnej i ewentualnemu oprocentowaniu, będące przedmiotem krajowej oferty publicznej"),
                to("366e2e2bd32fd28d7df4aaee27082cd3", "Dłużne pozaskarbowe p.w. zabezpieczone całkowicie dopuszczone do publicznego " +
                        "obrotu"),
                to("ff98b726e73870aace006ca9c0c6cd4a", "Dłużne zdematerializowane pozaskarbowe p.w. zabezpieczone całkowicie")
        );

        map(
                instrument(
                        "inne niż zdematerializowane obligacje i inne dłużne papiery wartościowe emitowane przez inne podmioty niż " +
                                "jednostki samorządu terytorialnego, ich związki, miasto stołeczne Warszawa, które zostały zabezpieczone " +
                                "w wysokości odpowiadającej wartości nominalnej wraz z ewentualnym oprocentowaniem"
                ),
                to("74a40940dcbcd354a27404fa058f09eb", "Dłużne całkowicie zabezpieczone, niezdematerializowane"),
                to("51fedccc9b58ff0238de5a42aeeb5aa5", "22. Inne niż będące przedmiotem krajowej oferty publicznej obligacje i inne " +
                        "dłużne papiery wartościowe emitowane przez podmioty inne niż jednostki samorządu terytorialnego lub ich związki," +
                        " mające siedzibę w kraju, które zostały zabezpieczone w wysokości odpowiadającej pełnej wartości nominalnej i " +
                        "ewentualnemu oprocentowaniu"),
                to("7e4bd5823d53d2e73d08e6e6684313a2", "Dłużne pozaskarbowe p.w. zabezpieczone całkowicie nie dopuszczone do publicznego " +
                        "obrotu"),
                to("c54bb6a90d7642a4182bf0319a44b0c4", "Dłużne pozaskarbowe p.w. zabezpieczone całkowicie nie zdematerializowane")
        );

        map(
                instrument(
                        "obligacje i inne dłużne papiery wartościowe emitowane przez spółki publiczne, inne niż papiery wartościowe, o " +
                                "których mowa w pkt 11 i 12"
                ),
                to("a0c4a80838fe8c699d979d27b8425c27", "Dłużne spółek publicznych"),
                to("465e35946949da4fde96105deb6b91d0", "25. Obligacje i inne dłużne papiery wartościowe emitowane przez spółki notowane " +
                        "na krajowym rynku regulowanym, inne niż papiery wartościowe w pkt. 21 i 22"),
                to("d59baab93d9a1af9f743b894338f30a2", "Inne dłużne p.w. spółek publicznych")
        );

        map(
                instrument(
                        "dematerializowane zgodnie z przepisami ustawy, o której mowa w pkt 5, obligacje i inne dłużne papiery " +
                                "wartościowe, inne niż w pkt 9 i 11"
                ),
                to("62deb72100a8d0c49b26b74a748fd0c7", "Inne dłużne, zdematerializowane"),
                to("fd19a4290faeb0b3315bff7396752a85", "26. Obligacje i inne dłużne papiery wartościowe będące przedmiotem krajowej " +
                        "oferty publicznej, inne niż papiery wartościowe w pkt. 15 i 21"),
                to("d64bb2aa38f63ba735def7d20bdc8ae7", "Inne dłużne p.w. dopuszczone do publicznego obrotu"),
                to("e67ffbafd840c7d58d4f7315b6fda79c", "Inne dłużne zdematerializowane p.w."),
                to("74a9657a56a8f6cba5b31fa3b3d0e4f7", "Inne dłużne p.w.")
        );

        map(
                instrument(
                        "listy zastawne"
                ),
                to("478f7281e520883da7ecbe8b1c70285e", "Listy zastawne"),
                to("5813913bba51f18857f0b97839c62f20", "29. Krajowe listy zastawne")
        );

        map(
                instrument(
                        "obligacje emitowane przez Bank Gospodarstwa Krajowego na zasadach określonych w ustawie z dnia 27 października " +
                                "1994 r. o autostradach płatnych oraz o Krajowym Funduszu Drogowym"
                ),
                to("0490d298ae16a291f84b5d79f7f5e253", "Obligacje autostradowe"),
                to("ad635bd671957c9775e50f645759d461", "33. Obligacje emitowane przez BGK na zasadach określonych w ustawie o " +
                        "autostradach płatnych oraz o Krajowym Funduszu Drogowym"),
                to("33e2e8ef6c3665a1be8537dd4dbb25dc", "Obligacje BGK autostrady KFD")
        );

        map(
                instrument(
                        "Inne zagraniczne papiery wartościowe"
                ),
                to("485bc36af93abb45e04d5c98765fe1cf", "Inne inwestycje zagraniczne"),
                to("708c34f1d9f95bce76674e7fd387e941", "36. Inne zagraniczne papiery wartościowe"),
                to("3db089d99ca766e8f2c719004c6a46af", "Inwestycje za granicą"),
                to("422e3452770e8bab979169aa2b39d22c", "Inne zagraniczne p.w.")
        );

    }

    private final InstrumentRepository instrumentRepository;

    @Inject
    public XlsInvestmentsWritter(InstrumentRepository instrumentRepository) {
        this.instrumentRepository = instrumentRepository;
    }

    @Override
    public void write(List<OpenPensionFund> funds, Workbook wb) {

        new SumOfInvestments().write(funds, wb);

        instrumentRepository.findAll()
                .stream()
                .map(instrument -> {
                    if (instrument2instrument.containsKey(instrument)) {
                        Instrument tmp = instrument2instrument.get(instrument);
                        return new Instrument(instrument.getId(), tmp.getIdentifier(), tmp.getName(), null);
                    }
                    return instrument;
                })
                .collect(Collectors.toSet())
                .stream()
                .forEach(instrument -> new InvestmentByInstrument(instrument).write(funds, wb));

    }

    private static Instrument to(String identifer, String name) {
        return new Instrument(identifer, name.trim(), null);
    }

    private static Instrument instrument(String name) {
        return new Instrument(name.trim(), name.trim(), null);
    }

    private static void map(Instrument instrument, Instrument... to) {
        instrument2Set.put(instrument, new HashSet<>(Arrays.asList(to)));
        for (Instrument t : to) {
            instrument2instrument.put(t, instrument);
        }
    }

    private static class SumOfInvestments extends AbstractXlsWritter {

        public SumOfInvestments() {
            this.secondColumnName = "Total sum of investments for a given open pension fund";
            this.sheetName = "Investments - total";
        }

        @Override
        protected void writeCell(Cell cell, OpenPensionFund fund) {

            long sum = fund.getInvestmens()
                    .stream()
                    .mapToLong(inv -> inv.getValue())
                    .sum();

            cell.setCellValue((float) sum / 100.0);

        }
    }

    private static class InvestmentByInstrument extends AbstractXlsWritter {

        private final Instrument instrument;

        public InvestmentByInstrument(Instrument instrument) {
            this.instrument = instrument;
            this.secondColumnName = instrument.getName();
            this.sheetName = "Investment By instrument " + instrument.getId();
        }

        @Override
        protected void writeCell(Cell cell, OpenPensionFund fund) {

            Optional<Long> value;

//            long sum = fund.getInvestmens()
//                    .stream()
//                    .mapToLong(inv -> inv.getValue())
//                    .sum();

            if (!instrument2Set.containsKey(instrument)) {

                value = fund.getInvestmens()
                        .stream()
                        .filter(inv -> inv.getInstrument().equals(instrument))
                        .map(inv -> inv.getValue())
                        .findFirst();
            } else {

                Set<Instrument> synonyms = instrument2Set.get(instrument);

                value = Optional.of(
                        fund.getInvestmens()
                                .stream()
                                .filter(inv -> synonyms.contains(inv.getInstrument()))
                                .mapToLong(inv -> inv.getValue())
                                .sum()
                );

            }


            if (value.isPresent()) {
                cell.setCellValue((float) value.get() / 100.0);
//                if (sum > 0) {
//                    cell.setCellValue((float) value.get() / (float) sum * 100.0);
//                } else {
//                    cell.setCellValue(0);
//                }
            } else {
                cell.setCellValue(0);
            }


        }
    }
}
