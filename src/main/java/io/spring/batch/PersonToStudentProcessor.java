package io.spring.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

@Component
public class PersonToStudentProcessor implements ItemProcessor<Person, Student> {
	private final DataSource dataSource;

	@Autowired
	public PersonToStudentProcessor(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Student process(Person person) throws Exception {
		boolean personExistsInPerson = checkIfPersonExistsInPersonTable(person);

		if (personExistsInPerson) {
			boolean personExistsInStudent = checkIfPersonExistsInStudentTable(person);

			if (personExistsInStudent) {
				Student student = new Student();
				student.setId1(person.getId1());
				student.setFirstName(person.getFirstName());
				student.setLastName(person.getLastName());
				student.setBirthdate(person.getBirthdate());
				return student;
			} else {
				Student student = new Student();
				student.setId1(person.getId1());
				student.setFirstName(person.getFirstName());
				student.setLastName(person.getLastName());
				student.setBirthdate(person.getBirthdate());
				return student;
			}
		} else {
			deleteStudentRecord(person.getId1());

			return null;
		}
	}

	private boolean checkIfPersonExistsInStudentTable(Person person) {
		try (Connection connection = dataSource.getConnection()) {
			String sql = "SELECT COUNT(*) FROM STUDENT WHERE id = ? AND first_name = ? AND last_name = ? AND birthdate = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.setLong(1, person.getId1());
				preparedStatement.setString(2, person.getFirstName());
				preparedStatement.setString(3, person.getLastName());
				preparedStatement.setDate(4, new java.sql.Date(person.getBirthdate().getTime()));

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						int count = resultSet.getInt(1);
						return count > 0;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean checkIfPersonExistsInPersonTable(Person person) {
		try (Connection connection = dataSource.getConnection()) {
			String sql = "SELECT COUNT(*) FROM PERSON WHERE id = ? AND first_name = ? AND last_name = ? AND birthdate = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.setLong(1, person.getId1());
				preparedStatement.setString(2, person.getFirstName());
				preparedStatement.setString(3, person.getLastName());
				preparedStatement.setDate(4, new java.sql.Date(person.getBirthdate().getTime()));

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						int count = resultSet.getInt(1);
						return count > 0;

					}
				}
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return false;
	}

	private void deleteStudentRecord(Long id) {
		try (Connection connection = dataSource.getConnection()) {
			String sql = "DELETE FROM STUDENT WHERE id = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.setLong(1, id);
				preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}