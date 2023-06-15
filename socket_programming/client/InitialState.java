package client;

class InitialState implements CLIState {
    private CLI cli;

    InitialState(CLI _cli) {
        this.cli = _cli;
    }

    @Override
    public void showPrompt() {
        System.out.println("initial state");
    }

    @Override
    public void login() {
        this.cli.setState(this.cli.getLoginState());
    }

    @Override
    public void register() {
        this.cli.setState(this.cli.getRegisterState());
    }

}