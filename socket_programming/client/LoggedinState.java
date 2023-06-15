package client;

class LoggedinState implements CLIState {
    private CLI cli;

    LoggedinState(CLI _cli) {
        this.cli = _cli;
    }

    @Override
    public void showPrompt() {
        System.out.println("loggedin state");
    }

    @Override
    public void logout() {
        this.cli.setState(this.cli.getInitialState());
    }

}