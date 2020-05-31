package net.sabercrafts.coursemgmt.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Data
public class Enrollment implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private EnrollmentId id;

	@ManyToOne
	@MapsId("courseId")
	private Course course;

	@ManyToOne
	@MapsId("userId")
	private User user;

	@Column(name="completion_rate")
	private Float completionRate;
	
	@Column(name="is_completed")
	private Boolean isCompleted;
	
	@Column(name="created_at")
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	public Enrollment() {
		
	}
	
	public Enrollment(Course course, User user) {
		this.course = course;
		this.user = user;
		this.id = new EnrollmentId(course.getId(), user.getId());
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		Enrollment that = (Enrollment) o;
		return Objects.equals(course, that.getCourse()) && Objects.equals(user, that.getUser());
	}

	@Override
	public int hashCode() {
		return Objects.hash(course, user);
	}

}
