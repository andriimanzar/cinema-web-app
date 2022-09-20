package com.manzar.persistence.entity;

import java.math.BigDecimal;

public class Booking {

    private BigDecimal ticketPrice;
    private Long userId;
    private Long movieSessionId;
    private Long seatId;

    public BigDecimal getCost() {
        return ticketPrice;
    }

    public void setCost(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMovieSessionId() {
        return movieSessionId;
    }

    public void setMovieSessionId(Long movieSessionId) {
        this.movieSessionId = movieSessionId;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "ticketPrice=" + ticketPrice +
                ", userId=" + userId +
                ", movieSessionId=" + movieSessionId +
                ", seatId=" + seatId +
                '}';
    }
}
