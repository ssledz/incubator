package pl.softech.knf.ofe.shared.xls.parser;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public abstract class AbstractState implements State {

    protected final StateContext context;

    protected AbstractState(final StateContext context) {
        this.context = context;
    }

}
