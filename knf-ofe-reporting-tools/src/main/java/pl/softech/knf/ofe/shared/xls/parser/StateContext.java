package pl.softech.knf.ofe.shared.xls.parser;

import org.apache.poi.ss.usermodel.Row;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class StateContext implements State {

    private State state;

    private boolean parsingFailed;

    private int startCellIndex;

    public int getStartCellIndex() {
        return startCellIndex;
    }

    public void setStartCellIndex(int startCellIndex) {
        this.startCellIndex = startCellIndex;
    }

    public void setParsingFailed(boolean parsingFailed) {
        this.parsingFailed = parsingFailed;
    }

    public boolean isParsingFailed() {
        return parsingFailed;
    }

    @Override
    public void parse(final Row row) {
        state.parse(row);
    }

    public void setState(final State state) {
        this.state = state;
    }


}
