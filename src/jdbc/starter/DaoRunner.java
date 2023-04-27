package jdbc.starter;

import jdbc.starter.dao.TicketDao;
import jdbc.starter.dto.TicketFilter;
import jdbc.starter.entity.Ticket;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class DaoRunner {

    public static void main(String[] args) {

        var optionalTicket = TicketDao.getInstance().findById(5L);
        System.out.println(optionalTicket);

    }

    private static void filterTest() {
        var ticketFilter = new TicketFilter(5, 0, "Дмитрий Столяров", "A2");
        var tickets = TicketDao.getInstance().findAll(ticketFilter);
        System.out.println(tickets);
    }

    private static void findAllTest() {
        TicketDao ticketDao = TicketDao.getInstance();
        var tickets = ticketDao.findAll();
        System.out.println(tickets);
    }

    private static void updateTest() {
        TicketDao ticketDao = TicketDao.getInstance();
        var optionalTicket = ticketDao.findById(60L);
        System.out.println(optionalTicket);

        optionalTicket.ifPresent(ticket -> {
            ticket.setCost(BigDecimal.valueOf(188.00));
            ticketDao.update(ticket);
        });
        System.out.println(optionalTicket);
    }

    private static void deleteTest(Long id) {
        TicketDao ticketDao = TicketDao.getInstance();
        System.out.println(ticketDao.delete(id));
    }

    private static void saveTest() {
        TicketDao ticketDao = TicketDao.getInstance();

        Ticket ticket = new Ticket();
        ticket.setPassengerNo("D18231");
        ticket.setPassengerName("Дмитрий Столяров");
        ticket.setFlight(null);
        ticket.setSeatNo("A2");
        ticket.setCost(BigDecimal.TEN);

        var savedTicket = ticketDao.save(ticket);
        System.out.println(savedTicket);
    }
}
