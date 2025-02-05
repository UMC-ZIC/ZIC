package com.umc7.ZIC.practiceRoom.dto;


import java.time.LocalTime;

public record AvailableTimeSlot(LocalTime startTime, LocalTime endTime) {
}