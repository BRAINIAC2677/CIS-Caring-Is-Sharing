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
            String prompt = currentUser.getUsername() + "@cis:~" + currentUser.getWorkingDir();
            System.out.println(prompt);
        }
    }

    @Override
    public void logout() {
        this.cli.setState(this.cli.getInitialState());
    }

}