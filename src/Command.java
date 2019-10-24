import org.apache.commons.cli.*;

public class Command {

    public Options options;

    public Command() {

        Option message = Option.builder("m")
                .desc("Send a message to a topic")
                .argName("mBody")
                .required(false)
                .hasArg()
                .build();
        Option topic = Option.builder("t")
                .desc("Select a topic")
                .argName("tName")
                .required(false)
                .hasArg()
                .build();
        Option list = Option.builder("l")
                .desc("List topics")
                .argName("lTopics")
                .required(false)
                .hasArg()
                .build();
        Option user = Option.builder("u")
                .desc("Select a user")
                .argName("User")
                .required(false)
                .hasArg()
                .build();

        options = new Options();
        options.addOption(message);
        options.addOption(topic);
        options.addOption(list);
        options.addOption(user);

    }

    public CommandLine parse(String args[]) throws ParseException {

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = null;

        try {
            commandLine = parser.parse(options, args);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        return commandLine;
    }
}
