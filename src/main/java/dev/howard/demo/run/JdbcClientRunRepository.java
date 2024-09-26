package dev.howard.demo.run;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class JdbcClientRunRepository {
    
    private final JdbcClient jdbcClient;

    public JdbcClientRunRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Run> findAll(){
        return jdbcClient.sql("SELECT * from run").query(Run.class).list();
                            
    }

    public Optional<Run> findById(Integer id) {
        return jdbcClient.sql("SELECT id, title, started_on, completed_on, miles, location FROM Run WHERE id= :id")
        .param("id", id)
        .query(Run.class)
        .optional();
    }

    public void create(Run run){
        var updated = jdbcClient.sql("INSERT INTO Run(id, title, started_on, completed_on, miles, location) values(?, ?, ?, ?, ?, ?)")
        .params(List.of(run.id(),run.title(),run.startedOn(),run.completedOn(),run.miles(),run.location().toString())).update();

        Assert.state(updated == 1, "failed to create run" + run.title());
    }

    public void update(Run run, Integer id){
        var updated = jdbcClient.sql("UPDATE Run SET title=?, started_on=?, completed_on=?, miles=?, location=? WHERE id=?")
       .params(List.of(run.title(),run.startedOn(),run.completedOn(),run.miles(),run.location().toString(), id)).update();
    
        Assert.state(updated == 1, "failed to update run with id " + id);
    }

    public void delete(Integer id){
        var updated = jdbcClient.sql("DELETE FROM Run WHERE id=?")
       .params("id", id).update();
    
        Assert.state(updated == 1, "failed to delete run with id " + id);
    }

    public int count(){
        return jdbcClient.sql("SELECT * from Run").query().listOfRows().size();
    }

    public void saveAll(List<Run> runs){
        runs.stream().forEach(this::create);
    }

    public List<Run> findByLocation(String location){
        return jdbcClient.sql("SELECT * FROM Run WHERE location=?")
       .param("location", location).query(Run.class).list();
    }


}
