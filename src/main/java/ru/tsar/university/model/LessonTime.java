package ru.tsar.university.model;

import java.time.LocalTime;

public class LessonTime {

	private int id;
	private int orderNumber;
	private LocalTime startTime;
	private LocalTime endTime;

	public LessonTime(int id, int orderNumber, LocalTime startTime, LocalTime endTime) {
		this.id = id;
		this.orderNumber = orderNumber;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public LessonTime(int orderNumber, LocalTime startTime, LocalTime endTime) {
		this.orderNumber = orderNumber;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

}
