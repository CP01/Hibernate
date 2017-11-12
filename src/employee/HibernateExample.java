/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employee;


import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * @author Producer
 */
public class HibernateExample {
	private static SessionFactory factory;
	private static ServiceRegistry registry;
	private static Scanner in = new Scanner(System.in);

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		try{
			// factory = new Configuration().configure().buildSessionFactory();
			final Configuration configuration = new Configuration().configure();
			registry = new StandardServiceRegistryBuilder().applySettings(
					configuration.getProperties()).build();
			factory = configuration.buildSessionFactory(registry);
		}catch (final Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}

		final HibernateExample HE = new HibernateExample();
		final String more = "yes";
		final Integer empID=0;

		/* Add few employee records in database */
		/*		while(more.charAt(0) == 'y' || more.charAt(0) =='Y')
		{
			empID = HE.addEmployee();
			System.out.println("More employees? (y/n)");
			more = in.nextLine();
		}*/

		/* Update employee's records */
		//HE.updateEmployee(65, 95000);

		/* Delete an employee from the database */
		// HE.deleteEmployee(67);

		//HE.listEmployees();
		final Session session = factory.openSession();
		final Criteria criteria = session.createCriteria(Employee.class);
		List<Employee> list = criteria.list();
		showData(list);
		criteria.add(Restrictions.gt("salary", 1500000));
		list = criteria.list();
		showData(list);
		criteria.setFetchSize(2);
		list = criteria.list();
		showData(list);
		criteria.addOrder(Order.desc("id"));
		list = criteria.list();
		showData(list);
		//showData(list);

		StandardServiceRegistryBuilder.destroy(registry);
	}

	public static void showData(List<Employee> list)
	{
		for(final Employee e : list)
		{
			System.out.print("First Name: " + e.getFirstName());
			System.out.print("  Last Name: " + e.getLastName());
			System.out.println("  Salary: " + e.getSalary());
		}
		System.out.println("---------------");
	}
	/* Method to CREATE an employee in the database */
	public Integer addEmployee(){

		System.out.println("Enter first name: ");
		final String fname = in.nextLine();
		System.out.println("Enter last name: ");
		final String lname = in.nextLine();
		/*		System.out.println("Enter cell: ");
		final String cell = in.nextLine();
		System.out.println("Enter home phone: ");
		final String hPhone = in.nextLine();*/
		System.out.println("Enter salary: ");
		final int salary = in.nextInt();
		in.nextLine();
		/*        HashSet hs = new HashSet();
        hs.add(new Phone(cell));
        hs.add(new Phone(hPhone));*/

		final Session session = factory.openSession();
		Transaction tx = null;
		Integer employeeID = null;
		try{
			tx = session.beginTransaction();
			final Employee employee = new Employee(fname, lname, salary);
			/*employee.setPhones(hs);*/
			employeeID = (Integer) session.save(employee);
			tx.commit();
		}catch (final HibernateException e) {
			if (tx!=null) {
				tx.rollback();
			}
			e.printStackTrace();
		}finally {
			session.close();
		}
		return employeeID;
	}
	/* Method to print all the employees */
	public void listEmployees( ){
		final Session session = factory.openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();

			final List employees = session.createQuery("FROM Employee").list();
			for (final Iterator iterator1 =
					employees.iterator(); iterator1.hasNext();){
				final Employee employee = (Employee) iterator1.next();
				System.out.print("First Name: " + employee.getFirstName());
				System.out.print("  Last Name: " + employee.getLastName());
				System.out.println("  Salary: " + employee.getSalary());
				/*Set phoneNums = employee.getPhones();
            for (Iterator iterator2 =
                         phoneNums.iterator(); iterator2.hasNext();){
                  Phone phoneNum = (Phone) iterator2.next();
                  System.out.println("Phone: " + phoneNum.getPhoneNumber());
            }*/
			}
			tx.commit();
		}catch (final HibernateException e) {
			if (tx!=null) {
				tx.rollback();
			}
			e.printStackTrace();
		}finally {
			session.close();
		}
	}
	/* Method to UPDATE salary for an employee */
	public void updateEmployee(Integer EmployeeID, int salary ){
		final Session session = factory.openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			final Employee employee =
					(Employee)session.get(Employee.class, EmployeeID);
			employee.setSalary( salary );
			session.update(employee);
			tx.commit();
		}catch (final HibernateException e) {
			if (tx!=null) {
				tx.rollback();
			}
			e.printStackTrace();
		}finally {
			session.close();
		}
	}
	/* Method to DELETE an employee from the records */
	public void deleteEmployee(Integer EmployeeID){
		final Session session = factory.openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			final Employee employee =
					(Employee)session.get(Employee.class, EmployeeID);
			session.delete(employee);
			tx.commit();
		}catch (final HibernateException e) {
			if (tx!=null) {
				tx.rollback();
			}
			e.printStackTrace();
		}finally {
			session.close();
		}
	}
}


