package pl.softech.knf.ofe.opf.accounts.xls;

import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.OpenPensionFundRepository;

import java.util.List;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class XlsAccountsRepository implements OpenPensionFundRepository  {

    @Override
    public List<OpenPensionFund> findAll() {
        return null;
    }

    @Override
    public void save(List<OpenPensionFund> opfs) {

    }
}
