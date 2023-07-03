package client;

import java.util.HashMap;

class Manual {
    public static final String lsum = "LSUM(1)                          General Commands Manual                          LSUM(1)\n\n"
            +
            "NAME\n" +
            "----\n" +
            "       lsum - list unread messages\n\n" +
            "SYNOPSIS\n" +
            "--------\n" +
            "       lsum\n\n" +
            "DESCRIPTION\n" +
            "-----------\n" +
            "       List all the unread messages of the user. Unread messages contain file requests from other users and file uploads against file request of the user.\n\n"
            +
            "AUTHOR\n" +
            "------\n" +
            "       This  manual  page  was  written  by  Asif Azad.\n\n"
            +
            "COPYRIGHT\n" +
            "---------\n" +
            "       Copyright (C) 1992 Free Software Foundation, Inc.  This is free software; see the source for copying conditions.  There is NO warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.\n";

    public static final String up = "UP(1)                          General Commands Manual                          UP(1)\n\n"
            +
            "NAME\n" +
            "----\n" +
            "       up - upload a file from local storage to remote storage.\n\n" +
            "SYNOPSIS\n" +
            "--------\n" +
            "       up <absolute_file_path_in_local_storage> <file_name_in_remote_storage> public|private|<file_request_id> \n\n"
            +
            "DESCRIPTION\n" +
            "-----------\n" +
            "       upload any type of file from local storage to remote storage.\n\n"
            +
            "AUTHOR\n" +
            "------\n" +
            "       This  manual  page  was  written  by  Asif Azad.\n\n"
            +
            "COPYRIGHT\n" +
            "---------\n" +
            "       Copyright (C) 1992 Free Software Foundation, Inc.  This is free software; see the source for copying conditions.  There is NO warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.\n";

    public static final String down = "DOWN(1)                          General Commands Manual                          DOWN(1)\n\n"
            +
            "NAME\n" +
            "----\n" +
            "       down - download a file to local storage from remote storage.\n\n" +
            "SYNOPSIS\n" +
            "--------\n" +
            "       down <absolute_file_directory_in_local_storage> <file_name_in_local_storage> <relative_file_name_in_remote_storage>|<public_file_id> -o|-p \n\n"
            +
            "DESCRIPTION\n" +
            "-----------\n" +
            "       download a file to local storage from remote storage..\n\n"
            +
            "OPTIONS\n" +
            "-------\n" +
            "       -p \n" +
            "              If the -p option is given as last argument, then the third argument is considered as <public_file_id> of public files.(maybe of uploaded by other users) \n\n"
            +
            "       -o \n" +
            "              If the -o option is given as last argument, then the third argument is considered as <relative_file_name_in_remote_storage>. \n\n"
            +
            "AUTHOR\n" +
            "------\n" +
            "       This  manual  page  was  written  by  Asif Azad.\n\n"
            +
            "COPYRIGHT\n" +
            "---------\n" +
            "       Copyright (C) 1992 Free Software Foundation, Inc.  This is free software; see the source for copying conditions.  There is NO warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.\n";

    private static HashMap<String, String> man_pages = new HashMap<String, String>();

    public static void show_man(String[] _splitted_input) {
        init_manual();
        if (_splitted_input.length == 1) {
            System.out.println(ClientLoader.YELLOW_ANSI + cat_manual() + ClientLoader.RESET_ANSI);
        } else if (_splitted_input.length == 2 && man_pages.containsKey(_splitted_input[1])) {
            System.out.println(ClientLoader.YELLOW_ANSI + man_pages.get(_splitted_input[1]) + ClientLoader.RESET_ANSI);
        } else {
            LoggedinSession.out_error_msg("unknown command.");
        }
    }

    private static void init_manual() {
        man_pages.put("lsum", lsum);
        man_pages.put("up", up);
        man_pages.put("down", down);
        // todo: add other man_pages
    }

    private static String cat_manual() {
        return lsum + "\n\n" + up + "\n\n" + down;
    }

    String bashManual = "BASH(1)                          General Commands Manual                          BASH(1)\n\n"
            +
            "NAME\n" +
            "----\n" +
            "       bash - GNU Bourne-Again SHell\n\n" +
            "SYNOPSIS\n" +
            "--------\n" +
            "       bash [options] [file]\n\n" +
            "DESCRIPTION\n" +
            "-----------\n" +
            "       Bash  is  an sh-compatible command language interpreter that executes commands read from the standard input or from a file.  Bash also incorporates useful features from the Korn and C shells (ksh and csh).\n\n"
            +
            "       Bash is intended to be a conformant implementation of the Shell and Utilities portion of the IEEE POSIX specification (IEEE Standard 1003.1).  Bash can be configured to be POSIX-conformant by default.\n\n"
            +
            "OPTIONS\n" +
            "-------\n" +
            "       -c string\n" +
            "              If the -c option is present, then commands are read from string.  If there are arguments after the string, they are assigned to the positional parameters, starting with $0.\n\n"
            +
            "AUTHOR\n" +
            "------\n" +
            "       This  manual  page  was  written  by  Asif Azad.\n\n"
            +
            "COPYRIGHT\n" +
            "---------\n" +
            "       Copyright (C) 1992 Free Software Foundation, Inc.  This is free software; see the source for copying conditions.  There is NO warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.\n";

}