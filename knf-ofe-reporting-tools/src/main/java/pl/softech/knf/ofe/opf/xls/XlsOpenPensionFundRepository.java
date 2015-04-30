package pl.softech.knf.ofe.opf.xls;

import com.google.inject.assistedinject.Assisted;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import pl.softech.knf.ofe.opf.DataProvider;
import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.OpenPensionFundRepository;
import pl.softech.knf.ofe.shared.xls.PoiException;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class XlsOpenPensionFundRepository implements OpenPensionFundRepository {

    private final Set<DataProvider> dataProviders;
    private final File xlsFile;

    @Inject
    public XlsOpenPensionFundRepository(final @Assisted File xlsFile, Set<DataProvider> dataProviders) {
        this.xlsFile = xlsFile;
        this.dataProviders = dataProviders;
    }

    private static Workbook loadWorkbook(final File xlsFile) {
        try (final InputStream inp = new FileInputStream(xlsFile)) {
            return WorkbookFactory.create(inp);
        } catch (final Exception e) {
            throw new PoiException(e);
        }
    }

    @Override
    public List<OpenPensionFund> findAll() {

        Map<OpenPensionFund.Key, OpenPensionFund.Builder> key2fund = new HashMap<>();

        Workbook wb = loadWorkbook(xlsFile);

        for (DataProvider provider : dataProviders) {
            provider.iterator(wb).forEachRemaining(applier -> {
                OpenPensionFund.Key key = applier.key();
                OpenPensionFund.Builder builder = key2fund.get(key);
                if (builder == null) {
                    builder = new OpenPensionFund.Builder().withKey(key);
                    key2fund.put(key, builder);
                }
                applier.populate(builder);
            });


        }

        return key2fund.values()
                .stream()
                .map(builder -> new OpenPensionFund(builder))
                .collect(Collectors.toList());
    }

    @Override
    public void save(List<OpenPensionFund> opfs) {

    }
}
