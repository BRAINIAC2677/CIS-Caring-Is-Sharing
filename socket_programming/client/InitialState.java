package client;

class InitialState implements CLIState {
    private CLI cli;

    InitialState(CLI _cli) {
        this.cli = _cli;
    }

    @Override
    public void showPrompt() {
        String prompt = "cis | caring is sharing\nDo you want to login?(press l/L)\nDo you want to register?(press r/R)\nDo you want to quit?(press q/Q)";
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