import java.io.Serializable;
import java.time.LocalDate;

public class Task implements Serializable {
    private String title;
    private String description;
    private LocalDate dueDate;
    private String category;

    public Task(String title, String description, LocalDate dueDate, String category) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return title + " (" + category + ")";
    }
}
