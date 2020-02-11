import java.io.Serializable;

public class Rental implements Serializable {
    private int rentalNumber;
    private String dateRental;
    private String returnDate;
    private double pricePerDay;
    private int custNumber;
    private int vehNumber;
    private double totalPrice;

    public Rental() {
    }

    public Rental(int rentalNumber, String dateRental, String returnDate, double pricePerDay, int custNumber, int vehNumber) {
        this.rentalNumber = rentalNumber;
        this.dateRental = dateRental;
        this.returnDate = returnDate;
        this.pricePerDay = pricePerDay;
        this.custNumber = custNumber;
        this.vehNumber = vehNumber;
    }

    public Rental(String dateRental, int custNumber, int vehNumber) {
        this.dateRental = dateRental;
        this.custNumber = custNumber;
        this.vehNumber = vehNumber;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getRentalNumber() {
        return rentalNumber;
    }

    public void setRentalNumber(int rentalNumber) {
        this.rentalNumber = rentalNumber;
    }

    public String getDateRental() {
        return dateRental;
    }

    public void setDateRental(String dateRental) {
        this.dateRental = dateRental;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public int getCustNumber() {
        return custNumber;
    }

    public void setCustNumber(int custNumber) {
        this.custNumber = custNumber;
    }

    public int getVehNumber() {
        return vehNumber;
    }

    public void setVehNumber(int vehNumber) {
        this.vehNumber = vehNumber;
    }
}
