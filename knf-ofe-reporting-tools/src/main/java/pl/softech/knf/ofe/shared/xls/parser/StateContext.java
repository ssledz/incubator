package pl.softech.knf.ofe.shared.xls.parser;

import org.apache.poi.ss.usermodel.Row;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class StateContext implements State {

    private State state;

    @Override
    public void parse(final Row row) {
        state.parse(row);
    }

    public void setState(final State state) {
        this.state = state;
    }


}
