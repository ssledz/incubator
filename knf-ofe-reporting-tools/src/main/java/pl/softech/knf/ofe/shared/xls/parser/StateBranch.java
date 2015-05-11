package pl.softech.knf.ofe.shared.xls.parser;

import org.apache.poi.ss.usermodel.Row;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class StateBranch extends AbstractState {

    private final State nextState, parsingFailedState;

    public StateBranch(StateContext context, State nextState, State parsingFailedState) {
        super(context);
        this.nextState = nextState;
        this.parsingFailedState = parsingFailedState;
    }

    @Override
    public void parse(Row row) {

        nextState.parse(row);

        if (context.isParsingFailed()) {
            context.setState(parsingFailedState);
            parsingFailedState.parse(row);
        }

    }
}
