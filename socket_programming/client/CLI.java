package client;

class CLI {
    private InitialState initialState;
    private LoginState loginState;
    private RegisterState registerState;
    private LoggedinState loggedinState;
    private CLIState state;

    CLI() {
        this.initialState = new InitialState(this);
        this.loginState = new LoginState(this);
        this.registerState = new RegisterState(this);
        this.loggedinState = new LoggedinState(this);
        this.setState(this.initialState);
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

    void rightCredentials() {
        this.state.rightCredentials();
    }

    void wrongCredentials() {
        this.state.wrongCredentials();
    }

    void logout() {
        this.state.logout();
    }

    void unknownCommand() {
        this.state.unknownCommand();
    }
}