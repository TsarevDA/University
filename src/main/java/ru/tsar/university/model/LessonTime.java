package ru.tsar.university.model;

import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import ru.tsar.university.model.Lesson.LessonBuilder;

public class LessonTime {

	private int id;
	private int orderNumber;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime startTime;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime endTime;
	
	private LessonTime () {

	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public LocalTime getStartTime() {
		return startTime;
	}


	public LocalTime getEndTime() {
		return endTime;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "LessonTime [orderNumber=" + orderNumber + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + id;
		result = prime * result + orderNumber;
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LessonTime other = (LessonTime) obj;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (id != other.id)
			return false;
		if (orderNumber != other.orderNumber)
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		return true;
	}
	
	public static LessonTimeBuilder builder() {
		return new LessonTimeBuilder();
	}

	public static class LessonTimeBuilder {

		private int id;
		private int orderNumber;
		private LocalTime startTime;
		private LocalTime endTime;
		
		public LessonTimeBuilder() {
		}
		
		public LessonTimeBuilder id(int id) {
			this.id = id;
			return this;
		}
		
		public LessonTimeBuilder orderNumber(int orderNumber) {
			this.orderNumber = orderNumber;
			return this;
		}
		public LessonTimeBuilder startTime(LocalTime startTime) {
			this.startTime = startTime;
			return this;
		}

		public LessonTimeBuilder endTime(LocalTime endTime) {
			this.endTime = endTime;
			return this;
		}
		
		public LessonTime build() {
			LessonTime lessonTime = new LessonTime();
			lessonTime.setId(id);
			lessonTime.setOrderNumber(orderNumber);
			lessonTime.setStartTime(startTime);
			lessonTime.setEndTime(endTime);
			return lessonTime;
		}
	}
}
