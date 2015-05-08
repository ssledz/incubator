package pl.softech.knf.ofe.opf.contributions.xls.imp;

import java.util.Date;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class ContributionParsingEventListenerAdapter implements ContributionParsingEventListener {
    @Override
    public void date(Date date) {

    }

    @Override
    public void header(String[] columns) {

    }

    @Override
    public void record(String name, double amount, double interests, long number, double averageBasis) {

    }

    @Override
    public void total(double amount, double interests, long number, double averageBasis) {

    }
}
