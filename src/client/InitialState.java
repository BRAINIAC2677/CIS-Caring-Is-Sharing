package client;

class InitialState implements CLIState {
    private CLI cli;

    InitialState(CLI _cli) {
        this.cli = _cli;
    }

    @Override
    public void showPrompt() {
        String prompt = ClientLoader.BLUE_ANSI + "\ncis | caring is sharing\n" + ClientLoader.RESET_ANSI
                + "------------------------\n"
                + "Do you want to login?(type login)\n" + "Do you want to register?(type reg)\n"
                + "Do you want to quit?(press q/Q)";
        System.out.println(prompt);
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