package org.example;
import org.example.model.Employee;
import org.example.model.Job;
import org.example.service.impl.EmployeeServiceImpl;
import org.example.service.impl.JobServiceImpl;

import java.util.List;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        JobServiceImpl jobService= new JobServiceImpl() ;
        EmployeeServiceImpl employeeService= new EmployeeServiceImpl();
         Scanner scanner= new Scanner(System.in);
          int num;
          while (true){
              System.err.println("""
                      1.Create table jobs
                      2.Save job
                      3.Get job by Id
                      4.Sort job by experience
                      5.Get job by employee id
                      6.Delete description column
                      
                      
                      7.Create table employees
                      8.Save employee
                      9.Drop table employees
                      10.Clean table employees
                      11.Update Employee
                      12.Get all employees
                      13.Find employee by email
                      14.Get employee by id with job 
                      15.Get employee by position""");
              num=scanner.nextInt();
              switch (num){
                  case 1:
                      jobService.createJobTable();
                      break;
                  case 2:
                      jobService.addJob(new Job("Mentor","Java","Back-End dev",1));
                      jobService.addJob(new Job("Instructor","JavaScript","Front-End dev",4));
                      jobService.addJob(new Job("Director","Python","Back-End dev",2));
                      jobService.addJob(new Job("Mentor","JavaScript","Front-End dev",6));
                      break;
                  case 3:
                      System.out.println(jobService.getJobById(1L));
                      break;
                  case 4:
                      System.out.println(jobService.sortByExperience("asc"));
                      break;
                  case 5:
                      System.out.println(jobService.getJobByEmployeeId(1L));
                      break;
                  case 6:
                      jobService.deleteDescriptionColumn();
                      break;
                  case 7:
                      employeeService.createEmployee();
                      break;
                  case 8:
                      employeeService.addEmployee(new Employee("Ulan","Kubanychbekov",26,"ulan@gmail.com",1));
                      employeeService.addEmployee(new Employee("Aizat","Duisheeva",20,"aizat@gmail.com",3));
                      employeeService.addEmployee(new Employee("Nurik","Alynbai uulu",22,"nur@gmail.com",2));
                      employeeService.addEmployee(new Employee("Asel","Asanova",23,"asel@gmail.com",4));
                      break;
                  case 9:
                      employeeService.dropTable();
                      break;
                  case 10:
                      employeeService.cleanTable();
                      break;
                  case 11:
                      employeeService.updateEmployee(1L,new Employee("Baktulan","Nazirbek uulu",24,"baku@mail.ru",1));
                      break;
                  case 12:
                      System.out.println(employeeService.getAllEmployees());
                      break;
                  case 13:
                      System.out.println(employeeService.findByEmail("aizat@gmail.com"));
                      break;
                  case 14:
                      System.out.println(employeeService.getEmployeeById(1L));
                      break;
                  case 15:
                      System.out.println(employeeService.getEmployeeByPosition("Instructor"));

                      break;
                  default:
                      System.out.println("Invalid");
              }

          }


    }
}
