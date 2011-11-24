/**
 * Copyright 2011 Sjukvardsradgivningen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public

 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the

 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,

 *   Boston, MA 02111-1307  USA
 */
package se.skl.tp.ticket.exception;

/**
 * 
 * Indicates there was some trouble generating ticket from ticket machine.
 * 
 */
public class TicketMachineException extends Exception {

    private static final long serialVersionUID = 1L;

    public TicketMachineException() {
	super("Ticket machine exception occured!");
    }

    public TicketMachineException(String msg) {
	super(msg);
    }

    public TicketMachineException(String msg, Throwable cause) {
	super(msg, cause);
    }
}
