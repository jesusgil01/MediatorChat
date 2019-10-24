import java.util.Vector;

public class Topic {

    public static Vector<Connection> connections = new Vector<>();
    public String topicTitle;

    public Topic(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public Topic(){}

    public void removeUser(Connection connection){
        connections.remove(connection);
    }

    public void addUser(Connection connection){
        connections.add(connection);
    }

    public Vector<Connection> getClients(){
        return connections;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public void publish(String msg){
        for (Connection connection : connections) {
            connection.sendMessage(msg);
        }
    }

}
