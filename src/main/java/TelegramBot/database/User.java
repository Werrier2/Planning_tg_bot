package TelegramBot.database;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tg_user")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    private String username;
    private String password;

    @OneToMany(mappedBy = "tg_user", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();

    public User(long id, String username, String password, List<Task> tasks) {
        Id = id;
        this.username = username;
        this.password = password;
        this.tasks = tasks;
    }

    public User() {
    }

    public long getId() {
        return Id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setId(long id) {
        Id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
