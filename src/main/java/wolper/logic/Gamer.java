package wolper.logic;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;


//Сущность для сохранения в базе данных
public class Gamer {

    @NotEmpty(message = "Введите непустое имя")
    @Pattern(regexp="[a-zA-Z0-9]*", message = "Английские буквы и/или цифры без пробелов")
    private String name;

    @NotEmpty(message = "Введите непустой пароль")
    @Pattern(regexp="[a-zA-Z0-9]*", message = "Английские буквы и/или цифры без пробелов")
    private String password;

    private String role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Gamer() {
        this.name="";
        this.password = "";
    }

    public Gamer(String name, String password, String role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }


}
