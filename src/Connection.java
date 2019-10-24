import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Connection extends Thread{

    Socket client;
    public int puerto = 9500;
    public static Vector<Connection> connectionList = new Vector<>();
    public static List<Topic> topicList = new ArrayList<>();
    DataInputStream inputStream;
    DataOutputStream outputStream;
    Command command = new Command();
    Topic topic = new Topic();
    CommandLineParser parser = new DefaultParser();

    public Connection(Socket client, DataInputStream input, DataOutputStream output) {
            this.client = client;
            inputStream = input;
            outputStream = output;
            Topic topic = topicList.stream().
                filter(current -> "BroadCasts".equals(current.getTopicTitle()))
                .findAny()
                .orElse(null);
            topic.getClients().add(this);
    }

    public void run(){
        try {
            Boolean done = true;
            System.out.println("Clientes conectados: " + connectionList.size());
            while (done){

                String message = inputStream.readUTF();

                Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
                Matcher regexMatcher = regex.matcher(message);

                ArrayList<String> args = new ArrayList<>();


                int index = 0;
                while (regexMatcher.find()){
                    if (regexMatcher.group(1) != null) args.add(regexMatcher.group(1));
                    else if (regexMatcher.group(2) != null) args.add(regexMatcher.group(2));
                    else args.add(regexMatcher.group());
                }

                String command = args.get(0);
                switch (command){
                    case "enviar":
                        commandEnviar(args);
                        break;
                    case "crear":
                        commandCrear(args);
                        break;
                    case "remove":
                        commandRemove(args);
                        break;
                    case "subscribe":
                        commandSubscribe(args);
                        break;
                    case "unsuscribe":
                        commandUnsubscribe(args);
                        break;
                    case "topic":
                        commandListar(args);
                        break;
                    default:

                }


            }

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //Por mientras no se usa
    private void addToBroadCast(){
        for (Topic topic:topicList) {
            if (topic.getTopicTitle().compareTo("BroadCast") == 0){
                topic.addUser(this);
            }
        }
    }

    public Vector<Connection> getList(){
        return connectionList;
    }

    public void sendMessage(String msg){
        try {
            outputStream.writeUTF(msg);
            outputStream.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void commandEnviar(ArrayList<String> args){
        CommandLine commandLine = null;
        try {
            commandLine = command.parse(args.toArray(new String[args.size()]));
            if (commandLine != null){
                if (commandLine.hasOption("m")){
                    String messageBody = commandLine.getOptionValue("m").trim();
                    if (commandLine.hasOption("t")){
                        String topicName = commandLine.getOptionValue("t").trim();
                        for (Topic topic:topicList) {
                            if (topic.getTopicTitle().compareTo(topicName) == 0){
                                topic.publish(messageBody);
                            } else {
                                //Pendiente validar que exista el topic
                            }
                        }
                    }
                }
            } else {
                sendMessage("No hay comando para ejecutar");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void commandCrear(ArrayList<String> args){
        CommandLine commandLine = null;
        try {
            commandLine = command.parse(args.toArray(new String[args.size()]));
            if (commandLine != null){
                if (commandLine.hasOption("t")){
                    String topicName = commandLine.getOptionValue("t").trim();
                    topicList.add(new Topic(topicName));
                    sendMessage("Se ha creado el topic " +topicName);
                }
            } else {
                sendMessage("No hay comando para ejecutar");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void commandRemove(ArrayList<String> args){
        CommandLine commandLine = null;
        try {
            commandLine = command.parse((args.toArray(new String[args.size()])));
            if (commandLine != null){
                if (commandLine.hasOption("t")){
                    String topicName = commandLine.getOptionValue("t");
                    for (Topic topic:topicList) {
                        if (topic.getTopicTitle().compareTo(topicName) == 0){
                            topicList.remove(topic);
                        } else {
                            //Pendiente validar administrador y enviar mensaje
                        }
                    }
                }
            } else {
                sendMessage("No hay comando para ejecutar");
            }
        } catch (ParseException e){
            e.printStackTrace();
        }

    }

    private void commandSubscribe(ArrayList<String> args){
        CommandLine commandLine = null;
        try {
            commandLine = command.parse((args.toArray(new String[args.size()])));
            if (commandLine != null){
                if (commandLine.hasOption("t")){
                    String topicName = commandLine.getOptionValue("t");
                    for (Topic topic:topicList) {
                        if (topic.getTopicTitle().compareTo(topicName) == 0){
                            topic.addUser(this);
                        } else {
                            //Pendiente validar administrador y enviar mensaje
                        }
                    }
                }
            } else {
                sendMessage("No hay comando para ejecutar");
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
    }

    private void commandUnsubscribe(ArrayList<String> args){
        CommandLine commandLine = null;
        try {
            commandLine = command.parse((args.toArray(new String[args.size()])));
            if (commandLine != null){
                if (commandLine.hasOption("t")){
                    String topicName = commandLine.getOptionValue("t");
                    for (Topic topic:topicList) {
                        if (topic.getTopicTitle().compareTo(topicName) == 0){
                            topic.removeUser(this);
                        } else {
                            //Pendiente validar administrador y enviar mensaje
                        }
                    }
                }
            } else {
                sendMessage("No hay comando para ejecutar");
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
    }

    private void commandListar(ArrayList<String> args){
        CommandLine commandLine = null;
        try {
            commandLine = command.parse(args.toArray(new String[args.size()]));
            if (commandLine != null){
                if (commandLine.hasOption("l")){
                    StringBuilder sb =  new StringBuilder();
                    for (Topic topic: topicList) {
                        sb.append(topic.getTopicTitle());
                    }
                    sendMessage(sb.toString());
                }
            } else {
                sendMessage("No hay comando para ejecutar");
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
    }
}
