package TelegramBot.database;

import jakarta.persistence.*;

@Entity
@Table(name = "Task")

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    private String title;
    private String description;
    private String status;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User tg_user;

    public Task(long id, String title, String description, String status, User user) {
        Id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.tg_user = user;
    }

    public Task() {
    }

    public long getId() {
        return Id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public User getTg_user() {
        return tg_user;
    }

    public void setId(long id) {
        Id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTg_user(User tg_user) {
        this.tg_user = tg_user;
    }
}
