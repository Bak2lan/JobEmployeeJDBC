package org.example.dao.impl;

import org.example.config.DataBaseConfig;
import org.example.dao.JobDao;
import org.example.model.Job;
import org.example.service.JobService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobDaoImpl implements JobDao {

    private final Connection connection= DataBaseConfig.getConnection();
    @Override
    public void createJobTable() {
    try{
        Statement statement = connection.createStatement();
        statement.executeUpdate("create table jobs(" +
                "id serial primary key," +
                "position varchar ," +
                "profession varchar ," +
                "description varchar ," +
                "experience int )");
        statement.close();
        System.out.println("Table successfully created");

    }catch (SQLException e){
        System.out.println(e.getMessage());
    }
    }

    @Override
    public void addJob(Job job) {
        String sql = "insert into jobs(position,profession,description,experience)" +
                "values(?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, job.getPosition());
            preparedStatement.setString(2, job.getProfession());
            preparedStatement.setString(3, job.getDescription());
            preparedStatement.setInt(4, job.getExperience());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Job successfully saved");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Job getJobById(Long jobId) {
        String sql="select * from jobs where id=?";
        Job job= new Job();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,jobId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                job.setId(resultSet.getLong("id"));
                job.setPosition(resultSet.getString("position"));
                job.setProfession(resultSet.getString("profession"));
                job.setDescription(resultSet.getString("description"));
                job.setExperience(resultSet.getInt("experience"));
            }
            preparedStatement.close();

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return job;
    }


    @Override
    public List<Job> sortByExperience(String ascOrDesc) {

        String orderby=null;
        if (ascOrDesc.equals("asc")){
            orderby="asc";
        } else if (ascOrDesc.equals("desc")) {
            orderby="desc";
        }
        String sql="select * from jobs order by experience "+orderby;
        List<Job>jobs= new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                jobs.add(new Job((
                        resultSet.getLong("id")),
                        resultSet.getString("position"),
                        resultSet.getString("profession"),
                        resultSet.getString("description"),
                        resultSet.getInt("experience")));
            }
            statement.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return jobs;
    }

    @Override
    public Job getJobByEmployeeId(Long employeeId) {
        String sql1= """
select jobs.* from employees inner join 
jobs on employees.job_id=jobs.id where employees.id=?
""";
        Job job= new Job();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql1);
            preparedStatement.setLong(1,employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                job.setId(resultSet.getLong("id"));
                job.setPosition(resultSet.getString("position"));
                job.setProfession(resultSet.getString("profession"));
                job.setDescription(resultSet.getString("description"));
                job.setExperience(resultSet.getInt("experience"));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return job;
    }

    @Override
    public void deleteDescriptionColumn() {
        String column="alter table jobs alter drop description";
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(column);
            System.out.println("Column successfully deleted");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
}
