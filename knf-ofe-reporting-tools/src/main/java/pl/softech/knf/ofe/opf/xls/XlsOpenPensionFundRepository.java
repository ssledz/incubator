package pl.softech.knf.ofe.opf.xls;

import com.google.inject.assistedinject.Assisted;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import pl.softech.knf.ofe.InjectLogger;
import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.OpenPensionFundRepository;
import pl.softech.knf.ofe.shared.jdbc.DataAccessException;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.softech.knf.ofe.shared.xls.XlsUtils.loadOrCreateWorkbook;
import static pl.softech.knf.ofe.shared.xls.XlsUtils.loadWorkbook;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class XlsOpenPensionFundRepository implements OpenPensionFundRepository {

    @InjectLogger
    protected Logger logger;

    private final Set<DataProvider> dataProviders;
    private final Set<XlsWritter> writers;
    private final File xlsFile;

    @Inject
    public XlsOpenPensionFundRepository(final @Assisted File xlsFile, Set<DataProvider> dataProviders, Set<XlsWritter> writers) {
        this.xlsFile = xlsFile;
        this.dataProviders = dataProviders;
        this.writers = writers;
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

        Workbook wb = loadOrCreateWorkbook(xlsFile);

        try (FileOutputStream out = new FileOutputStream(xlsFile)) {

            for (XlsWritter writer : writers) {
                writer.write(opfs, wb);
            }

            wb.write(out);
        } catch (final Exception e) {
            logger.error("", e);
            new DataAccessException(e);
        }

    }
}
