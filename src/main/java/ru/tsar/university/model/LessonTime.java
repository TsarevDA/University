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
	
}
