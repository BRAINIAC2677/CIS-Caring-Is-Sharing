package client;

class RegisterState implements CLIState {
    private CLI cli;

    RegisterState(CLI _cli) {
        this.cli = _cli;
    }

    @Override
    public void showPrompt() {
        System.out.println("register state");
    }

    @Override
    public void rightCredentials() {
        this.cli.setState(this.cli.getLoggedinState());
    }

    @Override
    public void wrongCredentials() {
        this.cli.setState(this.cli.getRegisterState());
    }
}