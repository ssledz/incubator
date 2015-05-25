package pl.softech.knf.ofe.opf.investments.jdbc;

import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.investments.Investment;
import pl.softech.knf.ofe.opf.jdbc.OpenPensionFundRowMapper;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class InvestmentsRowMapper implements OpenPensionFundRowMapper {

    private final InvestmentRepository investmentRepository;

    @Inject
    public InvestmentsRowMapper(InvestmentRepository investmentRepository) {
        this.investmentRepository = investmentRepository;
    }

    @Override
    public void mapRow(ResultSet rs, OpenPensionFund.Builder builder) throws SQLException {
        List<Investment> investments = investmentRepository.findByOpenPensionFundId(rs
                .getLong("id"));
        builder.addInvestments(investments);
    }
}
