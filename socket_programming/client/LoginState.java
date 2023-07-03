package client;

class LoginState implements CLIState {
    private CLI cli;

    LoginState(CLI _cli) {
        this.cli = _cli;
    }

    @Override
    public void showPrompt() {
        String prompt = ClientLoader.BLUE_ANSI + "\ncis | login\n" + ClientLoader.RESET_ANSI
                + "---------------\n"
                + "Enter credentials: <username> <password>\n";
        System.out.println(prompt);
    }

    @Override
    public void succeed() {
        LoggedinSession.out_success_msg("you are logged in!");
        this.cli.setState(this.cli.getLoggedinState());
    }

    @Override
    public void failed(String _cause) {
        System.out.println(_cause);
        this.cli.setState(this.cli.getInitialState());
    }

}