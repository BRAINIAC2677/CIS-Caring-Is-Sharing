package client;

class LoginState implements CLIState {
    private CLI cli;

    LoginState(CLI _cli) {
        this.cli = _cli;
    }

    @Override
    public void showPrompt() {
        System.out.println("login state");
    }

    @Override
    public void rightCredentials() {
        this.cli.setState(this.cli.getLoggedinState());
    }

    @Override
    public void wrongCredentials() {
        this.cli.setState(this.cli.getLoginState());
    }

}