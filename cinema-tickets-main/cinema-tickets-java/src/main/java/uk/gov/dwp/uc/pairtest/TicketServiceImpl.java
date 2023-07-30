/** Author: Priyanthan Seevaratnam
 * Email: Priyanthan1990@gmail.com
 * Applciation ID: 8938674
 */
package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.lang.reflect.Type;

public class TicketServiceImpl implements TicketService {
    /**Should only have private methods other than the one below.**/
    public TicketTypeRequest ticketTypeRequest;
    public Long accountId;

    public int noOfTickets;

    public int totalAmountToPay;
    public int totalNumberOfSeats;

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequest) throws InvalidPurchaseException {

        /** Retrieved the values for INFANT, CHILD and ADULT for comparison for later use
         *
         */
        TicketTypeRequest.Type adult = TicketTypeRequest.Type.ADULT;
        TicketTypeRequest.Type infant = TicketTypeRequest.Type.INFANT;
        TicketTypeRequest.Type child = TicketTypeRequest.Type.CHILD;


        int totalPrice = 0;
        int totalSeats = 0;

        /** Iterating through the ticketTypeRequest objects and multiplying by the NoOfTickets**/

        for (TicketTypeRequest value : ticketTypeRequest) {
            if (((value.getTicketType()) == adult) &&
                    ((value.getNoOfTickets() > 0 && value.getNoOfTickets() <= 20))) {
                int adultPrice = 20;
                int adultSeats = 0;
                totalPrice = adultPrice * value.getNoOfTickets();
                totalSeats = adultSeats + value.getNoOfTickets();
                if (((value.getTicketType()) == infant) && ((value.getNoOfTickets() < 20))) {
                    int infantPrice = 0;
                    int infantSeats = 0;
                    totalPrice = totalPrice + infantPrice;
                    totalSeats = totalSeats + infantSeats;
                }
                if (((value.getTicketType()) == child) && ((value.getNoOfTickets() < 20))) {
                    int childPrice = 10;
                    int childSeats = 0;
                    totalPrice = totalPrice + (childPrice * value.getNoOfTickets());
                    totalSeats = totalSeats + (childSeats * value.getNoOfTickets());
                }

                TicketPaymentServiceImpl purchase = new TicketPaymentServiceImpl();
                purchase.makePayment(setAccount(accountId), totalPrice);

                SeatReservationServiceImpl reserve = new SeatReservationServiceImpl();
                reserve.reserveSeat(setAccount(accountId), totalSeats);
            }
        }
    }




    /**
     * Created a private method for setting the accountId if it is greater than 0.
     *
     * @return
     */
    private long setAccount(Long accountId) {
        if (accountId > 0) {
            this.accountId = accountId;
        }
        return accountId;
    }


    }

