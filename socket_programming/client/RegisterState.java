package client;

class RegisterState implements CLIState {
    private CLI cli;

    RegisterState(CLI _cli) {
        this.cli = _cli;
    }

    @Override
    public void showPrompt() {
        String prompt = "cis | register\nEnter credentials\n<username> <password>\n";
        System.out.println(prompt);
    }

    @Override
    public void succeed() {
        System.out.println("successful registration! you are logged in!");
        this.cli.setState(this.cli.getLoggedinState());
    }

    @Override
    public void failed(String _cause) {
        System.out.println(_cause);
        this.cli.setState(this.cli.getInitialState());
    }
}