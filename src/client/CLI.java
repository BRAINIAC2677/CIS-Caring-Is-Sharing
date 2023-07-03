package client;

import util.*;

class CLI {
    private User currentUser;
    private InitialState initialState;
    private LoginState loginState;
    private RegisterState registerState;
    private LoggedinState loggedinState;
    private CLIState state;

    CLI() {
        this.currentUser = null;
        this.initialState = new InitialState(this);
        this.loginState = new LoginState(this);
        this.registerState = new RegisterState(this);
        this.loggedinState = new LoggedinState(this);
        this.setState(this.initialState);
    }

    void setCurrentUser(User _currentUser) {
        this.currentUser = _currentUser;
    }

    User getCurrentUser() {
        return this.currentUser;
    }

    void update() {
        this.setState(this.state);
    }

    void update(String _prompt) {
        System.out.println(_prompt);
        this.setState(this.state);
    }

    void setState(CLIState _state) {
        this.state = _state;
        this.state.showPrompt();
    }

    CLIState getState() {
        return this.state;
    }

    InitialState getInitialState() {
        return this.initialState;
    }

    LoginState getLoginState() {
        return this.loginState;
    }

    RegisterState getRegisterState() {
        return this.registerState;
    }

    LoggedinState getLoggedinState() {
        return this.loggedinState;
    }

    void login() {
        this.state.login();
    }

    void register() {
        this.state.register();
    }

    void succeed() {
        this.state.succeed();
    }

    void failed(String _cause) {
        this.state.failed(_cause);
    }

    void failed() {
        this.state.failed("unknown cause.");
    }

    void logout() {
        this.state.logout();
    }

    void unknownCommand() {
        this.state.unknownCommand();
    }
}