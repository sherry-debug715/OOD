package Reservation System.Restaurant Reservation System;

class NoTableException extends Exception{

	public NoTableException(Party p)
	{
		super("No table available for party size: " + p.getSize());
	}
}

class Meal {
	private float price;
	
	public Meal(float price) {
		this.price = price;
	}
	
	public float getPrice() {
		return this.price;
	}
}

class Order {
	private List<Meal> meals;
	
	public Order() {
		meals = new ArrayList<Meal>();
	}
	
	public List<Meal> getMeals() {
		return meals;
	}
	
	public void mergeOrder(Order order) {
		if(order != null) {
			for(Meal meal : order.getMeals()) {
				meals.add(meal);
			}
		}
	}
	
	public float getBill() {
		int bill = 0;
		for(Meal meal : meals) {
			bill += meal.getPrice();
		}
		return bill;
	}
}

class Party {
	private int size;
	
	public Party(int _size) {
		size = _size;
	}
	
	public int getSize() {
		return size;
	}
}

class Table implements Comparable<Table>{
	private int id;
	private int capacity;
	private boolean available;
	private Order order;
	List<Date> reservations;
	
	public Table(int _id, int _capacity) {
		id = _id;
		capacity = _capacity;
		available = true;
		order = null;
		reservations = new ArrayList<>();
	}
	
	public int getId() {
		return id;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public List<Date> getReservation() {
		return reservations;
	}

	public void setReservation(Date d) {
		reservations.add(d);
	}
	
	public boolean isAvailable() {
		return available;
	}
	
	public void markAvailable() {
		available = true;
	}
	
	public void markUnavailable() {
		available = false;
	}
	
	public Order getCurrentOrder() {
		return order;
	}
	
	public void setOrder(Order o) {
		if(order == null) {
			order = o;
		} else {
			if(o != null) {
				order.mergeOrder(o);
			}
		}
	}

	@Override
	public int compareTo(Table compareTable) {
		return capacity - compareTable.getCapacity();
	}
	
	private int findDatePosition(Date d) {
		return reservations.indexOf(d);
	}
	
	public boolean noFollowReservation(Date d) {
		if (reservations.isEmpty()) {
			return true;
		}
		Collections.sort(reservations);
		return reservations.get(reservations.size() - 1).equals(d);
	}
	
	public boolean reserveForDate(Date d) {
		//check if there is overlap of Date 
		for (Date reservedDate : reservations) {
			if (d.equals(reservedDate)) {
				return false;
			}
		}
		// add d to reservation 
		setReservation(d);
		return true;
	}
	
	public void removeReservation(Date d) {
		int idx = findDatePosition(d);
		reservations.remove(idx);
	}
}

class Reservation {
	private Table table;
	private Date date;
	
	public Reservation(Table _table, Date _date) {
		table = _table;
		date = _date;
	}
	
	public Date getDate() {
		return date;
	}
	
	public Table getTable() {
		return table;
	}
}

public class Restaurant {
	private List<Table> tables;
	private List<Meal> menu;
	public static final int MAX_DINEHOUR = 2;
	public static final long HOUR = 3600*1000;
	
	public Restaurant() {
		tables = new ArrayList<Table>();
		menu = new ArrayList<Meal>();
	}
	
	public void findTable(Party p) throws NoTableException {
		int start = findSizeForParty(p);
		Table table = null;
		for (int i = start; i < tables.size(); i++) {
			if (tables.get(i).isAvailable()) {
				table = tables.get(i);
			}
		}
		if (table == null) {
			throw new NoTableException(p);
		}
	}
	
	public void takeOrder(Table t, Order o) {
		t.setOrder(o);
	}
	
	public float checkOut(Table t) {
		// return the amount of the table 
		Order order = t.getCurrentOrder();
		int amount = 0;
		for (Meal m : order.getMeals()) {
			amount += m.getPrice();
		}
		// mark table as available
		t.markAvailable();
		return amount;
	}
	
	public List<Meal> getMenu() {
		return menu;
	}
	
	public void addTable(Table t) {
		tables.add(t);
	}

	private int findSizeForParty(Party p) {
		int partySize = p.getSize();
		int left = 0, right = tables.size() - 1;
		while (left + 1 < right) {
			int mid = left + (right - left) / 2;
			if (tables.get(mid).getCapacity() < partySize) {
				left = mid;
			} else {
				right = mid;
			}
		}
		return left;
	}
	
	public Reservation findTableForReservation(Party p, Date date) {
		// table is already sorted by capacity, find the first table with capacity and isAvailable 
		int left = findSizeForParty(p);
		Table findTable = null;
		for (int i = left; i < tables.size(); i++) {
			if (tables.get(i).reserveForDate(date)) {
				findTable = tables.get(i);
				break;
			}
		}

		if (findTable == null) {
			return null;
		}

		return new Reservation(findTable, date);
	}
	
	public void cancelReservation(Reservation r) {
		Date date = r.getDate();
		r.getTable().removeReservation(date);
	}
	
	public void redeemReservation(Reservation r) {
		Date date = r.getDate();
		Table table = r.getTable();
		
		table.markUnavailable();
		table.removeReservation(date);
	}
	
	public String restaurantDescription() {
		String description = "";
		for(int i = 0; i < tables.size(); i++)
		{
			Table table = tables.get(i);
			description += ("Table: " + table.getId() + ", table size: " + table.getCapacity() + ", isAvailable: " + table.isAvailable() + ".");
			if(table.getCurrentOrder() == null)
				description += " No current order for this table"; 
			else
				description +=  " Order price: " + table.getCurrentOrder().getBill();
			
			description += ". Current reservation dates for this table are: ";
			
			for(Date date : table.getReservation())
			{
				description += date.toGMTString() + " ; ";
			}
			
			description += ".\n";
		}
		description += "*****************************************\n";
		return description;
	}
}
