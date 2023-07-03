package client;

import util.*;

class LoggedinState implements CLIState {
    private CLI cli;

    LoggedinState(CLI _cli) {
        this.cli = _cli;
    }

    @Override
    public void showPrompt() {
        User currentUser = this.cli.getCurrentUser();
        if (currentUser != null) {
            String prompt = ClientLoader.BLUE_ANSI + currentUser.get_username() + "@cis:~"
                    + currentUser.getWorking_directory()
                    + ClientLoader.RESET_ANSI;
            System.out.println(prompt);
        }
    }

    @Override
    public void logout() {
        this.cli.setState(this.cli.getInitialState());
    }

}