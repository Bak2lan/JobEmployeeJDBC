package org.example.dao.impl;

import org.example.config.DataBaseConfig;
import org.example.dao.EmployeeDao;
import org.example.exception.MyException;
import org.example.model.Employee;
import org.example.model.Job;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDaoImpl implements EmployeeDao {
    private final Connection connection = DataBaseConfig.getConnection();

    @Override
    public void createEmployee() {
        String sql = """
                create table employees(
                id serial primary key,
                first_name varchar ,
                last_name varchar ,
                age int,
                email varchar,
                job_id int references jobs(id))
                """;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            System.out.println("Table successfully created");
            statement.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addEmployee(Employee employee) {
        String sql = """
                insert into employees(first_name,last_name,age,email,job_id)
                values
                (?,?,?,?,?)
                """;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setInt(3, employee.getAge());
            preparedStatement.setString(4, employee.getEmail());
            preparedStatement.setInt(5, employee.getJobId());
            preparedStatement.executeUpdate();
            System.out.println("Employee successfully saved");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void dropTable() {
        String sql = """
                drop table employees;
                """;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            System.out.println("Table successfully dropped");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void cleanTable() {
        String sql = "truncate table employees";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            System.out.println("Table successfully cleaned");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEmployee(Long id, Employee employee) {
        String sql = """
                update employees set first_name=?,last_name=?,age=?,email=?,job_id=? where id=?
                """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setInt(3, employee.getAge());
            preparedStatement.setString(4, employee.getEmail());
            preparedStatement.setLong(5, employee.getJobId());
            preparedStatement.setLong(6, id);
            int i = preparedStatement.executeUpdate();
            if (i > 0) {
                System.out.println("Employee successfully updated");
            } else throw new MyException("Not found");


        } catch (SQLException | MyException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        String sql = "select * from employees";
        List<Employee> employees = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                employees.add(new Employee(
                        resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getInt("job_id")));
            }
            statement.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return employees;
    }

    @Override
    public Employee findByEmail(String email) {
        String sql="select * from employees where email=?";
        Employee employee= new Employee();
        boolean isFound=false;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                isFound=true;
                employee.setId(resultSet.getLong("id"));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setAge(resultSet.getInt("age"));
                employee.setEmail(resultSet.getString("email"));
                employee.setJobId(resultSet.getInt("job_id"));
            }
            if (isFound){
                return employee;
            }else throw new MyException("Not found email");
        }catch (SQLException |MyException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Map<Employee, Job> getEmployeeById(Long employeeId) {
        String sql = """
                select employees.* , jobs.* from employees 
                inner join jobs on employees.job_id=jobs.id
                where employees.id=?
                """;
        Map<Employee, Job> employeeJobMap = new HashMap<>();
        boolean isFound=false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                isFound=true;
                Employee employee = new Employee();
                employee.setId(resultSet.getLong("id"));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setAge(resultSet.getInt("age"));
                employee.setEmail(resultSet.getString("email"));
                employee.setJobId(resultSet.getInt("job_id"));

                Job job = new Job();
                job.setId(resultSet.getLong("id"));
                job.setPosition(resultSet.getString("position"));
                job.setProfession(resultSet.getString("profession"));
                job.setDescription(resultSet.getString("description"));
                job.setExperience(resultSet.getInt("experience"));
                employeeJobMap.put(employee, job);
            }
            if (isFound){
                return employeeJobMap;
            }else throw new MyException("Not found id");
        }catch (SQLException |MyException e){
            System.out.println(e.getMessage());
        }return null;
    }

    @Override
    public List<Employee> getEmployeeByPosition(String position) {
        String sql="select employees.* from employees inner join jobs on employees.job_id=jobs.id " +
                   "where jobs.position=?";
        boolean isFound=false;

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,position);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Employee>employees= new ArrayList<>();
            while (resultSet.next()){
                isFound=true;
                Employee employee= new Employee();
                employee.setId(resultSet.getLong("id"));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setAge(resultSet.getInt("age"));
                employee.setEmail(resultSet.getString("email"));
                employee.setJobId(resultSet.getInt("job_id"));
                employees.add(employee);
            }
            if(isFound){
                return employees;
            }else throw new MyException("Not found position");
        }catch (SQLException|MyException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
