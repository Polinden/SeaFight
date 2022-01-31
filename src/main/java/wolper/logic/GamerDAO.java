package wolper.logic;


import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class GamerDAO extends NamedParameterJdbcDaoSupport {

    @Transactional(readOnly = true)
    public boolean ifDoubleGamer(String chalenger) {
        String ifQuery = "select username from USER_AUTHENTICATION where username=:name";

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("name", chalenger);
        SqlParameterSource parameterSource =
                new MapSqlParameterSource(parameters);

        List<String> results = new ArrayList();
        try {
            results = getNamedParameterJdbcTemplate().query(ifQuery, parameterSource, new RowMapper<String>() {
                public String mapRow(ResultSet rs, int row) {
                    return new String("");
                }
            });
        }
        catch (DataAccessException e) {
            throw new LogicEception("Ошибка чтения БД", e.getMessage());
        }

        if (results.isEmpty() || results==null) return false;
        return true;
    }


    @Transactional(rollbackFor = Exception.class)
    public void saveGamer(Gamer candidat) {

        String ifQuery1 = "insert into USER_AUTHENTICATION (username, password, enabled, won)"
                                    + "VALUES (:name, :passwd, :enbl, :win)";
        String ifQuery2 = "insert into USER_AUTHORIZATION (USER_ID, ROLE)"
                                    + "VALUES (:user_id, :role)";

        KeyHolder holder = new GeneratedKeyHolder();


        //Транзакция
        Map<String, Object> parameters1 = new HashMap<String, Object>();
        parameters1.put("name", candidat.getName());
        parameters1.put("passwd", candidat.getPassword());
        parameters1.put("enbl", 1);
        parameters1.put("win", 0);
        SqlParameterSource parameterSource1 =
                new MapSqlParameterSource(parameters1);

        try {
            getNamedParameterJdbcTemplate().update(ifQuery1, parameterSource1, holder);
        }
        catch (DataAccessException e) {
            throw new LogicEception("Ошибка сохраниения пользователя в БД", e.getMessage());
        }
        
        Map<String, Object> parameters2 = new HashMap<String, Object>();
        parameters2.put("user_id", holder.getKey());
        parameters2.put("role", "GAMER");
        SqlParameterSource parameterSource2 =
                new MapSqlParameterSource(parameters2);

        try {
            getNamedParameterJdbcTemplate().update(ifQuery2, parameterSource2);
        }
        catch (DataAccessException e) {
            throw new LogicEception("Ошибка сохраниения пользователя в БД", e.getMessage());
        }

        return;
    }


    @Transactional(rollbackFor = Exception.class)
    public void setRatingOnExit(String name, int rating) {
        String ifQuery = "update USER_AUTHENTICATION set won=:rating where username=:name";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("name", name);
        parameters.put("rating", rating);
        SqlParameterSource parameterSource =
                new MapSqlParameterSource(parameters);
        try {
            getNamedParameterJdbcTemplate().update(ifQuery, parameterSource);
        }
        catch (DataAccessException e) {
            throw new LogicEception("Ошибка сохраниения пользователя в БД", e.getMessage());
        }
    }


    @Transactional(readOnly = true)
    public int getRatingOnStartUp(String name) {
        String ifQuery = "select won from USER_AUTHENTICATION where username=:name";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("name", name);
        SqlParameterSource parameterSource =
                new MapSqlParameterSource(parameters);
        int result=0;
        try {
            result=getNamedParameterJdbcTemplate().queryForObject(ifQuery, parameterSource, Integer.class);
        }
        catch (DataAccessException e) {
            throw new LogicEception("Ошибка сохраниения пользователя в БД", e.getMessage());
        }
        return result;
    }
    
    

    //Использовать раз  - для формирования базы данных с одним тестовым пользователем

    public static String initializeDB (DataSource dataSource) {
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS USER_AUTHENTICATION (USER_ID INTEGER AUTO_INCREMENT, USERNAME VARCHAR(50), PASSWORD VARCHAR(50), ENABLED BOOLEAN, PRIMARY KEY (USER_ID));");
            statement.executeUpdate("INSERT INTO USER_AUTHENTICATION (USERNAME, PASSWORD, ENABLED) VALUES('papa','qwas',TRUE);");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS USER_AUTHORIZATION (USER_ROLE_ID INTEGER AUTO_INCREMENT,USER_ID INTEGER, ROLE VARCHAR(50), PRIMARY KEY (USER_ROLE_ID));");
            statement.executeUpdate("INSERT INTO USER_AUTHORIZATION (USER_ID, ROLE) VALUES(1,'USER');");

            statement.close();
            connection.close();
            return "Sucsess";
        } catch (SQLException e) {
            int i = 0;
            StringBuilder sb = new StringBuilder();
            for (Throwable e1 : e) {
                sb.append(e1.getMessage());
            }
            e.printStackTrace();
            return ("Fail " + e.getCause() + "  " + e.getSQLState() + " " + sb.toString());
        }
    }
}
